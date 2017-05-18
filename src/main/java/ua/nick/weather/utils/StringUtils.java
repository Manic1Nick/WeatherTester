package ua.nick.weather.utils;

import ua.nick.weather.model.AverageDiff;
import ua.nick.weather.model.Provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtils {

    public static String changeDateByIndex(String date, String index) {
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        if (date == null || index == null) {
            date = LocalDateTime.now().format(yyyyMMdd);

        } else {
            LocalDate localDate = LocalDate.parse(date, yyyyMMdd);
            LocalDate changedDate = Integer.valueOf(index) == 1 ? localDate.plusDays(1)
                    : Integer.valueOf(index) == -1 ? localDate.minusDays(1)
                    : localDate ;
            date = changedDate.format(yyyyMMdd);
        }
        return date;
    }

    public static String createMessageAboutUpdateForecasts(Map<Provider, Long> map) {
        String message;

        if (map.keySet().size() > 0) {
            String countedByProviders = "";
            int total = 0;
            for (Provider provider : map.keySet()) {
                countedByProviders += String.format("</br>%s from %s;", map.get(provider), provider);
                total += map.get(provider);
            }
            message = String.format("New %s forecast(s) were added to database:%s", total, countedByProviders);

        } else {
            message = "There is no need to update forecasts from providers for this date. " +
                    "</br>Try tomorrow";
        }
        return message;
    }

    public static String createMessageAboutUpdateAverageDiff(List<AverageDiff> list) {

        //providers updated -> days updated
        Map<Provider, Integer> mapCounts = list.stream()
                .collect(Collectors.toMap(AverageDiff::getProvider, AverageDiff::getDays));

        String message;
        int size = mapCounts.keySet().size();
        if (size > 0) {
            String countedByProviders = "";
            for (Provider provider : mapCounts.keySet())
                countedByProviders += String.format("</br>%s from %s;", mapCounts.get(provider), provider);

            message = String.format("New %s average differences were updated:%s", size, countedByProviders);
        } else {
            message = "There is no need to update average differences for any providers.";
        }
        return message;
    }
}
