package edu.nanodegreeprojects.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.nanodegreeprojects.bakingapp.R;
import edu.nanodegreeprojects.bakingapp.adapters.StepAdapter;
import edu.nanodegreeprojects.bakingapp.model.Recipe;
import edu.nanodegreeprojects.bakingapp.model.Step;

public class StepListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    private RecyclerView recyclerViewSteps;
    private StepAdapter stepAdapter;
    private static final int NUMBER_OF_COLUMNS = 1;
    private Recipe recipe;

    private static String M_TWO_PANELS_TAG = "m_two_panels_tag";
    private boolean mTwoPanels = false;
    private Parcelable gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);


        if (getArguments() != null) {
            Bundle extras = getArguments();
            recipe = (Recipe) extras.get("recipe");
            mTwoPanels = extras.getBoolean(M_TWO_PANELS_TAG);
        }

        if (savedInstanceState != null) {
            Parcelable recyclerViewStatus = savedInstanceState.getParcelable("recycler_view_steps");
            if (recyclerViewStatus != null)
                gridLayoutManager = recyclerViewStatus;
        }

        loadComponents(rootView, recipe);

        return rootView;
    }

    private void loadComponents(View rootView, Recipe recipe) {
        recyclerViewSteps = rootView.findViewById(R.id.recyclerViewSteps);

        recyclerViewSteps.setLayoutManager(new GridLayoutManager(rootView.getContext(), NUMBER_OF_COLUMNS));
        if (gridLayoutManager != null)
            recyclerViewSteps.getLayoutManager().onRestoreInstanceState(gridLayoutManager);

        stepAdapter = new StepAdapter(this);
        if (recipe != null)
            stepAdapter.setStepData(recipe.getStepList());
        recyclerViewSteps.setAdapter(stepAdapter);
        recyclerViewSteps.setHasFixedSize(false);

    }

    @Override
    public void onClick(Step step) {
        if (step != null) {
            StepListDetailFragment stepListDetailFragment = new StepListDetailFragment();
            Bundle extras = new Bundle();

            if (step.getId() == -1)
                extras.putSerializable("recipe", recipe);
            extras.putSerializable("step", step);

            stepListDetailFragment.setArguments(extras);
            FragmentManager fragmentManager = this.getFragmentManager();

            if (fragmentManager != null) {
                if (mTwoPanels) {
                    fragmentManager.beginTransaction().replace(R.id.fl_steps_details, stepListDetailFragment).commit();

                } else {
                    fragmentManager.beginTransaction().replace(R.id.fl_steps, stepListDetailFragment).addToBackStack(null).commit();
                }
            }

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recyclerViewSteps != null)
            outState.putParcelable("recycler_view_steps", recyclerViewSteps.getLayoutManager().onSaveInstanceState());
    }
}
