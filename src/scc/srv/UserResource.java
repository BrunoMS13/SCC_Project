package scc.srv;

import api.RestUsers;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import utils.CosmosDBLayer;
import data_classes.UserDAO;
import utils.RedisLayer;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Set;


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
        mr.upload(photo, user.getPhotoId());
        db.putUser(new UserDAO(user));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(User user) throws WebApplicationException {
        System.out.println("Creating user...");
        if (badUser(user))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (getUserHelper(user.getId()) != null)
            throw new WebApplicationException(Response.Status.CONFLICT);
        // add to database
        CosmosItemResponse<UserDAO> udao = db.putUser(new UserDAO(user));
        // add to cache
        rl.addUser(udao.getItem());
    }

    public void deleteUser(String id, String password) throws WebApplicationException {
        System.out.println("Deleting user...");
        User user = getUser(id, password);
        // delete from database
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
        // update cache
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
