package com.articles.nytimes.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.articles.nytimes.model.News;
import com.articles.nytimes.view.DetailsActivity;


/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */
public class NewsClickHandler {

    private final Context context;

    public NewsClickHandler(Context context) {
        this.context = context;
    }

    public void onSaveClick(View view, News news) {
        Intent nextInt = new Intent(view.getContext(), DetailsActivity.class);
        nextInt.putExtra("SELECTED_NEWS",news);
        context.startActivity(nextInt);
    }
}
