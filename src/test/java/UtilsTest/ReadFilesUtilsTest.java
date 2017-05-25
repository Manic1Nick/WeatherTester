package UtilsTest;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.ReadFilesUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class ReadFilesUtilsTest {

    private static ReadFilesUtils readFilesUtils;

    @BeforeClass
    public static void setupBeforeTests() {
        readFilesUtils = new ReadFilesUtils();
    }

    @Test
    public void test_readJsonFromFiles_Negative_EmptyLinkDir() {

        Object obj = readFilesUtils.readJsonFromFiles("");
        assertThat(obj, instanceOf(HashMap.class));

        HashMap map = (HashMap) obj;
        assertThat(Collections.singletonList(map.keySet()), hasSize(0));
    }

    @Test
    public void test_readJsonFromFiles_Negative_ErrorLinkDir() {

        Object obj = readFilesUtils.readJsonFromFiles("/home/jessy/IdeaProjects/WeatherTester/src/test/errorDirExample/");
        assertThat(obj, instanceOf(HashMap.class));

        HashMap map = (HashMap) obj;
        assertThat(Collections.singletonList(map.keySet()), hasSize(0));
    }

    @Test
    public void test_readJsonFromFiles_Positive() {

        Object obj = readFilesUtils.readJsonFromFiles("/home/jessy/IdeaProjects/WeatherTester/src/test/filesDirExample/");
        assertThat(obj, instanceOf(HashMap.class)); //get Map<Provider, Map<String, String>>

        //map outer
        HashMap mapOuter = (HashMap) obj;
        assertThat(mapOuter.keySet().size(), greaterThan(0)); //size > 0

        List outerKeySet = Collections.singletonList(mapOuter.keySet());
        assertThat(outerKeySet.get(0), instanceOf(Provider.class)); //keySet = providers

        Provider provider = (Provider) outerKeySet.get(0);
        assertThat(mapOuter.get(provider), instanceOf(HashMap.class)); //get Map<String, String>

        //map inner
        HashMap mapInner = (HashMap) obj;
        assertThat(mapInner.keySet().size(), greaterThan(0)); // size > 0

        List innerKeySet = Collections.singletonList(mapInner.keySet());
        assertThat(innerKeySet.get(0), instanceOf(String.class)); //keySet = "forecast" or "actual"

        String forecastOrActual = (String) innerKeySet.get(0);
        assertThat(mapInner.get(forecastOrActual), instanceOf(String.class)); //value = "link"
    }
}
