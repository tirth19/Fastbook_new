package com.gyansaarthi.fastbook.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.gyansaarthi.fastbook.Adapters.RecyclerViewAdapter;
import com.gyansaarthi.fastbook.BookDescriptionActivity;
//import com.gyansaarthi.fastbook.Login.LoginActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.R;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private ArrayList<String> mAuthor = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mBookName = new ArrayList<>();

    RecyclerViewAdapter mAdapter;
    DatabaseReference homepageRef;

    private Context mContext = HomeActivity.this;
    String featuredBook;

//    //firebase
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.. ");

        RecyclerView mainrecycler = findViewById(R.id.recycler_view);
        RecyclerView hindirecycler = findViewById(R.id.recycler_view2);

        initCollection("homepage", mainrecycler);
        initCollection2("hinditop10", hindirecycler);
        featuredBook="12rulesforlife";
        initFeaturedbook();

//        setupFireBaseAuth();
//        initImageLoader();
        setupBottomNavigationView();
//        setupViewPager();

    }

    private void initCollection(String collectionname, final RecyclerView rview){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        homepageRef= FirebaseDatabase.getInstance().getReference("collections");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String author = ds.child("author").getValue(String.class);
                    mAuthor.add(author);
                    String title = ds.child("title").getValue(String.class);
                    mBookName.add(title);
                    String thumbnail = ds.child("thumbnail").getValue(String.class);
                    mImageUrls.add(thumbnail);
                    Log.d("TAG", author + title);
                }

                long numOfBooks=dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
                initRecyclerView(rview);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
        };
        homepageRef.child(collectionname).addListenerForSingleValueEvent(eventListener);
    }
    private void initCollection2(String collectionname, final RecyclerView rview2){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        homepageRef= FirebaseDatabase.getInstance().getReference("collections");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String author = ds.child("author").getValue(String.class);
                    mAuthor.add(author);
                    String title = ds.child("title").getValue(String.class);
                    mBookName.add(title);
                    String thumbnail = ds.child("thumbnail").getValue(String.class);
                    mImageUrls.add(thumbnail);
                    Log.d("TAG", author + title);
                }

                long numOfBooks=dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
                initRecyclerView2(rview2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
        };
        homepageRef.child(collectionname).addListenerForSingleValueEvent(eventListener);
    }


    private void initRecyclerView(RecyclerView rview){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        rview.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    }

    private void initRecyclerView2(RecyclerView rview){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        rview.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
    }
    private void initFeaturedbook(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        homepageRef= FirebaseDatabase.getInstance().getReference("collections/featured_books");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String featuredBook1 = dataSnapshot.getValue(String.class);
                DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference("books/"+featuredBook1);
                ValueEventListener otherEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String thumbnail = dataSnapshot.child("thumbnail").getValue(String.class);
                        ImageView todaysImageView=findViewById(R.id.todaysImageView);
                        Glide.with(mContext)
                                .asBitmap()
                                .load(thumbnail)
                                .into(todaysImageView);
                        CardView todaysCardView = findViewById(R.id.todayscard);
                        todaysCardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //create a Bundle object
                                Bundle extras = new Bundle();
                                //Adding key value pairs to this bundle
                                //there are quite a lot data types you can store in a bundle
                                extras.putString("BOOK_NAME",featuredBook1);
                                Intent intent = new Intent(mContext, BookDescriptionActivity.class);
                                intent.putExtras(extras);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                    }
                };
                bookRef.addListenerForSingleValueEvent(otherEventListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        };
        homepageRef.addListenerForSingleValueEvent(eventListener);

    }


    private void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
//        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /*
    Responsible for adding 3 tabs: camera, home, message
    */
    public void setupViewPager(){
//        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new HomeFragment());
//        ViewPager viewPager = findViewById(R.id.container);
//        viewPager.setAdapter(adapter);
//
//        TabLayout tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager );
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
//        tabLayout.getTabAt(1).setIcon(R.drawable.insta_logo);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);

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


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        }
    }
