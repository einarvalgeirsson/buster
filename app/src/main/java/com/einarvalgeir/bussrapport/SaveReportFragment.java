package com.einarvalgeir.bussrapport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.einarvalgeir.bussrapport.model.Report;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveReportFragment extends BaseFragment {

    @BindView(R.id.summary_bus_number)
    TextView summaryBusNumber;

    @BindView(R.id.summary_service_number)
    TextView summaryServiceNumber;

    @BindView(R.id.summary_problem)
    TextView summaryProblem;

    @BindView(R.id.summary_date)
    TextView summaryDate;

    @BindView(R.id.summary_reporter)
    TextView summaryReporter;

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

        Report currentReport = getMainActivity().getPresenter().getReport();

        summaryBusNumber.setText("Bussnummer: " + currentReport.getBusNumber());
        summaryServiceNumber.setText("Tjänstnummer: " + currentReport.getServiceNumber());
        summaryProblem.setText("Problemområde: " + currentReport.getProblem().getName());
        summaryDate.setText("Datum: " + currentReport.getTimeOfReporting());
        summaryReporter.setText("Rapporterat av: " + currentReport.getReporterName());

//        setNextButtonVisible(View.GONE);

        RxView.clicks(saveButton).subscribe(aVoid -> getMainActivity().getPresenter().generatePdf());

        return rootView;
    }
}
