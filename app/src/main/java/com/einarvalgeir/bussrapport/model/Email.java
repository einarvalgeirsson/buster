package com.einarvalgeir.bussrapport.model;

public class Email {

    private String to;
    private String subject;
    private String body;

    public String getTo() {
        return to;
    }

    public Email withToAdress(String to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Email withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Email withBody(String body) {
        this.body = body;
        return this;
    }

}
