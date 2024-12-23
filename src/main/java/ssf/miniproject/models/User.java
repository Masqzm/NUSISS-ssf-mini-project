package ssf.miniproject.models;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String password;
    private String email;
    private String phoneNo;
    private List<String> jioIDsList;    // to keep track of upcoming jios user is attending
    
    
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public List<String> getJioIDsList() {
        return jioIDsList;
    }
    public void setJioIDsList(List<String> jioIDsList) {
        this.jioIDsList = jioIDsList;
    }
}
