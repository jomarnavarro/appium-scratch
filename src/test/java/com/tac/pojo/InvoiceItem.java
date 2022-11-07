package com.tac.pojo;

import com.github.javafaker.Faker;
import com.tac.utils.Utils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class InvoiceItem {

    public static final int DEFAULT_INVOICE_NO_LENGTH = 12;
    int invoiceNoLength = DEFAULT_INVOICE_NO_LENGTH;

    String InvoiceNumber;
    String InvoiceDate;
    String VendorNumber;
    String PONumber;
    String RegularGNFRDeliveryDate;
    String PaymentTerms;
    String StoreNumber;
    String InvoiceType;
    String InvoiceDepartment;
    String AssemblyTypeCode;
    String SpendCategory;
    String SpendSubCategory;
    String CreditMemo;
    String ServiceReceiptID;
    String InvoiceComments;
    String TrackingNumber;
    String CustomerReferenceNumber;
    String LineNumber;
    String Qty;
    String UOM;
    String UnitPrice;
    String ProductID;
    String InvoiceLineDesc;
    String LineType;
    String DateOfService;
    String FreightCharge;
    String USSalesTax;
    String CanTaxType;
    String GSTAmount;
    String HSTAmount;
    String GST_HSTVATID;
    String QSTAmount;
    String QSTVATID;
    String PSTAmount;

    private static final String[] vendorNumbers = new String[] {
            "31211:915858181","31213:915858183", "31212:915858182","31214:915858184"
            };

    private static final String[] storeNumbers = new String[] {
            "1001", "1002"
    };

    private static final String[] invoiceTypes = new String[] {
            "9249"
    };

    private static final String[] assemblyServicesType = new String[] {
            "Bikes / Patio / Grills / Modular Resets", "Painting or Cleaning", "Maintenance or Repairs"
    };

    private String chooseRamdomString(String[] data) {
        return data[new Random().nextInt(data.length)];
    }

    private static final String[] uomValues = new String[] {
            "EA", "BG", "BI", "BF", "BD", "BX", "BR", "BO", "CA", "CN", "CC", "AF", "C3", "CM", "CT", "DA", "DZ", "DR",
            "EA", "FT", "GS", "GA", "GR", "HR", "IN", "JR", "KG", "KT", "K6", "LB", "LO", "LT", "MJ", "ML", "MM", "MO",
            "MR", "TN", "OZ", "PK", "PA", "PF", "PR", "PT", "QT", "RL", "RM", "ST", "SO", "SF", "SI", "SY", "TO", "TB",
            "UN", "WK", "YD", "YR", "All", "Dollars", "Hours", "Other"
    };

    private static final String[] lineTypeValues = new String[] {"Labor", "Material"};

    public InvoiceItem() {
        this( -1);

    }

    public InvoiceItem(int srReferenceNo) {
        this.initValuesBlank();
        Date d = Date.from(Instant.EPOCH);
        LocalDateTime date = LocalDateTime.now();
        String localDateString = date.format(DateTimeFormatter.ofPattern("MMddHHmmss[SS]"));
        localDateString = localDateString.length() == this.invoiceNoLength
                ? localDateString
                : Utils.formatToLength(localDateString, this.invoiceNoLength);
        this.InvoiceNumber = localDateString;
        this.InvoiceDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.VendorNumber = chooseRamdomString(vendorNumbers);
        this.StoreNumber = chooseRamdomString(storeNumbers);
        this.InvoiceType = chooseRamdomString(invoiceTypes);
        this.AssemblyTypeCode = chooseRamdomString(assemblyServicesType);
        this.ServiceReceiptID = srReferenceNo > 0 ? String.valueOf(srReferenceNo) : "";
        this.InvoiceComments = String.format("I like to drink %s on %S", new Faker().beer().name(), this.InvoiceDate);
        this.LineNumber = "1";
        this.Qty = String.valueOf(new Random().nextInt(100));
        this.UOM = chooseRamdomString(uomValues);
        this.UnitPrice = String.valueOf(String.format("%.2f", new Random().nextDouble()*20));
        this.ProductID = "EZ Bikes";
        this.InvoiceLineDesc = "EZ Bikes (Kids)";
        this.LineType = chooseRamdomString(lineTypeValues);
        this.DateOfService = date.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
    }


    public static void main(String[] args){
        InvoiceItem i = new InvoiceItem();
        i.setInvoiceNoLength(15);
        i.setInvoiceNoLength(5);
        System.out.println(i.toString());
        i = new InvoiceItem(35625);
        System.out.println(i.toString());
        InvoiceItem i2 = new InvoiceItem(i);
    }


    public void setInvoiceNoLength(int newLength) {
        if(newLength > 0 && this.invoiceNoLength != newLength) {
            this.invoiceNoLength = newLength;
            setInvoiceNumber(Utils.formatToLength(this.getInvoiceNumber(), newLength));
        }
    }

    public int getInvoiceNoLength() {
        return this.invoiceNoLength;
    }

    public InvoiceItem(InvoiceItem item){
        this.initValuesBlank();
        try {
            Field[] invoiceFields = InvoiceItem.class.getDeclaredFields();

            for (Field f : invoiceFields) {
                if (!f.getType().equals(String.class))
                    continue;
                String currentValue = (String) f.get(item);
                if (f.getName().equals("FreightCharge") || !currentValue.equals("")) {
                    f.set(this, currentValue);
                }
            }
            this.AssemblyTypeCode = chooseRamdomString(assemblyServicesType);
            this.LineNumber = String.valueOf(Integer.parseInt(item.getLineNumber()) + 1);
            this.Qty = String.valueOf(new Random().nextInt(100));
            this.UOM = chooseRamdomString(uomValues);
            this.UnitPrice = String.valueOf(String.format("%.2f", new Random().nextDouble()*20));
            this.LineType = chooseRamdomString(lineTypeValues);

        } catch (IllegalAccessException iae) {
            this.init(item);
        }
    }

    public void init(InvoiceItem item) {
        this.initValuesBlank();
        this.invoiceNoLength = item.getInvoiceNoLength();
        this.InvoiceNumber = item.getInvoiceNumber();
        this.InvoiceDate = item.getInvoiceDate();
        this.VendorNumber = item.getVendorNumber();
        this.StoreNumber = item.getStoreNumber();
        this.InvoiceType = item.getInvoiceType();
        this.AssemblyTypeCode = chooseRamdomString(assemblyServicesType);
        this.ServiceReceiptID = item.getServiceReceiptID();
        this.InvoiceComments = item.getInvoiceComments();
        this.LineNumber = String.valueOf(Integer.parseInt(item.getLineNumber()) + 1);;
        this.Qty = String.valueOf(new Random().nextInt(100));
        this.UOM = chooseRamdomString(uomValues);
        this.UnitPrice = String.valueOf(String.format("%.2f", new Random().nextDouble()*20));
        this.ProductID = item.getProductID();
        this.InvoiceLineDesc = getInvoiceLineDesc();
        this.LineType = chooseRamdomString(lineTypeValues);
        this.DateOfService = item.getDateOfService();
    }

    public String toString() {

        String srReceiptNoStr =
                this.ServiceReceiptID.equals("") ? "" : String.format("%07d", Integer.parseInt(ServiceReceiptID));
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                this.InvoiceNumber,
                this.InvoiceDate,
                this.VendorNumber,
                this.PONumber,
                this.RegularGNFRDeliveryDate,
                this.PaymentTerms,
                this.StoreNumber,
                this.InvoiceType,
                this.InvoiceDepartment,
                this.AssemblyTypeCode,
                this.SpendCategory,
                this.SpendSubCategory,
                this.CreditMemo,
                srReceiptNoStr,
                this.InvoiceComments,
                this.TrackingNumber,
                this.CustomerReferenceNumber,
                this.LineNumber,
                this.Qty,
                this.UOM,
                this.UnitPrice,
                this.ProductID,
                this.InvoiceLineDesc,
                this.LineType,
                this.DateOfService,
                this.FreightCharge,
                this.USSalesTax,
                this.CanTaxType,
                this.GSTAmount,
                this.HSTAmount,
                this.GST_HSTVATID,
                this.QSTAmount,
                this.QSTVATID,
                this.PSTAmount
        );
    }

    private static final String BLANK = "";

    private void initValuesBlank() {
        this.InvoiceNumber = BLANK;
        this.InvoiceDate = BLANK;
        this.VendorNumber = BLANK;
        this.PONumber = BLANK;
        this.RegularGNFRDeliveryDate = BLANK;
        this.PaymentTerms = BLANK;
        this.StoreNumber = BLANK;
        this.InvoiceType = BLANK;
        this.InvoiceDepartment = BLANK;
        this.AssemblyTypeCode = BLANK;
        this.SpendCategory = BLANK;
        this.SpendSubCategory = BLANK;
        this.CreditMemo = BLANK;
        this.ServiceReceiptID = BLANK;
        this.InvoiceComments = BLANK;
        this.TrackingNumber = BLANK;
        this.CustomerReferenceNumber = BLANK;
        this.LineNumber = BLANK;
        this.Qty = BLANK;
        this.UOM = BLANK;
        this.UnitPrice = BLANK;
        this.ProductID = BLANK;
        this.InvoiceLineDesc = BLANK;
        this.LineType = BLANK;
        this.DateOfService = BLANK;
        this.FreightCharge = BLANK;
        this.USSalesTax = BLANK;
        this.CanTaxType = BLANK;
        this.GSTAmount = BLANK;
        this.HSTAmount = BLANK;
        this.GST_HSTVATID = BLANK;
        this.QSTAmount = BLANK;
        this.QSTVATID = BLANK;
        this.PSTAmount = BLANK;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public String getVendorNumber() {
        return VendorNumber;
    }

    public String getPONumber() {
        return PONumber;
    }

    public String getRegularGNFRDeliveryDate() {
        return RegularGNFRDeliveryDate;
    }

    public String getPaymentTerms() {
        return PaymentTerms;
    }

    public String getStoreNumber() {
        return StoreNumber;
    }

    public String getInvoiceType() {
        return InvoiceType;
    }

    public String getInvoiceDepartment() {
        return InvoiceDepartment;
    }

    public String getAssemblyTypeCode() {
        return AssemblyTypeCode;
    }

    public String getSpendCategory() {
        return SpendCategory;
    }

    public String getSpendSubCategory() {
        return SpendSubCategory;
    }

    public String getCreditMemo() {
        return CreditMemo;
    }

    public String getServiceReceiptID() {
        return ServiceReceiptID;
    }

    public String getInvoiceComments() {
        return InvoiceComments;
    }

    public String getTrackingNumber() {
        return TrackingNumber;
    }

    public String getCustomerReferenceNumber() {
        return CustomerReferenceNumber;
    }

    public String getLineNumber() {
        return LineNumber;
    }

    public String getQty() {
        return Qty;
    }

    public String getUOM() {
        return UOM;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public String getProductID() {
        return ProductID;
    }

    public String getInvoiceLineDesc() {
        return InvoiceLineDesc;
    }

    public String getLineType() {
        return LineType;
    }

    public String getDateOfService() {
        return DateOfService;
    }

    public String getFreightCharge() {
        return FreightCharge;
    }

    public String getUSSalesTax() {
        return USSalesTax;
    }

    public String getCanTaxType() {
        return CanTaxType;
    }

    public String getGSTAmount() {
        return GSTAmount;
    }

    public String getHSTAmount() {
        return HSTAmount;
    }

    public String getGST_HSTVATID() {
        return GST_HSTVATID;
    }

    public String getQSTAmount() {
        return QSTAmount;
    }

    public String getQSTVATID() {
        return QSTVATID;
    }

    public String getPSTAmount() {
        return PSTAmount;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public void setVendorNumber(String vendorNumber) {
        VendorNumber = vendorNumber;
    }

    public void setPONumber(String PONumber) {
        this.PONumber = PONumber;
    }

    public void setRegularGNFRDeliveryDate(String regularGNFRDeliveryDate) {
        RegularGNFRDeliveryDate = regularGNFRDeliveryDate;
    }

    public void setPaymentTerms(String paymentTerms) {
        PaymentTerms = paymentTerms;
    }

    public void setStoreNumber(String storeNumber) {
        StoreNumber = storeNumber;
    }

    public void setInvoiceType(String invoiceType) {
        InvoiceType = invoiceType;
    }

    public void setInvoiceDepartment(String invoiceDepartment) {
        InvoiceDepartment = invoiceDepartment;
    }

    public void setAssemblyTypeCode(String assemblyTypeCode) {
        AssemblyTypeCode = assemblyTypeCode;
    }

    public void setSpendCategory(String spendCategory) {
        SpendCategory = spendCategory;
    }

    public void setSpendSubCategory(String spendSubCategory) {
        SpendSubCategory = spendSubCategory;
    }

    public void setCreditMemo(String creditMemo) {
        CreditMemo = creditMemo;
    }

    public void setServiceReceiptID(String serviceReceiptID) {
        ServiceReceiptID = serviceReceiptID;
    }

    public void setInvoiceComments(String invoiceComments) {
        InvoiceComments = invoiceComments;
    }

    public void setTrackingNumber(String trackingNumber) {
        TrackingNumber = trackingNumber;
    }

    public void setCustomerReferenceNumber(String customerReferenceNumber) {
        CustomerReferenceNumber = customerReferenceNumber;
    }

    public void setLineNumber(String lineNumber) {
        LineNumber = lineNumber;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public void setInvoiceLineDesc(String invoiceLineDesc) {
        InvoiceLineDesc = invoiceLineDesc;
    }

    public void setLineType(String lineType) {
        LineType = lineType;
    }

    public void setDateOfService(String dateOfService) {
        DateOfService = dateOfService;
    }

    public void setFreightCharge(String freightCharge) {
        FreightCharge = freightCharge;
    }

    public void setUSSalesTax(String USSalesTax) {
        this.USSalesTax = USSalesTax;
    }

    public void setCanTaxType(String canTaxType) {
        CanTaxType = canTaxType;
    }

    public void setGSTAmount(String GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public void setHSTAmount(String HSTAmount) {
        this.HSTAmount = HSTAmount;
    }

    public void setGST_HSTVATID(String GST_HSTVATID) {
        this.GST_HSTVATID = GST_HSTVATID;
    }

    public void setQSTAmount(String QSTAmount) {
        this.QSTAmount = QSTAmount;
    }

    public void setQSTVATID(String QSTVATID) {
        this.QSTVATID = QSTVATID;
    }

    public void setPSTAmount(String PSTAmount) {
        this.PSTAmount = PSTAmount;
    }
}
