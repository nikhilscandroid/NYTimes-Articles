package com.articles.nytimes.server;

import com.articles.nytimes.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */

public interface ApiInterface {

    @GET("mostpopular/v2/{section}/{period}.json")
    Call<ApiResponse> getNewsDetails(@Path("section") String section, @Path("period") String period,
                                     @Query("api-key") String apiKey);
}
