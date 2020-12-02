package com.example.newsapp.api;

import com.example.newsapp.api.ApiClient;
import com.example.newsapp.api.ApiService;

public class Server {
    public static final String URL_API = "https://newsapi.org/";

    public static ApiService getApiService(){
        return ApiClient.getClient(URL_API).create(ApiService.class);
    }
}
