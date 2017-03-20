package com.popular_movies.mvp.presenter;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class PopularMoviesPresenterImpl implements PopularMoviesPresenter.Presenter {

    private final PopularMoviesPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public PopularMoviesPresenterImpl(PopularMoviesPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchPopularMovies() {
        movieService.getPopularMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onPopularMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onPopularMoviesRetreivalFailure(throwable);
            }
        });
    }
}