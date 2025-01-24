package UtilitiesFactory;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class WaitFactory {

//    private final WebDriver driver;
    WebDriver driver = BrowserFactory.getDriver();
//    WaitFactory waitFactory = new WaitFactory(driver);

    private ElementFactory elementFactory = new ElementFactory();

    private String envPropFile = "environment.properties";

    protected int timeOutInMilliSeconds = Integer.valueOf(new PropertyLoaderFactory().getPropertyFile(envPropFile).getProperty("timeOut"));
    protected int pollingTimeInMilliSeconds = Integer.valueOf(new PropertyLoaderFactory().getPropertyFile(envPropFile).getProperty("pollingTime"));


    public WaitFactory(WebDriver driver) throws Exception {
//        if (driver == null) {
//            System.out.println("WebDriver is null!");
//        }
        this.driver = driver;
    }


    public void waitForPageToFinishLoading(WebDriver driver) {
        if (driver == null) {
            System.out.println("Driver is null in WaitFactory!");
        }
        ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(timeOutInMilliSeconds))
                .pollingEvery(Duration.ofMillis(pollingTimeInMilliSeconds))
                .ignoring(NoSuchElementException.class);

        wait.until(pageLoadCondition);
    }

    public void waitForElementToBeClickable(String locatorValue, WebDriver driver) {
        if (driver == null) {
            System.out.println("Driver is null in WaitFactory!");
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorValue)));
    }

    public void waitForElementToBeClickable(WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeVisible(String locatorValue) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
    }

    public void waitForElementToBeVisible(WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBeInVisible(String locatorValue) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locatorValue)));
    }

    public void waitForElementToBeInVisible(WebElement element) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeOutInMilliSeconds));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void staticWait(int waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
