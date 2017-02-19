package com.einarvalgeir.bussrapport.model;

import java.util.Date;

public class Report {
    private int busNumber;
    private int serviceNumber;
    private String reporterName;
    private Problem problem;
    private Date timeOfReporting;

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public void setServiceNumber(int serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public int getServiceNumber() {
        return serviceNumber;
    }

    public String getReporterName() {
        return reporterName;
    }

    public Problem getProblem() {
        return problem;
    }

    public String getTimeOfReporting() {
        return new Date().toString();
    }
}
