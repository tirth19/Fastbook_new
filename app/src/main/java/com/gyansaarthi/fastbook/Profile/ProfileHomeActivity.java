package com.gyansaarthi.fastbook.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.AchievementAdapter;
//import com.gyansaarthi.fastbook.Models.UserAccountSettings;
//import com.gyansaarthi.fastbook.Models.UserSettings;
import com.gyansaarthi.fastbook.Objects.Achievement;
import com.gyansaarthi.fastbook.R;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
//import com.gyansaarthi.fastbook.Utils.FirebaseMethods;
//import com.gyansaarthi.fastbook.Utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileHomeActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 4;

    private static final String TAG = "ProfileHomeActivity";
    DatabaseReference achievementRef;



    private TextView mDisplayName;
    private CircleImageView mProfilePhoto;
    private Context mContext = ProfileHomeActivity.this;
    private ImageView profilemenu;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, userRef;
    List<Achievement> achievementList ;
    AchievementAdapter mAchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_home);

        mDisplayName = findViewById(R.id.display_name);
        mProfilePhoto = findViewById(R.id.profile_photo);
        profilemenu = findViewById(R.id.profileMenu);
        mDisplayName = findViewById(R.id.username);
        Log.d(TAG, "onCreate: ");

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: emailid: " + mAuth.getCurrentUser().getDisplayName());
        mDisplayName.setText(mAuth.getCurrentUser().getDisplayName());

        Glide.with(this)
                .load(Uri.parse(String.valueOf(mAuth.getCurrentUser().getPhotoUrl())))
                .into(mProfilePhoto);

        achievementList = new ArrayList<>();
        setupBottomNavigationView();
        loadAchievement();
        setupToolbar();
        //Getting user reference in db
        String user = FirebaseAuth.getInstance().getUid();
        userRef= FirebaseDatabase.getInstance().getReference("users/"+user+"/email");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           String emailstring = dataSnapshot.getValue(String.class);
                Toast.makeText(ProfileHomeActivity.this, "Email is " + emailstring, Toast.LENGTH_LONG).show();
                long numOfBooks=dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileHomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        };
        userRef.addListenerForSingleValueEvent(eventListener);

//Code for Streak starts here
        SharedPreferences sharedPreferences = getSharedPreferences("YOUR PREF KEY", Context.MODE_PRIVATE);
        Calendar c = Calendar.getInstance();

        int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR

        int lastDay = sharedPreferences.getInt("YOUR DATE PREF KEY", 0); //If we don't have a saved value, use 0.

        int counterOfConsecutiveDays = sharedPreferences.getInt("YOUR COUNTER PREF KEY", 0); //If we don't have a saved value, use 0.

        if(lastDay == thisDay -1){
            // CONSECUTIVE DAYS
            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;


            sharedPreferences.edit().putInt("YOUR DATE PREF KEY", thisDay);

            sharedPreferences.edit().putInt("YOUR COUNTER PREF KEY", counterOfConsecutiveDays).commit();
        } else {

            sharedPreferences.edit().putInt("YOUR DATE PREF KEY", thisDay);

            sharedPreferences.edit().putInt("YOUR COUNTER PREF KEY", 1).commit();
        }
        //Code for streak ends here

        TextView streakLengthText = (TextView) findViewById(R.id.streakLengthOnProfile);
        streakLengthText.setText(String.valueOf(counterOfConsecutiveDays));
    }

    private void setupToolbar(){





        profilemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loadAchievement(){
        Log.d(TAG, "loadAchievement: ");
        achievementRef= FirebaseDatabase.getInstance().getReference("achievements");
        /*        initAchievementRecyclerView();*/
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    achievementList.add(new Achievement(
                            ds.child("title").getValue(String.class),
                            ds.child("description").getValue(String.class),
                            ds.child("achieved").getValue(String.class),
                            ds.child("target").getValue(String.class),
                            ds.child("thumbnail").getValue(String.class)
                    ));
                }

                long numOfBooks=dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
                initAchievementRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileHomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        };
        achievementRef.addListenerForSingleValueEvent(eventListener);
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: Setting up bottom navigation tab");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }





    private void initAchievementRecyclerView(){
        Log.d(TAG, "initAchievementRecyclerView: ");
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView=findViewById(R.id.achievement_recycler_view);
        mAchAdapter = new AchievementAdapter(ProfileHomeActivity.this, achievementList);
        recyclerView.setAdapter(mAchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //creating adapter object and setting it to recyclerview
//        ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        /*        RecyclerViewAdapter adapter= new RecyclerViewAdapter(mHeadings, mContent, this);*/

    }


//    private void setProfileWidgets(UserSettings userSettings){
//
//        Log.d(TAG, "setProfileWidgets: setting widgets form data retrieved from firebase database " + userSettings.toString());
//        // User user = userSettings.getUser();
//        UserAccountSettings settings = userSettings.getSettings();
//
//        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");
//
//        mDisplayName.setText(settings.getDisplay_name());
//        // mProgressBar.setVisibility(View.GONE);
//
//
//
//    }

    //-------------------------------Firebase---------------------------
    //setup the firebase auth object

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFireBaseAuth: setting up firebase");

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //check the user is logged in

                if(user != null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
                }else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //retrieve user details form the db

//                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));


                //retrieve images for the user in question
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
