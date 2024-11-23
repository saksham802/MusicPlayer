package com.sak.musicplayer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

import java.util.List;

public class ShortVideoAdapter extends RecyclerView.Adapter<ShortVideoAdapter.ShortViewHolder> {
    private List<ShortVideo> videoList;

    public ShortVideoAdapter(List<ShortVideo> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ShortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_item, parent, false);
        return new ShortViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortViewHolder holder, int position) {
        holder.setVideoView(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ShortViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title, uploader;
        ImageView imageView, likeBtn, shareBtn, saveBtn, payBtn;
        int like = 0;
        int save = 0;

        public ShortViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            title = itemView.findViewById(R.id.titleofvideo);
            uploader = itemView.findViewById(R.id.uploderofvideo);
            imageView = itemView.findViewById(R.id.pause);
            likeBtn = itemView.findViewById(R.id.like);
            shareBtn = itemView.findViewById(R.id.share);
            saveBtn = itemView.findViewById(R.id.save);
            payBtn = itemView.findViewById(R.id.payment);
        }

        public void setVideoView(ShortVideo video) {
            title.setText(video.getTitle());
            uploader.setText(video.getDesc());
            videoView.setVideoPath(video.getVideoUrl());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f) {
                        videoView.setScaleX(scale);
                    } else {
                        videoView.setScaleY(1f / scale);
                    }
                }
            });

            payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), upi.class);
                    view.getContext().startActivity(intent);
                }
            });

            // Share button handling
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    Intent chooser = Intent.createChooser(sharingIntent, "Share Using");
                    view.getContext().startActivity(chooser);
                }
            });

            // Like button handling
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateLikeButton(likeBtn);
                    if (like == 0) {
                        likeBtn.setImageResource(R.drawable.liked); // Liked icon
                        like = 1;
                    } else {
                        likeBtn.setImageResource(R.drawable.like); // Unliked icon
                        like = 0;
                    }
                }
            });

            // Save button handling
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animateLikeButton(saveBtn);
                    if (save == 0) {
                        saveBtn.setImageResource(R.drawable.saved); // Saved icon
                        Toast.makeText(itemView.getContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                        save = 1;
                    } else {
                        saveBtn.setImageResource(R.drawable.save); // Unsaved icon
                        Toast.makeText(itemView.getContext(), "Removed from Saved", Toast.LENGTH_SHORT).show();
                        save = 0;
                    }
                }
            });

            // Video play/pause button handling
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        imageView.setImageResource(R.drawable.baseline_pause_circle_24);
                    } else {
                        videoView.start();
                        imageView.setImageDrawable(null);
                    }
                }
            });

            // Loop video when it ends
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }

        // Animation for like and save buttons
        private void animateLikeButton(ImageView img) {
            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(img, "scaleX", 1f, 1.5f);
            ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(img, "scaleY", 1f, 1.5f);
            scaleUpX.setDuration(200); // Animation duration in milliseconds
            scaleUpY.setDuration(200);

            ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(img, "scaleX", 1.5f, 1f);
            ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(img, "scaleY", 1.5f, 1f);
            scaleDownX.setDuration(200);
            scaleDownY.setDuration(200);

            scaleUpX.start();
            scaleUpY.start();
            scaleUpX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scaleDownX.start();
                    scaleDownY.start();
                }
            });
        }
    }
}
