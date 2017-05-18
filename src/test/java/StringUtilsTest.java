import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ua.nick.weather.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(value = Parameterized.class)
public class StringUtilsTest {

    private String date;
    private String index;
    private String expectedDate;

    public StringUtilsTest(String date, String index, String expectedDate) {
        this.date = date;
        this.index = index;
        this.expectedDate = expectedDate;
    }

    @Parameterized.Parameters(name = "{index}: testChangeDateByIndex({0} add {1} days) = {2}")
    public static Collection<Object[]> data() {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String today = LocalDateTime.now().format(yyyyMMdd);

        return Arrays.asList(new Object[][]{
                {"2017/01/01", "1", "2017/01/02"},
                {"2017/01/01", "-1", "2016/12/31"},
                {"2017/01/01", "0", "2017/01/01"},
                {"2017/01/01", null, today},
                {null, null, today}
        });
    }

    @Test
    public void test_changeDateByIndex() {
        assertThat(StringUtils.changeDateByIndex(date, index), is(expectedDate));
    }
}