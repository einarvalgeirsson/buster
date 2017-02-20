package com.einarvalgeir.bussrapport.model;

import org.joda.time.DateTime;

public class Report {
    private int busNumber;
    private int serviceNumber;
    private String reporterName;
    private Problem problem;

    private DateTime date;

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public void setServiceNumber(int serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public void setTimeOfReporting(DateTime date) {
        this.date = date;
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
        int year = date.getYear();
        int month = date.getMonthOfYear() + 1;
        int day = date.getDayOfMonth();
        return year + "-" + (month < 10 ? "0"+month : month) + "-" + (day < 10 ? "0"+day : day);
    }
}
