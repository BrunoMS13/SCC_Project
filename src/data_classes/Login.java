package data_classes;

public class Login {


    private String userId;
    private String pwd;

    public Login() {}

    public Login(String userId, String pwd) {
        this.userId = userId;
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getPwd() {
        return pwd;
    }
}
