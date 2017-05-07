package ua.nick.weather.utils;

import ua.nick.weather.model.AverageDiff;
import ua.nick.weather.model.Provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static String getDateFromParameter(String date, String index) {
        if (date == null)
            date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        if (index != null)
            date = createPreviousOrNextDate(date, index);

        return date;
    }

    private static String createPreviousOrNextDate(String date, String indexText) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        int index = Integer.valueOf(indexText);
        LocalDate changedDate = index > 0 ? localDate.plusDays(1) : localDate.minusDays(1);

        return formatter.format(changedDate);
    }

    public static String createMessageAboutUpdateForecasts(Map<Provider, Long> map) {

        String message;
        if (map.keySet().size() > 0) {
            String countedByProviders = "";
            int total = 0;
            for (Provider provider : map.keySet()) {
                countedByProviders += "</br>" + map.get(provider) + " from " + provider + ";";
                total += map.get(provider);
            }
            message = "New " + total + " forecast(s) were added to database:" + countedByProviders;

        } else {
            message = "There is no need to update forecasts from providers for this date. " +
                    "</br>Try tomorrow";
        }
        return message;
    }

    public static String createMessageAboutUpdateAverageDiff(List<AverageDiff> list) {

        Map<Provider, Integer> mapCounts = new HashMap<>();//providers updated -> days updated
        for (AverageDiff averageDiff : list)
            if (list.size() > 0)
                mapCounts.put(averageDiff.getProvider(), averageDiff.getDays());

        String message;
        int size = mapCounts.keySet().size();
        if (size > 0) {
            String countedByProviders = "";
            for (Provider provider : mapCounts.keySet())
                countedByProviders += "</br>" + mapCounts.get(provider) + " from " + provider + ";";

            message = "New " + size + " average differences were updated:" + countedByProviders;
        } else {
            message = "There is no need to update average differences for any providers.";
        }
        return message;
    }
}
