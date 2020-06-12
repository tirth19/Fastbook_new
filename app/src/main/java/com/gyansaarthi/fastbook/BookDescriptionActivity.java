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

    private static final String TAG = "BookDescriptionActivity";
    private static final int ACTIVITY_NUM = 1;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);
        final String bookTitle= getIntent().getExtras().getString("BOOK_NAME");
        bookNodeRef= FirebaseDatabase.getInstance().getReference("books/"+bookTitle);

        Button readButton = (Button) findViewById(R.id.readNowButton);
        final TextView authorView = findViewById(R.id.authorTextView);
        final TextView bookName = findViewById(R.id.bookTitleTextView);
        final TextView descriptionView = findViewById(R.id.descriptionTextView);
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
        bookNameRef = bookNodeRef.child("book_title");
        bookNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                bookName.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDescriptionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        final ImageView bookCover = findViewById(R.id.bookcoverImageView);
        bookCover.setClipToOutline(true);

        bookNodeRef.child("thumbnail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
//                val imageView = ImageView(this)
                Glide.with(mContext).load(value).into(bookCover);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDescriptionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        final TextView authorTextView = findViewById(R.id.authorTextView);
        bookNodeRef.child("book_author").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                authorTextView.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDescriptionActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        final TextView description = findViewById(R.id.descriptionTextView);
        bookNodeRef.child("book_description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                description.setText(value);
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
                final String user = FirebaseAuth.getInstance().getUid();
                DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("users/"+user);
                userRef.child("library").child(bookTitle).setValue(bookTitle);

                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString("BOOK_NAME",bookTitle);
                extras.putInt("CHAPTER",0);
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
