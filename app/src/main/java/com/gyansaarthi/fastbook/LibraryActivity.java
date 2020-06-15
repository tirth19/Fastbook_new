package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.BookCoverAdapter;
import com.gyansaarthi.fastbook.Adapters.LibraryAdapter;
import com.gyansaarthi.fastbook.Home.HomeActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {
    private static final String TAG = "CollectionActivity";

    DatabaseReference userLibRef;
    List<BookCover> bookCoverList, readBookCoverList ;
    private static final int ACTIVITY_NUM = 3;

    LibraryAdapter newAdapter;
    TextView pageName;

    private Context mContext = LibraryActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        bookCoverList = new ArrayList<>();
        readBookCoverList = new ArrayList<>();
        new LoadCollectionAsyncTask().execute("library");
        pageName = findViewById(R.id.discover);
        pageName.setText("Your Library");
        setupBottomNavigationView();
    }
    private class LoadCollectionAsyncTask extends AsyncTask<String, Integer, Long> {

        protected void onPreExecute(){


//            Toast.makeText(getApplicationContext(), "Loading 1st recycler", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Long doInBackground(String... strings) {
            Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
            final String user = FirebaseAuth.getInstance().getUid();
            userLibRef= FirebaseDatabase.getInstance().getReference("users/"+user);

            ValueEventListener eventListenerLibrary = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(!ds.child("finished").getValue(Boolean.class))
                        bookCoverList.add(new BookCover(
                                ds.child("title").getValue(String.class),
                                ds.child("author").getValue(String.class),
                                ds.child("thumbnail").getValue(String.class),
                                ds.child("total_pages").getValue(int.class),
                                ds.child("pages_read").getValue(int.class)
                        ));
                    }

                    long numOfBooks=dataSnapshot.getChildrenCount();
                    Log.d(TAG, "Value is: " + numOfBooks);
                    RecyclerView mainRecycler = findViewById(R.id.library_recycler);
                    initRecyclerView(mainRecycler, bookCoverList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LibraryActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
                //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
            };

            ValueEventListener eventListenerFinished = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.child("finished").getValue(Boolean.class))
                            readBookCoverList.add(new BookCover(
                                    ds.child("title").getValue(String.class),
                                    ds.child("author").getValue(String.class),
                                    ds.child("thumbnail").getValue(String.class),
                                    ds.child("total_pages").getValue(int.class),
                                    ds.child("pages_read").getValue(int.class)
                            ));
                    }

                    long numOfBooks=dataSnapshot.getChildrenCount();
                    Log.d(TAG, "Value is: " + numOfBooks);
                    RecyclerView finishedRecycler = findViewById(R.id.finished_recycler);
                    initRecyclerView(finishedRecycler, readBookCoverList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LibraryActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
                //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
            };


            userLibRef.child(strings[0]).addListenerForSingleValueEvent(eventListenerLibrary);
            userLibRef.child(strings[0]).addListenerForSingleValueEvent(eventListenerFinished);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
        }
    }
    private void initRecyclerView(RecyclerView rview, List<BookCover> bookCovers){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        Collections.shuffle(bookCovers);
        newAdapter = new LibraryAdapter(LibraryActivity.this, bookCovers);
        rview.setAdapter(newAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rview.setLayoutManager(new GridLayoutManager(this, 2));
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
}