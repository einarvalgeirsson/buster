package com.einarvalgeir.bussrapport.presenter;

import com.einarvalgeir.bussrapport.model.Problem;
import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.pdf.PdfCreator;
import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;

import org.joda.time.DateTime;

public class MainPresenter {

    private Report report;

    public MainPresenter(Report report, SharedPrefsUtil prefsUtil) {
        this.report = report;
        this.report.setReporterName(prefsUtil.getReporterName());
        this.report.setAssigneeEmail(prefsUtil.getAssigneeEmail());
    }

    public void addBusNumber(int busNumber) {
        report.setBusNumber(busNumber);
    }

    public void addServiceNumber(int serviceNumber) {
        report.setServiceNumber(serviceNumber);
    }

    public void setProblem(Problem problem) {
        report.setProblem(problem);
    }

    public void generatePdf() {
        new PdfCreator(report).createPdf();
    }

    public void setDate(DateTime date) {
        report.setTimeOfReporting(date);
    }

    public void setImage(String path) {
        report.setImage(path);
    }
}
