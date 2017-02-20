package com.einarvalgeir.bussrapport.pdf;


import android.os.Environment;

import com.einarvalgeir.bussrapport.events.ViewPdfEvent;
import com.einarvalgeir.bussrapport.model.Report;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfCreator {

    private static final String DIRECTORY = "external_files";
    private static final String FILE_NAME = "bus_report.pdf";

    private static Font headerFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
    private static Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font smallNormal = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);

    private Report report;

    public PdfCreator(Report report) {
        this.report = report;
    }

    public void createPdf() {
        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIRECTORY;

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, FILE_NAME);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            doc.open();

            addMetaData(doc);
            addTitlePage(doc);

            doc.close();

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } finally {
            EventBus.getDefault().post(new ViewPdfEvent(FILE_NAME, DIRECTORY));
        }
    }


    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private void addMetaData(Document document) {
        document.addTitle("Felrapportering av buss");
        document.addSubject("Felrapport");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Einar Valgeirsson");
        document.addCreator("Einar Valgeirsson");
    }

    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Felrapport", headerFont));
        preface.add(new DottedLineSeparator());
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Buss: " + report.getBusNumber(), subHeaderFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Rapport skapad av: " + report.getReporterName(), smallBold));
        preface.add(new Paragraph("Tjänst: " + report.getServiceNumber(), smallBold));
        preface.add(new Paragraph("Datum: " + report.getTimeOfReporting(), smallBold));
        preface.add(new Paragraph(
                "Felområde: " + report.getProblem().getName(),
                smallBold));

        addEmptyLine(preface, 1);

        preface.add(new Paragraph(
                report.getProblem().getDescription(),
                smallNormal));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
