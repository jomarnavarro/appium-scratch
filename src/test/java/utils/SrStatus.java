package utils;

public enum SrStatus {
    PendingApproval("Pending Approval"),
    Approved("Approved"),
    Rejected("Rejected"),
    Cancelled("Cancelled"),
    Invoiced("Invoiced");

    public String status;

    SrStatus(String status) {
        this.status = status;
    }
    public String toString() {
        return this.status;
    }
}