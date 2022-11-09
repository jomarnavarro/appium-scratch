package com.tac.tests;

import com.tac.pages.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import com.tac.utils.DesiredCapabilityHelper;
import com.tac.utils.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private AppiumDriverLocalService service;
    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected DesiredCapabilities jsonCaps;
    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected MenuPage menuPage;
    protected ProductDetailsPage productDetailsPage;
    protected SettingsPage settingsPage;

    protected AppiumDriver getDriver() {
        return this.driver.get();
    }

    private void setDriver(AppiumDriver driver2) {
        this.driver.set(driver2);
    }

    @BeforeSuite
    public void beforeSuite() {
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(PropertyHelper.getProperty("appiumMainFilePath")))
                .withIPAddress(PropertyHelper.getProperty("appiumServerHost"))
                .usingPort(Integer.parseInt(PropertyHelper.getProperty("appiumServerPort")))
                .withArgument(() -> "--base-path", PropertyHelper.getProperty("appiumServerBasePath"))
                .build();
        service.start();
    }

    @AfterSuite
    public void afterSuite() {
        service.stop();
    }
    @Parameters({"deviceName"})
    @BeforeTest
    public void beforeTest(String deviceName) throws IOException, ParseException {


        jsonCaps  = DesiredCapabilityHelper.getDesiredCapabilities(deviceName);
        if(jsonCaps.getCapability("app") != null) {
            jsonCaps.setCapability("app",
                    String.format(
                            "%s%s/%s",
                            System.getProperty("user.dir"),
                            PropertyHelper.getProperty("appPath"),
                            jsonCaps.getCapability("app")
                    )
            );
        }
        AppiumDriver myDriver;
        myDriver = new AndroidDriver(
                new URL(PropertyHelper.getProperty("appiumUrl")),
                jsonCaps
        );
        System.out.println("appPath:  " + jsonCaps.getCapability("app"));
        setDriver(myDriver);
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        loginPage = new LoginPage(getDriver());
        productsPage = new ProductsPage(getDriver());
        menuPage = new MenuPage(getDriver());
        productDetailsPage = new ProductDetailsPage(getDriver());
        settingsPage = new SettingsPage(getDriver());
    }

    @AfterTest
    public void afterTest() {
        if(getDriver() != null)
            getDriver().quit();
    }

    public void closeApp() {
        ((InteractsWithApps) getDriver()).closeApp();
    }

    public void launchApp() {
        ((InteractsWithApps) getDriver()).launchApp();
    }

}
