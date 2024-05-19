package com.example.testmap;

import com.example.testmap.model.ItemList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://geocode.search.hereapi.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);
    @GET("/v1/geocode")
    Call<ItemList> getList(@Query("q") String q,  @Query("apiKey") String apiKey);

}
