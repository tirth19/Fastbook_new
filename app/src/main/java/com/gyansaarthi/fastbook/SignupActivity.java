package com.gyansaarthi.fastbook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gyansaarthi.fastbook.Home.HomeActivity;
import com.gyansaarthi.fastbook.Objects.User;

public class SignupActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 123;
    SignInButton googleSignIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    ImageView logo;
    TextView signInActivity;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_signup);
        googleSignIn = findViewById(R.id.btn_google_signin);
        signInActivity= findViewById(R.id.link_to_sign_in_activity);
        logo = findViewById(R.id.logo);
        logo.setClipToOutline(true);

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("192306257951-45p9l82ivn9n3b8i40nvq1t3bj6uvjoq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);

        mDatabase = FirebaseDatabase.getInstance().getReference();



        //check if user loggedin earlier
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            //send user to home activity
            startActivity(new Intent(this, HomeActivity.class));

        }
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_popup = signInClient.getSignInIntent();
                startActivityForResult(signin_popup, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });
        signInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInActivity = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(signInActivity);
            }
        });
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
        mDatabase.child("users").child(userId).child("stats").child("xp_score").setValue(100);
        mDatabase.child("users").child(userId).child("stats").child("total_books_read").setValue(0);
        mDatabase.child("users").child(userId).child("stats").child("streak_length").setValue(0);
        mDatabase.child("users").child(userId).child("stats").child("invites").setValue(0);
        mDatabase.child("users").child(userId).child("stats").child("pages_read").setValue(0);
        mDatabase.child("users").child(userId).child("stats").child("last_login").setValue(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = signInAccountTask.getResult(ApiException.class);
                assert signInAccount != null;
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "Your google account is connected to Fastbook ", Toast.LENGTH_SHORT).show();

                        //if user does no exists - add him to firebase database
                        FirebaseUser user = task.getResult().getUser();
                        writeNewUser(user.getUid(), usernameFromEmail(user.getEmail()), user.getEmail());


                        //check if user already exists
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}