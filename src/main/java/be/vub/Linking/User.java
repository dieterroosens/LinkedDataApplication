package be.vub.Linking;

import java.util.Objects;
import java.util.UUID;

public class User {
    private String name;
    private String firstName;
    private String password;
    private String email;
    private String experience;
    private String agentID;

    public User(String name, String firstName, String password, String email, String experience) {
        this.name = name;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.experience = experience;
        this.agentID= UUID.randomUUID().toString();
    }

    public User(String name, String firstName, String password, String email, String experience, String agentID) {
        this.name = name;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.experience = experience;
        this.agentID= agentID;
    }

    public User(String email, String password) {
        this.name = "";
        this.firstName = "";
        this.password = password;
        this.email = email;
        this.experience = "";
    }

    public User() {
        this.name = "unknown";
        this.firstName = "unknown";
        this.password = "unknown";
        this.email = "unknown";
        this.experience = "unknown";
        this.agentID= UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return  Objects.equals(password, user.password) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, firstName, password, email, experience);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAgentID() { return agentID; }
}
