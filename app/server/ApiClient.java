package com.articles.nytimes.server;

import com.articles.nytimes.utility.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
