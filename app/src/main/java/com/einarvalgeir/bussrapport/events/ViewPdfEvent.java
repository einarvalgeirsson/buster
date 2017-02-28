package com.einarvalgeir.bussrapport.events;

public class ViewPdfEvent {

    private final String file;

    public ViewPdfEvent(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
