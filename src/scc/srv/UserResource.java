package scc.srv;

import api.RestUsers;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.Login;
import data_classes.Session;
import data_classes.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import utils.CosmosDBLayer;
import data_classes.UserDAO;
import utils.RedisLayer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


@Path("/user")
public class UserResource {

    private CosmosDBLayer db;
    private MediaResource mr;
    private RedisLayer rl;

    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
        this.rl = RedisLayer.getInstance();
    }

    public void createUserWithPhoto(User user, byte[] photo) {//throws WebApplicationException {
        System.out.println("Creating user with photo...");
        //if (badUser(user))
        //    throw new WebApplicationException(Response.Status.BAD_REQUEST);
        //if (getUserHelper(user.getId()) != null)
        //    throw new WebApplicationException(Response.Status.CONFLICT);
        mr.upload(photo);
        db.putUser(new UserDAO(user));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(User user) throws WebApplicationException {
        System.out.println("Creating user...");
        if (badUser(user)) {
            System.out.println("Bad user...");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (getUserHelper(user.getId()) != null) {
            System.out.println("Already exists user...");
            throw new WebApplicationException(Response.Status.CONFLICT);
        }

        CosmosItemResponse<UserDAO> udao = db.putUser(new UserDAO(user));
        rl.addUser(udao.getItem());
        System.out.println(user.toString());
        return user;
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteUser(String id, String password) throws WebApplicationException {
        System.out.println("Deleting user...");
        User user = getUser(id, password);

        db.delUser(new UserDAO(user));
        // delete from cache
        rl.deleteUser(id);
    }

    public void updateUser(String id, String password, User user) throws WebApplicationException {
        System.out.println("Updating user...");
        User u = getUser(id, password);
        if (!u.getId().equals(user.getId()))
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        // update database
        CosmosItemResponse<UserDAO> udao = db.updateUser(new UserDAO(user));
        rl.updateUser(udao.getItem());
    }

    public User getUser(String id, String password) throws WebApplicationException {
        if (badParam(id))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        UserDAO userDAO = getUserHelper(id);
        if (userDAO == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if (badParam(password) || wrongPassword(userDAO, password))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        return userDAO.toUser();
    }

    public jakarta.ws.rs.core.Response auth(Login user) {
        boolean pwd0k = false;

        // check pwd
        try {
            if(getUser(user.getUserId(), user.getPwd()) != null)
                pwd0k = true;
        } catch (WebApplicationException e) {
            // password is incorrect, user does not exist, etc
        }

        if(pwd0k) {
            String uid = UUID.randomUUID().toString();
            NewCookie cookie = new NewCookie.Builder("scc:session")
                    .value(uid)
                    .path("/")
                    .comment("sessionid")
                    .maxAge(3600)
                    .secure(false)
                    .httpOnly(true)
                    .build();
            rl.addSession(new Session(uid, user.getUserId()));
            return jakarta.ws.rs.core.Response.ok().cookie(cookie).build();
        } else
            throw new NotAuthorizedException("Incorrect login");
    }

    public Session checkCookieUser(Cookie session, String id) throws NotAuthorizedException {
        if (session == null || session.getValue() == null)
            throw new NotAuthorizedException("No session initialized");
        Session s;
        try {
            s = rl.getSession(session.getValue());
        } catch (Exception e) {
            throw new NotAuthorizedException("");
        }
        if (s == null || s.getUser() == null || s.getUser().length() == 0)
            throw new NotAuthorizedException("No valid session initialized");
        if (!s.getUser().equals(id) && !s.getUser().equals("adim"))
            throw new NotAuthorizedException("Invalid user : " + s.getUser());
        return s;
    }

    private UserDAO getUserHelper(String id) {
        // try through cache
        UserDAO user = rl.getUser(id);
        if (user != null) return user;
        // try through database
        CosmosPagedIterable<UserDAO> resGet = db.getUserById(id);
        Iterator<UserDAO> it = resGet.iterator();
        if (!it.hasNext()) return null;
        UserDAO temp = resGet.iterator().next();
        return temp;
    }

    private boolean badParam(String str) {
        return str == null;
    }
    private boolean badUser( User user ) {
        return user == null || badParam(user.getId()) || badParam(user.getName()) || badParam(user.getPwd()) || badParam(user.getNickname()) || badParam(user.getPhotoId());
    }
    private boolean wrongPassword(UserDAO user, String password) {
        return !user.getPwd().equals(password);
    }

    private void printRedisContents() {
        rl.printContents();
    }

    private void clearRedis() {
        rl.clearCache();
    }

    public static void main(String[] args) {

        UserResource ur = new UserResource();

        ur.clearRedis();

        ur.createUser(new User("123","b","c12312asdasd","d", "e"));
        ur.createUser(new User("234","oldname","c12312asdasd","d", "e"));

        ur.printRedisContents();

        System.out.println(ur.getUser("123", "c12312asdasd"));

        ur.updateUser("234", "c12312asdasd", new User("234", "newname", "c12312asdasd", "afdsa", "das"));

        System.out.println(ur.getUser("234", "c12312asdasd"));

        ur.deleteUser("123", "c12312asdasd");

        ur.printRedisContents();

        ur.deleteUser("234", "c12312asdasd");

        ur.printRedisContents();

        System.out.println("Over...");

    }
}
