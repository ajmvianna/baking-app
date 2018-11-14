package edu.nanodegreeprojects.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.nanodegreeprojects.bakingapp.R;
import edu.nanodegreeprojects.bakingapp.model.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private List<Ingredient> ingredientList = new ArrayList<>();
    private Context context;

    private final IngredientAdapterOnClickHandler mClickHandler;

    public interface IngredientAdapterOnClickHandler {
        void onClick(Ingredient ingredient);
    }

    public IngredientAdapter(IngredientAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView tvQuantity;
        private final TextView tvMeasure;
        private final TextView tvIngredient;


        IngredientAdapterViewHolder(View view) {
            super(view);
            tvQuantity = view.findViewById(R.id.tv_quantity);
            tvMeasure = view.findViewById(R.id.tv_measure);
            tvIngredient = view.findViewById(R.id.tv_ingredient);

            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(ingredientList.get(adapterPosition));
        }
    }


    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapterViewHolder ingredientAdapterViewHolder, int position) {
        String quantity = context.getString(R.string.ingredient_quantity_title) + String.valueOf(ingredientList.get(position).getQuantity());
        String measure = context.getString(R.string.ingredient_measure_title) + ingredientList.get(position).getMeasure();
        String ingredient = ingredientList.get(position).getIngredient();

        ingredientAdapterViewHolder.tvQuantity.setText(quantity);
        ingredientAdapterViewHolder.tvMeasure.setText(measure);
        ingredientAdapterViewHolder.tvIngredient.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if (ingredientList.isEmpty()) return 0;
        return ingredientList.size();
    }

    public void setIngredientData(List<Ingredient> ingredientList) {
        if (ingredientList != null && ingredientList.size() > 0) {
            this.ingredientList = ingredientList;
        } else {
            this.ingredientList.clear();
        }
        notifyDataSetChanged();
    }
}