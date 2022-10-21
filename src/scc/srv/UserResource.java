package scc.srv;

import api.RestUsers;
import com.azure.cosmos.util.CosmosPagedIterable;
import data_classes.User;
import utils.CosmosDBLayer;
import data_classes.UserDAO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Iterator;


public class UserResource implements RestUsers {

    private CosmosDBLayer db;
    private MediaResource mr;

    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
    }

    public void createUserWithPhoto(User user, byte[] photo) throws WebApplicationException {
        System.out.println("Creating user with photo...");
        if (badUser(user))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (getUserHelper(user.getId()) != null)
            throw new WebApplicationException(Response.Status.CONFLICT);
        mr.upload(photo, user.getPhotoId());
        db.putUser(new UserDAO(user));
    }

    @Override
    public void createUser(User user) throws WebApplicationException {
        System.out.println("Creating user...");
        if (badUser(user))
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        if (getUserHelper(user.getId()) != null)
            throw new WebApplicationException(Response.Status.CONFLICT);
        db.putUser(new UserDAO(user));
    }

    @Override
    public void deleteUser(String id, String password) throws WebApplicationException {
        System.out.println("Deleting user...");
        User user = getUser(id, password);
        db.delUser(new UserDAO(user));
    }

    @Override
    public void updateUser(String id, String password, User user) throws WebApplicationException {
        System.out.println("Updating user...");
        User u = getUser(id, password);
        if (!u.getId().equals(user.getId()))
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        db.updateUser(new UserDAO(user));
    }

    @Override
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

    public static void main(String[] args) {

        UserResource ur = new UserResource();

        ur.createUser(new User("addd25","b","c12312asdasd","d", "e"));

        System.out.println(ur.getUser("addd25", "c12312asdasd"));

        ur.updateUser("addd25", "c12312asdasd", new User("addd25", "c12312asdasd", "c", "afdsa", "das"));

        System.out.println(ur.getUser("addd25", "c"));

        ur.deleteUser("addd25", "c");

        System.out.println("Over...");

    }
}
