package com.example.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private OnReviewInteractionListener listener;

    public interface OnReviewInteractionListener {
        void onLikeClicked(Review review, int position);
        void onReplyClicked(Review review, int position);
    }

    public ReviewAdapter(OnReviewInteractionListener listener) {
        this.reviews = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public void addReview(Review review) {
        reviews.add(0, review);
        notifyItemInserted(0);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView userAvatar;
        private TextView userName;
        private TextView reviewDate;
        private RatingBar reviewRating;
        private TextView reviewContent;
        private Button likeButton;
        private Button replyButton;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewContent = itemView.findViewById(R.id.review_content);
            likeButton = itemView.findViewById(R.id.like_button);
            replyButton = itemView.findViewById(R.id.reply_button);
        }

        public void bind(final Review review) {
            userName.setText(review.getUserName());
            reviewDate.setText(review.getDate());
            reviewRating.setRating(review.getRating());
            reviewContent.setText(review.getContent());
            
            likeButton.setText(review.isLiked() ? "Liked" : "Like");
            likeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClicked(review, getAdapterPosition());
                }
            });

            replyButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onReplyClicked(review, getAdapterPosition());
                }
            });
        }
    }
} 