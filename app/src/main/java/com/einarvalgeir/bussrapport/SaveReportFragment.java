package com.einarvalgeir.bussrapport;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.einarvalgeir.bussrapport.events.ViewPdfEvent;
import com.einarvalgeir.bussrapport.model.Email;
import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.pdf.PdfCreator;
import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;
import com.jakewharton.rxbinding.view.RxView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

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

    @BindView(R.id.send_report_button)
    Button sendButton;
    private String email;

    public static SaveReportFragment newInstance() {
        return new SaveReportFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_save_report, container, false);
        ButterKnife.bind(this, rootView);

        email = new SharedPrefsUtil(getContext()).getAssigneeEmail();

        sendButton.setText("Skicka Rapport till: " + email);
        sendButton.setVisibility(View.INVISIBLE);

        saveButton.setText("Generera en rapport");

        Report currentReport = getMainActivity().getPresenter().getReport();

        summaryBusNumber.setText("Bussnummer: " + currentReport.getBusNumber());
        summaryServiceNumber.setText("Tjänstnummer: " + currentReport.getServiceNumber());
        summaryProblem.setText("Problemområde: " + currentReport.getProblem().getName());
        summaryDate.setText("Datum: " + currentReport.getTimeOfReporting());
        summaryReporter.setText("Rapporterat av: " + currentReport.getReporterName());

//        setNextButtonVisible(View.GONE);

        RxView.clicks(sendButton).subscribe(aVoid -> {
            Email email = getMainActivity().getPresenter().getEmail();
            sendEmail(email);
        });

        RxView.clicks(saveButton).subscribe(aVoid -> {
            getMainActivity().getPresenter().generatePdf();
            sendButton.setVisibility(View.VISIBLE);
        });

        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewPdfEvent e) {
        changeButtonStatusToView(e.getFile());
    }

    private void changeButtonStatusToView(String file) {
        saveButton.setText("Visa genererad PDF");
        saveButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        RxView.clicks(saveButton).subscribe(aVoid -> viewPdf(file));
    }

    public void viewPdf(String file) {

        File pdfFile = new File(file);
        Uri path = FileProvider
                .getUriForFile(getContext(), "com.einarvalgeir.bussrapport.provider" ,pdfFile);

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        List<ResolveInfo> resInfoList =
                getContext().getPackageManager()
                        .queryIntentActivities(pdfIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getContext().grantUriPermission(packageName, path,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getMainActivity(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(Email email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email.getTo()});
        intent.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT, email.getBody());

        Uri fileUri = FileProvider.getUriForFile(getContext(), "com.einarvalgeir.bussrapport.provider",
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PdfCreator.FILE_NAME));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
