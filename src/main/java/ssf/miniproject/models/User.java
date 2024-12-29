package ssf.miniproject.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 16, message = "Username must be 3-16 characters long")
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Only alphanumeric characters and underscores are allowed")
    private String name;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a vaild email")
    private String email;

    private List<String> jioIDsList;    // to keep track of upcoming jios user is attending
    //private List<Restaurant> favRestaurantsList;      // add-on feature

    public User() {}
    public User(String name, String password, String email, List<String> jioIDsList) {
        this.name = name;
        this.password = password;
        this.email = email;

        this.jioIDsList = jioIDsList;
    }

    public static User jsonToUser(String json) {
        if (json == null)
            return null;
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        List<String> jioIDs = new ArrayList<>(); 

        for(int i = 0; i < j.getJsonArray("jioIDs").size(); i++) 
            jioIDs.add(j.getJsonArray("jioIDs").getString(i));

        User user = new User(j.getString("name"),
                        j.getString("password"),
                        j.getString("email"),
                        jioIDs);

        return user;
    }

    public String toJson() {
        JsonArrayBuilder jArrBuilder = Json.createArrayBuilder();

        // If jioIDsList is null, empty JSON array will be built (arr = [])
        if(jioIDsList != null && !jioIDsList.isEmpty()) {
            for(String jioID : jioIDsList) 
                jArrBuilder.add(jioID);
        }

        JsonObject job = Json.createObjectBuilder()
                        .add("name", name)
                        .add("password", password)
                        .add("email", email)
                        .add("jioIDs", jArrBuilder.build())
                        .build();

        return job.toString();
    }
    
    @Override
    public String toString() {
        return toJson();
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
    public List<String> getJioIDsList() {
        return jioIDsList;
    }
    public void setJioIDsList(List<String> jioIDsList) {
        this.jioIDsList = jioIDsList;
    }
}
