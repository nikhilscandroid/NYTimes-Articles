package com.articles.nytimes.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.articles.nytimes.R;
import com.articles.nytimes.databinding.ActivityDetailsBinding;
import com.articles.nytimes.model.News;

/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        News news = (News) getIntent().getSerializableExtra("SELECTED_NEWS");

        ActivityDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        binding.setNews(news);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
