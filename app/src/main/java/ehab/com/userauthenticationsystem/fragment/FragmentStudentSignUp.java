package ehab.com.userauthenticationsystem.fragment;

/**
 * Created by Ehab on 6/18/16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ehab.com.userauthenticationsystem.R;
import ehab.com.userauthenticationsystem.model.User;
import ehab.com.userauthenticationsystem.ui.ActivityLogin;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentStudentSignUp extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = "Message";
    private static final String ARG_SECTION_NUMBER = "section_number";


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    EditText mUsernameField;
    EditText mEmailField;
    EditText mPasswordField;

    private ProgressDialog mProgressDialog;

    public FragmentStudentSignUp() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentStudentSignUp newInstance(int sectionNumber) {
        FragmentStudentSignUp fragment = new FragmentStudentSignUp();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_signup, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) rootView.findViewById(R.id.emailEditText);
        mPasswordField = (EditText) rootView.findViewById(R.id.passwordEditText);
        mUsernameField = (EditText) rootView.findViewById(R.id.usernameEditText);

        final Button signupBtn = (Button) rootView.findViewById(R.id.signupButton);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameField.getText().toString();
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();

                //create new user
                signUp(username, email, password);
                //Toast.makeText(SignUpActivity.this, email, Toast.LENGTH_SHORT).show();
                //Log.i(TAG, email);


            }
        });

        return rootView;
    }

    public void signUp(final String username, String email, String password){

        //Check if the email or password fields are empty
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), username);
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                        }else {
                            Toast.makeText(getActivity(), "Sign Up Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError("Required");
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError("Required");
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    private void onAuthSuccess(FirebaseUser user, String username) {
        //String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username);

        // Go to MainActivity
//        Intent intent = new Intent(getActivity(), ActivityUserMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);

        Toast.makeText(getActivity(), "Check your email inbox to verify your email and login here", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), ActivityLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        //finish();
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name) {
        User user = new User(name);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }




    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
