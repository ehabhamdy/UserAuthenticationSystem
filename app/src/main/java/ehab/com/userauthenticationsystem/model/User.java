package ehab.com.userauthenticationsystem.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ehab on 10/22/2016.
 */


@IgnoreExtraProperties
public class User {

    public String username;
    public String gradeYear;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String gradeYear){
        this.username = username;
        this.gradeYear = gradeYear;
    }
}