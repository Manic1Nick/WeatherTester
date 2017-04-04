package ua.nick.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.nick.weather.model.*;
import ua.nick.weather.service.WeatherService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ApplicationController {

    @Autowired
    private WeatherService weatherService;

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model) {

        LocalDate today = LocalDate.now();
        List<Diff> diffs = weatherService.createListDiffsForPeriod(today.minusDays(7), today);
        model.addAttribute("listDiffs", diffs);

        Map<Provider, Integer> map = new HashMap<>();
        List<Provider> listProviders = Arrays.asList(Provider.values());
        for (int i = 0; i < listProviders.size(); i++)
            map.put(listProviders.get(i), i);
        model.addAttribute("mapProviders", map);

        List<AverageDiff> averages = weatherService.getAllAverageDiffs();
        model.addAttribute("listAverages", averages);

        model.addAttribute("message", message);

        return "welcome";
    }

    @RequestMapping(value = {"/welcome2"}, method = RequestMethod.GET)
    public String welcome2(@ModelAttribute("message") String message, Model model) {

        LocalDate today = LocalDate.now();
        List<Diff> diffs = weatherService.createListDiffsForPeriod(today.minusDays(7), today);
        model.addAttribute("listDiffs", diffs);

        Map<Provider, Integer> map = new HashMap<>();
        List<Provider> listProviders = Arrays.asList(Provider.values());
        for (int i = 0; i < listProviders.size(); i++)
            map.put(listProviders.get(i), i);
        model.addAttribute("mapProviders", map);

        List<AverageDiff> averages = weatherService.getAllAverageDiffs();
        model.addAttribute("listAverages", averages);

        model.addAttribute("message", message);

        return "welcome2";
    }

    @RequestMapping(value = {"/forecasts/get/new"}, method = RequestMethod.GET)
    public void getNewForecasts(HttpServletResponse resp) throws IOException {

        String message;
        try {
            List<List<Forecast>> listAllForecasts = weatherService.getAllNewForecasts();
            Map<Provider, Long> countingByProvider = new HashMap<>();
            for (List<Forecast> list : listAllForecasts)
                if (list.size() > 0)
                    countingByProvider.put(list.get(0).getProvider(), (long) list.size());

            message = createMessageAboutUpdateForecasts(countingByProvider);

        } catch (Exception e) {
            message = e.getMessage();
        }
        resp.getWriter().print(message);
    }

    @RequestMapping(value = {"/actuals/get/new"}, method = RequestMethod.GET)
    public void getNewActuals(HttpServletResponse resp) throws IOException {

        String message;
        try {
            List<Forecast> actuals = weatherService.getAllNewActuals();
            Map<Provider, Long> countingByProvider = actuals.stream().collect(
                    Collectors.groupingBy(Forecast::getProvider, Collectors.counting()));

            message = createMessageAboutUpdateForecasts(countingByProvider);

        } catch (Exception e) {
            message = e.getMessage();
        }
        resp.getWriter().print(message);
    }

    @RequestMapping(value = {"/forecasts/find/ids"}, method = RequestMethod.GET)
    public void findForecastIdsByDay(HttpServletRequest req, HttpServletResponse resp, Model model)
            throws IOException {

        String date = req.getParameter("date");
        if (date == null)
            date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        if (req.getParameter("index") != null)
            date = createPreviousOrNextDate(date, req.getParameter("index"));
        model.addAttribute("date", date);

        try {
            List<List<Long>> listIds = weatherService.getListForecastsAndActualsIds(date);
            String ids = "";
            for (List<Long> list : listIds)
                ids += list.get(0) + "," + list.get(1) + ";";
            resp.getWriter().print(ids);

        } catch (Exception e) {
            resp.getWriter().print(e.getMessage());
        }
    }

    @RequestMapping(value = {"/forecasts/show/day"}, method = RequestMethod.GET)
    public String showForecastsByDate(HttpServletRequest req, Model model)
            throws IOException {

        String ids = req.getParameter("ids"); //ids=1,2;3,4;5,6;...
        String date = weatherService.getForecastById(Long.parseLong(ids.split(",")[1].split(";")[0])).getDate();
        model.addAttribute("date", date);

        List<TesterAverage> averageTesters = weatherService.createListAverageTesters(date);
        Map<Provider, List<TesterItem>> mapItemTesters = weatherService.createMapItemTesters(ids);

        model.addAttribute("mapItemTester", mapItemTesters);
        model.addAttribute("listAverageTester", averageTesters);

        return "day_tester";
    }

    @RequestMapping(value = {"/update/all/average_diff"}, method = RequestMethod.GET)
    public void updateAverageDiffForAllDays(HttpServletResponse resp)
            throws IOException {

        List<AverageDiff> allAverageDiff = weatherService.updateAverageDiffForAllDays();

        resp.getWriter().print(createMessageAboutUpdateAverageDiff(allAverageDiff));
    }

    @RequestMapping(value = {"/forecasts/get/all"}, method = RequestMethod.GET)
    public String getTotalAnalysis(@ModelAttribute("message") String message, Model model) {

        List<Provider> providers = new ArrayList<>();
        List<AverageDiff> averagesTotal = new ArrayList<>();
        Map<Provider, List<Integer>> mapTemps = new HashMap<>();
        for (Provider provider : Arrays.asList(Provider.values())) {
            providers.add(provider);
            averagesTotal.add(weatherService.getAverageDiff(provider));

            List<Forecast> forecasts = weatherService.getAllForecastsFromProvider(provider);
            forecasts.sort((o1, o2) -> o1.getId().intValue()-o2.getId().intValue());
            List<Integer> temps = new ArrayList<>();
            for (Forecast forecast : forecasts)
                temps.add((forecast.getTempMax() + forecast.getTempMin()) / 2);

            mapTemps.put(provider, temps);

        }
        model.addAttribute("providers", providers);
        model.addAttribute("averagesTotal", averagesTotal);
        model.addAttribute("temps1", mapTemps.get(Provider.OPENWEATHER));
        model.addAttribute("temps2", mapTemps.get(Provider.WUNDERGROUND));
        model.addAttribute("temps3", mapTemps.get(Provider.FORECA));

        model.addAttribute("items", new ArrayList<>(Constants.FIELDS_AND_RANGES_MAP.keySet()));
        model.addAttribute("values", weatherService.createListOfAverageItems());


        model.addAttribute("message", message);

        return "total_analysis";
    }


    private String createPreviousOrNextDate(String date, String indexText) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        int index = Integer.valueOf(indexText);
        LocalDate changedDate = index > 0 ? localDate.plusDays(1) : localDate.minusDays(1);

        return formatter.format(changedDate);
    }

    private String createMessageAboutUpdateForecasts(Map<Provider, Long> map) {

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
            message = "All forecasts from providers for requested dates are already exist in the database. " +
                    "</br>Try tomorrow";
        }
        return message;
    }

    private String createMessageAboutUpdateAverageDiff(List<AverageDiff> list) {
        Map<Provider, Integer> mapCounts = new HashMap<>();//providers updated -> days updated
        for (AverageDiff averageDiff : list)
            if (list.size() > 0)
                mapCounts.put(averageDiff.getProvider(), averageDiff.getDays());

        String message = "";
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
