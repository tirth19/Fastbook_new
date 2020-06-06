package com.gyansaarthi.fastbook.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyansaarthi.fastbook.BookDescriptionActivity;
import com.gyansaarthi.fastbook.Home.HomeActivity;
//import com.gyansaarthi.fastbook.Profile.ProfileHomeActivity;
import com.gyansaarthi.fastbook.LibraryActivity;
import com.gyansaarthi.fastbook.Profile.ProfileHomeActivity;
import com.gyansaarthi.fastbook.R;
import com.gyansaarthi.fastbook.SearchActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper  {
    private static final String TAG = "BottomNavigationViewHel";


    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

        Log.d(TAG, "setupBottomNavigationView: Setting up navigation view");
         bottomNavigationViewEx.enableAnimation(false);
         bottomNavigationViewEx.enableItemShiftingMode(false);
         bottomNavigationViewEx.enableShiftingMode(false);
         bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, HomeActivity.class); //ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class); //ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_circle:
                        Bundle extras = new Bundle();
                        //Adding key value pairs to this bundle
                        //there are quite a lot data types you can store in a bundle
                        extras.putString("BOOK_NAME","digitalminimalism");
                        Intent intent3 = new Intent(context, BookDescriptionActivity.class);
                        intent3.putExtras(extras);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_alert:
                        Intent intent4 = new Intent(context, LibraryActivity.class); //ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_android:
                        Intent intent5 = new Intent(context, ProfileHomeActivity.class); //ACTIVITY_NUM = 4
                        context.startActivity(intent5);
                        break;
                }


                return false;
            }
        });



    }
}
