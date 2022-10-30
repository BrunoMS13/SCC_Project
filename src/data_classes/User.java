package data_classes;

/**
 * Represents a User, as returned to the clients
 */
public class User {
	private String id;
	private String name;
	private String pwd;
	private String nickname;
	private String photoId;
	public User() {}
	public User(String id, String name, String pwd, String nickname, String photoId) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.nickname = nickname;
		this.photoId = photoId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getNickname() {return this.nickname;}
	public void setNickname(String nickname) {this.nickname = nickname;}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhoto(String photo) {
		this.photoId = photoId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + "]";
	}

}
