package com.gyansaarthi.fastbook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gyansaarthi.fastbook.BookDescriptionActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.R;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.BookCoverViewHolder>{
    private static final String TAG = "BookCoverAdapter";
    private Context mCtx;
    private List<BookCover> bookCoverList;

    public LibraryAdapter(Context mCtx, List<BookCover> bookCoverList) {
        this.mCtx = mCtx;
        this.bookCoverList = bookCoverList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public LibraryAdapter.BookCoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_library, null);
        return new LibraryAdapter.BookCoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LibraryAdapter.BookCoverViewHolder holder, int position) {
        final BookCover bookCover = bookCoverList.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        Glide.with(mCtx)
                .asBitmap()
                .load(bookCover.getThumbnail())
                .into(holder.thumbnail);
        holder.authorName.setText(bookCover.getBook_author());
        holder.simpleProgressBar.setProgress(bookCover.getPages_read());
        int mPagesRead = bookCover.getPages_read();
        int mPagesTotal = bookCover.getTotal_pages();
        Log.d(TAG, "onBindViewHolder: "+ mPagesRead+"total" +mPagesTotal);
        //        Toast.makeText(mCtx, "pagesread" + bookCover.getPages_read(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(mCtx, "total" + bookCover.getTotal_pages(), Toast.LENGTH_SHORT).show();
        holder.simpleProgressBar.setMax(bookCover.getTotal_pages());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a Bundle object
                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString("BOOK_NAME",bookCover.getBook_title());
                extras.putInt("CHAPTER", bookCover.getPages_read());
                Intent intent = new Intent(mCtx, BookDescriptionActivity.class);
                intent.putExtras(extras);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookCoverList.size();
    }

    class BookCoverViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        TextView authorName;
        RelativeLayout parentLayout;
        ProgressBar simpleProgressBar;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public BookCoverViewHolder(View itemView){
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            thumbnail.setClipToOutline(true);

            authorName = itemView.findViewById(R.id.author);
            parentLayout = itemView.findViewById(R.id.parent_id);
            simpleProgressBar = itemView.findViewById(R.id.bookReadProgressBar);
        }

    }
}
