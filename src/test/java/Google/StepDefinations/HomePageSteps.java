package Google.StepDefinations;

import PageObjectFactory.HomePageFactory;
import UtilitiesFactory.PropertyLoaderFactory;

public class HomePageSteps extends HarnessVariables {
    protected String url;
    protected String runPropFile = "run.properties";
    HomePageFactory homePage;

    public HomePageSteps() throws Exception {
        homePage = new HomePageFactory();
    }

    public void userNavigateToUrl(String urlToLoad) throws Exception {
//        url = new PropertyLoaderFactory().getPropertyFile(runPropFile).getProperty(urlToLoad);
        loadUrl(urlToLoad);
    }
    public void userClicksOnSearchIcon()  throws Exception {
        homePage.clickOnSearchIcon();
    }

    public void userEnterQuery()  throws Exception {
        homePage.enterQuery(QUERY_TO_ENTER);
    }
}
