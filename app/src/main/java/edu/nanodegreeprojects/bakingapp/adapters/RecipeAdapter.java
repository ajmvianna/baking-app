package edu.nanodegreeprojects.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.nanodegreeprojects.bakingapp.R;
import edu.nanodegreeprojects.bakingapp.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private Context context;

    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView tvRecipeName;
        private final TextView tvRecipeServing;
        private final ImageView ivRecipeImage;

        RecipeAdapterViewHolder(View view) {
            super(view);
            tvRecipeName = view.findViewById(R.id.tv_recipe_name);
            tvRecipeServing = view.findViewById(R.id.tv_recipe_serving);
            ivRecipeImage = view.findViewById(R.id.iv_recipe_image);
            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(recipeList.get(adapterPosition));
        }
    }


    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {

        if (!recipeList.get(position).getImage().equals("")) {
            Picasso.with(context).load(recipeList.get(position).getImage()).error(R.drawable.ic_local_serving_24dp).into(recipeAdapterViewHolder.ivRecipeImage);

        }

        String recipeName = recipeList.get(position).getName();
        String recipeServing = context.getString(R.string.recipe_serving) + String.valueOf(recipeList.get(position).getServings());
        recipeAdapterViewHolder.tvRecipeName.setText(recipeName);
        recipeAdapterViewHolder.tvRecipeServing.setText(recipeServing);
    }

    @Override
    public int getItemCount() {
        if (recipeList.isEmpty()) return 0;
        return recipeList.size();
    }

    public void setRecipeData(List<Recipe> recipeList) {
        if (recipeList != null && recipeList.size() > 0) {
            this.recipeList = recipeList;
        } else {
            this.recipeList.clear();
        }
        notifyDataSetChanged();
    }
}