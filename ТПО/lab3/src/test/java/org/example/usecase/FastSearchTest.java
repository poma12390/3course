package org.example.usecase;

import org.example.Utils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FastSearchTest {

    @BeforeAll
    public static void prepareDrivers() {
        Utils.prepareDrivers();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Москва", "Санкт-Петербург"})
    public void getTemperatureFromExistedCity(String city) {
        ChromeDriver driver = Utils.getChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        // Откройте веб-страницу
        driver.get(Utils.BASE_URL);

        // Найдем поисковую строку и введем название города
        WebElement searchInput = driver.findElement(By.xpath("//*[@class=\"input js-input\"]"));
        searchInput.sendKeys(city);
        wait.until(ExpectedConditions.attributeToBe(searchInput, "value", city));

        Utils.getElementWhenVisibleByXpath(driver, "/html/body/header/div[2]/div[2]/div/div/div[2]/div/div[2]/a[1]");
        WebElement cityButton = Utils.getElementWhenVisibleByXpath(driver, "//a[contains(@class, 'search-item')]//div[contains(@class, 'city-title') and contains(text(), '" + city + "')]");

        cityButton.click();
        Utils.waitUntilPageLoads(driver, 3);

        WebElement temperature = Utils.getElementWhenVisibleByXpath(driver, "/html/body/section[2]/div[1]/section[2]/div/a[1]/div/div[1]/div[3]/div[1]/span[1]");


        Assertions.assertDoesNotThrow(() -> Integer.parseInt(temperature.getText().replace("+", "").replace("-", "")));

        driver.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"МоскваСША", "валера"})
    public void getTemperatureFromNotExistedCity(String city) {
        ChromeDriver driver = Utils.getChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 3);

        // Откройте веб-страницу
        driver.get(Utils.BASE_URL);

        // Найдем поисковую строку и введем название города
        WebElement searchInput = driver.findElement(By.xpath("//*[@class=\"input js-input\"]"));
        searchInput.sendKeys(city);
        wait.until(ExpectedConditions.attributeToBe(searchInput, "value", city));

        WebElement clickableButton = Utils.getElementWhenVisibleByXpath(driver, "/html/body/header/div[2]/div[2]/div/div/div[2]/div/div[2]/a[1]");

        assertFalse(clickableButton.getText().startsWith(city));
        driver.close();
    }
}

