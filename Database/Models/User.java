package Database.Models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String middleName;
    private boolean onePass;
    private int expire;
    private Set<Role> roles = new HashSet<>(); // Initialize roles to an empty set
    private List<String> groups;
    private String preferredName;
    private LocalDateTime passwordExpirationDate;
    private Boolean isFinished; // Add finished status
    private String password; // Add password field
    private String id; // Add id field for MongoDB

    public User() {
        // Ensure roles is never null
        this.roles = new HashSet<>();
    }

    public User(String email, String username, String firstName, String lastName, String middleName,
                boolean onePass, int expire, Set<Role> roles, List<String> groups,
                String preferredName, LocalDateTime passwordExpirationDate, Boolean isFinished) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.onePass = onePass;
        this.expire = expire;
        this.roles = (roles != null) ? roles : new HashSet<>();
        this.groups = groups;
        this.preferredName = preferredName;
        this.passwordExpirationDate = passwordExpirationDate;
        this.isFinished = isFinished;
    }

    public String getName() {
        return firstName + " " + lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        this.roles = (roles != null) ? roles : new HashSet<>();
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public LocalDateTime getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    public void setPasswordExpirationDate(LocalDateTime passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
