package com.example.tutoforreal;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PdfGenerator {
    public void creatingPdf() throws SQLException {
        String logs = "Controller creatingPdf : ";
        try{
            Document document = new Document();
            TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
            ArrayList<Task>queryOutput = taskQueries.getAlltask();
            PdfWriter.getInstance(document, new FileOutputStream("projectPdf.pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            Paragraph chunk = new Paragraph(Data.project.toString(), font);
            Paragraph tasks = new Paragraph(queryOutput.toString(), font);

            document.add(chunk);
            document.add(tasks);
            try {
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + "projectPdf.pdf");
            } catch (IOException e) {
                e.printStackTrace();
                logs = logs + "failed " + e;
                LogWriter.writeLogs(logs);
            }
            document.close();
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            logs = logs + "failed " + e;
            LogWriter.writeLogs(logs);
        }
    }
}
