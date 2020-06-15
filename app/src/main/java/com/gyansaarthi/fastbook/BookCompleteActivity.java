package com.gyansaarthi.fastbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gyansaarthi.fastbook.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BookCompleteActivity extends AppCompatActivity {

    private static final String TAG = "BookCompleteActivity";
    private static final int ACTIVITY_NUM = 2;
    Context mContext = this;
    TextView bookTitleTV;
    LinearLayout linearLayout;
    String bookName, bookTitle;
    Boolean bookFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_complete);

        bookName= getIntent().getExtras().getString("BOOK_DISPLAY_NAME");
        bookTitle = getIntent().getExtras().getString("BOOK_NAME");


        bookTitleTV = findViewById(R.id.completedBookTitle);
        bookTitleTV.setText(bookName);
        linearLayout = findViewById(R.id.shareBook);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShareBook();
            }
        });

        bookFinished = true;

        final String user = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("users/"+user);
        userRef.child("library").child(bookTitle).child("finished").setValue(bookFinished);


        setupBottomNavigationView();
    }

    private void initShareBook() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FastBook");
        String shareMessage= "I just completed reading: " + bookName + ". Would you like to give it a try?\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "Share now"));
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