package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.BookCoverAdapter;
import com.gyansaarthi.fastbook.Adapters.ChunkAdapter;
import com.gyansaarthi.fastbook.Adapters.SimpleListAdapter;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.Objects.Chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndexActivity extends AppCompatActivity {
    private static final String TAG = "IndexActivity";

    DatabaseReference bookRef;
    List<Chunk> chunkList ;

    SimpleListAdapter newAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        chunkList = new ArrayList<>();
        new LoadIndexAsyncTask().execute(bookTitle);
    }
    private class LoadIndexAsyncTask extends AsyncTask<String, Integer, Long> {

        protected void onPreExecute(){


//            Toast.makeText(getApplicationContext(), "Loading 1st recycler", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected Long doInBackground(String... strings) {
            Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
            bookRef= FirebaseDatabase.getInstance().getReference("books/"+strings[0]+"/contents");
            final String bookName = strings[0];
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        chunkList.add(new Chunk(
                                ds.child("page_heading").getValue(String.class),
                                ds.child("page_content").getValue(String.class)
                        ));
                    }

                    long numOfBooks=dataSnapshot.getChildrenCount();
                    Log.d(TAG, "Value is: " + numOfBooks);
                    RecyclerView mainRecycler = findViewById(R.id.indexRecycler);
                    initRecyclerView(mainRecycler, chunkList, bookName);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(IndexActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
                }
                //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
            };
            bookRef.addListenerForSingleValueEvent(eventListener);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Long result) {
        }
    }
    private void initRecyclerView(RecyclerView rview, List<Chunk> chunkList, String bookTitle){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        mAdapter = new RecyclerViewAdapter(HomeActivity.this, mAuthor, mImageUrls, mBookName);
        newAdapter = new SimpleListAdapter(IndexActivity.this, chunkList, bookTitle);
        rview.setAdapter(newAdapter);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        rview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
