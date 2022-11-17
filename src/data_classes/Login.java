package data_classes;

public class Login {


    private String user;
    private String pwd;

    public Login() {}

    public Login(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }
}
