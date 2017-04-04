package ua.nick.weather.exception;

public class ForecastNotFoundInDBException extends Exception {

    public ForecastNotFoundInDBException(String message) {
        super(message);
    }

}
