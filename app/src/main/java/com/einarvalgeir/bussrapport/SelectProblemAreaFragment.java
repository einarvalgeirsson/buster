package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectProblemAreaFragment extends BaseFragment {

    @BindView(R.id.problem_area_spinner)
    protected MaterialSpinner spinner;

    public static SelectProblemAreaFragment newInstance() {
        return new SelectProblemAreaFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_problem_area, container, false);
        ButterKnife.bind(this, rootView);
        getCallback().changeNextButtonStatus(false);
        setupSpinner();

        return rootView;
    }

    private void setupSpinner() {

        String[] problemAreas = new String[]{"Broms, fot", "Broms, hand", "AC", "Avgassystem", "Backspeglar"};
        spinner.setItems(problemAreas);
        spinner.setOnItemSelectedListener((view, position, id, item) -> {
            spinnerItemSelected(item);
        });
    }

    private void spinnerItemSelected(Object item) {
        Snackbar.make(getView(), ((String) item), Snackbar.LENGTH_SHORT).show();
    }


}
