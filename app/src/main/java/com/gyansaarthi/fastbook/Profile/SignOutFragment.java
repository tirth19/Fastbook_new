package com.gyansaarthi.fastbook.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gyansaarthi.fastbook.LoginActivity;
import com.gyansaarthi.fastbook.MainActivity;
import com.gyansaarthi.fastbook.R;

public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar mProgressBar;
    private TextView tvSignout, tvSigningOut;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        tvSignout = view.findViewById(R.id.btnConfirmSignout);
        Button btnConfirmSignout = view.findViewById(R.id.btnConfirmSignout);
        mProgressBar = view.findViewById(R.id.progressBar);
        tvSigningOut = view.findViewById(R.id.tvSigningOut);

        mProgressBar.setVisibility(View.GONE);
        tvSigningOut.setVisibility(View.GONE);

//        setupFireBaseAuth();

        btnConfirmSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to signout");
                mProgressBar.setVisibility(View.VISIBLE);
                tvSigningOut.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                        .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Successfully signed out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Signout Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                getActivity().finish();
            }
        });

        return view;

    }

    //-------------------------------Firebase---------------------------
    //setup the firebase auth object

//    private void setupFireBaseAuth(){
//        Log.d(TAG, "setupFireBaseAuth: setting up firebase");
//
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if(user != null){
//                    //user is signed in
//                    Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
//                }else {
//                    //user is signed out
//                    Log.d(TAG, "onAuthStateChanged: signed out");
//                    Log.d(TAG, "onAuthStateChanged: Navigating back to login screen");
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            }
//        };
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
