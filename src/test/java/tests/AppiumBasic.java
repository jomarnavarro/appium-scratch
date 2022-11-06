package tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public class AppiumBasic extends BaseTest {

    @Test
    public void test1() {
        findElement(AppiumBy.accessibilityId("Preference")).click();

        //driver.findElement(AppiumBy.accessibilityId("Preference")).click();
        findElement(By.xpath("//*[@content-desc='3. Preference dependencies']")).click();
        findElement(By.id("android:id/checkbox")).click();


        wait.until(
                ExpectedConditions.attributeToBe(
                        By.xpath("//*[contains(@text, 'WiFi settings')]"),
                        "enabled",
                        "true"
                )
        );
        findElement(By.xpath("//*[contains(@text, 'WiFi settings')]")).click();
        System.out.println("Succesful test.");
        String actualAlertTitle = findElement(By.xpath("//*[contains(@resource-id, 'alertTitle')]")).getText();

        Assert.assertEquals(actualAlertTitle, "WiFi settings");
        findElement(By.id("android:id/edit")).sendKeys("Omars WiFi");
        findElement(By.xpath("//*[contains(@text, 'OK')]")).click();
    }
}