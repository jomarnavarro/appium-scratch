package com.tac.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class SettingsPage extends BasePage {

    @AndroidFindBy(accessibility="test-LOGOUT")
    @iOSXCUITFindBy(id = "test-LOGOUT")
    private MobileElement logoutBtn;

    public SettingsPage(AppiumDriver driver) {
        super(driver);
    }

    public LoginPage pressLogoutBtn() {
        logoutBtn.click();
        return new LoginPage(driver);
    }
}
