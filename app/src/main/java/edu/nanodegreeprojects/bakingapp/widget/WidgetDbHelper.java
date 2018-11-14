package edu.nanodegreeprojects.bakingapp.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.nanodegreeprojects.bakingapp.widget.WidgetContract.IngredientEntry;

public class WidgetDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredient.db";
    private static final int DATABASE_VERSION = 1;

    public WidgetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngredientEntry.INGREDIENT_NAME + " TEXT NOT NULL, " +
                IngredientEntry.INGREDIENT_MEASURE + " TEXT NOT NULL, " +
                IngredientEntry.INGREDIENT_QUANTITY + " FLOAT NOT NULL)";

        db.execSQL(SQL_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
