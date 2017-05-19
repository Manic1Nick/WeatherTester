package UtilsTest;

import org.junit.Test;
import ua.nick.weather.utils.NetUtils;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class NetUtilsTest {

    @Test
    public void test_urlToString_success() {
        String json = null;
        try {
            json = NetUtils.urlToString(new URL("http://api.coindesk.com/v1/bpi/currentprice.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(json, containsString(":"));
    }

    @Test(expected = IOException.class)
    public void test_urlToString_fail() throws IOException {
        NetUtils.urlToString(new URL("error_url"));
    }
}