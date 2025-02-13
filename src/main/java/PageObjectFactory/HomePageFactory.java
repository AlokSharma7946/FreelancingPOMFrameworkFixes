package PageObjectFactory;

import EnumFactory.HomePageEnum;
import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.ExtentReportFactory;
import UtilitiesFactory.UtilFactory;
import UtilitiesFactory.WaitFactory;
import com.aventstack.extentreports.Status;

public class HomePageFactory extends UtilFactory {

    private WaitFactory waitFactory = new WaitFactory(BrowserFactory.getDriver());

    public HomePageFactory() throws Exception {
    }

    public void clickOnSearchIcon() throws Exception {
        String locator = HomePageEnum.XPATH_SEARCH_ICON.getValue();
        try {
//            scenarioDef.set(ExtentReportFactory.testThreadLocal.get());
            waitFactory.waitForElementToBeClickable(locator, BrowserFactory.getDriver());
            click(locator);
            scenarioDef.get().log(Status.PASS, "clickSearchIcon PASS");
        } catch (Exception e) {
            scenarioDef.get().log(Status.FAIL, "clickSearchIcon FAIL");
            failureException.set(e.toString());
            throw e;
        }
    }

    public void enterQuery(String textToEnter) throws Exception {
        String locator = HomePageEnum.XPATH_SEARCH_ICON.getValue();
        try {
//            scenarioDef.set(ExtentReportFactory.testThreadLocal.get());
            waitFactory.waitForElementToBeClickable(locator,BrowserFactory.getDriver());
            enterString(locator, textToEnter);
            scenarioDef.get().log(Status.PASS, "ENTER QUERY PASS");
        } catch (Exception e) {
            scenarioDef.get().log(Status.FAIL, "ENTER QUERY FAIL");
            failureException.set(e.toString());
            throw e;
        }
    }
}
