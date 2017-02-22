package com.einarvalgeir.bussrapport;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetDateFragment extends BaseFragment implements INextButton {

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    DateTime date;

    public static SetDateFragment newInstance() {
        return new SetDateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_set_date, container, false);
        ButterKnife.bind(this, rootView);

        calendarView.setMaxDate(System.currentTimeMillis());
        date = new DateTime(calendarView.getDate());
        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            date = new DateTime(i, i1+1, i2, 0 ,0);
            callback.changeNextButtonStatus(true);
        });

        return rootView;
    }

    @Override
    public void nextButtonClicked() {
        getMainActivity().getPresenter().setDate(date);
    }
}
