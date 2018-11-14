package edu.nanodegreeprojects.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.nanodegreeprojects.bakingapp.adapters.RecipeAdapter;
import edu.nanodegreeprojects.bakingapp.model.Recipe;
import edu.nanodegreeprojects.bakingapp.utils.JsonParser;
import edu.nanodegreeprojects.bakingapp.utils.NetworkUtils;


public class MainActivityAllRecipes extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecyclerView recyclerViewAllRecipes;
    private ProgressBar progressBarRecipes;
    private RecipeAdapter recipeAdapter;
    private int NUMBER_OF_COLUMNS = 1;
    boolean mTwoPanels = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_all_recipes);

        if (findViewById(R.id.recycler_view_all_recipes_tablet) != null)
            mTwoPanels = true;

        loadComponents();
        new FetchRecipes().execute();

    }

    private void loadComponents() {
        if (mTwoPanels) {
            recyclerViewAllRecipes = findViewById(R.id.recycler_view_all_recipes_tablet);
            NUMBER_OF_COLUMNS = 3;
        } else
            recyclerViewAllRecipes = findViewById(R.id.recycler_view_all_recipes);

        progressBarRecipes = findViewById(R.id.pb_recipes);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerViewAllRecipes.setLayoutManager(gridLayoutManager);
        recipeAdapter = new RecipeAdapter(this);
        recyclerViewAllRecipes.setAdapter(recipeAdapter);
        recyclerViewAllRecipes.setHasFixedSize(false);

    }

    private void loadRecipes(String json) {
        List<Recipe> recipes = new ArrayList<>();
        if (json != null) {
            try {
                recipes = JsonParser.getRecipes(json, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            recipeAdapter.setRecipeData(recipes);
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    class FetchRecipes extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarRecipes.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL videosRequestUrl = NetworkUtils.buildUrl();
            try {
                return NetworkUtils.getResponseFromHttpUrl(videosRequestUrl);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String movieData) {

            if (movieData != null)
                loadRecipes(movieData);
            else
                showMessageError();
            progressBarRecipes.setVisibility(View.INVISIBLE);
        }
    }

    public void showMessageError() {
        Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_recipe_list:
                new FetchRecipes().execute();
                break;
        }
        return true;
    }


}
