package com.example.fady.movienanoapp.Model;

/**
 * Created by Fady on 2017-03-04.
 */

public class Movie {

    int id;
    String title;
    String overview;
    String poster_path;
    String release_date;

    public Movie() {
    }

    public Movie(String title, String overview, String poster_path, String release_date, String vote_average) {
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    String vote_average;

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public int getId() {
        return id;
    }


}
