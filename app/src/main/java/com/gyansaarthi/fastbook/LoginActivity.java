package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.auth.GoogleAuthProvider;
import com.gyansaarthi.fastbook.Home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 123;
    SignInButton googleSignIn;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleSignIn = findViewById(R.id.btn_google_signin);

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("192306257951-45p9l82ivn9n3b8i40nvq1t3bj6uvjoq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, gso);


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
}