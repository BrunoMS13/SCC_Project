package scc.srv;

import api.RestAuctions;
import api.RestMedia;
import api.RestUsers;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;
import utils.CosmosDBLayer;
import utils.RedisLayer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public class UserResource implements RestUsers {

    private CosmosDBLayer db;
    private RedisLayer rl;
    private RestAuctions ar;
    private RestMedia mr;


    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
        this.rl = RedisLayer.getInstance();
        this.ar = new AuctionResource();
        this.mr = new MediaResource();
    }

    public void createUserWithPhoto(User user, byte[] photo) {
        System.out.println("Creating user with photo...");
        mr.upload(photo);
        createUser(user);
    }

    @Override
    public User createUser(User user) throws WebApplicationException {
        System.out.println("Creating user...");
        if (badUser(user)) {
            System.out.println("Bad user...");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        CosmosItemResponse<UserDAO> udao = db.putUser(new UserDAO(user));
        rl.addUser(udao.getItem());
        System.out.println(user);
        return user;
    }

    @Override
    public User deleteUser(@CookieParam("scc:session") Cookie session, String id) throws WebApplicationException {
        System.out.println("Deleting user with id: " + id);
        checkCookieUser(session, id);
        UserDAO userDAO = getUserHelper(id);
        ar.updateAuctionsOfDeletedUser(id);
        db.delUser(userDAO);
        rl.deleteUser(id);
        return userDAO.toUser();
    }

    @Override
    public User updateUser(@CookieParam("scc:session") Cookie session, User user) throws WebApplicationException {
        System.out.println("Updating user... " + user.toString());
        checkCookieUser(session, user.getId());
        updateDataBases(new UserDAO(user));
        return user;
    }

    @Override
    public List<Auction> getUserAuctions(Cookie session, String status) throws WebApplicationException {
        Session s = rl.getSession(session.getValue());
        System.out.println("Getting auctions of user= " + s.getUser() + " with status= " + status);
        if (s == null || s.getUser() == null || s.getUser().length() == 0)
            throw new NotAuthorizedException("No valid session initialized");
        return ar.getUserAuctions(s.getUser(), status);
    }

    @Override
    public jakarta.ws.rs.core.Response auth(Login user) {
        System.out.println("Authorizing user... " + user.getUser() + " " + user.getPwd());
        boolean pwd0k = getUser(user.getUser(), user.getPwd()) != null;

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
            rl.addSession(new Session(user.getUser(), uid));
            return jakarta.ws.rs.core.Response.ok().cookie(cookie).build();
        } else
            throw new NotAuthorizedException("Incorrect login");
    }

    private User getUser(String id, String password) throws WebApplicationException {
        if (badParam(id))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        UserDAO userDAO = getUserHelper(id);
        if (userDAO == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if (badParam(password) || wrongPassword(userDAO, password))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        return userDAO.toUser();
    }

    private UserDAO getUserHelper(String id) {
        UserDAO user = rl.getUser(id);
        if (user != null)
            return user;

        Iterator<UserDAO> it = db.getUserById(id).iterator();
        if (!it.hasNext())
            return null;
        UserDAO temp = it.next();
        rl.addUser(temp);
        return temp;
    }

    private Session checkCookieUser(Cookie session, String id) {
        if (session == null || session.getValue() == null)
            throw new NotAuthorizedException("No session initialized");
        Session s = rl.getSession(session.getValue());
        System.out.println(s.getUser() + " == " + id);
        if (s == null || s.getUser() == null || s.getUser().length() == 0)
            throw new NotAuthorizedException("No valid session initialized");
        if (!s.getUser().equals(id) && !s.getUser().equals("admin"))
            throw new NotAuthorizedException("Invalid user : " + s.getUser());
        return s;
    }

    private void updateDataBases(UserDAO userDAO) {
        db.updateUser(userDAO);
        rl.updateUser(userDAO);
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

    public static void main(String[] args) {

        UserResource ur = new UserResource();

        //ur.createUser(new User());
        /**
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
        */
    }
}
