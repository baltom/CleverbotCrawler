package com.sjonky;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CleverbotPageObject {

    private final WebDriver driver;

    public CleverbotPageObject(WebDriver driver) {
        this.driver = driver;
        goToCleverbotPage();
    }

    public void goToCleverbotPage() {
        driver.get("http://www.cleverbot.com");
        waitForPageToLoad();
    }

    public String askAndGetResponse(String question) {
        writeQuestion(question, driver);
        try {
            Thread.sleep(3500);
            return getResponse(driver);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String getResponse(WebDriver driver) {
        List<WebElement> webElements = driver.findElements(By.className("bot"));
        return webElements.get(webElements.size() - 1).getText();
    }

    private void writeQuestion(String question, WebDriver driver) {
        WebElement element = driver.findElement(By.className("stimulus"));
        element.sendKeys(question);
        clickSayItButton(driver);
        waitForPageToLoad();
    }

    private void clickSayItButton(WebDriver driver) {
        WebElement e = driver.findElement(By.className("sayitbutton"));
        e.click();
    }

    private Boolean waitForPageToLoad() {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }

    public void close() {
        driver.close();
    }
}
