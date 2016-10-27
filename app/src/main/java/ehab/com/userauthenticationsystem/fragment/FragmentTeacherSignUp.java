package ehab.com.userauthenticationsystem.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import ehab.com.userauthenticationsystem.model.Teacher;
import ehab.com.userauthenticationsystem.ui.ActivityTeacherMain;


/**
 * Created by Ehab on 6/18/16.
 */
public class FragmentTeacherSignUp extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    Button btn;
    EditText mUsernameField;
    EditText mEmailField;
    EditText mPasswordField;
    EditText mPhone;
    EditText mLocation;
    EditText mCraft;
    EditText mSSn;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;



    public FragmentTeacherSignUp() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentTeacherSignUp newInstance(int sectionNumber) {
        FragmentTeacherSignUp fragment = new FragmentTeacherSignUp();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_teacher_signup, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) v.findViewById(R.id.Email);
        mPasswordField = (EditText) v.findViewById(R.id.Password1);
        mUsernameField = (EditText) v.findViewById(R.id.fullname);
        mPhone = (EditText) v.findViewById(R.id.mobile);
        mLocation = (EditText) v.findViewById(R.id.Location);
        mCraft = (EditText) v.findViewById(R.id.Craft);
        mSSn = (EditText) v.findViewById(R.id.SSN);
        btn = (Button) v.findViewById(R.id.btnSingup);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = mUsernameField.getText().toString();
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                String location = mLocation.getText().toString();
                String phone = mPhone.getText().toString();
                String craft = mCraft.getText().toString();
                String ssn = mSSn.getText().toString();
                //create new user
                signUp(username, email, password,phone,location,craft,ssn);
                //Toast.makeText(SignUpActivity.this, email, Toast.LENGTH_SHORT).show();
                //Log.i(TAG, email);
            }
        });
        return v;
    }

    public void signUp(final String username, String email, String password, final String phone, final String location, final String craft, final String ssn){

        //Check if the email or password fields are empty
//        if (!validateForm()) {
//            return;
//        }
//
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser(), username,phone,location,craft,ssn);
                        }else {
                            Toast.makeText(getActivity(), "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void onAuthSuccess(FirebaseUser user, String username, String phone, String location, String craft, String ssn) {
        //String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username,phone,location,craft,ssn);

        // Go to MainActivity
        Intent intent = new Intent(getActivity(), ActivityTeacherMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void writeNewUser(String userId, String name,String phone,String location,String craft,String ssn) {
        Teacher teacher = new Teacher(name,phone,location,craft,ssn,userId);

        mDatabase.child("craftsmen").child(userId).setValue(teacher);
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
