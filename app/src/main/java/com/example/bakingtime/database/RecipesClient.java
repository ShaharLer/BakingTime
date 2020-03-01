package com.example.bakingtime.database;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesClient {

    String BAKING_RECIPES_HTTP_URL_ENDPOINT = "/topher/2017/May/59121517_baking/baking.json";

    @GET(BAKING_RECIPES_HTTP_URL_ENDPOINT)
    Call<List<Recipe>> bakingRecipes();
}
