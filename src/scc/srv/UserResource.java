package scc.srv;

import api.RestUsers;
import com.azure.cosmos.util.CosmosPagedIterable;
import temppackage.CosmosDBLayer;
import temppackage.UserDAO;

import javax.ws.rs.QueryParam;

public class UserResource implements RestUsers {

    CosmosDBLayer db;

    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
    }

    @Override
    public void createUser(String id, String name, @QueryParam(PASSWORD) String password, String nickname, String photoId) {
        System.out.println("Creating user -> " + name);
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
        ur.createUser("a333444","b","c12312asdasd","d", "e");

        UserDAO u = ur.getUser("a333444", "c12312asdasd");

        System.out.println(u);

        ur.deleteUser("a333444", "c12312asdasd");
    }
}
