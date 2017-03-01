package com.einarvalgeir.bussrapport.model;

import org.joda.time.DateTime;

public class Report {

    public static final String USER_NAME = "user_name";
    public static final String ASSIGNEE_EMAIL = "asignee_email";


    private int busNumber;
    private int serviceNumber;
    private String reporterName;
    private Problem problem;
    private String assigneeEmail;

    private String image;

    private DateTime date;

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public void setServiceNumber(int serviceNumber) {
        this.serviceNumber = serviceNumber;
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
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();
        return year + "-" + (month < 10 ? "0"+month : month) + "-" + (day < 10 ? "0"+day : day);
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
