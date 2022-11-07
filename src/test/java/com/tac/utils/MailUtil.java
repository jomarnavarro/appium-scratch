package com.tac.utils;

import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.tac.pojo.ApprovalMessageContents;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

public class MailUtil {


    private GmailClient gmailClient;
    private static final String USER_AGENT = "Mozilla" ;
    private String base64Login;
    protected String baseUrl = "https://qa-mail.directcommerce.com/roundcube/";

    private Map<String, String> gatewayHeaders = new HashMap<>(Map.of(
            "Upgrade-Insecure-Requests", "1"));

    private Map<String, String> roundcubeHeaders = new HashMap<>(Map.of(
            "Content-Type", "application/x-www-form-urlencoded",
            "Host", "qa-mail.directcommerce.com",
            "Origin", "https://qa-mail.directcommerce.com",
            "Referer", "https://qa-mail.directcommerce.com/roundcube/?_task=mail&_mbox=INBOX",
            "Cache-Control", "max-age=0",
            "Connection", "keep-alive",
            "Sec-Fetch-Dest", "document",
            "Sec-Fetch-Mode", "navigate",
            "Sec-Fetch-Site", "same-origin",
            "Sec-Fetch-User", "?1"
    ));

    Map<String, String> roundcubeAuthParams = new HashMap<>(Map.of(
        "_token", "",
        "_task", "login",
        "_action", "login",
        "_timezone", "America/Monterrey",
        "_url", "_task=mail&_mbox=INBOX",
        "_user", "",
        "_pass", ""
    ));

    private Map<String, String> roundcubeComposeParams;

    private String loc;
    private HashMap<String, String> inboxListParams;
    private final String FIRST_COOKIE_NAME = "roundcube_sessid";
    private Map<String, String> emailComposeParams = new HashMap<>(Map.of(
            "_token","",
            "_task","mail",
            "_action","send",
            "_id","",
            "_attachments","",
            "_from","2",
            "_to","",
            "_cc","",
            "_bcc","",
            "_replyto",""
    ));

    private Map<String, String> complementaryEmailComposeHeaders = new HashMap<>(Map.of(
            "_followupto","",
            "_subject","",
            "editorSelector","html",
            "_priority","0",
            "_store_target","Sent",
            "_draft_saveid","13",
            "_draft","",
            "_is_html","1",
            "_framed","1",
            "_message",""
    ));

    /**
     * This method takes care of the first authentication in roundcube (the one with an alert)
     * @param testerUser tester email handler as in handler@directcommerce.com
     * @param testerPass password to email handler above
     * @return <code>org.jsoup.nodes.Document</code>, an HTML document with the roundcube authentication
     * @throws IOException
     */
    private Document authenticateGateway(String testerUser, String testerPass) throws IOException {
        String gatewayUrl = String.format("%s?_task=mail&_mbox=INBOX", baseUrl);
        String login = String.format("%s:%s", testerUser, testerPass);
        base64Login = new String(Base64.getEncoder().encodeToString(login.getBytes()));
        gatewayHeaders.put("Authorization", String.format("Basic %s", base64Login));
        long retries = Constants.SHORT_WAIT;
        Document gatewayDoc = null;
        while(retries > 0) {
            try {
                gatewayDoc = Jsoup.connect(gatewayUrl)
                        .userAgent(USER_AGENT)
                        .headers(gatewayHeaders)
                        .timeout(5000)
                        .get();
                if (gatewayDoc != null
                        && gatewayDoc.connection().response().cookies().get(FIRST_COOKIE_NAME) != null
                        && gatewayDoc.select("input[name='_token']").size() > 0) {
                    return gatewayDoc;
                }

            } catch (SocketTimeoutException ste) { }
            retries--;
        }
        return null;
    }

    /**
     * Takes care of athenticating on the roundcube email form.
     * @param doc HTML document from the roundcube email form
     * @param mailUser roundcube email user
     * @param mailPass roundcube email password
     * @return <code>org.jsoup.nodes.Document</code> once authenticated into roundcube email, containing new Cookies.
     * @throws IOException
     */
    private Document authenticateRoundCube(Document doc, String mailUser, String mailPass) throws IOException {

        //grabs the first cookie provided after authenticating on the gateway
        String firstCookie = String.format("%s=%s", FIRST_COOKIE_NAME,
                doc.connection().response().cookies().get(FIRST_COOKIE_NAME));
        //The roundcube login form contains a hidden input with a token, we grab this value
        Element tokenNode = doc.selectFirst("input[name='_token']");
        String tokenVal = tokenNode.attr("value");
        //add cookie header
        roundcubeHeaders.put("Cookie", firstCookie);
        //add all the headers from the gatewayHeaders (containing the Authentication header)
        for(Map.Entry<String, String> entry: gatewayHeaders.entrySet()) {
            roundcubeHeaders.put(entry.getKey(), entry.getValue());
        }

        //roundcube email login form auth parameters.
        roundcubeAuthParams.put("_token", tokenVal);
        roundcubeAuthParams.put("_user", mailUser);
        roundcubeAuthParams.put("_pass", mailPass);

        return Jsoup.connect(String.format("%s?_task=login", baseUrl))
                .headers(roundcubeHeaders)
                .data(roundcubeAuthParams)
                .followRedirects(false)
                .userAgent("Mozilla")
                .timeout(5000)
                .post();
    }

    /**
     * Fetches the mailbox by using the cookies provided by the previous step <code>com.walmart.sr.utils.MailUtil.authenticateRoundCube</code>
     * It also provides valuable information such as a token in the 'Location' header, as well as the renewed cookies from the authentification.
     * @param doc HTML response containing the 'messages' panel.
     * @return HTML document that contains location
     * @throws IOException
     */
    private Document fetchMailbox(Document doc) throws IOException {
        //grab the document response, to obtain location and cookies.
        Connection.Response res = doc.connection().response();
        loc = res.header("Location").substring(2);
        List<String> individualCookies = Arrays.asList(res.header("Set-Cookie").split(";"));
        StringBuilder cookieStrBuilder = new StringBuilder();
        individualCookies.stream()
                .filter( x -> x.contains("roundcube_") && !x.endsWith("del-"))
                .forEach(x -> cookieStrBuilder.append(x + ";"));
        String newCookies = cookieStrBuilder.toString().replace("HttpOnly, ", "");
        newCookies = newCookies.substring(0, newCookies.length() - 1).trim();
        roundcubeHeaders.put("Cookie", newCookies);


        return Jsoup.connect(String.format("%s%s", baseUrl, loc))
                .headers(roundcubeHeaders)
                .timeout(5000)
                .get();
    }

    /**
     * It obtains the email id from the previous response and the service receipt number.
     * @param doc HTML response from previous call
     * @param srNo Service Receipt number
     * @return String with the emailId value.
     * @throws IOException
     * @throws ParseException
     */
    private String fetchMailId(Document doc, int srNo) throws IOException, ParseException {
        //change referer, xroundcoberequest and xxrequesterwith headers
        //Uses the token from the 'location' header to request the mail ID.
        List<String> locParts = Arrays.asList(loc.split("token="));
        String locToken = locParts.get(locParts.size() - 1);
        roundcubeHeaders.put("Referer", String.format("%s%s", baseUrl, loc));
        roundcubeHeaders.put("X-Roundcube-Request", locToken);
        roundcubeHeaders.put("X-Requested-With", "XMLHttpRequest");
        //It tries a max. number of 60 times to get the mail id from the mailbox.  Sometimes the email takes
        //a few seconds to arrive.
        for( int i = 0; i < 60; i++) {
            long currentTime = System.currentTimeMillis();
            inboxListParams = new HashMap<>(
                    Map.of("_task", "mail",
                            "_action", "list",
                            "_refresh", "1",
                            "_mbox", "INBOX",
                            "_remote", "1",
                            "_unlock", String.format("loading%d", currentTime),
                            "_", String.valueOf(currentTime)
                    )
            );

            Document inboxMailList = Jsoup.connect(baseUrl)
                    .headers(roundcubeHeaders)
                    .data(inboxListParams)
                    .get();

            //this takes the <body> tag and splits it by the string "this.add_message_row(" marking the start of
            // an email message
            List<String> resText = Arrays.asList(inboxMailList.body().text().split("this.add_message_row\\("));
            //filters the strings by the ones containingthe Approval subject with the srNo
            List<String> filteredMessages = resText
                    .stream()
                    .filter( txt -> txt.contains(
                            String.format("Approval Required: Service Receipt #%07d", srNo))
                    )
                    .collect(Collectors.toList());
            if (filteredMessages.size() != 0) {
                //the filtered message takes the first string in the format <emailId>,<restOfTheText>
                return filteredMessages.get(0).split(",")[0].trim();
            } else {
                try {
                    System.out.println(String.format("Waiting for %d seconds...", Constants.SHORT_WAIT));
                    Thread.sleep(Constants.SHORT_WAIT_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Fetches the email folder as an html document.
     * @param folder i.e. Inbox, Sent, etc.
     * @return HTML Parsed Document with mail list contents.
     */
    public Document fetchEmailFolderMails(String folder) throws IOException {
        long currentTime = System.currentTimeMillis();
        inboxListParams = new HashMap<>(
                Map.of("_task", "mail",
                        "_action", "list",
                        "_refresh", "1",
                        "_mbox", folder,
                        "_remote", "1",
                        "_unlock", String.format("loading%d", currentTime),
                        "_", String.valueOf(currentTime)
                )
        );

        Document inboxMailList = Jsoup.connect(baseUrl)
                .headers(roundcubeHeaders)
                .data(inboxListParams)
                .get();

        return inboxMailList;
    }

    /**
     * Fetchess the email by its id and grabs all the relevant info from it, packaged in a POJO
     * <code>com.walmart.sr.pojo.ApprovalMessageContents</code>
     * @param emailId
     * @return
     * @throws IOException
     */
    private ApprovalMessageContents getEmailContents(String emailId) throws IOException {
        inboxListParams.put("_caps", "pdf=1,flash=0,tiff=0,webp=1");
        inboxListParams.put("_uid", emailId);
        inboxListParams.put("_action", "preview");
        inboxListParams.put("_framed", "1");
        inboxListParams.remove("_refresh");
        inboxListParams.remove("_remote");
        inboxListParams.remove("_unlock");
        inboxListParams.remove("_");

        Document emailcontent = Jsoup.connect(baseUrl)
                .headers(roundcubeHeaders)
                .data(inboxListParams)
                .timeout(5000)
                .get();

        String subject = emailcontent.selectFirst("h3.subject").text();
        subject = subject.substring(subject.indexOf("Approval"));
//                emailcontent.selectFirst("title").ownText();

        Element mailBody = emailcontent.selectFirst("#message-htmlpart1");
        Element redirectionParagraph = mailBody.selectFirst("p");
        redirectionParagraph.remove();

        String emailBody = mailBody.toString();

        Element approveLink = emailcontent.selectFirst("a.approve");
        String approveEmailHref = approveLink.attr("href");
        String approveOnclickAction = approveLink.attr("onclick");

        Element rejectLink  = emailcontent.selectFirst("a.reject");
        String rejectEmailHref = rejectLink.attr("href");
        String rejectOnclickAction = rejectLink.attr("onclick");

        return new ApprovalMessageContents(subject, emailBody, approveEmailHref, approveOnclickAction, rejectEmailHref, rejectOnclickAction);
    }

    /**
     * Utility method that encapsulates all the process of fetching email contents from sr approval on roundcube.
     * @param srNo Service Receipt number
     * @return <code>con.walmart.sr.pojo.ApprovalMessageContents</code> encapsulating all relevant information from the
     * email such as subject, body, approve and reject href and onclick message.
     * @throws IOException
     * @throws ParseException
     */
    public ApprovalMessageContents retrieveEmailAprrovalContents(int srNo) throws IOException, ParseException {
        Document gatewayDoc = authenticateGateway(
                PropertyHelper.getProperty("roundqb.gateway.user"),
                PropertyHelper.getProperty("roundqb.gateway.pass")
        );
        Document mainMailbox = authenticateRoundCube(
                gatewayDoc,
                PropertyHelper.getProperty("roundqb.email.user"),
                PropertyHelper.getProperty("roundqb.email.pass")
        );
        Document maiboxContents = fetchMailbox(mainMailbox);
        String mailId = fetchMailId(maiboxContents, srNo);
        if(mailId != null) {
            return getEmailContents(mailId);
        }
        return null;
    }

    /**
     * Sends an approval email via roundcube using http requests.
     * @param messageDetails message details collected from roundcube
     * @return true if the messsage was sent succesfully, false otherwise.
     * @throws IOException
     */
    public boolean sendApprovalEMailRoundcube(ApprovalMessageContents messageDetails) throws IOException {
        //gets roundcubeHeaders to contain a differnt refererPage
        roundcubeHeaders.put("Referer", String.format("%s%s", baseUrl, "?_task=mail&_mbox=INBOX"));
        roundcubeComposeParams = new HashMap<>(Map.of(
                "_task", "mail",
                "_mbox", "INBOX",
                "_action", "compose"
        ));
        //this first get request provides the location where an '_id' value is provided.  It then redirects
        //I'm stopping this redirection and making the second request separatedly.
        Document composeRedirect = Jsoup.connect(baseUrl)
                .followRedirects(false)
                .headers(roundcubeHeaders)
                .data(roundcubeComposeParams)
                .timeout(5000)
                .get();

        String location = composeRedirect.connection().response().header("Location").substring(2);
        int idIndex = location.indexOf("id=") + "id=".length();
        String id = location.substring(idIndex);

        Document composeDoc = Jsoup.connect(String.format("%s%s", baseUrl, location))
                .headers(roundcubeHeaders)
                .timeout(5000)
                .get();

        //this composeform has a token
        Element tokenInput = composeDoc.selectFirst("input[name='_token']");
        String tokenValue = tokenInput.attr("value");

        //Query strings getting done manually.
        String queryStrings = String.format(
                "?_task=mail&_unlock=loading%s&_lang=en&_framed=1", System.currentTimeMillis());

        String approvalOnclick = messageDetails.getApproveEmailOnclickAction();
        String to = messageDetails.getApproveEmailHref().substring("mailto:".length());
        String subject = getSubjectFromOnclick(approvalOnclick);
        String body = getBodyFromOnclick(approvalOnclick);


        //merge these two huge maps for the data.
        emailComposeParams.putAll(complementaryEmailComposeHeaders);
        emailComposeParams.put("_token", tokenValue);
        emailComposeParams.put("_id", id);
        emailComposeParams.put("_to", to);
        emailComposeParams.put("_subject", subject);
        emailComposeParams.put("_message", body);


        //document obtained from sending the email
        Document emailSentDoc = Jsoup.connect(baseUrl + queryStrings)
                .headers(roundcubeHeaders)
                .data(emailComposeParams)
                .timeout(5000)
                .post();

        Document sentFolderEmailList = fetchEmailFolderMails("Sent");

        //this takes the <body> from the html in string form, then checks whether it contains the subject
        //from the email sent previously.
        return sentFolderEmailList.body().text().contains(subject);
    }

    /**
     * Uses the GMAIL API to send approval/rejection messages
     * @param messageDetails
     */
    public void sendApprovalEmail(ApprovalMessageContents messageDetails) throws MessagingException, GeneralSecurityException, IOException {
        gmailClient = new GmailClient();
        String approvalOnclick = messageDetails.getApproveEmailOnclickAction();

        String from = PropertyHelper.getProperty("sr.approver.email");
        String to = messageDetails.getApproveEmailHref().substring("mailto:".length());
        String subject = getSubjectFromOnclick(approvalOnclick);
        String body = getBodyFromOnclick(approvalOnclick);
        MimeMessage msg = gmailClient.createEmail(to, from, subject, body);
        gmailClient.sendMessage(msg);

    }

    private String getBodyFromOnclick(String approvalOnclick) {
        String app = approvalOnclick.substring(approvalOnclick.indexOf(",'")+2, approvalOnclick.indexOf("',this"));
        int startIndex = app.indexOf("body=") + 5;
        return app.substring(startIndex);
    }

    private String getSubjectFromOnclick(String approvalOnclick) {
        String app = approvalOnclick.substring(approvalOnclick.indexOf(",'")+2, approvalOnclick.indexOf("',this"));
        int startIndex = app.indexOf("subject=") + 8;
        int stopIndex = app.indexOf("&body");
        return app.substring(startIndex, stopIndex);
    }
}
