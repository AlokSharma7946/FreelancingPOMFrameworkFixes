package UtilitiesFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ElementFactory {

//    WebDriver driver = BrowserFactory.getDriver();
    private WebDriver driver;

    public ElementFactory() {
        // Ensuring that driver is initialized before use
        if (driver == null) {
            driver = BrowserFactory.getDriver(); // Get the driver from BrowserFactory
        }
    }
    public WebElement getElement(String locatorType, String locatorValue) {

        WebElement element = null;
        try {
            if (locatorType.equalsIgnoreCase("id")) {
                element = driver.findElement(By.id(locatorValue));
            } else if (locatorType.equalsIgnoreCase("name")) {
                element = driver.findElement(By.name(locatorValue));
            } else if (locatorType.equalsIgnoreCase("xpath")) {
                element = driver.findElement(By.xpath(locatorValue));
            } else if (locatorType.equalsIgnoreCase("linktext")) {
                element = driver.findElement(By.linkText(locatorValue));
            } else if (locatorType.equalsIgnoreCase("partiallinktext")) {
                element = driver.findElement(By.partialLinkText(locatorValue));
            } else if (locatorType.equalsIgnoreCase("cssSelector")) {
                element = driver.findElement(By.cssSelector(locatorValue));
            }

            return element;

        } catch (Exception e) {

            throw e;
        }
    }

    public WebElement getElement(String locatorValue) {

        WebElement element = null;
        try {
            element = BrowserFactory.getDriver().findElement(By.xpath(locatorValue));
            return element;

        } catch (Exception e) {

            throw e;
        }
    }

    public int getElementList(String locatorValue) {

        List<WebElement> elements = null;
        elements = driver.findElements(By.xpath(locatorValue));
        return elements.size();
    }

    public List<WebElement> getElementsList(String locatorValue) {

        List<WebElement> elements = null;
        elements = driver.findElements(By.xpath(locatorValue));
        return  elements;
    }
}
