package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
    Context mContext;
    private static int currentPage, numOfBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        final int chapter= getIntent().getExtras().getInt("CHAPTER");

        chunks = new ArrayList<>();
        mContext=this;
        loadBook(bookTitle, chapter);
        TextView pageNumberTextView;

        pageNumberTextView = findViewById(R.id.pageNumberTextView);
        pageNumberTextView.setText(1 + " of " + numOfBooks);
        pageNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString("BOOK_NAME",bookTitle);
                Intent chunkIntent = new Intent(getApplicationContext(), IndexActivity.class);
                chunkIntent.putExtras(extras);
                startActivity(chunkIntent);
            }
        });
    }

    private class PageListener extends ViewPager.SimpleOnPageChangeListener {


        public void onPageSelected(int position) {
            Log.i(TAG, "page selected " + position);
            currentPage = position+1;
            Log.i(TAG, "page selected " + currentPage);
            TextView pageNumberTextView;

            pageNumberTextView = findViewById(R.id.pageNumberTextView);
            pageNumberTextView.setText(currentPage + " of " + numOfBooks);

        }
    }

    private void loadBook(String bookTitle, final int chapter){
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

                numOfBooks = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, "Value is: " + numOfBooks);
                //Toast.makeText(ChunkActivity.this, "Num of pages" + numOfBooks, Toast.LENGTH_LONG).show();
                mAdapter= new ViewPagerAdapter(chunks, PageActivity.this);
                viewPager= findViewById(R.id.viewPager);
                viewPager.setAdapter(mAdapter);
                int pagePosition=viewPager.getCurrentItem()+1;
                PageListener pageListener = new PageListener();
                viewPager.setOnPageChangeListener(pageListener);
                viewPager.setCurrentItem(chapter);

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