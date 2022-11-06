package tests;

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
import utils.DesiredCapabilityHelper;
import utils.PropertyHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

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

    private static final String projectPath = System.getProperty("user.dir");


    protected DesiredCapabilities jsonCaps;
    private static final String apkPath = "/Users/omarn/repo/appium-scratch/src/test/resources/ApiDemos-debug.apk";

    @BeforeClass
    public void beforeClass() throws IOException, ParseException {
        String deviceName = PropertyHelper.getProperty("deviceName");
        String appPath = PropertyHelper.getProperty("appPath");
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumMainFilePath))
                .withIPAddress(host)
                .usingPort(port)
                .withArgument(() -> "--base-path", basePath)
                .build();
        service.start();
        jsonCaps  = DesiredCapabilityHelper.getDesiredCapabilities(deviceName);
        jsonCaps.setCapability("app", String.format(
                "%s%s/%s", projectPath, appPath, jsonCaps.getCapability("app")
        ));
        jsonCaps.getCapability("app");
        dc = jsonCaps;
        driver = new AndroidDriver(new URL(String.format("%s%s:%d%s",protocol, host, port, basePath)), dc);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
