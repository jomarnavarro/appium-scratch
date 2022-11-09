package com.tac.tests;

import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTests extends BaseTest{

	@BeforeClass
	public void beforeClass() {
		closeApp();
		launchApp();
	}

  @Test
  public void invalidUserName() {
	  loginPage.enterUserName("invalidusername");
	  loginPage.enterPassword("secret_sauce");
	  loginPage.pressLoginBtn();

	  String actualErrTxt = loginPage.getErrTxt();
	  String expectedErrTxt = "Username and password do not match any user in this service.";

	  Assert.assertEquals(actualErrTxt, expectedErrTxt);
  }

  @Test
  public void invalidPassword() {
	  loginPage.enterUserName("standard_user");
	  loginPage.enterPassword("invalidpassword");
	  loginPage.pressLoginBtn();

	  String actualErrTxt = loginPage.getErrTxt();
	  String expectedErrTxt = "Username and password do not match any user in this service.";

	  Assert.assertEquals(actualErrTxt, expectedErrTxt);
  }

  @Test
  public void successfulLogin() {
	  loginPage.enterUserName("standard_user");
	  loginPage.enterPassword("secret_sauce");
	  productsPage = loginPage.pressLoginBtn();

	  String actualProductTitle = productsPage.getTitle();
	  String expectedProductTitle = "PRODUCTS";

	  Assert.assertEquals(actualProductTitle, expectedProductTitle);
  }
}
