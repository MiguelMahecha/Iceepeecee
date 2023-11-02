package iceepeecee;

public class IceepeeceeException extends Exception {
    public static final String INVALID_ISLAND = "Not a valid island";
    public static final String INVALID_FLIGHT = "Not a valid flight";
    public static final String INVALID_ANGLE = "Not a valid angle";
    public static final String ISLAND_ALREADY_EXISTS = "There is already an island with that color";
    public static final String FLIGHT_ALREADY_EXISTS = "There is already a flight with that color";
    public static final String ISLAND_NOT_FOUND = "There is no island with that color";
    public static final String FLIGHT_NOT_FOUND = "There is no flight with that color";
    public static final String INVALID_FLAT_FLIGHT = "Flat flights should not have different heights";

    public IceepeeceeException(String message) {
        super(message);
    }
}
