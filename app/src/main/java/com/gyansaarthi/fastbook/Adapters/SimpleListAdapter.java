package com.gyansaarthi.fastbook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gyansaarthi.fastbook.BookDescriptionActivity;
import com.gyansaarthi.fastbook.Objects.Chunk;
import com.gyansaarthi.fastbook.PageActivity;
import com.gyansaarthi.fastbook.R;

import java.util.List;

public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.SimpleListViewHolder> {

    private Context mCtx;
    private List<Chunk> chunkList;
    private String bookTitle;

    public SimpleListAdapter(Context mCtx, List<Chunk> chunkList, String bookTitle) {
        this.mCtx = mCtx;
        this.chunkList = chunkList;
        this.bookTitle = bookTitle;
    }

    @Override
    public SimpleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_index, null);
        return new SimpleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleListViewHolder holder, final int position) {
        Chunk chunk = chunkList.get(position);

        holder.headingTextView.setText(String.valueOf(position+1));
        holder.contentTextTextView.setText(chunk.getHeading());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a Bundle object
                Bundle extras = new Bundle();
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putInt("CHAPTER",position);
                extras.putString("BOOK_NAME", bookTitle);
                Intent intent = new Intent(mCtx, PageActivity.class);
                intent.putExtras(extras);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chunkList.size();
    }

    class SimpleListViewHolder extends RecyclerView.ViewHolder{
        TextView headingTextView, contentTextTextView;
        RelativeLayout parentLayout;

        public SimpleListViewHolder(View itemView){
            super(itemView);
            headingTextView = itemView.findViewById(R.id.chapterNumberTextView);
            contentTextTextView = itemView.findViewById(R.id.chapter_title_text_view);
            parentLayout = itemView.findViewById(R.id.index_parent_id);

            if (Build.VERSION.SDK_INT >= 26) {
                contentTextTextView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
        }

    }
}
