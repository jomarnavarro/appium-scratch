package com.tac.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InvoiceFile {

    private int srReferenceNo;

    private static final String CSV_FILE_HEADERS =
            "InvoiceNumber,InvoiceDate,VendorNumber,PONumber,RegularGNFRDeliveryDate,PaymentTerms,StoreNumber," +
            "InvoiceType,InvoiceDepartment,AssemblyTypeCode,SpendCategory,SpendSubCategory,CreditMemo,ServiceReceiptID," +
            "InvoiceComments,TrackingNumber,CustomerReferenceNumber,LineNumber,Qty,UOM,UnitPrice,ProductID," +
            "InvoiceLineDesc,LineType,DateOfService,FreightCharge,USSalesTax,CanTaxType,GSTAmount,HSTAmount," +
            "GST-HSTVATID,QSTAmount,QSTVATID,PSTAmount";

    private List<InvoiceItem> invoiceItems;

    public InvoiceFile() {
        this(-1);

    }

    public InvoiceFile(int srReferenceNo) {
        this.srReferenceNo = srReferenceNo;
        invoiceItems = new ArrayList<InvoiceItem>();
    }


    private String getSingleInvoiceFile(int numItems) throws IllegalAccessException {
        return getSingleInvoiceFile(numItems, InvoiceItem.DEFAULT_INVOICE_NO_LENGTH);
    }

    private String getSingleInvoiceFile(int numItems, int invoiceNoCharLength) {
        InvoiceItem firstItem = new InvoiceItem(this.srReferenceNo);
        firstItem.setInvoiceNoLength(invoiceNoCharLength);
        invoiceItems.add(firstItem);
        for(int i = 0; i < numItems - 1; i++) {
            InvoiceItem lastItem = invoiceItems.get(invoiceItems.size() - 1);
            invoiceItems.add(new InvoiceItem(lastItem));
        }

        return this.toString();
    }

    public void createSingleInvoiceFile(String filePath, int numItems) {
        this.createSingleInvoiceFile(filePath, numItems, 8);
    }

    public void createSingleInvoiceFile(String filePath, int numItems, int invoiceCharLength) {
        try {
            File invoiceFile = new File(filePath);
            if(invoiceFile.createNewFile()) {
                FileWriter myWriter = new FileWriter(invoiceFile, StandardCharsets.US_ASCII);
                myWriter.write(getSingleInvoiceFile(numItems));
                myWriter.close();
            } else {
                FileWriter myWriter = new FileWriter(invoiceFile, StandardCharsets.US_ASCII);
                myWriter.write("");
                myWriter.close();
                myWriter = new FileWriter(invoiceFile, StandardCharsets.US_ASCII);
                myWriter.write(getSingleInvoiceFile(numItems));
                myWriter.close();
            }
        } catch(IOException | IllegalAccessException ioe) {}
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s", CSV_FILE_HEADERS));
        for(InvoiceItem item: invoiceItems) {
            sb.append(String.format("\n%s", item.toString()));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        InvoiceFile ifile = new InvoiceFile();
        ifile.createSingleInvoiceFile("./invoiceFiles/file.csv", 1);

        InvoiceFile iFileMultipleRows = new InvoiceFile();
        iFileMultipleRows.createSingleInvoiceFile("./invoiceFiles/file2.csv", 3);

        InvoiceFile iFileMultipleRowsSrId = new InvoiceFile(35526);
        iFileMultipleRowsSrId.createSingleInvoiceFile("./invoiceFiles/file3.csv", 3);


    }
}
