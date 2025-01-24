package Google.StepDefinations;

import Google.Enums.HomePageEnumVariables;
import UtilitiesFactory.UtilFactory;

public class HarnessVariables extends UtilFactory {
    public HarnessVariables() throws Exception {}

    protected static String QUERY_TO_ENTER = HomePageEnumVariables.VALID_SEARCH_QUERY.getValue();
}
