package ua.nick.weather.weatherFactory;

import org.springframework.stereotype.Component;
import ua.nick.weather.model.AverageDiff;
import ua.nick.weather.model.Diff;
import ua.nick.weather.model.Forecast;
import ua.nick.weather.model.Provider;
import ua.nick.weather.utils.DiffUtils;

@Component
public class DiffsFactory {

    private DiffUtils diffUtils;

    public DiffsFactory() {
        diffUtils = new DiffUtils();
    }

    public Diff createNewDiff(Forecast forecast, Forecast actual) {

        String date = forecast.getDate();
        Provider provider = forecast.getProvider();

        double tempDiff = diffUtils.calculateTempDiff(
                forecast.getTempMin(), forecast.getTempMax(),
                actual.getTempMin(), actual.getTempMax());

        double pressureDiff = diffUtils.calculatePressureDiff(
                forecast.getPressure(), actual.getPressure());

        double cloudsDiff = diffUtils.calculateCloudsDiff(
                forecast.getClouds(), actual.getClouds());

        double windSpeedDiff = diffUtils.calculateWindSpeedDiff(
                forecast.getWindSpeed(), actual.getWindSpeed());

        double descriptionDiff = diffUtils.calculateDescriptionDiff(
                forecast.getDescription(), actual.getDescription());

        double averageDayDiff = diffUtils.calculateAverageDayDiff(
                tempDiff, pressureDiff, cloudsDiff, windSpeedDiff, descriptionDiff);

        return new Diff(date, provider,
                tempDiff, pressureDiff, cloudsDiff, windSpeedDiff,
                descriptionDiff, averageDayDiff);
    }

    public AverageDiff addDiffToAverageDiff(AverageDiff avDiff, Diff diff) {

        int days = avDiff.getDays();

        double temp = diffUtils.calculateAverageValue(
                avDiff.getTempTotalDiff(), diff.getTempDiff(), days);

        double pressure = diffUtils.calculateAverageValue(
                avDiff.getPressureTotalDiff(), diff.getPressureDiff(), days);

        double clouds = diffUtils.calculateAverageValue(
                avDiff.getCloudsTotalDiff(), diff.getCloudsDiff(), days);

        double windSpeed = diffUtils.calculateAverageValue(
                avDiff.getWindSpeedTotalDiff(), diff.getWindSpeedDiff(), days);

        double description = diffUtils.calculateAverageValue(
                avDiff.getDescrTotalDiff(), diff.getDescriptionDiff(), days);

        double value = diffUtils.calculateAverageValue(
                avDiff.getValue(), diff.getAverageDayDiff(), days);

        avDiff.setTempTotalDiff(temp);
        avDiff.setCloudsTotalDiff(pressure);
        avDiff.setPressureTotalDiff(clouds);
        avDiff.setWindSpeedTotalDiff(windSpeed);
        avDiff.setDescrTotalDiff(description);
        avDiff.setValue(value);
        avDiff.setDays(days + 1);

        return avDiff;
    }
}