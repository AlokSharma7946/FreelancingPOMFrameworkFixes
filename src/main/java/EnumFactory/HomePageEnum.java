package EnumFactory;

public enum HomePageEnum {

    XPATH_SEARCH_ICON("//*[@class='gLFyf']");

    private String homePageVariables;

    private HomePageEnum(String homePageVariables) {
        this.homePageVariables = homePageVariables;
    }

    public String getValue() {
        return this.homePageVariables;
    }
}