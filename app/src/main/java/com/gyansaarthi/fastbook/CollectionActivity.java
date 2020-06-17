package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.BookCoverAdapter;
import com.gyansaarthi.fastbook.Objects.BookCover;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    private static final String TAG = "CollectionActivity";

    DatabaseReference homepageRef;
    List<BookCover> bookCoverList ;

    BookCoverAdapter newAdapter;
    TextView pageName;

    private Context mContext = CollectionActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        final String collectionTitle= getIntent().getExtras().getString("COLLECTION_NAME");
        bookCoverList = new ArrayList<>();
        new LoadCollectionAsyncTask().execute(collectionTitle);
        pageName = findViewById(R.id.discover);
        pageName.setText(collectionTitle);
    }
    private class LoadCollectionAsyncTask extends AsyncTask<String, Integer, Long> {

        protected void onPreExecute(){
//            Toast.makeText(getApplicationContext(), "Loading 1st recycler", Toast.LENGTH_SHORT).show();
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
                    RecyclerView mainRecycler = findViewById(R.id.collection_recycler);
                    initRecyclerView(mainRecycler, bookCoverList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CollectionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
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
        }
    }
    private void initRecyclerView(RecyclerView rview, List<BookCover> bookCovers){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        Collections.shuffle(bookCovers);
        newAdapter = new BookCoverAdapter(CollectionActivity.this, bookCovers);
        rview.setAdapter(newAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rview.setLayoutManager(new GridLayoutManager(this, 2));
    }
}