package utils;

import com.github.javafaker.Faker;

public class Utils {

    public static String getRandomSrDescription() {
        return "Service created by Appium " + System.currentTimeMillis();
    }

    public static String getApproverEmail(int wmStoreNo, ApproverType approverType) {
        StringBuilder email = new StringBuilder();
        String defaultCountry = "us";
        if(approverType.equals(ApproverType.walmartStoreEmail)) {
            email.append(getRandomIdString());
            email.append(String.format(".s%05d", wmStoreNo));
            email.append(String.format(".%s", Constants.DEFAULT_COUNTRY));
            email.append(Constants.DEFAULT_WM_STORE_DOMAIN);

        } else {
            email.append(getRandomIdString());
            email.append(String.format("@%s",Constants.DEFAULT_HOME_OFFICE_EMAIL_DOMAIN));
        }
        return email.toString();
    }

    private static String getRandomIdString() {
        //get a beer name and grab the first word.
        String word = new Faker().beer().name().toString().replace(".", "");
        if(word.length() >= Constants.MAX_NUMBER_CHARS_USER_ID) {
            word = word.substring(0, Constants.MAX_NUMBER_CHARS_USER_ID);
        }

        return word.replace(" ", "");
    }

    public static String getWmStoreUserId(String email) {
        String[] emailParts = email.split("\\.");
        return emailParts[0];
    }

    public static int getWmStoreStoreNo(String email) {
        String[] emailParts = email.split("\\.");
        return Integer.parseInt(emailParts[1].substring(1));
    }

    public static String getWmStoreCountryInfo(String email) {
        String[] emailParts = email.split("\\.");
        return emailParts[2].substring(0, emailParts[2].indexOf('@'));
    }

    public static String getWmStoreEmailDomain(String email) {
        String[] emailParts = email.split("\\.");
        return String.format("%s.%s", emailParts[2].substring(emailParts[2].indexOf('@') + 1), emailParts[3]);
    }

    public static int getNumericSrReferenceNo(String referenceNoText) {
        String[] referenceNoTextParts = referenceNoText.split(" ");
        return Integer.parseInt(
                referenceNoTextParts[referenceNoTextParts.length - 1]
        );
    }
    /**
     *  pad with zeroes or substring the input string to match the desired length.
     * @param str
     * @param strLen
     * @return
     */
    public static String formatToLength(String str, int strLen) {
        return str.length() < strLen ? padWithZeroes(str, strLen) : trimString(str, strLen);
    }

    private static String padWithZeroes(String str, int strLen) {
        StringBuilder prefix = new StringBuilder();
        for(int i = 0; i < strLen - str.length(); i++) {
            prefix.append(String.valueOf(0));
        }
        return String.format("%s%s", prefix.toString(), str);
    }

    private static String trimString(String str, int strLen) {
        return str.substring(str.length() - strLen);

    }
}
