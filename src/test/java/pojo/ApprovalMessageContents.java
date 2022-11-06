package pojo;

public class ApprovalMessageContents {

    String emailSubject;
    String emailHtmlBody;
    String approveEmailHref;
    String approveEmailOnclickAction;
    String rejectEmailHref;
    String rejectEmailOnclickAction;

    public ApprovalMessageContents(String subject, String emailBody, String approveEmailHref,
                                   String approveOnclickAction, String rejectEmailHref,
                                   String rejectOnclickAction) {
        this.emailSubject = subject;
        this.emailHtmlBody = emailBody;
        this.approveEmailHref = approveEmailHref;
        this.approveEmailOnclickAction = approveOnclickAction;
        this.rejectEmailHref = rejectEmailHref;
        this.rejectEmailOnclickAction = rejectOnclickAction;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailHtmlBody() {
        return emailHtmlBody;
    }

    public String getApproveEmailHref() {
        return approveEmailHref;
    }

    public String getApproveEmailOnclickAction() {
        return approveEmailOnclickAction;
    }

    public String getRejectEmailHref() {
        return rejectEmailHref;
    }

    public String getRejectEmailOnclickAction() {
        return rejectEmailOnclickAction;
    }
}
