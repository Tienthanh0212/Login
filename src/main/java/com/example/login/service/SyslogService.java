package com.example.login.service;

import com.example.login.DTO.Syslog.DeletedCountDTO;
import com.example.login.DTO.Syslog.MonthlyRecordCountDTO;
import com.example.login.DTO.Syslog.SysLogDTO;
import com.example.login.repository.SyslogRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
public interface SyslogService {
List<MonthlyRecordCountDTO> getMonthlyRecordCountsBetweenDates(SysLogDTO sysLogDTO);

    DeletedCountDTO deleteRecordsByDateRangeAndMethod(SysLogDTO sysLogDTO);

    void exportMonthlyRecordCountsToPDF(SysLogDTO sysLogDTO) throws DocumentException, FileNotFoundException;

    void exportMonthlyRecordCountsToCSV(SysLogDTO sysLogDTO) throws IOException;
}
@Service
class SyslogServiceImpl implements  SyslogService{

    @Autowired
    private SyslogRepository syslogRepository;

    @Override
    public List<MonthlyRecordCountDTO> getMonthlyRecordCountsBetweenDates(SysLogDTO sysLogDTO) {
        List<Object[]> results = syslogRepository.findCountByMonth(sysLogDTO.getStartDate(), sysLogDTO.getEndDate(), sysLogDTO.getMethod());
        return results.stream()
                .map(record -> {
                    java.sql.Date monthDate = (java.sql.Date) record[0];
                    Number count = (Number) record[1];
                    count = (count != null && count.intValue() == 0) ? null : count;

                    // Convert java.sql.Date to java.util.Date for compatibility
                    Date utilDate = new Date(monthDate.getTime());
                    // Format the date into "yyyy/MM" format
                    String formattedMonth = new SimpleDateFormat("yyyy/MM", Locale.ENGLISH).format(utilDate);

                    return new MonthlyRecordCountDTO(formattedMonth, count);
                })
                .collect(Collectors.toList());
    }


    @Override
    public DeletedCountDTO deleteRecordsByDateRangeAndMethod(SysLogDTO sysLogDTO) {
        int deletedCount = syslogRepository.deleteByDateRange(sysLogDTO.getStartDate(), sysLogDTO.getEndDate(), sysLogDTO.getMethod());
        return new DeletedCountDTO(deletedCount);
    }

    @Override
    public void exportMonthlyRecordCountsToPDF(SysLogDTO sysLogDTO) throws DocumentException, FileNotFoundException {
        List<MonthlyRecordCountDTO> records = getMonthlyRecordCountsBetweenDates(sysLogDTO);

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("monthly_record_counts.pdf"));
        doc.open();

        Paragraph title = new Paragraph("Monthly Record Count\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Add table headers
        PdfPCell header1 = new PdfPCell(new Paragraph("Month", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        header1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(header1);

        PdfPCell header2 = new PdfPCell(new Paragraph("Record Count", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        header2.setBackgroundColor(BaseColor.GRAY);
        table.addCell(header2);

        // Add table data
        for (MonthlyRecordCountDTO record : records) {
            table.addCell(record.getMonth());
            table.addCell(record.getCount() != null ? record.getCount().toString() : "0");
        }

        doc.add(table);
        doc.close();
    }

    @Override
    public void exportMonthlyRecordCountsToCSV(SysLogDTO sysLogDTO) throws IOException {
        List<MonthlyRecordCountDTO> records = getMonthlyRecordCountsBetweenDates(sysLogDTO);

        try (CSVWriter writer = new CSVWriter(new FileWriter("monthly_record_counts.csv"))) {
            String[] header = { "Month", "Record Count" };
            writer.writeNext(header);

            for (MonthlyRecordCountDTO record : records) {
                String[] data = { record.getMonth(), record.getCount() != null ? record.getCount().toString() : "0" };
                writer.writeNext(data);
            }
        }
    }

}
