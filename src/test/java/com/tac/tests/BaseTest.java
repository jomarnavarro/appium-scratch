package com.tac.tests;

import com.tac.pages.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.tac.utils.DesiredCapabilityHelper;
import com.tac.utils.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected AppiumDriverLocalService service;
    protected AppiumDriver driver;

    protected WebDriverWait wait;
    protected DesiredCapabilities dc;
    private static final String appiumMainFilePath = "/usr/local/lib/node_modules/appium/build/lib/main.js";
    private static final String protocol = "http://";
    private static final String host = "127.0.0.1";
    private static final int port = 4723;
    private static final String basePath = "/wd/hub";

    protected DesiredCapabilities jsonCaps;

    protected LoginPage loginPage;
    protected ProductsPage productsPage;
    protected MenuPage menuPage;
    protected ProductDetailsPage productDetailsPage;
    protected SettingsPage settingsPage;

    @BeforeClass
    public void beforeClass() throws IOException, ParseException {
        String deviceName = PropertyHelper.getProperty("deviceName");
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumMainFilePath))
                .withIPAddress(host)
                .usingPort(port)
                .withArgument(() -> "--base-path", basePath)
                .build();
        service.start();
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
        System.out.println("appPath:  " + jsonCaps.getCapability("app"));
        dc = jsonCaps;
        driver = new AndroidDriver(new URL(String.format("%s%s:%d%s",protocol, host, port, basePath)), dc);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 15);
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        menuPage = new MenuPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        settingsPage = new SettingsPage(driver);
    }

    protected WebElement findElement(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
        service.stop();
    }
}
