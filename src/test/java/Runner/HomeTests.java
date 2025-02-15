package Runner;

import Google.StepDefinations.HomePageSteps;
import UtilitiesFactory.ExcelFactory;
import UtilitiesFactory.Hooks;
import org.testng.annotations.Test;

public class HomeTests extends Hooks {
    HomePageSteps HPS;

    public HomeTests() throws Exception {
        HPS = new HomePageSteps();
    }

    @Test(priority = 1)
    public void userIsSearchingForSomething() throws Exception {
        String url = ExcelFactory.getValue("HomePageData", "URL");
        HPS.userNavigateToUrl(url);
        HPS.userClicksOnSearchIcon();
    }
    @Test(priority = 2)
    public void userIsSearching() throws Exception {
        HPS.userEnterQuery();
    }
}
