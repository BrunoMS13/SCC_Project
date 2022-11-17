package data_classes;

public class Session {

    private String user;

    private String token;

    public Session() {}
    public Session(String user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

}
