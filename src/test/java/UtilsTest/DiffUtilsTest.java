package UtilsTest;

import org.junit.Test;
import ua.nick.weather.utils.DiffUtils;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DiffUtilsTest {

    private static DiffUtils diffUtils = new DiffUtils();
    private static List<Object[]> data;

    @Test
    public void test_calculateTempDiff() {
        //int tempMinForecast, int tempMaxForecast, int tempMinActual, int tempMaxActual
        data = Arrays.asList(new Object[][]{
                {0, 20, 0, 20, 0.0},
                {0, 20, 5, 25, -8.3},
                {0, 5, -5, 0, 8.3},
                {0, 0, -10, -5, 11.7}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculateTempDiff(
                    (int) array[0], (int) array[1], (int) array[2], (int) array[3]),
                    is(array[4]));
    }

    @Test
    public void test_calculatePressureDiff() {
        //int pressureForecast, int pressureActual
        data = Arrays.asList(new Object[][]{
                {1000, 1020, 2.0},
                {1000, 980, -2.0},
                {0, 1020, 2.0},
                {0, 1000, 0.0}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculatePressureDiff(
                    (int) array[0], (int) array[1]),
                    is(array[2]));
    }

    @Test
    public void test_calculateCloudsDiff() {
        //int cloudsForecast, int cloudsActual
        data = Arrays.asList(new Object[][]{
                {50, 60, 10.0},
                {50, 40, -10.0},
                {0, 100, 100.0},
                {0, 0, 0.0}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculateCloudsDiff(
                    (int) array[0], (int) array[1]),
                    is(array[2]));
    }

    @Test
    public void test_calculateWindSpeedDiff() {
        //int windSpeedForecast, int windSpeedActual
        data = Arrays.asList(new Object[][]{
                {5, 6, 10.0},
                {5, 4, -10.0},
                {0, 10, 100.0},
                {0, 0, 0.0}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculateWindSpeedDiff(
                    (int) array[0], (int) array[1]),
                    is(array[2]));
    }

    @Test
    public void test_calculateDescriptionDiff() {
        //String descriptionForecast, String descriptionActual
        data = Arrays.asList(new Object[][]{
                {"half cloudy no precipitation rain", "broken no precipitation rain", 33.3},
                {"half cloudy", "half cloudy", 0.0},
                {"rain", "broken no precipitation rain", 66.7}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculateDescriptionDiff(
                    (String) array[0], (String) array[1]),
                    is(array[2]));
    }

    @Test
    public void test_calculateAverageValue() {
        //double value, double addingValue, int days
        data = Arrays.asList(new Object[][]{
                {10.0, 20.0, 10, 10.9},
                {10.0, 5.0, 10, 9.5},
                {10.0, 20.0, 1, 15.0},
                {10.0, 5.0, 1, 7.5},
                {0.0, 10.0, 1, 5.0}
        });

        for (Object[] array : data)
            assertThat(diffUtils.calculateAverageValue(
                    (double) array[0], (double) array[1], (int) array[2]),
                    is(array[3]));
    }
}
