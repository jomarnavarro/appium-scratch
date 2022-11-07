package com.tac.pages;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductsPage extends BasePage {
	@AndroidFindBy (xpath = "//*[@text = 'PRODUCTS']")
	@iOSXCUITFindBy (xpath ="//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
	private MobileElement productTitleTxt;
	
	@AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]") 
	@iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
	private MobileElement SLBTitle;
	
	@AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]") 
	@iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
	private MobileElement SLBPrice;

	public ProductsPage(AppiumDriver driver) {
		super(driver);
	}
	
	public String getTitle() {
		String title = productTitleTxt.getText();
		return title;
	}

	public String getSLBTitle() {
		String title = SLBTitle.getText();
		return title;
	}

	public String getSLBPrice() {
		System.out.println("Products Page " + driver.getPageSource());
		String price = SLBPrice.getText();
		return price;
	}

	public ProductDetailsPage pressSLBTitle() {
		SLBTitle.click();
		return new ProductDetailsPage(driver);
	}

}
