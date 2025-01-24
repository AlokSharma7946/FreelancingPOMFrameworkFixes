package Runner;

import Google.StepDefinations.HomePageSteps;
import UtilitiesFactory.Hooks;
import org.testng.annotations.Test;

public class HomePageTests{
    HomePageSteps HPS;

    public HomePageTests() throws Exception {
        HPS = new HomePageSteps();
    }

    @Test(priority = 1)
    public void userIsSearchingForSomething() throws Exception {
        HPS.userNavigateToUrl("google.home");
        HPS.userClicksOnSearchIcon();
    }
    @Test(priority = 2)
    public void userIsSearching() throws Exception {
        HPS.userEnterQuery();
    }
}
