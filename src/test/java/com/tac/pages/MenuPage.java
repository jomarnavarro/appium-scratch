package com.tac.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class MenuPage extends BasePage {

    public MenuPage(AppiumDriver driver) {
        super(driver);
    }

    @AndroidFindBy(xpath="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView\n" +
            "")
    @iOSXCUITFindBy(xpath="//XCUIElementTypeOther[@name=\"test-Menu\"]/XCUIElementTypeOther")
    private MobileElement settingsBtn;

    public SettingsPage pressSettingsBtn() {
        settingsBtn.click();
        return new SettingsPage(driver);
    }
}
