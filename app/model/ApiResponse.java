package com.articles.nytimes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */

public class ApiResponse {

    @SerializedName("status")
    private final String status;

    @SerializedName("num_results")
    private final int num_results;

    @SerializedName("results")
    private final List<News> results;

    public ApiResponse(String status, int num_results, List<News> results) {
        this.status = status;
        this.num_results = num_results;
        this.results = results;
    }

    public List<News> getResults() {
        return results;
    }
}
