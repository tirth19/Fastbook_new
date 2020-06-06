package com.gyansaarthi.fastbook;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.ChunkAdapter;
import com.gyansaarthi.fastbook.Objects.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkActivity extends AppCompatActivity {

    private static final String TAG = "ChunkActivity";
    DatabaseReference bookRef;
    List<Chunk> chunkList ;
    ChunkAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chunk);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        Log.d(TAG, "onCreate: ");

        //getting the recyclerview from xml
/*        recyclerView = findViewById(R.id.recyclerViewChunk);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        chunkList = new ArrayList<>();
       loadBook(bookTitle);
//        new FetchAsyncTask(FirebaseDatabase.getInstance().getReference("books/"+bookTitle), chunkList, mAdapter, ChunkActivity.this).execute();
//        new FetchAsyncTask(chunkList, mAdapter, this).execute();
    }
    private void loadBook(String bookTitle){
        Log.d(TAG, "loadBook: ");
        bookRef= FirebaseDatabase.getInstance().getReference("books/"+bookTitle+"/contents");

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
                initChunkRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChunkActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        };
        bookRef.addListenerForSingleValueEvent(eventListener);
    }
    private void initChunkRecyclerView(){
        Log.d(TAG, "initChunkRecyclerView: ");
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView=findViewById(R.id.recyclerViewChunk);
        mAdapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        //creating adapter object and setting it to recyclerview
//        ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
/*        RecyclerViewAdapter adapter= new RecyclerViewAdapter(mHeadings, mContent, this);*/

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
