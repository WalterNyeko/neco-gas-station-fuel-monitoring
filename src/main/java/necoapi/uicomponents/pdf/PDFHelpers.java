package necoapi.uicomponents.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import necoapi.domain.AccountResponse;
import necoapi.domain.TransactionResponse;
import necoapi.helpers.Selected;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PDFHelpers {
    public static void addTableHeader(PdfPTable table) {
        Stream.of("Account Name", "Opening Date", "Opening Balance", "Current Balance")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void addTableHeaderTransaction(PdfPTable table) {
        Stream.of("Account Name", "Product Name", "Transaction Date", "Vehicle Number", "Order Number", "Transaction Amount", "Current Balance")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    public static void addRows(PdfPTable table, List<AccountResponse> accountResponses) {
        accountResponses.forEach(accountResponse -> {
            PdfPCell accountName = new PdfPCell();
            accountName.setBorderWidth(1);
            accountName.setPhrase(new Phrase(accountResponse.getAccountName()));
            table.addCell(accountName);

            PdfPCell accountOpeningDate = new PdfPCell();
            accountOpeningDate.setBorderWidth(1);
            accountOpeningDate.setPhrase(new Phrase(accountResponse.getOpeningDate()));
            table.addCell(accountOpeningDate);

            PdfPCell accountOpeningBalance = new PdfPCell();
            accountOpeningBalance.setBorderWidth(1);
            accountOpeningBalance.setPhrase(new Phrase(accountResponse.getOpeningBalance().toString()));
            table.addCell(accountOpeningBalance);

            PdfPCell accountCurrentBalance = new PdfPCell();
            accountCurrentBalance.setBorderWidth(1);
            accountCurrentBalance.setPhrase(new Phrase(accountResponse.getCurrentBalance().toString()));
            table.addCell(accountCurrentBalance);
        });

    }

    public static void addRowsTransactions(PdfPTable table, List<TransactionResponse> transactionResponses) {
        transactionResponses.forEach(transactionResponse -> {

            PdfPCell accountName = new PdfPCell();
            accountName.setBorderWidth(1);
            accountName.setPhrase(new Phrase(transactionResponse.getAccountName()));
            table.addCell(accountName);

            PdfPCell productName = new PdfPCell();
            productName.setBorderWidth(1);
            productName.setPhrase(new Phrase(transactionResponse.getProductName()));
            table.addCell(productName);

            PdfPCell transactionDate = new PdfPCell();
            transactionDate.setBorderWidth(1);
            transactionDate.setPhrase(new Phrase(transactionResponse.getTransactionDate()));
            table.addCell(transactionDate);

            PdfPCell transactionVehicle = new PdfPCell();
            transactionVehicle.setBorderWidth(1);
            transactionVehicle.setPhrase(new Phrase(transactionResponse.getVehicleNumber()));
            table.addCell(transactionVehicle);

            PdfPCell transactionOrder = new PdfPCell();
            transactionOrder.setBorderWidth(1);
            transactionOrder.setPhrase(new Phrase(transactionResponse.getOrderNumber()));
            table.addCell(transactionOrder);

            PdfPCell transactionAmount = new PdfPCell();
            transactionAmount.setBorderWidth(1);
            transactionAmount.setPhrase(new Phrase(transactionResponse.getTransactionAmount().toString()));
            table.addCell(transactionAmount);

            PdfPCell transactionCurrentBalance = new PdfPCell();
            transactionCurrentBalance.setBorderWidth(1);
            transactionCurrentBalance.setPhrase(new Phrase(transactionResponse.getCurrentBalance().toString()));
            table.addCell(transactionCurrentBalance);
        });

    }

    public static void printCustomers() {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/Customers.pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 24, BaseColor.BLUE);
            Paragraph title = new Paragraph("NECO PETROL STATION'S FUEL ACCOUNTS", font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setLeading(2);
            //To add the title to PDF
            document.addTitle("Neco Gas Station Fuel Accounts");
            //To add the Author for the PDF
            document.addAuthor("Neco Gas Station");
            //To add the subject to the PDF document
            document.addSubject("Fuel Accounts");

            document.add(title);
            writer.setPageEvent(new PDFBackground());

            // add a couple of blank lines
            blankLinesInPDF(document);

            PdfPTable table = new PdfPTable(4);
            table.setTotalWidth(PageSize.A4.getWidth() - 50);
            table.setLockedWidth(true);

            PDFHelpers.addTableHeader(table);
            PDFHelpers.addRows(table, Selected.getInstance().getAccountResponses());

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void blankLinesInPDF(Document document) throws DocumentException {
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
    }

    public static void printTransaction() {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/Transactions.pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 24, BaseColor.BLUE);
            Paragraph title = new Paragraph(Selected.getInstance().getSelectedAccount().getAccountName().toUpperCase()+"'s TRANSACTIONS", font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setLeading(2);
            //To add the title to PDF
            document.addTitle("Neco Gas Station, "+Selected.getInstance().getSelectedAccount().getAccountName()+"'s Transactions");
            //To add the Author for the PDF
            document.addAuthor("Neco Gas Station");
            //To add the subject to the PDF document
            document.addSubject("Fuel Account's Transactions");

            document.add(title);
            writer.setPageEvent(new PDFBackground());

            // add a couple of blank lines
            PDFHelpers.blankLinesInPDF(document);

            PdfPTable table = new PdfPTable(7);
            table.setTotalWidth(PageSize.A4.getWidth() - 50);
            table.setLockedWidth(true);

            PDFHelpers.addTableHeaderTransaction(table);
            PDFHelpers.addRowsTransactions(table, Selected.getInstance().getTransactionResponses());

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
