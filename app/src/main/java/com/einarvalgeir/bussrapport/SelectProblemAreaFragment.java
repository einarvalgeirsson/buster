package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.einarvalgeir.bussrapport.model.Problem;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class SelectProblemAreaFragment extends BaseFragment implements ProblemAreasAdapter.IProblemAreaSelector, INextButton {

    @BindView(R.id.recycler_view)
    protected RecyclerView recyclerView;

    @BindView(R.id.selected_problem_area)
    protected TextView problemAreaName;

    @BindView(R.id.problem_area_description)
    protected EditText problemAreaDescriptions;

    protected ProblemAreasAdapter adapter;
    private LinearLayoutManager layoutManager;

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
        problemAreaName.setText("");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProblemAreasAdapter(getProblemAreasArray(), this);
        recyclerView.setItemAnimator(new FadeInAnimator(new OvershootInterpolator(1f)));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onProblemAreaSelected(ProblemAreasAdapter.ViewHolder vh, String name) {
        problemAreaName.setText(name);
    }

    private String[] getProblemAreasArray() {
        return new String[]{
                "AC",
                "Avgassystem",
                "Backspeglar",
                "Bagagerum",
                "Bakaxelväxel",
                "Batterier",
                "Belysning",
                "Biljettmaskin",
                "Biljettskrivare",
                "Blinkers",
                "Broms, fot",
                "Broms, hand",
                "Bromsljus",
                "Bränsletank",
                "Däck",
                "Dörr, bak",
                "Dörr, fram",
                "Fjädrar, bak",
                "Fjädrar, fram",
                "Fönster",
                "Förarstol",
                "Förorening",
                "Hastighetsmätare",
                "Hjul, muttrar",
                "Hållplats utrop",
                "Högtalare",
                "Kablar ",
                "Kollision",
                "Kompressor",
                "Komradio",
                "Kontrollampor",
                "Koppling",
                "Kortläsare",
                "Kortterminal",
                "Laddning",
                "Linje utrop",
                "Linjeskylt",
                "Motor",
                "Passagerarsäte",
                "Signalhorn",
                "Skyltning",
                "Stannarsignal",
                "Startmotor",
                "Strålkastare",
                "Styrning",
                "Tryckluftsystem",
                "Vattenläckage",
                "Ventilation",
                "Vindrutetorkare",
                "Värme",
                "Värmefläktar",
                "Växelförare",
                "Växellåda"
        };
    }

    @Override
    public void nextButtonClicked() {
        Problem p = new Problem();
        p.setName(problemAreaName.getText().toString());
        p.setDescription(problemAreaDescriptions.getText().toString());

        getMainActivity().getPresenter().setProblem(p);
    }
}
