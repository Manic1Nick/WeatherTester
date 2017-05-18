import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(value = Parameterized.class)
public class StringUtilsTest02 {

    private String parameter;
    private Map<Provider, Long> mapForecastsByProviders;
    private String message;

    public StringUtilsTest02(String parameter, Map<Provider, Long> mapForecastsByProviders, String message) {
        this.parameter = parameter;
        this.mapForecastsByProviders = mapForecastsByProviders;
        this.message = message;
    }

    @Parameterized.Parameters(name = "{index}: testCreateMessageAboutUpdateForecasts(map is {0}) = {2}")
    public static Collection<Object[]> data2() {
        Map<Provider, Long> mapEmpty = new HashMap<>();
        Map<Provider, Long> mapWithNullData = new HashMap<>();
        mapWithNullData.put(null, null);
        Map<Provider, Long> mapWithData = new HashMap<>();
        mapWithData.put(Provider.FORECA, 10L);

        String messagePositive = "forecast(s) were added to database";
        String messageNegative = "no need to update forecasts from providers for this date";

        return Arrays.asList(new Object[][]{
                {"null", null, messageNegative},
                {"empty map", mapEmpty, messageNegative},
                {"map with null data", mapWithNullData, messageNegative},
                {"map with data", mapWithData, messagePositive}
        });
    }

    @Test
    public void test_createMessageAboutUpdateForecasts() {
        assertThat(StringUtils.createMessageAboutUpdateForecasts(mapForecastsByProviders), containsString(message));
    }
}