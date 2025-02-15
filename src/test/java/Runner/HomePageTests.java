package Runner;

import Google.StepDefinations.HomePageSteps;
import UtilitiesFactory.ExcelFactory;
import org.testng.annotations.Test;

public class HomePageTests {
    HomePageSteps HPS;

    public HomePageTests() throws Exception {
        HPS = new HomePageSteps();
    }

    @Test(priority = 1)
    public void userIsSearchingForSomething() throws Exception {
        // Fetch URL from Excel
        String url = ExcelFactory.getValue("HomePageData", "URL");
        HPS.userNavigateToUrl(url);
        HPS.userClicksOnSearchIcon();
    }

    @Test(priority = 2)
    public void userIsSearching() throws Exception {
        HPS.userEnterQuery();
    }
}
