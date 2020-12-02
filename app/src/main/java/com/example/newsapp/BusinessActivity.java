package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.newsapp.api.ApiService;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.NewsAdapter;
import com.example.newsapp.models.ResponseNews;
import com.example.newsapp.api.Server;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessActivity extends AppCompatActivity {

    private RecyclerView news;
    private NewsAdapter adapter;
    List<News> list = new ArrayList<>();

    final String category = "business";
    final String key = "6aff0138ea3342dfbc8eb5d4eaf35765";
    ProgressDialog loading;
    ApiService api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);


        news = findViewById(R.id.news);
        api = Server.getApiService();
        adapter = new NewsAdapter(this, list);

        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(this));
        news.setAdapter(adapter);
        refresh("");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search News..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    refresh(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refresh(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }

    public void refresh(final String keyword) {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage("Loading...");
        showDialog();

        if (keyword.length() > 0) {
            api.getListSearch(keyword, "id", category,"publishedAt", key).enqueue(new Callback<ResponseNews>() {
                @Override
                public void onResponse(Call<ResponseNews> call, Response<ResponseNews> response) {
                    if (response.isSuccessful()) {
                        hideDialog();
                        list = response.body().getNewsList();
                        news.setAdapter(new NewsAdapter(BusinessActivity.this, list));
                        adapter.notifyDataSetChanged();
                    } else {
                        hideDialog();
                        Toast.makeText(BusinessActivity.this, "Gagal mengambil data !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseNews> call, Throwable t) {
                    hideDialog();
                    Toast.makeText(BusinessActivity.this, "Gagal menyambung ke internet !", Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            api.getListNews("id", category, key).enqueue(new Callback<ResponseNews>() {
                @Override
                public void onResponse(Call<ResponseNews> call, Response<ResponseNews> response) {
                    if (response.isSuccessful()) {
                        hideDialog();
                        list = response.body().getNewsList();
                        news.setAdapter(new NewsAdapter(BusinessActivity.this, list));
                        adapter.notifyDataSetChanged();
                    } else {
                        hideDialog();
                        Toast.makeText(BusinessActivity.this, "Gagal mengambil data !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseNews> call, Throwable t) {
                    hideDialog();
                    Toast.makeText(BusinessActivity.this, "Gagal menyambung ke internet !", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void showDialog() {
        if (!loading.isShowing())
            loading.show();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


    }
