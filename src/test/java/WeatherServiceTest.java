import org.junit.Test;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.service.WeatherServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {

    /*
    * void saveNewForecast(Forecast forecast);
    Forecast getForecastById(Long id);
    List<List<Forecast>> getAllNewForecasts() throws IOException, URISyntaxException, ParseException, NoDataFromProviderException;
    List<Forecast> getNewForecastsFromProvider(Provider provider) throws URISyntaxException, IOException, ParseException, NoDataFromProviderException;
    List<Forecast> getAllNewActuals() throws IOException, URISyntaxException, ParseException;
    Forecast getActualWeatherFromProvider(Provider provider) throws URISyntaxException, IOException, ParseException;
    List<Forecast> getAllForecastsFromProvider(Provider provider);
    List<List<Long>> getListForecastsAndActualsIds(String date) throws ForecastNotFoundInDBException;

    void saveNewDiff(Diff diff);
    AverageDiff getAverageDiff(Provider provider);
    List<AverageDiff> getAllAverageDiffs();
    List<Integer> createListOfAverageItems();
    List<TesterAverage> createListAverageTesters(String date);
    Map<Provider, List<TesterItem>> createMapItemTesters(String ids);
    List<Diff> createListDiffsForPeriod(LocalDate from, LocalDate to);
    Map<Provider, List<Forecast>> createMapProviderForecastsForPeriod(LocalDate from, LocalDate to);
    List<String> createListStringDatesOfPeriod(LocalDate from, LocalDate to);

    //admin
    List<AverageDiff> updateAverageDiffForAllDays();
    */

    @Test
    public void test_total_book_by_mockito() {

        //1. Setup
        Forecast forecast = new Forecast();

        WeatherServiceImpl mockito = mock(WeatherServiceImpl.class);

        //if the author is "mkyong", then return a 'books' object.
        when(mockito.getForecastById(null)).thenReturn(forecast);

        /*AuthorServiceImpl obj = new AuthorServiceImpl();
        obj.setBookService(mockito);
        obj.setBookValidatorService(new FakeBookValidatorService());

        //2. Test method
        int qty = obj.getTotalBooks("mkyong");

        //3. Verify result
        assertThat(qty, is(2));*/

    }
}
