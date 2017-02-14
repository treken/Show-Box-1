package com.popular_movies.ui.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.activity.MovieDetailActivity;
import com.popular_movies.ui.fragment.DetailedViewFragment;

import java.util.List;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class MovieAdapterHorizontal extends RecyclerView.Adapter<MovieAdapterHorizontal.ViewHolder> {

    private static String TAG = MovieAdapterHorizontal.class.getSimpleName();
    private List<MovieData> movieItemArrayList;
    private LayoutInflater inflater;
    private Context context;
    public static ClickListener clickListener;

    public MovieAdapterHorizontal(Context context, List<MovieData> movieDataList) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.movieItemArrayList = movieDataList;
            if (MainActivity.mIsDualPane) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movieItemArrayList.get(0)))
                        .commit();
            }
        } else {
            Log.e(TAG, "MovieAdapterHorizontal: context is null");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieData movieData = movieItemArrayList.get(position);
        holder.setData(movieData);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView thumbnail;
        //public MovieAdapterHorizontal.ClickListener clickListener;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);

        }

        @Override
        public void onClick(View v) {
            if (MovieAdapterHorizontal.clickListener != null) {
                MovieAdapterHorizontal.clickListener.itemClicked(v, getPosition());
            }
        }

        private void setData(final MovieData movieData) {
            title.setText(movieData.getOriginal_title());
            ImageLoader.loadPosterImage(context, movieData.getPoster_path(), thumbnail);

            if (!MainActivity.mIsDualPane) {
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(context.getString(R.string.key_movie), movieData);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                            ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context,
                                    Pair.create((View) thumbnail, context.getString(R.string.transition_name)),
                                    Pair.create((View) barLayout, context.getString(R.string.transition_name_action_bar)));
                            context.startActivity(intent, compat.toBundle());
                        } else
                            context.startActivity(intent);
                    }
                });
            } else {
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                                .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movieData))
                                .commit();
                    }
                });
            }
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList.size();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}