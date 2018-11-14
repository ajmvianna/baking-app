package edu.nanodegreeprojects.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import edu.nanodegreeprojects.bakingapp.R;


public class WidgetService extends RemoteViewsService {

    private ListView listView;

    public final static String SHOW_INGREDIENTS_LIST = "edu.nanodegreeprojects.bakingapp.ingredients_list";

    public Cursor getIngredientsList() {
        Uri INGREDIENT = WidgetContract.IngredientEntry.CONTENT_URI;
        Cursor cursor = getContentResolver().query(INGREDIENT, null, null, null, null);
        return cursor;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListWidgetItem(getApplicationContext(), getIngredientsList());
    }

    public class ListWidgetItem implements RemoteViewsFactory {

        private Context context;
        private Cursor cursor;
        private String[] exampleData = {"one", "two", "three", "four",
                "five", "six", "seven", "eight", "nine", "ten"};

        public ListWidgetItem(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
        }

        @Override
        public void onCreate() {
            SystemClock.sleep(500);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (cursor != null)
                return cursor.getCount();
            else
                return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.row_listview_widget);

            if (cursor != null && getCount() > 0) {
                Log.d("entrou no update", String.valueOf(getCount()));
                int indexIngredient = cursor.getColumnIndex(WidgetContract.IngredientEntry.INGREDIENT_NAME);
                cursor.moveToPosition(position);

                String ingredient = cursor.getString(indexIngredient);
                remoteViews.setTextViewText(R.id.txt_ingredient_widget, ingredient);
            } else {
                Log.d("nao entrou no update", String.valueOf(getCount()));
                remoteViews.setTextViewText(R.id.txt_ingredient_widget, "No data");
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
