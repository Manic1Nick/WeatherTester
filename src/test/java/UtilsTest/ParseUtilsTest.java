package UtilsTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.ReadFilesUtils;
import ua.nick.weather.weatherFactory.ActualWeatherFactory;
import ua.nick.weather.weatherFactory.ForecastFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class ParseUtilsTest {

    private static ForecastFactory forecastFactory;
    private static ActualWeatherFactory actualWeatherFactory;

    //map is "provider=(forecast=json, actual=json)"
    private static Map<Provider, Map<String, String>> mapJsonsByProvider;

    @BeforeClass
    public static void setupBeforeTests() {
        mapJsonsByProvider = new ReadFilesUtils()
                .readJsonFromFiles("/home/jessy/IdeaProjects/WeatherTester/src/test/filesForecastExamples/");
        forecastFactory = new ForecastFactory();
        actualWeatherFactory = new ActualWeatherFactory();
    }

    @Test
    public void test_parseForecastsFromAllProviders() throws ParseException {

        for (Provider provider : Provider.values()) {
            String json = mapJsonsByProvider.get(provider).get("forecast");

            Object obj = forecastFactory.createListForecastsFromJsonByProvider(provider, json);
            assertThat(obj, instanceOf(ArrayList.class));

            ArrayList list = (ArrayList) obj;
            assertThat(list.size(), greaterThan(0));
            assertThat(list.get(0), instanceOf(Forecast.class));

            Forecast forecast = (Forecast) list.get(0);
            assertThat(forecast.isActual(), is(false));
            assertThat(forecast.getProvider(), is(provider));
        }
    }

    @Test
    public void test_parseActualsFromAllProviders() throws ParseException {

        for (Provider provider : Provider.values()) {
            String json = mapJsonsByProvider.get(provider).get("actual");

            Object obj = actualWeatherFactory.createActualModelFromJson(provider, json);
            assertThat(obj, instanceOf(Forecast.class));

            Forecast forecast = (Forecast) obj;
            assertThat(forecast.isActual(), is(true));
            assertThat(forecast.getProvider(), is(provider));
        }
    }

}
