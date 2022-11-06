package pojo;

import utils.ApproverType;
import utils.SrStatus;
import utils.UOM;
import utils.Utils;

import java.util.Random;

public class SrInformation {

    private int srReferenceNo;
    private SrStatus srStatus;
    private String approverEmail;
    private String srDate;
    private int wmStoreNo;
    private String invoiceNo;
    private double amount;
    private UOM uom;
    private String serviceDesc;
    private int internalId;

    public SrInformation(int srRef, SrStatus status, String approverEmail, String srDate,
                         int wmStoreNo, String invoiceNo, int amount, UOM uom, String serviceDesc) {
        this.srReferenceNo = srRef;
        this.srStatus = status;
        this.approverEmail = approverEmail;
        this.srDate = srDate;
        this.wmStoreNo = wmStoreNo;
        this.invoiceNo = invoiceNo;
        this.amount = amount;
        this.uom = uom;
        this.serviceDesc = serviceDesc;

    }

    public SrInformation(int wmStoreNo, UOM uom, ApproverType approverType) {
        this.wmStoreNo = wmStoreNo;
        this.uom = uom;
        this.setAmount(new Random().nextInt(1000));
        this.setServiceDesc(Utils.getRandomSrDescription());
        this.setApproverEmail(Utils.getApproverEmail(wmStoreNo, approverType));
        this.invoiceNo = "";
    }

    public SrInformation(int srReferenceNo, int id) {
        this.setSrReferenceNo(srReferenceNo);
        this.setInternalId(id);
    }

    public int getInternalId() {
        return internalId;
    }

    public void setInternalId(int internalId) {
        this.internalId = internalId;
    }


    public int getSrReferenceNo() {
        return srReferenceNo;
    }

    public void setSrReferenceNo(int srReferenceNo) {
        this.srReferenceNo = srReferenceNo;
    }

    public SrStatus getSrStatus() {
        return srStatus;
    }

    public void setSrStatus(SrStatus srStatus) {
        this.srStatus = srStatus;
    }

    public String getApproverEmail() {
        return approverEmail;
    }

    public void setApproverEmail(String approverEmail) {
        this.approverEmail = approverEmail;
    }

    public String getSrDate() {
        return srDate;
    }

    public void setSrDate(String srDate) {
        this.srDate = srDate;
    }

    public int getWmStoreNo() {
        return wmStoreNo;
    }

    public void setWmStoreNo(int wmStoreNo) {
        this.wmStoreNo = wmStoreNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public UOM getUom() {
        return uom;
    }

    public void setUom(UOM uom) {
        this.uom = uom;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }



}
