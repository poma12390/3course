package org.example.usecase;

import org.example.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewsTest {
    @BeforeAll
    public static void prepareDrivers() {
        Utils.prepareDrivers();
    }

    @Test
    public void showNews() {
        ChromeDriver driver = Utils.getChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        // Откройте веб-страницу
        driver.get(Utils.BASE_URL);

        // Найдем поисковую строку и введем название города
        WebElement newsButton = driver.findElement(By.xpath("/html/body/header/div[1]/div/div/div[3]/div/a[1]"));
        newsButton.click();
        WebElement firstNews = driver.findElement(By.xpath("/html/body/section/div[1]/section[1]/div/div[1]"));

        Assertions.assertNotNull(firstNews);

        driver.close();
    }
}
