package com.einarvalgeir.bussrapport.presenter;

import com.einarvalgeir.bussrapport.model.Problem;
import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.pdf.PdfCreator;

public class MainPresenter {

    private Report report;



    public MainPresenter(Report report) {
        this.report = report;
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
}
