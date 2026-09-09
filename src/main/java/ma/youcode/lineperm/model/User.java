package ma.youcode.lineperm.model;

public class User {
    public String user;
    public String password;

    public User(String user , String password){
        this.user = user;
        this.password = password;
    }

    public String getUser(){
        return user;
    }

    public String getPassword(){
        return password;
    }
}