package com.bjsn.finance.Service;

import com.bjsn.finance.Module.Item;
import com.bjsn.finance.repo.Itemrepo;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class Itemservice {
    @Autowired
    private Itemrepo repo;
    public Item getitemById(int id) {
        return repo.findById(id).orElseThrow(()-> new RuntimeException("Item Not Found"));
    }

    public Item additem(Item item) {
        return repo.save(item);
    }

    public void deleteitem(int id) {
        repo.deleteById(id);
    }


    public Item updateItem(int jwlno, Item item) {
        Item item1 = repo.findById(jwlno).orElse(null);

        if (item1 == null) {
            return null; // item with given ID doesn't exist
        }

        // Update all fields
        item1.setName(item.getName());
        item1.setFathername(item.getFathername());
        item1.setPrincipalamt(item.getPrincipalamt());
        item1.setStdate(item.getStdate());
        item1.setAddress(item.getAddress());
        item1.setItemtype(item.getItemtype());
        item1.setNumber(item.getNumber());
        item1.setStatus(item.getStatus());

        return repo.save(item1);
    }
    public Item getItemWithInterestDetails(int jwlno, double monthlyRate, LocalDate currentDate) {
        Item item = repo.findById(jwlno).orElse(null);
        if (item == null) return null;

        LocalDate loanDate = item.getStdate();
        long daysBetween = ChronoUnit.DAYS.between(loanDate, currentDate);

        int months = (int) (daysBetween / 30);
        if (daysBetween % 30 > 10) months++; // follow 10-day rule

        double principal = item.getPrincipalamt();

        double simpleInterest = (principal * monthlyRate * months)/100;
        double compoundInterest = 0.0;

        if (months <= 12) {
            compoundInterest = 0.0; // no compound interest
        } else {
            int years = months / 12;
            int extraMonths = months % 12;

            double compoundPrincipal = principal;

            for (int i = 0; i < years; i++) {
                double si = (compoundPrincipal * monthlyRate * 12) / 100;
                compoundPrincipal += si;
            }

            double siRemaining = (compoundPrincipal * monthlyRate * extraMonths) / 100;

            compoundInterest = (compoundPrincipal + siRemaining) - principal;
        }


        Item result = new Item();
        result.setJwlno(item.getJwlno());
        result.setName(item.getName());
        result.setFathername(item.getFathername());
        result.setPrincipalamt(item.getPrincipalamt());
        result.setStdate(loanDate);
        result.setAddress(item.getAddress());
        result.setItemtype(item.getItemtype());
        result.setNumber(item.getNumber());
        result.setStatus(item.getStatus());
        result.setReminderDate(item.getReminderDate());
        result.setCurrDate(currentDate);
        result.setMonths(months);
        result.setSimpleInterest(simpleInterest);
        result.setCompoundInterest(compoundInterest);
        return result;
    }
    public byte[] generateMonthlyReportPdf(int month, int year, double monthlyRate, String filterStatus) throws Exception {
        List<Item> allItems = repo.findAll();

        // Filter by month, year, and status (if provided)
        List<Item> filteredItems = allItems.stream()
                .filter(item -> {
                    LocalDate date = item.getStdate();
                    boolean matchMonth = date.getMonthValue() == month && date.getYear() == year;
                    boolean matchStatus = filterStatus == null ||
                            (filterStatus.equalsIgnoreCase("inloan") && item.getStatus()) ||
                            (filterStatus.equalsIgnoreCase("released") && !item.getStatus());
                    return matchMonth && matchStatus;
                })
                .collect(Collectors.toList());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, out);
        doc.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        doc.add(new Paragraph("Pawn Shop Monthly Report - " + Month.of(month) + " " + year, titleFont));
        doc.add(new Paragraph("Generated on: " + LocalDate.now()));
        doc.add(Chunk.NEWLINE);

        // Create Table with Headers
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 3, 2, 1.5f, 2, 2.5f, 2});

        // Table headers
        String[] headers = {"JWL No", "Name", "Principal", "Months", "Status", "Simple Interest", "Compound Interest"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new Color(220, 220, 220));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // Table content
        for (Item item : filteredItems) {
            LocalDate start = item.getStdate();
            long days = ChronoUnit.DAYS.between(start, LocalDate.now());
            int months = (int) (days / 30);
            if (days % 30 > 5) months++;

            double simpleInterest = item.getPrincipalamt() * monthlyRate * months;
            double compoundInterest = 0;

            if (months > 12) {
                int years = months / 12;
                int extraMonths = months % 12;
                double compoundPrincipal = item.getPrincipalamt();

                for (int i = 0; i < years; i++) {
                    double si = compoundPrincipal * monthlyRate * 12;
                    compoundPrincipal += si;
                }
                compoundInterest = (compoundPrincipal * monthlyRate * extraMonths);
                compoundInterest += (compoundPrincipal - item.getPrincipalamt());
            }

            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getJwlno()), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(item.getName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase("₹" + item.getPrincipalamt(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(months), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(item.getStatus() ? "In Loan" : "Released", bodyFont)));
            table.addCell(new PdfPCell(new Phrase("₹" + String.format("%.2f", simpleInterest), bodyFont)));
            table.addCell(new PdfPCell(new Phrase("₹" + String.format("%.2f", compoundInterest), bodyFont)));
        }

        doc.add(table);
        doc.close();
        return out.toByteArray();
    }
    public String updateItemStatus(int jwlno, boolean status) {
        Item item = repo.findById(jwlno).orElse(null);
        if (item == null) {
            return "Item not found";
        }
        item.setStatus(status);
        repo.save(item);
        return status ? "Item marked as In Loan" : "Item marked as Released";
    }
    public double getTotalLoan() {
        return repo.findAll()
                .stream()
                .mapToDouble(Item::getLoanAmount)
                .sum();
    }

//    public double getTotalInterest() {
//        return repo.findAll()
//                .stream()
//                .mapToDouble(Item::getInterest)
//                .sum();
//    }
//
//    public double getNetBalance() {
//        return getTotalInterest() - getTotalLoan();
//    }

    public List<Item> filter(String itemtype, String status) {

        if (itemtype != null && status != null) {
            boolean stat = status.equalsIgnoreCase("inloan");

            return repo.findAll().stream()
                    .filter(item ->
                            item.getItemtype().equalsIgnoreCase(itemtype)
                                    && item.getStatus() == stat)
                    .toList();
        }
        else if (itemtype != null) {
            return repo.findByItemtype(itemtype);
        }
        else if (status != null) {
            boolean stat = status.equalsIgnoreCase("inloan");
            return repo.findByStatus(stat);
        }
        else {
            return repo.findAll();
        }
    }




}
