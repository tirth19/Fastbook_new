package com.gyansaarthi.fastbook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gyansaarthi.fastbook.BookDescriptionActivity;
import com.gyansaarthi.fastbook.Objects.BookCover;
import com.gyansaarthi.fastbook.Objects.Chunk;
import com.gyansaarthi.fastbook.R;

import java.util.List;

public class BookCoverAdapter extends RecyclerView.Adapter<BookCoverAdapter.BookCoverViewHolder>{
    private static final String TAG = "BookCoverAdapter";
    private Context mCtx;
    private List<BookCover> bookCoverList;

    public BookCoverAdapter(Context mCtx, List<BookCover> bookCoverList) {
        this.mCtx = mCtx;
        this.bookCoverList = bookCoverList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public BookCoverAdapter.BookCoverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_item_book, null);
        return new BookCoverAdapter.BookCoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookCoverAdapter.BookCoverViewHolder holder, int position) {
        final BookCover bookCover = bookCoverList.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        Glide.with(mCtx)
                .asBitmap()
                .load(bookCover.getThumbnail())
                .into(holder.thumbnail);
        holder.authorName.setText(bookCover.getBook_author());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a Bundle object
                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString("BOOK_NAME",bookCover.getBook_title());
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public BookCoverViewHolder(View itemView){
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            thumbnail.setClipToOutline(true);

            authorName = itemView.findViewById(R.id.author);
            parentLayout = itemView.findViewById(R.id.parent_id);
        }

    }
}
