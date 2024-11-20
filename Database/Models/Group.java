package Database.Models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import java.util.List;

public class Group {
    @BsonId
    private ObjectId id; // MongoDB's unique identifier

    private String name;
    private boolean isSpecialAccess; // Indicates if it's a Special Access Group (SAG)
    private List<String> adminUsers; // Usernames with admin rights
    private List<String> instructors; // Usernames with instructor rights
    private List<String> students; // Usernames with student access

    // Constructors
    public Group() {}

    public Group(String name, boolean isSpecialAccess, List<String> adminUsers, List<String> instructors, List<String> students) {
        this.name = name;
        this.isSpecialAccess = isSpecialAccess;
        this.adminUsers = adminUsers;
        this.instructors = instructors;
        this.students = students;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public boolean isSpecialAccess() {
		return isSpecialAccess;
	}

	public void setSpecialAccess(boolean specialAccess) {
		isSpecialAccess = specialAccess;
	}

	public List<String> getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(List<String> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public List<String> getInstructors() {
		return instructors;
	}

	public void setInstructors(List<String> instructors) {
		this.instructors = instructors;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}
}
