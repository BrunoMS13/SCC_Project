package srv;

import api.RestUsers;
import com.azure.cosmos.util.CosmosPagedIterable;
import temppackage.CosmosDBLayer;
import temppackage.User;
import temppackage.UserDAO;

public class UserResource implements RestUsers {

    CosmosDBLayer db;

    public UserResource() {
        this.db = CosmosDBLayer.getInstance();
    }

    @Override
    public void createUser(UserDAO user) {
        db.putUser(user);
    }

    @Override
    public boolean deleteUser(String nickname, String password) {
        UserDAO temp = this.getUser(nickname, password);
        if (temp != null) {
            db.delUser(temp);
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(String name, String nickname, String password, byte[] photo) {

    }

    @Override
    public UserDAO getUser(String nickname, String password) {
        CosmosPagedIterable<UserDAO> resGet = db.getUser(nickname);
        UserDAO temp = resGet.iterator().next();
        if (temp.getPwd().equals(scc.utils.Hash.of(password))) {
            return temp;
        }
        return null;
    }
}
