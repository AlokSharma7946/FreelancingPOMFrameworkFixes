package Google.Enums;

import UtilitiesFactory.PropertyLoaderFactory;

public enum HomePageEnumVariables {

    VALID_SEARCH_QUERY("");

    private String homePageVariables;

    private HomePageEnumVariables(String homePageVariables)
    {
        this.homePageVariables = homePageVariables;
    }

    public String getValue()
    {
        return this.homePageVariables;
    }

    static {
        try {
            String userDataFile = "userData.properties";

            VALID_SEARCH_QUERY.homePageVariables = new PropertyLoaderFactory().getPropertyFile(userDataFile).getProperty("valid.query");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
