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
import edu.nanodegreeprojects.bakingapp.model.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private List<Step> stepList = new ArrayList<>();
    private Context context;

    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView tvStepName;
        private final TextView tvStepNumber;

        StepAdapterViewHolder(View view) {
            super(view);
            tvStepName = view.findViewById(R.id.tv_step_name);
            tvStepNumber = view.findViewById(R.id.tv_step_number);
            view.setOnClickListener(this);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(stepList.get(adapterPosition));
        }
    }


    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapterViewHolder stepAdapterViewHolder, int position) {
        String stepName = stepList.get(position).getShortDescription();
        stepAdapterViewHolder.tvStepName.setText(stepName);

        if (stepList.get(position).getId() == -1) {
            //ingredient
            stepAdapterViewHolder.tvStepNumber.setBackground(null);
            stepAdapterViewHolder.tvStepNumber.setBackground(context.getDrawable(R.drawable.ingredients_24dp));
        } else if (stepList.get(position).getId() == 0) {
            //Recipe introduction
            stepAdapterViewHolder.tvStepNumber.setBackground(null);
            stepAdapterViewHolder.tvStepNumber.setBackground(context.getDrawable(R.drawable.start_step_filled_24dp));
        } else {
            stepAdapterViewHolder.tvStepNumber.setText(String.valueOf(position - 1));
//        }
//        switch (String.valueOf(stepList.get(position).getId())) {
//            //ingredient
//            case "-1":
//                stepAdapterViewHolder.tvStepNumber.setBackground(null);
//                stepAdapterViewHolder.tvStepNumber.setBackground(context.getDrawable(R.drawable.ingredients_24dp));
//
//                break;
//            //Recipe introduction
//            case "0":
//                stepAdapterViewHolder.tvStepNumber.setBackground(null);
//                stepAdapterViewHolder.tvStepNumber.setBackground(context.getDrawable(R.drawable.start_step_filled_24dp));
//                break;
//            default:
//                stepAdapterViewHolder.tvStepNumber.setText(String.valueOf(position - 1));
        }


    }

    @Override
    public int getItemCount() {
        if (stepList.isEmpty()) return 0;
        return stepList.size();
    }

    public void setStepData(List<Step> stepList) {
        if (stepList != null && stepList.size() > 0) {
            this.stepList = stepList;
        } else {
            this.stepList.clear();
        }
        notifyDataSetChanged();
    }
}