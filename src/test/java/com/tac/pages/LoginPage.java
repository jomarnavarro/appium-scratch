package com.tac.pages;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage{

	@AndroidFindBy (accessibility = "test-Username")
	@iOSXCUITFindBy (id = "test-Username")
	private MobileElement usernameTxtFld;

	@AndroidFindBy (accessibility = "test-Password") 
	@iOSXCUITFindBy (id = "test-Password")
	private MobileElement passwordTxtFld;
	
	@AndroidFindBy (accessibility = "test-LOGIN") 
	@iOSXCUITFindBy (id = "test-LOGIN")
	private MobileElement loginBtn;
	
	@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView") 
	@iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]/child::XCUIElementTypeStaticText")
	private MobileElement errTxt;

	public LoginPage(AppiumDriver driver) {
		super(driver);
	}
	public LoginPage enterUserName(String username) {
		usernameTxtFld.clear();
		usernameTxtFld.sendKeys(username);
		return this;
	}

	public LoginPage enterPassword(String password) {
		passwordTxtFld.clear();
		passwordTxtFld.sendKeys(password);
		return this;
	}

	public String getErrTxt() {
		return this.errTxt.getText();
	}

	public ProductsPage pressLoginBtn() {
		loginBtn.click();
		return new ProductsPage(driver);
	}

	public ProductsPage login(String username, String password) {
		enterUserName(username);
		enterPassword(password);
		return pressLoginBtn();
	}
}
