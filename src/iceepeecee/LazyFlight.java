package iceepeecee;

public class LazyFlight extends Flight{
    public LazyFlight(String color, int[] from, int[] to) {
        super(color, from, to);
    }

    @Override
    public void takePhoto(float theta) throws IceepeeceeException {
        if (camera.getPhoto() != null) camera.takePhoto(theta);
    }
}
