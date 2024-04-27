package org.example.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.example.Utils;

public class ModelViewPage extends Page {
    public ModelViewPage(WebDriver driver) {
        super(driver);
    }

    public String getModelName() {
        return Utils.getElementBySelector(driver, By.xpath("//*[@id=\"bChatRoomTitle\"]")).getText();
    }

    public void goToNextModel() {
        Utils.getElementBySelector(driver, By.xpath("//*[@id=\"bCamControlsNextRoom\"]")).click();
    }

    public void goToPreviousModel() {
        Utils.getElementBySelector(driver, By.xpath("//*[@id=\"bCamControlsPrevRoom\"]")).click();
    }
}
