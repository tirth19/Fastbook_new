package com.gyansaarthi.fastbook.Home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.BookCoverAdapter;
import com.gyansaarthi.fastbook.BookDescriptionActivity;
//import com.gyansaarthi.fastbook.Login.LoginActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.R;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private ArrayList<String> mAuthor = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mBookName = new ArrayList<>();

    List<BookCover> bookCoverList, bookCoverList2 ;

    BookCoverAdapter newAdapter;
    DatabaseReference homepageRef;
    ProgressBar loadingProgressBar;

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
        bookCoverList = new ArrayList<>();
        bookCoverList2 = new ArrayList<>();

        loadingProgressBar=findViewById(R.id.loadingPanel);
        loadingProgressBar.setVisibility(View.VISIBLE);
 //       initCollection("homepage", mainrecycler);
        new DownloadFilesTask().execute("homepage");
        new DownloadFilesTask2().execute("hinditop10");
        initFeaturedbook();

//        setupFireBaseAuth();
//        initImageLoader();
        setupBottomNavigationView();
//        setupViewPager();

    }
    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {

        protected void onPreExecute(){


            Toast.makeText(getApplicationContext(), "Loading 1st recycler", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Long doInBackground(String... strings) {
            Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
            homepageRef= FirebaseDatabase.getInstance().getReference("collections");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        bookCoverList.add(new BookCover(
                                ds.child("title").getValue(String.class),
                                ds.child("author").getValue(String.class),
                                ds.child("thumbnail").getValue(String.class),
                                10, 0
                        ));
                    }

                    long numOfBooks=dataSnapshot.getChildrenCount();
                    Log.d(TAG, "Value is: " + numOfBooks);
                    RecyclerView mainRecycler = findViewById(R.id.recycler_view);
                    RecyclerView hindiRecycler = findViewById(R.id.recycler_view2);
                    initRecyclerView(mainRecycler, bookCoverList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
                //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
            };
            homepageRef.child(strings[0]).addListenerForSingleValueEvent(eventListener);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    private class DownloadFilesTask2 extends AsyncTask<String, Integer, Long> {

        protected void onPreExecute(){

            Toast.makeText(getApplicationContext(), "Loading 1st recycler", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Long doInBackground(String... strings) {
            Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
            homepageRef= FirebaseDatabase.getInstance().getReference("collections");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        bookCoverList2.add(new BookCover(
                                ds.child("title").getValue(String.class),
                                ds.child("author").getValue(String.class),
                                ds.child("thumbnail").getValue(String.class),
                                10, 0
                        ));
                    }

                    long numOfBooks=dataSnapshot.getChildrenCount();
                    Log.d(TAG, "Value is: " + numOfBooks);
                    RecyclerView hindiRecycler = findViewById(R.id.recycler_view2);
                    initRecyclerView(hindiRecycler, bookCoverList2);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
                //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
            };
            homepageRef.child(strings[0]).addListenerForSingleValueEvent(eventListener);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
//            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(RecyclerView rview, List<BookCover> bookCovers){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        newAdapter = new BookCoverAdapter(HomeActivity.this, bookCovers);
        rview.setAdapter(newAdapter);
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
