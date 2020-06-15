package com.gyansaarthi.fastbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Adapters.ViewPagerAdapter;
import com.gyansaarthi.fastbook.Home.HomeActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.Objects.Chunk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PageActivity extends AppCompatActivity {
    private static final String TAG = "PageActivity";
    ViewPager viewPager;
    List<Chunk> chunks;
    ViewPagerAdapter mAdapter;
    DatabaseReference bookRef, bookCoverRef;
    Context mContext;
    private static int currentPage, numOfBooks;
    List<BookCover> bookCoverListrand;
    ImageView mIndex;
    Button mMarkRead;
    RelativeLayout relativeLayout;
    boolean gone = false;
    private String bookName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        final int chapter= getIntent().getExtras().getInt("CHAPTER");

        chunks = new ArrayList<>();
        bookCoverListrand = new ArrayList<>();
        mContext=this;
        loadBook(bookTitle, chapter);
        TextView pageNumberTextView;

        pageNumberTextView = findViewById(R.id.pageNumberTextView);
        pageNumberTextView.setText(1 + " of " + numOfBooks);
        mMarkRead = findViewById(R.id.btn_markRead);
        mMarkRead.setVisibility(View.GONE);
        mIndex = findViewById(R.id.index);
        mIndex.setOnClickListener(new View.OnClickListener() {
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


        @SuppressLint("ClickableViewAccessibility")
        public void onPageSelected(int position) {
            Log.i(TAG, "page selected " + position);
            currentPage = position+1;
            Log.i(TAG, "page selected " + currentPage);
            TextView pageNumberTextView;

            pageNumberTextView = findViewById(R.id.pageNumberTextView);
            pageNumberTextView.setText(currentPage + " of " + numOfBooks);

            if(currentPage == numOfBooks){
                mMarkRead.setVisibility(View.VISIBLE);
                mMarkRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                mMarkRead.setVisibility(View.GONE);
            }

            mMarkRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle extras = new Bundle();
                    final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
                    //Adding key value pairs to this bundle
                    //there are quite a lot data types you can store in a bundle
                    extras.putString("BOOK_NAME",bookTitle);
                    extras.putString("BOOK_DISPLAY_NAME",bookName);

                    Intent intent = new Intent(PageActivity.this, BookCompleteActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });

//            final LinearLayout extraOptions = findViewById(R.id.extraOptions);
//            final RelativeLayout relativeLayout1 = findViewById(R.id.pageRelLayout);
//
//            relativeLayout1.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    if(event.getAction() == MotionEvent.ACTION_DOWN){
//                        if(!gone){
//                            extraOptions.setVisibility(View.GONE);
//                            gone = true;
//                        }else{
//                            extraOptions.setVisibility(View.VISIBLE);
//                            gone = false;
//                        }
//                    }
//
//                    return true;
//                }
//            });

        }
    }

    private void loadBook(String bookTitle, final int chapter){
        Log.d(TAG, "loadBook: ");
        bookRef= FirebaseDatabase.getInstance().getReference("books/"+bookTitle);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("contents").getChildren()) {
                    chunks.add(new Chunk(
                            ds.child("page_heading").getValue(String.class),
                            ds.child("page_content").getValue(String.class)
                    ));
                }
                bookName =dataSnapshot.child("book_title").getValue(String.class);

                numOfBooks = (int) dataSnapshot.child("contents").getChildrenCount();
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

        bookCoverRef= FirebaseDatabase.getInstance().getReference("collections/homepage/"+bookTitle);
        ValueEventListener eventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    bookCoverListrand.add(new BookCover(
                            ds.child("title").getValue(String.class),
                            ds.child("author").getValue(String.class),
                            ds.child("thumbnail").getValue(String.class),
                            10, 0
                    ));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PageActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
            //creating adapter object and setting it to recyclerview
//            BookAdapter adapter = new BookAdapter(MainActivity.this, bookList);
        };
        bookCoverRef.addListenerForSingleValueEvent(eventListener2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int pageNumber = viewPager.getCurrentItem()+1;
        final String user = FirebaseAuth.getInstance().getUid();
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");

        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("users/"+user);
        userRef.child("/library").child(bookTitle).child("pages_read").setValue(pageNumber);
    }
}