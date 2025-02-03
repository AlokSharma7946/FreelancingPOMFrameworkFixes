package UtilitiesFactory;

import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static BrowserFactory instance = null;

    private BrowserFactory() {}  // Private constructor for Singleton

    public static BrowserFactory getInstance() {
        if (instance == null) {
            instance = new BrowserFactory();
        }
        return instance;
    }

    public final void setDriver(String browser, boolean headless) {
        switch (browser.toUpperCase()) {
            case "MOBILE":
                initializeChromeMobile(headless);
                break;
            case "FIREFOX":
                initializeFirefox(headless);
                break;
            case "CHROME":
                initializeChrome(headless);
                break;
            case "EDGE":
                initializeEdge(headless);
                break;
            case "SAFARI":
                initializeSafari();
                break;
            default:
                initializeChrome(headless);
                break;
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public void cleanUp() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    private void initializeChromeMobile(boolean headless) {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver(chromeOptionsAndroidMobile(headless)));
    }

    private void initializeChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        driver.set(new ChromeDriver(chromeOptionsDesktop(headless)));
    }

    private void initializeFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        driver.set(new FirefoxDriver(firefoxOptions(headless)));
    }

    private void initializeEdge(boolean headless) {
        WebDriverManager.edgedriver().setup();
        driver.set(new EdgeDriver(edgeOptions(headless)));
    }

    private void initializeSafari() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            driver.set(new SafariDriver());
        } else {
            throw new UnsupportedOperationException("Safari is not supported on " + os);
        }
    }

    private ChromeOptions chromeOptionsAndroidMobile(boolean headless) {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "Galaxy S5");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions", "--no-sandbox", "--window-size=240,720");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        if (headless) options.addArguments("--headless");  // Apply headless mode dynamically
        return options;
    }

    private ChromeOptions chromeOptionsDesktop(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions", "--no-sandbox", "--start-maximized");

        if (headless) options.addArguments("--headless");  // Apply headless mode dynamically
        return options;
    }

    private FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--window-size=1536,722");

        if (headless) options.addArguments("--headless");  // Apply headless mode dynamically
        return options;
    }

    private EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        if (headless) options.addArguments("--headless");  // Apply headless mode dynamically
        return options;
    }
}
