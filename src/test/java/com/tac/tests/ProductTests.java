package com.tac.tests;

import com.tac.pages.LoginPage;
import com.tac.pages.MenuPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class ProductTests extends BaseTest {

    @BeforeClass
    public void beforeClass() {
        closeApp();
        launchApp();
    }

    @BeforeMethod
    public void beforeMethod(Method m) {
        System.out.println("\n" + "****** starting test:" + m.getName() + "******" + "\n");
        loginPage = new LoginPage(getDriver());
        productsPage = loginPage.login("standard_user", "secret_sauce");
    }

    @AfterMethod
    public void afterMethod() {
        menuPage = new MenuPage(getDriver());
        settingsPage = menuPage.pressSettingsBtn();
        loginPage = settingsPage.pressLogoutBtn();
    }

    @Test
    public void validateProductOnProductsPage() {
        SoftAssert sa = new SoftAssert();

        String SLBTitle = productsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, "Sauce Labs Backpack");

        String SLBPrice = productsPage.getSLBPrice();
        sa.assertEquals(SLBPrice, "$29.99");

        sa.assertAll();
    }

    @Test
    public void validateProductOnProductDetailsPageAndroid() {
        SoftAssert sa = new SoftAssert();

        productDetailsPage = productsPage.pressSLBTitle();

        String SLBTitle = productDetailsPage.getSLBTitle();
        sa.assertEquals(SLBTitle, "Sauce Labs Backpack");

        String SLBPrice = productDetailsPage.scrollToSLBPriceAndGetSLBPrice();
        sa.assertEquals(SLBPrice, "$29.99");

//        if(getPlatform().equalsIgnoreCase("iOS")) {
//            String SLBTxt = productDetailsPage.getSLBTxt();
//            sa.assertEquals(SLBTxt, getStrings().get("product_details_page_slb_txt"));
//
//            productDetailsPage.scrollPage();
//            sa.assertTrue(productDetailsPage.isAddToCartBtnDisplayed());
//        }
//		  productsPage = productDetailsPage.pressBackToProductsBtn(); // -> Commented as this is causing stale element exception for the Settings icon

        sa.assertAll();
    }
}
