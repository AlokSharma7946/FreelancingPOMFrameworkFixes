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

    import java.util.Collections;
    import java.util.HashMap;
    import java.util.Map;

    public class BrowserFactory {

        private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
        private static BrowserFactory instance = null;

        // Singleton to ensure only one instance of BrowserFactory
        private BrowserFactory() {}

        public static BrowserFactory getInstance() {
            if (instance == null) {
                instance = new BrowserFactory();
            }
            return instance;
        }

        public final void setDriver(String browser) {
            switch (browser.toUpperCase()) {
                case "MOBILE":
                    initializeChromeMobile();
                    break;
                case "FIREFOX":
                    initializeFirefox();
                    break;
                case "CHROME":
                    initializeChrome();
                    break;
                case "EDGE":
                    initializeEdge();
                    break;
                case "SAFARI":
                    initializeSafari();
                    break;
                default:
                    initializeChrome();
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

        private void initializeChromeMobile() {
            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver(chromeOptions_androidMobile()));
        }

        private void initializeChrome() {
            WebDriverManager.chromedriver().setup();
            driver.set(new ChromeDriver(chromeOptions_desktop()));
        }

        private void initializeFirefox() {
            WebDriverManager.firefoxdriver().setup();
            driver.set(new FirefoxDriver(firefoxOptions()));
        }

        private void initializeEdge() {
            WebDriverManager.edgedriver().setup();
            driver.set(new EdgeDriver(edgeOptions()));
        }

        private void initializeSafari() {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac")) {
                driver.set(new SafariDriver());
            } else {
                throw new UnsupportedOperationException("Safari is not supported on " + os);
            }
        }

        private ChromeOptions chromeOptions_androidMobile() {
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Galaxy S5");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions", "--no-sandbox", "--window-size=240,720");
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
            return options;
        }

        private ChromeOptions chromeOptions_desktop() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-extensions", "--no-sandbox", "--start-maximized", "--headless");
            return options;
        }

        private FirefoxOptions firefoxOptions() {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--window-size=1536,722");
            return options;
        }

        private EdgeOptions edgeOptions() {
            EdgeOptions options = new EdgeOptions();
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.addArguments("--headless");
            return options;
        }


    }
