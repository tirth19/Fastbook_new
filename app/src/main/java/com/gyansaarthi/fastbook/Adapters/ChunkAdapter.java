package com.gyansaarthi.fastbook.Adapters;

import android.content.Context;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gyansaarthi.fastbook.Objects.Chunk;
import com.gyansaarthi.fastbook.R;

import java.util.List;

public class ChunkAdapter extends RecyclerView.Adapter<ChunkAdapter.ChunkViewHolder> {

    private Context mCtx;
    private List<Chunk> chunkList;

    public ChunkAdapter(Context mCtx, List<Chunk> chunkList) {
        this.mCtx = mCtx;
        this.chunkList = chunkList;
    }

    @Override
    public ChunkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_chunk, null);
        return new ChunkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChunkViewHolder holder, int position) {
        Chunk chunk = chunkList.get(position);

        holder.headingTextView.setText(chunk.getHeading());
        holder.contentTextTextView.setText(Html.fromHtml(chunk.getContentText()));
    }

    @Override
    public int getItemCount() {
        return chunkList.size();
    }

    class ChunkViewHolder extends RecyclerView.ViewHolder{
        TextView headingTextView, contentTextTextView;
        public ChunkViewHolder(View itemView){
            super(itemView);
            headingTextView = itemView.findViewById(R.id.titleTextView);
            contentTextTextView = itemView.findViewById(R.id.contentTextView);
            if (Build.VERSION.SDK_INT >= 26) {
                contentTextTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            }
        }

    }
}
