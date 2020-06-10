package com.gyansaarthi.fastbook.Adapters;

import android.content.Context;
import android.os.Build;
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
import com.gyansaarthi.fastbook.Objects.Achievement;
import com.gyansaarthi.fastbook.R;

import java.util.List;


public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>{
    private static final String TAG = "AchievementAdapter";
    private Context mCtx;
    private List<Achievement> achievementList;

    public AchievementAdapter(Context mCtx, List<Achievement> achievementList) {
        this.mCtx = mCtx;
        this.achievementList = achievementList;
    }

    @Override
    public AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_achievement, null);
        return new AchievementViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AchievementViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Achievement achievement = achievementList.get(position);
        Glide.with(mCtx)
                .asBitmap()
                .load(achievement.getThumbnail())
                .into(holder.image);
        holder.titleTextView.setText(achievement.getTitle());
        holder.targetNumbertextView.setText(achievement.getTarget());
        holder.descriptionTextView.setText(achievement.getDescription());
        holder.simpleProgressBar.setMax(Integer.parseInt(achievement.getTarget()));
        holder.simpleProgressBar.setProgress(Integer.parseInt(achievement.getAchieved())); // 100 maximum value for the progress bar
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }


   class AchievementViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView titleTextView, descriptionTextView, targetNumbertextView;
       ProgressBar simpleProgressBar;
        RelativeLayout parentLayout;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public AchievementViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.achievementThumbnail);
            titleTextView = itemView.findViewById(R.id.achievement_title);
            descriptionTextView = itemView.findViewById(R.id.achievement_description);
            targetNumbertextView = itemView.findViewById(R.id.achievement_target_number);
            simpleProgressBar=itemView.findViewById(R.id.simpleProgressBar); // initiate the progress bar

            image.setClipToOutline(true);

        }
    }
}