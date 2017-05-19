package UtilsTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.nick.weather.model.AverageDiff;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.StringUtils;

import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(value = Parameterized.class)
public class StringUtilsTest03 {

    private String parameter;
    private List<AverageDiff> list;
    private String message;

    public StringUtilsTest03(String parameter, List<AverageDiff> list, String message) {
        this.parameter = parameter;
        this.list = list;
        this.message = message;
    }

    @Parameterized.Parameters(name = "{index}: testCreateMessageAboutUpdateForecasts(map is {0}) = {2}")
    public static Collection<Object[]> data() {
        List<AverageDiff> listEmpty = new ArrayList<>();
        List<AverageDiff> listWithNullData = new ArrayList<>();
        listWithNullData.add(null);

        AverageDiff avDiff = new AverageDiff(Provider.FORECA);
        avDiff.setDays(10);
        List<AverageDiff> listWithData = new ArrayList<>();
        listWithData.add(avDiff);

        String messagePositive = "average differences were updated";
        String messageNegative = "no need to update average differences for any providers";

        return Arrays.asList(new Object[][]{
                {"null", null, messageNegative},
                {"empty map", listEmpty, messageNegative},
                {"map with null data", listWithNullData, messageNegative},
                {"correct", listWithData, messagePositive}
        });
    }

    @Test
    public void test_createMessageAboutUpdateForecasts() {
        assertThat(StringUtils.createMessageAboutUpdateAverageDiff(list), containsString(message));
    }
}