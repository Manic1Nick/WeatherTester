package UtilsTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.ParseUtils;
import ua.nick.weather.utils.ReadFilesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseUtilsTest {

    private static ParseUtils parseUtils;
    private static List<Forecast> forecasts;

    //map is "provider=(forecast=json, actual=json)"
    private static Map<Provider, Map<String, String>> mapJsonsByProvider;

    @BeforeClass
    public static void setupBeforeTests() {
        String forecastExamplesDir = "/home/jessy/IdeaProjects/WeatherTester/src/test/filesForecastExamples/";
        ReadFilesUtils readFilesUtils = new ReadFilesUtils(forecastExamplesDir);
        mapJsonsByProvider = readFilesUtils.readJsonFromFiles();
        parseUtils = new ParseUtils();
        forecasts = new ArrayList<>();
    }

    @Test
    public void test_parseForecastsFromOpenWeather() {
        String json = mapJsonsByProvider.get(Provider.OPENWEATHER).get("forecast");

        //assertThat(parseUtils.parseForecastsFromOpenWeather(forecasts, json), is(""));
    }

}
