package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveReportFragment extends BaseFragment {

    @BindView(R.id.save_report_button)
    Button saveButton;

    public static SaveReportFragment newInstance() {
        return new SaveReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_save_report, container, false);
        ButterKnife.bind(this, rootView);

        RxView.clicks(saveButton).subscribe(aVoid -> getMainActivity().getPresenter().generatePdf());

        return rootView;
    }
}
