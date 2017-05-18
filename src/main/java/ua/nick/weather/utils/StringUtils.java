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

        if (map != null && map.keySet().size() > 0) {
            Long total = map.values().stream()
                    .filter(count -> count != null)
                    .reduce((n1, n2) -> n1 + n2).orElse(0L);

            String countedByProviders = map.keySet().stream()
                    .map(provider -> "</br>" + map.get(provider) + " from " + provider)
                    .collect(Collectors.joining());

            if (total > 0)
                return String.format("New %s forecast(s) were added to database:%s", total, countedByProviders);
        }

        return "There is no need to update forecasts from providers for this date. " +
                "</br>Try tomorrow";
    }

    public static String createMessageAboutUpdateAverageDiff(List<AverageDiff> list) {

        if (list != null && list.size() > 0) {
            //providers updated -> days updated
            Map<Provider, Integer> mapCounts = list.stream()
                    .filter(avDiff -> avDiff != null)
                    .collect(Collectors.toMap(AverageDiff::getProvider, AverageDiff::getDays));

            int size = mapCounts.keySet().size();
            if (size > 0) {
                String countedByProviders = mapCounts.keySet().stream()
                        .map(provider -> "</br>" + mapCounts.get(provider) + " from " + provider)
                        .collect(Collectors.joining());

                return String.format("New %s average differences were updated:%s", size, countedByProviders);
            }
        }
        return "There is no need to update average differences for any providers.";
    }
}
