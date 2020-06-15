package com.gyansaarthi.fastbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.gyansaarthi.fastbook.Objects.Chunk;
import com.gyansaarthi.fastbook.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<Chunk> chunks;
    private LayoutInflater layoutInflater;
    private Context context;



    public ViewPagerAdapter(List<Chunk> chunks, Context context) {
        this.chunks = chunks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chunks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        TextView heading, content;
        View view = layoutInflater.inflate(R.layout.layout_page, container, false);

        heading= view.findViewById(R.id.headingTextView);
        content= view.findViewById(R.id.contentTextViewPage);

        heading.setText(chunks.get(position).getHeading());
        content.setText(Html.fromHtml(chunks.get(position).getContentText()));
        container.addView(view, 0);







        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
