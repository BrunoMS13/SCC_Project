package scc.srv;

import api.RestUsers;
import com.azure.cosmos.util.CosmosPagedIterable;
import utils.CosmosDBLayer;
import data_classes.UserDAO;


public class UserResource implements RestUsers {

    private CosmosDBLayer db;
    private MediaResource mr;

    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
        this.mr = new MediaResource();
    }

    public void createUserWithPhoto(String id, String name, String password, String nickname, String photoId, byte[] photo) {
        System.out.println("Creating user with photo: " + name);
        mr.upload(photo, photoId);
        db.putUser(new UserDAO(id, name, scc.utils.Hash.of(password), nickname, photoId));
    }

    @Override
    public void createUser(String id, String name, String password, String nickname, String photoId) {
        System.out.println("Creating user: " + name);
        db.putUser(new UserDAO(id, name, scc.utils.Hash.of(password), nickname, photoId));
    }

    @Override
    public void deleteUser(String id, String password) {
        System.out.println("Deleting user with ID: " + id);
        UserDAO tempUser = getUser(id, password);
        db.delUser(tempUser);
    }

    @Override
    public void updateUser(String id, String name, String nickname, String password, String photoId) {
        System.out.println("Updating user with ID: " + id);
        UserDAO u = getUser(id, password);
        if (u != null) {
            if(!name.equals("")) u.setName(name);
            if(!nickname.equals("")) u.setName(nickname);
            if(!photoId.equals("")) u.setName(photoId);
            db.updateUser(u);
        }
    }

    @Override
    public UserDAO getUser(String id, String password) {
        try {
            CosmosPagedIterable<UserDAO> resGet = db.getUserById(id);
            UserDAO temp = resGet.iterator().next();

            if (temp.getPwd().equals(scc.utils.Hash.of(password))) {
                return temp;
            }
            System.out.println("Wrong password :/.");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {

        UserResource ur = new UserResource();
        /*
        ur.createUser("a333444","b","c12312asdasd","d", "e");

        UserDAO u = ur.getUser("a333444", "c12312asdasd");

        System.out.println(u);

        ur.deleteUser("a333444", "c12312asdasd");

        System.out.println(ur.getUser("a333444", "c12312asdasd"));
        */
        ur.createUser("a25","b","c12312asdasd","d", "e");

        System.out.println(ur.getUser("a25", "c12312asdasd"));

        ur.updateUser("a25", "c", "e", "c12312asdasd", "r");
        System.out.println("aaaaa");
        System.out.println(ur.getUser("a25", "c12312asdasd"));
        ur.deleteUser("a25", "c12312asdasd");
    }
}
