package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.ViewPagerAdapter;
import com.gyansaarthi.fastbook.Objects.Chunk;

import java.util.ArrayList;
import java.util.List;

public class PageActivity extends AppCompatActivity {
    private static final String TAG = "PageActivity";
    ViewPager viewPager;
    List<Chunk> chunks;
    ViewPagerAdapter mAdapter;
    DatabaseReference bookRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");

        chunks = new ArrayList<>();
        loadBook(bookTitle);
    }

    private void loadBook(String bookTitle){
        Log.d(TAG, "loadBook: ");
        bookRef= FirebaseDatabase.getInstance().getReference("books/"+bookTitle+"/contents");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    chunks.add(new Chunk(
                            ds.child("page_heading").getValue(String.class),
                            ds.child("page_content").getValue(String.class)
                    ));
                }

                long numOfBooks=dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
                //Toast.makeText(ChunkActivity.this, "Num of pages" + numOfBooks, Toast.LENGTH_LONG).show();
                mAdapter= new ViewPagerAdapter(chunks, PageActivity.this);
                viewPager= findViewById(R.id.viewPager);
                viewPager.setAdapter(mAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PageActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            ChunkAdapter adapter = new ChunkAdapter(ChunkActivity.this, chunkList);
        };
        bookRef.addListenerForSingleValueEvent(eventListener);
    }
}