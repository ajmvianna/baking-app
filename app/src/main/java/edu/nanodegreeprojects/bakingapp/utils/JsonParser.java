package edu.nanodegreeprojects.bakingapp.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.nanodegreeprojects.bakingapp.model.Ingredient;
import edu.nanodegreeprojects.bakingapp.model.Recipe;
import edu.nanodegreeprojects.bakingapp.model.Step;


public final class JsonParser {

    public static String readJsonFromDisk(String jsonFile, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static List<Recipe> getRecipes(String jsonFile, Context context) throws JSONException {
        List<Recipe> recipeList = new ArrayList<>();
        Recipe recipe;
        //String json = readJsonFromDisk(jsonFile, context);

        JSONArray jsonObjectsList = null;
        jsonObjectsList = new JSONArray(jsonFile);

        for (int i = 0; i < jsonObjectsList.length(); i++) {
            JSONObject jsonObject = jsonObjectsList.getJSONObject(i);
            recipe = new Recipe(jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    getIngredientList(jsonObject),
                    getStepList(jsonObject),
                    jsonObject.getInt("servings"),
                    jsonObject.getString("image"),
                    false
            );
            recipeList.add(recipe);
        }
        return recipeList;
    }

    private static List<Ingredient> getIngredientList(JSONObject jsonObject) throws JSONException {
        List<Ingredient> ingredientList = new ArrayList<>();
        Ingredient ingredient;

        JSONArray jsonArray = jsonObject.getJSONArray("ingredients");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            ingredient = new Ingredient(jsonObject1.getDouble("quantity"),
                    jsonObject1.getString("measure"),
                    jsonObject1.getString("ingredient")
            );
            ingredientList.add(ingredient);
        }
        return ingredientList;
    }

    private static List<Step> getStepList(JSONObject jsonObject) throws JSONException {
        List<Step> stepList = new ArrayList<>();
        Step step;

        step = new Step(-1, "Ingredients", "", "", "");
        stepList.add(step);
        JSONArray jsonArray = jsonObject.getJSONArray("steps");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            step = new Step(jsonObject1.getInt("id"),
                    jsonObject1.getString("shortDescription"),
                    jsonObject1.getString("description"),
                    jsonObject1.getString("videoURL"),
                    jsonObject1.getString("thumbnailURL")
            );
            stepList.add(step);
        }
        return stepList;
    }


}