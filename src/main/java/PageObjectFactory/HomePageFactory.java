package PageObjectFactory;

import EnumFactory.HomePageEnum;
import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.UtilFactory;
import UtilitiesFactory.WaitFactory;

public class HomePageFactory extends UtilFactory {

    private WaitFactory waitFactory = new WaitFactory(BrowserFactory.getDriver());

    public HomePageFactory() throws Exception {
    }

    public void clickOnSearchIcon() throws Exception {
        String locator = HomePageEnum.XPATH_SEARCH_ICON.getValue();
        try {
            waitFactory.waitForElementToBeClickable(locator, BrowserFactory.getDriver());
            click(locator);
        } catch (Exception e) {
            failureException.set(e.toString());
            throw e;
        }
    }

    public void enterQuery(String textToEnter) throws Exception {
        String locator = HomePageEnum.XPATH_SEARCH_ICON.getValue();
        try {
            waitFactory.waitForElementToBeClickable(locator,BrowserFactory.getDriver());
            enterString(locator, textToEnter);
        } catch (Exception e) {
            failureException.set(e.toString());
            throw e;
        }
    }
}
