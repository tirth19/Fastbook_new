package com.gyansaarthi.fastbook;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BookDescriptionActivity extends AppCompatActivity {
    DatabaseReference bookNodeRef, bookNameRef;
    Context mContext;
    Boolean bookFinished;


    private static final String TAG = "BookDescriptionActivity";
    private static final int ACTIVITY_NUM = 1;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        final int pagesRead= getIntent().getExtras().getInt("CHAPTER");
        bookNodeRef= FirebaseDatabase.getInstance().getReference("books/"+bookTitle);

        Button readButton = (Button) findViewById(R.id.readNowButton);
        final TextView bookName = findViewById(R.id.bookTitleTextView);
        final TextView authorTextView = findViewById(R.id.authorTextView);
        final TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        mContext= this;

      //  setupBottomNavigationView();
   /*     bookNameRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String author = dataSnapshot.child("bookAuthor").getValue(String.class);
                    authorView.setText(author);
                    String title = dataSnapshot.child("bookTitle").getValue(String.class);
                    bookName.setText(title);
                    String description = dataSnapshot.child("bookDescription").getValue(String.class);
                    descriptionView.setText(description);
*//*                    String thumbnail = dataSnapshot.child("thumbnail").getValue(String.class);
                    mImageUrls.add(thumbnail);*//*
                    Log.d("TAG", author + title);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDescriptionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });*/
        bookNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("book_title").getValue(String.class);
                String author= dataSnapshot.child("book_author").getValue(String.class);
                String thumbnail= dataSnapshot.child("thumbnail").getValue(String.class);
                String description= dataSnapshot.child("book_description").getValue(String.class);
                long totalPages=dataSnapshot.child("contents").getChildrenCount();
                bookFinished = false;
                bookName.setText(title);
                final ImageView bookCover = findViewById(R.id.bookcoverImageView);
                bookCover.setClipToOutline(true);
                Glide.with(mContext).load(thumbnail).into(bookCover);
                authorTextView.setText(author);
                descriptionTextView.setText(description);
                final String user = FirebaseAuth.getInstance().getUid();
                DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("users/"+user);
                userRef.child("library").child(bookTitle).child("title").setValue(bookTitle);
                userRef.child("library").child(bookTitle).child("author").setValue(author);
                userRef.child("library").child(bookTitle).child("thumbnail").setValue(thumbnail);
                userRef.child("library").child(bookTitle).child("total_pages").setValue(totalPages);
                userRef.child("library").child(bookTitle).child("pages_read").setValue(pagesRead);
                userRef.child("library").child(bookTitle).child("finished").setValue(bookFinished);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDescriptionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting user reference in db

                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString("BOOK_NAME",bookTitle);
                extras.putInt("CHAPTER",pagesRead);
                Intent chunkIntent = new Intent(getApplicationContext(), PageActivity.class);
                chunkIntent.putExtras(extras);
                startActivity(chunkIntent);

            }
        });
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
