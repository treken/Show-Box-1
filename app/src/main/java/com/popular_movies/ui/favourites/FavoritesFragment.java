package com.popular_movies.ui.favourites;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popular_movies.R;
import com.popular_movies.domain.movie.MovieTable;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    //  recycler view
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private FavouriteMovieAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorites_list, container, false);
        ButterKnife.bind(this, layout);

        if (getResources().getBoolean(R.bool.isTablet)) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels / metrics.density);
            //For Tabs
            ///////////////////////////boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            ///////////////////////////width = isTablet ? (width / 2) : width;
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        } else {
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            else
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        }
        mAdapter = new FavouriteMovieAdapter(getContext(), null);
        recyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = MovieTable.CONTENT_URI;
        return new CursorLoader(getContext(), CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

}
