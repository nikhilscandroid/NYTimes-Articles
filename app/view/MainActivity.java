package com.articles.nytimes.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.articles.nytimes.NewsApplication;
import com.articles.nytimes.R;
import com.articles.nytimes.model.ApiResponse;
import com.articles.nytimes.model.News;
import com.articles.nytimes.server.ApiClient;
import com.articles.nytimes.server.ApiInterface;
import com.articles.nytimes.utility.Constant;
import com.articles.nytimes.utility.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_news)
    RecyclerView recyclerView;
    @BindView(R.id.textViewNoData)
    TextView noDataTv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.action_search)
    SearchView searchView;

    private Type listType;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inits();

        getArticles(getString(R.string.one));
    }

    private void inits() {
        listType = new TypeToken<List<News>>() {
        }.getType();
        gson = new Gson();

        progressBar =  findViewById(R.id.progressBar);
        noDataTv =  findViewById(R.id.textViewNoData);
        recyclerView =  findViewById(R.id.recycler_news);
    }

    private void callApi(String section, String period) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ApiResponse> call = apiService.getNewsDetails(section, period, Constant.API_KEY);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                int statusCode = response.code();

                if (statusCode == 200) {
                    List<News> newsList = response.body().getResults();
                    saveData(newsList);
                } else {
                    showError("Server Problem. Try again!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                showError(t.getMessage());
            }
        });
    }

    private void saveData(List<News> newsList) {
        //LIST DATA CONVERT TO GSON STRING
        String gsonStr = gson.toJson(newsList, listType);

        //SAVE IN SHARED-PREFERENCE
        NewsApplication.setNewsList(this, gsonStr);

        getSavedData();
    }

    private void getSavedData() {
        //GET SAVED DATA
        String gsonList = NewsApplication.getNewsList(this);

        if (!gsonList.equals("n/a")) {
            //CONVERT TO LIST
            List<News> newsList = gson.fromJson(gsonList, listType);

            setupRecycler(newsList);
        } else {
            showError("No saved news to display...!");
        }
    }

    private void setupRecycler(List<News> dataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new NewsAdapter(this, dataList));

        assert dataList != null;
        if (dataList.size() > 0) {
            dataVisible();
        } else {
            showError("No News..!");
        }
    }

    private void showError(String message) {
        noDataTv.setText(message);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noDataTv.setVisibility(View.VISIBLE);
    }

    private void dataVisible() {
        noDataTv.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                ((NewsAdapter)recyclerView.getAdapter()).getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                ((NewsAdapter)recyclerView.getAdapter()).getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.latest_one:
                getArticles(getString(R.string.one));
                return true;
            case R.id.latest_seven:
                getArticles(getString(R.string.seven));
                return true;
            case R.id.latest_thirty:
                getArticles(getString(R.string.thirty));
                return true;
            //noinspection SimplifiableIfStatement
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getArticles(String period)
    {
        if (Util.isOnline(this)) {
            callApi("viewed", period); // for section - all-sections, sports, international
        } else {
            Toast.makeText(MainActivity.this, "No network connection. Loaded Offline data", Toast.LENGTH_LONG).show();
            getSavedData();
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}
