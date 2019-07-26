package com.articles.nytimes.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.articles.nytimes.databinding.NewsDataBinding;
import com.articles.nytimes.model.News;
import com.articles.nytimes.presenter.NewsClickHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nikhil Chindarkar on 26-07-2019.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    private final List<News> newsList;
    private List<News> newsListFiltered=null;
    private LayoutInflater layoutInflater;

    NewsAdapter(Context context, List<News> dataList) {
        this.mContext = context;
        this.newsList = dataList;
        this.newsListFiltered=dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        NewsDataBinding dataBinding = NewsDataBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(dataBinding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //SET VIEW DATA
        final News news = newsListFiltered.get(position);
        holder.bind(news);
    }

    @Override
    public int getItemCount() {
        return newsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    newsListFiltered = newsList;
                } else {
                    List<News> filteredList = new ArrayList<>();
                    for (News row : newsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getPublished_date().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }

                    newsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = newsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                newsListFiltered = (ArrayList<News>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final NewsDataBinding newsDataBinding;

        ViewHolder(NewsDataBinding dataBinding) {
            super(dataBinding.getRoot());
            this.newsDataBinding = dataBinding;
            newsDataBinding.setHandler(new NewsClickHandler(mContext));
        }

        void bind(News news) {
            this.newsDataBinding.setNews(news);
        }
    }
}
