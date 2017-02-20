package com.einarvalgeir.bussrapport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetDateFragment extends BaseFragment implements INextButton {

    @BindView(R.id.calendarView)
    CalendarView cal;

    public static SetDateFragment newInstance() {
        return new SetDateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set_date, container, false);
        ButterKnife.bind(this, rootView);

        cal.setMaxDate(System.currentTimeMillis());

        return rootView;
    }

    @Override
    public void nextButtonClicked() {
        getMainActivity().getPresenter().setDate(cal.getDate());
    }
}
