package ehab.com.userauthenticationsystem.model;

/**
 * Created by Ahmed on 15/06/2016.
 */
public class Teacher {

    public String username;
    public String phone;
    public String location;
    public String craft;
    public String ssn;
    public String uid;

    public Teacher() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Teacher(String username, String phone, String location, String craft, String ssn, String uid) {
        this.username = username;
        this.phone=phone;
        this.location=location;
        this.craft=craft;
        this.ssn=ssn;
        this.uid=uid;
    }
}
