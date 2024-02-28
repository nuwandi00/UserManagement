package com.example.demo.user;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.dom4j.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class UserReportUtil {
    public static ByteArrayInputStream generateCsvReport(List<User> users) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("ID,Email,First Name,Last Name,Enabled\n");
            for (User user : users) {
                csvContent.append(user.getId()).append(",")
                        .append(user.getEmail()).append(",")
                        .append(user.getFirstName()).append(",")
                        .append(user.getLastName()).append(",")
                        .append(user.isEnabled()).append("\n");
            }
            out.write(csvContent.toString().getBytes());
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ByteArrayInputStream generatePdfReport(List<User> listUsers) throws DocumentException, com.itextpdf.text.DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();

        // Add title for the report
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("User Details Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        // Add spacing after the title
        title.setSpacingAfter(10);

        document.add(title);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);

        PdfPTable table = new PdfPTable(5);

        // Add table headers
        table.addCell(createCell("ID", headerFont));
        table.addCell(createCell("E-mail", headerFont));
        table.addCell(createCell("First Name", headerFont));
        table.addCell(createCell("Last Name", headerFont));
        table.addCell(createCell("Enabled", headerFont));


        // Add user details to the table
        for (User user : listUsers) {
            table.addCell(createCell(String.valueOf(user.getId()), cellFont));
            table.addCell(createCell(user.getEmail(), cellFont));
            table.addCell(createCell(user.getFirstName(), cellFont));
            table.addCell(createCell(user.getLastName(), cellFont));
            table.addCell(createCell(String.valueOf(user.isEnabled()), cellFont));
        }

        document.add(table);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new com.itextpdf.text.Phrase(content, font));
        cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        return cell;
    }


}

