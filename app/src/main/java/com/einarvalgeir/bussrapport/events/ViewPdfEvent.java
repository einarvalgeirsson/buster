package com.einarvalgeir.bussrapport.events;

public class ViewPdfEvent {

    private final String file;
    private final String directory;

    public ViewPdfEvent(String file, String directory) {
        this.file = file;
        this.directory = directory;
    }

    public String getFile() {
        return file;
    }

    public String getDirectory() {
        return directory;
    }
}
