package victor.pettengill.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import victor.pettengill.popularmovies.R;
import victor.pettengill.popularmovies.beans.Review;

/**
 * Created by appimagetech on 24/07/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(View.inflate(context, R.layout.movie_review_item, null));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        Review review = reviews.get(position);

        holder.author.setText(review.getReviewAuthor());
        holder.review.setText(review.getReviewContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author) TextView author;
        @BindView(R.id.review) TextView review;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}