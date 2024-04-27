package org.example.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.example.Utils;

import java.util.concurrent.TimeUnit;

public class AdultConfirmationPage extends Page {
    public AdultConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public void acceptAdultConfirmation() {
        WebElement continueButton = Utils.getElementBySelector(driver, By.xpath(".//div[@id='warning_popup']/div/div/div[2]/div[4]/a[1]"));
        continueButton.click();
    }

    public void dismissAdultConfirmation() {
        WebElement exitLink = Utils.getElementBySelector(driver, By.xpath(".//div[@id='warning_popup']/div/div/div[2]/div[4]/a[2]"));
        exitLink.click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
}

