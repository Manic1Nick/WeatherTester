package ua.nick.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.nick.weather.model.*;
import ua.nick.weather.modelTester.TesterAverage;
import ua.nick.weather.modelTester.TesterItem;
import ua.nick.weather.service.WeatherService;
import ua.nick.weather.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ApplicationController {

    @Autowired
    private WeatherService weatherService;

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(@ModelAttribute("message") String message, Model model) {

        LocalDate today = LocalDate.now();
        model.addAttribute("listDiffs",
                weatherService.createListDiffsForPeriod(today.minusDays(7), today));
        model.addAttribute("datesPrev",
                weatherService.createListStringDatesOfPeriod(today.minusDays(7), today));

        model.addAttribute("mapForecasts",
                weatherService.createMapProviderForecastsForPeriod(today.plusDays(1), today.plusDays(7)));
        model.addAttribute("datesNext",
                weatherService.createListStringDatesOfPeriod(today.plusDays(1), today.plusDays(7)));

        List<AverageDiff> averages = weatherService.getAllAverageDiffs();
        averages.sort((diff1, diff2) -> (int) diff1.getValue() - (int) diff2.getValue());
        model.addAttribute("listAverages", averages);

        model.addAttribute("message", message);

        return "welcome";
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

            message = StringUtils.createMessageAboutUpdateForecasts(countingByProvider);

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

            message = StringUtils.createMessageAboutUpdateForecasts(countingByProvider);

        } catch (Exception e) {
            message = e.getMessage();
        }
        resp.getWriter().print(message);
    }

    @RequestMapping(value = {"/forecasts/find/ids"}, method = RequestMethod.GET)
    public void findForecastIdsByDay(HttpServletRequest req, HttpServletResponse resp, Model model)
            throws IOException {

        String date = StringUtils.getDateFromParameter(req.getParameter("date"), req.getParameter("index"));
        model.addAttribute("date", date);

        try {
            String ids = weatherService.getListSeparatedIds(date).stream().collect(Collectors.joining(";"));
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

        resp.getWriter().print(StringUtils.createMessageAboutUpdateAverageDiff(allAverageDiff));
    }

    //todo usable or delete
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
}
