package com.tac.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppiumBasic extends BaseTest {

    @Test
    public void test1() {
        findElement(By.xpath("//*[@accessibilityId = 'Preference']")).click();

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