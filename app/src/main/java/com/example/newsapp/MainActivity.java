package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView news;
    private NewsAdapter adapter;
    List<News> list = new ArrayList<>();
    ApiService api;
    ProgressDialog loading;
    CardView cvHealth, cvScience, cvTechnology, cvBusiness, cvSports, cvEntertainment;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        news = findViewById(R.id.news);
        cvBusiness = findViewById(R.id.cv_business);
        cvEntertainment = findViewById(R.id.cv_entertainment);
        cvHealth = findViewById(R.id.cv_health);
        cvScience = findViewById(R.id.cv_science);
        cvSports = findViewById(R.id.cv_sports);
        cvTechnology = findViewById(R.id.cv_technology);
        api = Server.getApiService();
        adapter = new NewsAdapter(MainActivity.this, list);

        news.setHasFixedSize(true);
        news.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        news.setAdapter(adapter);
        refresh();
        initFunction();
    }

    public void initFunction(){
        cvBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent business = new Intent(MainActivity.this, BusinessActivity.class);
                startActivity(business);
            }
        });
        cvHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent health = new Intent(MainActivity.this, HealthActivity.class);
                startActivity(health);
            }
        });
        cvEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entertainment = new Intent(MainActivity.this, EntertainmentActivity.class);
                startActivity(entertainment);
            }
        });
        cvSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sports = new Intent(MainActivity.this, SportsActivity.class);
                startActivity(sports);
            }
        });
        cvScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent science = new Intent(MainActivity.this, ScienceActivity.class);
                startActivity(science);
            }
        });
        cvTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent technology = new Intent(MainActivity.this, TechnologyActivity.class);
                startActivity(technology);
            }
        });


    }


    public void refresh() {
        loading = new ProgressDialog(MainActivity.this);
        loading.setCancelable(false);
        loading.setMessage("Loading...");
        showDialog();
        api.getListAllNews("id", "6aff0138ea3342dfbc8eb5d4eaf35765").enqueue(new Callback<ResponseNews>() {
            @Override
            public void onResponse(Call<ResponseNews> call, Response<ResponseNews> response) {
                if (response.isSuccessful()){
                    hideDialog();
                    list = response.body().getNewsList();
                    news.setAdapter(new NewsAdapter(MainActivity.this, list));
                    adapter.notifyDataSetChanged();
                } else {
                    hideDialog();
                    Toast.makeText(MainActivity.this, "Gagal mengambil data !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNews> call, Throwable t) {
                hideDialog();
                Toast.makeText(MainActivity.this, "Gagal menyambung ke internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog() {
        if (!loading.isShowing())
            loading.show();
    }

    private void hideDialog() {
        if (loading.isShowing())
            loading.dismiss();
    }




}
