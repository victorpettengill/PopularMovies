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
import victor.pettengill.popularmovies.beans.Trailer;

/**
 * Created by appimagetech on 24/07/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private Context context;
    private List<Trailer> trailers;
    private TrailerClickListener listener;

    public TrailerAdapter(Context context, List<Trailer> trailers, TrailerClickListener listener) {
        this.context = context;
        this.trailers = trailers;
        this.listener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrailerViewHolder(View.inflate(context, R.layout.movie_trailer_item, null), listener);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        Trailer trailer = trailers.get(position);
        holder.name.setText(trailer.getTrailerName());

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;

        private TrailerViewHolder(View itemView, final TrailerClickListener listener) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onClick(trailers.get(getAdapterPosition()));

                }
            });

        }

    }

    public interface TrailerClickListener {

        void onClick(Trailer trailer);

    }

}