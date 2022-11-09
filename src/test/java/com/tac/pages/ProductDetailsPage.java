package com.tac.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductDetailsPage extends BasePage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]\n" +
            "")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
    private MobileElement SLBTitle;

    @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]"
            + "")
    @iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
    private MobileElement SLBTxt;

//	@AndroidFindBy (accessibility = "test-Price") private MobileElement SLBPrice;

    @AndroidFindBy (accessibility = "test-BACK TO PRODUCTS")
    @iOSXCUITFindBy (id = "test-BACK TO PRODUCTS")
    private MobileElement backToProductsBtn;

    @iOSXCUITFindBy (id = "test-ADD TO CART")
    private MobileElement addToCartBtn;

    public ProductDetailsPage(AppiumDriver driver) {
        super(driver);
    }

    public String getSLBTitle() {
        return SLBTitle.getText();
    }

    public String getSLBTxt() {
        return SLBTxt.getText();
    }

    /*
     * public String getSLBPrice() { String price = getText(SLBPrice);
     * utils.log("price is - " + price); return price; }
     */

    public String scrollToSLBPriceAndGetSLBPrice() {
        return scrollToElement().getText();
    }

    public Boolean isAddToCartBtnDisplayed() {
        return addToCartBtn.isDisplayed();
    }

    public ProductsPage pressBackToProductsBtn() {
        backToProductsBtn.click();
        return new ProductsPage(driver);
    }

}
