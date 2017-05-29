package UtilsTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.ReadFilesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class ReadFilesUtilsTest {

    private static ReadFilesUtils readFilesUtils;
    private static String linkDir;

    @BeforeClass
    public static void setupBeforeTests() {
        readFilesUtils = new ReadFilesUtils();
    }

    @Test
    public void test_readJsonFromFiles_Negative_EmptyLinkDir() {

        linkDir = "";
        Object obj = readFilesUtils.readJsonFromFiles(linkDir);
        assertThat(obj, instanceOf(HashMap.class));

        HashMap map = (HashMap) obj;
        assertThat(map.size(), is(0));
    }

    @Test
    public void test_readJsonFromFiles_Negative_ErrorLinkDir() {

        linkDir = "/home/jessy/IdeaProjects/WeatherTester/src/test/errorLinkDir/";
        Object obj = readFilesUtils.readJsonFromFiles(linkDir);
        assertThat(obj, instanceOf(HashMap.class));

        HashMap map = (HashMap) obj;
        assertThat(map.size(), is(0));
    }

    @Test
    public void test_readJsonFromFiles_Positive() {

        linkDir = "/home/jessy/IdeaProjects/WeatherTester/src/test/filesDirExample/";
        Object obj = readFilesUtils.readJsonFromFiles(linkDir);
        assertThat(obj, instanceOf(HashMap.class)); //get Map<Provider, Map<String, String>>

        //map outer
        HashMap mapOuter = (HashMap) obj;
        assertThat(mapOuter.size(), greaterThan(0)); //size > 0

        List outerKeySet = new ArrayList(mapOuter.keySet());
        assertThat(outerKeySet.get(0), instanceOf(Provider.class)); //keySet = providers

        Provider provider = (Provider) outerKeySet.get(0);
        assertThat(mapOuter.get(provider), instanceOf(HashMap.class)); //get Map<String, String>

        //map inner
        HashMap mapInner = (HashMap) mapOuter.get(provider);
        assertThat(mapInner.size(), greaterThan(0)); // size > 0

        List innerKeySet = new ArrayList(mapInner.keySet());
        assertThat(innerKeySet.get(0), instanceOf(String.class)); //keySet = "forecast" or "actual"

        String forecastOrActual = (String) innerKeySet.get(0);
        assertThat(mapInner.get(forecastOrActual), instanceOf(String.class)); //value = "json"

        String json = (String) mapInner.get(forecastOrActual);
        assertThat(json, containsString(":")); //value = "json"
    }
}
