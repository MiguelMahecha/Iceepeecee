package iceepeecee;

public class FlatFlight extends Flight{
    public FlatFlight(String color, int[] from, int[] to) throws IceepeeceeException {
        super(color, from, to);
        if (this.getLocation()[0][2] != this.getLocation()[0][2])
            throw new IceepeeceeException(IceepeeceeException.INVALID_FLIGHT);
    }
}
