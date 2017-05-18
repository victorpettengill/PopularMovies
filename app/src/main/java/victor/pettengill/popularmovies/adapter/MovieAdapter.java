package victor.pettengill.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import victor.pettengill.popularmovies.R;
import victor.pettengill.popularmovies.beans.Movie;

/**
 * Created by victorfernandes on 05/03/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    private Context context;
    private List<Movie> movies;
    private MovieClickListener movieClickListener;

    public MovieAdapter(Context context, List<Movie> movies, MovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.movieClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(View.inflate(parent.getContext(), R.layout.movie_item, null), movieClickListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = movies.get(position);

        Picasso.with(context).load(movie.getMoviePosterThumbnail()).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        public MovieViewHolder(View itemView, final MovieClickListener listener) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onClick(getAdapterPosition());

                }
            });

        }

    }

    public interface MovieClickListener {

        void onClick(int position);

    }

}