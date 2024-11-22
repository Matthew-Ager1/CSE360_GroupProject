package Database.Models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Set;  // You can use a Set if you don't want duplicate roles

public class User {
    @BsonId
    private ObjectId id; // MongoDB's unique identifier

    private String email;
    private String username;
    private String password;
    private String name;
    private boolean onePass;
    private int expire;
    private Set<Role> roles; // Change to a Set for multiple roles
    private List<String> groups; // List of group IDs the user belongs to

    // Constructors
    public User() {}

    public User(String email, String username, String password, String name, boolean onePass, int expire, Set<Role> roles, List<String> groups) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.onePass = onePass;
        this.expire = expire;
        this.roles = roles;
        this.groups = groups;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnePass() {
        return onePass;
    }

    public void setOnePass(boolean onePass) {
        this.onePass = onePass;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}

