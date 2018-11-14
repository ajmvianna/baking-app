package edu.nanodegreeprojects.bakingapp;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import edu.nanodegreeprojects.bakingapp.fragments.StepListDetailFragment;
import edu.nanodegreeprojects.bakingapp.fragments.StepListFragment;
import edu.nanodegreeprojects.bakingapp.model.Ingredient;
import edu.nanodegreeprojects.bakingapp.model.Recipe;
import edu.nanodegreeprojects.bakingapp.widget.WidgetContract;

public class StepsActivity extends AppCompatActivity {
    private boolean mTwoPanels = false;
    private Bundle extras;
    private Recipe recipe;
    private static String M_TWO_PANELS_TAG = "m_two_panels_tag";
    FragmentManager fragmentManager;
    StepListFragment stepListFragment;
    StepListDetailFragment stepListDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = this.getSupportActionBar();

        if (findViewById(R.id.fl_steps_details) != null)
            mTwoPanels = true;

        extras = getIntent().getExtras();
        if (extras != null) {
            recipe = (Recipe) extras.get("recipe");
            actionBar.setTitle(recipe.getName());
        }

        if (savedInstanceState == null) {
            if (mTwoPanels) {
                stepListFragment = new StepListFragment();
                if (stepListDetailFragment == null)
                    stepListDetailFragment = new StepListDetailFragment();


                if (extras != null) {
                    extras.putBoolean(M_TWO_PANELS_TAG, mTwoPanels);
                    stepListFragment.setArguments(extras);
                }
                fragmentManager = getSupportFragmentManager();

                if (stepListDetailFragment.isAdded())
                    fragmentManager.beginTransaction().add(R.id.fl_steps, stepListFragment).replace(R.id.fl_steps_details, stepListDetailFragment).commit();
                else
                    fragmentManager.beginTransaction().add(R.id.fl_steps, stepListFragment).add(R.id.fl_steps_details, stepListDetailFragment).commit();
            } else {
                stepListFragment = new StepListFragment();

                if (extras != null) {
                    extras.putBoolean(M_TWO_PANELS_TAG, mTwoPanels);
                    stepListFragment.setArguments(extras);
                }

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.fl_steps, stepListFragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_recipe, menu);
        if (recipe != null)
            setFavoriteRecipe(recipe.isFavorite(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.favorite_icon:
                if (recipe.isFavorite()) {
                    item.setIcon(R.drawable.ic_favorite_not_pressed_24dp);
                    recipe.setFavorite(false);
                } else {
                    item.setIcon(R.drawable.ic_favorite_pressed_24dp);
                    recipe.setFavorite(true);
                    setIngredientsDatabase(recipe.getIngredientList());
                    Toast.makeText(this, "Item added to widget", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private void setFavoriteRecipe(boolean isFavorite, Menu menu) {
        if (isFavorite)
            menu.getItem(0).setIcon(R.drawable.ic_favorite_pressed_24dp);
        else
            menu.getItem(0).setIcon(R.drawable.ic_favorite_not_pressed_24dp);
    }

    public void setIngredientsDatabase(List<Ingredient> ingredients) {

        getContentResolver().delete(WidgetContract.IngredientEntry.CONTENT_URI, WidgetContract.IngredientEntry._ID + " != ?", new String[]{""});

        for (Ingredient ingredient : ingredients) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_NAME, ingredient.getIngredient());
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_MEASURE, ingredient.getMeasure());
            contentValues.put(WidgetContract.IngredientEntry.INGREDIENT_QUANTITY, ingredient.getQuantity());

            getContentResolver().insert(WidgetContract.IngredientEntry.CONTENT_URI, contentValues);

        }

    }

}


