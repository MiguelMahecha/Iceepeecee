package iceepeecee;

import shapes.Canvas;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Iceepeecee {
    private boolean isVisible;
    private final HashMap<String, Island> islands;
    private final HashMap<String, Flight> flights;
    private final int length;
    private final int width;

    /**
     * Create a new Iceepeecee map with the given length and width
     *
     * @param length The map's length
     * @param width  The map's width
     */
    public Iceepeecee(int length, int width) {
        this.length = length;
        this.width = width;
        this.flights = new HashMap<>();
        this.islands = new HashMap<>();
        this.isVisible = false;
    }

    /**
     * Create a new Iceepeecee map with the given islands and flights
     *
     * @param islands The islands
     * @param flights The flights
     */
    public Iceepeecee(int[][][] islands, int[][][] flights) throws IceepeeceeException {
        this(1000, 1000);
        buildFlights(flights);
        buildIslands(islands);
    }

    /**
     * Add a new Island to the map.
     *
     * @param color   The Island's color
     * @param polygon The island's shape
     */
    public void addIsland(String color, int[][] polygon) throws IceepeeceeException {
        if (!this.validIslandPoints(polygon)) throw new IceepeeceeException(IceepeeceeException.INVALID_ISLAND);
        if (this.islands.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.ISLAND_ALREADY_EXISTS);

        Island i = new Island(color, polygon);
        this.islands.put(i.getColor(), i);
        if (isVisible) islands.get(i.getColor()).makeVisible();
    }

    /**
     * Add a new Island to the map.
     *
     * @param type    The Island's type: Normal | Fixed | Surprising
     * @param color   The island's color
     * @param polygon The island's polygon
     */
    public void addIsland(String type, String color, int[][] polygon) throws IceepeeceeException {
        if (!this.validIslandPoints(polygon)) throw new IceepeeceeException(IceepeeceeException.INVALID_ISLAND);
        if (this.islands.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.ISLAND_ALREADY_EXISTS);
        Island i = switch (type) {
            case "Normal" -> new Island(color, polygon);
            case "Fixed" -> new FixedIsland(color, polygon);
            default -> new SurprisingIsland(color, polygon);
        };
        this.islands.put(i.getColor(), i);
        if (isVisible) islands.get(i.getColor()).makeVisible();
    }

    /**
     * Add a new Flight to the map
     *
     * @param color The Flight's color
     * @param from  The Flight's origin
     * @param to    Flight destination
     */
    public void addFlight(String color, int[] from, int[] to) throws IceepeeceeException {
        if (!validPointsForFlight(from, to)) throw new IceepeeceeException(IceepeeceeException.INVALID_FLIGHT);
        if (flights.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.FLIGHT_ALREADY_EXISTS);

        Flight f = new Flight(color, from, to);
        flights.put(f.getColor(), f);
        if (isVisible) flights.get(f.getColor()).makeVisible();
    }

    /**
     * Add a new Flight to the map
     *
     * @param color The Flight's color
     * @param from  The Flight's origin
     * @param to    Flight destination
     */
    public void addFlight(String type, String color, int[] from, int[] to) throws IceepeeceeException {
        if (!validPointsForFlight(from, to)) throw new IceepeeceeException(IceepeeceeException.INVALID_FLIGHT);
        if (flights.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.FLIGHT_ALREADY_EXISTS);

        Flight f = switch (type) {
            case "Lazy" -> new LazyFlight(color, from, to);
            case "Flat" -> new FlatFlight(color, from, to);
            default -> new Flight(color, from, to);
        };
        flights.put(f.getColor(), f);
        if (isVisible) flights.get(f.getColor()).makeVisible();
    }

    /**
     * Tell the given flight to take a photograph using the given aperture angle theta
     *
     * @param flight The flight
     * @param theta  The angle to take the photo with
     */
    public void photograph(String flight, int theta) throws IceepeeceeException {
        Flight f = loadFlight(flight);
        if (f == null) throw new IceepeeceeException(IceepeeceeException.FLIGHT_NOT_FOUND);
        f.takePhoto(theta);
        String[] observed = observedIslands(f.getColor());
        if (observed != null && observed.length >= 1) {
            for (String s : observed) {
                islands.get(s).isObserved(true);
                islands.get(s).makeVisible();
            }
            if (isVisible) f.makePhotoVisible();
        }
    }

    /**
     * Tell all flights to take a photo with the given aperture angle theta
     *
     * @param theta The angle to take the photo with
     */
    public void photograph(int theta) throws IceepeeceeException {
        photograph((float) theta);
    }

    /**
     * Tell all flights to take a photo with the given aperture angle theta
     *
     * @param theta The angle to take the photo with
     */
    public void photograph(float theta) throws IceepeeceeException {
        for (Flight f : flights.values()) {
            f.takePhoto(theta);
            if (isVisible) f.makePhotoVisible();
        }

        List<String> observed = Arrays.asList(observedIslands());
        if (!observed.isEmpty()) {
//            for (String s : observed) {
//                islands.get(s).isObserved(true);
//                islands.get(s).makeVisible();
//            }
            for (String s : islands.keySet()) {
                islands.get(s).isObserved(observed.contains(s));
                islands.get(s).makeVisible();
            }
        }
    }

    /**
     * Delete an island
     *
     * @param color The island to delete
     */
    public void delIsland(String color) throws IceepeeceeException {
        if (!islands.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.ISLAND_NOT_FOUND);
        if (!(islands.get(color) instanceof FixedIsland)) islands.remove(color);
    }

    /**
     * Delete a flight
     *
     * @param color The flight to delete
     */
    public void delFlight(String color) throws IceepeeceeException {
        if (!flights.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.FLIGHT_NOT_FOUND);
        flights.remove(color);
    }

    /**
     * Get the location of an island
     *
     * @param island The island to query
     * @return A 2d array containing the island location points
     */
    public int[][] islandLocation(String island) throws IceepeeceeException {
        if (!islands.containsKey(island)) throw new IceepeeceeException(IceepeeceeException.ISLAND_NOT_FOUND);
        return islands.get(island).getLocation();
    }

    /**
     * Get the location of a flight
     *
     * @param flight The flight to query
     * @return A 2d array containing the flight location points
     */
    public int[][] flightLocation(String flight) throws IceepeeceeException {
        if (!flights.containsKey(flight)) throw new IceepeeceeException(IceepeeceeException.FLIGHT_NOT_FOUND);
        return flights.get(flight).getLocation();
    }

    /**
     * The aperture angle of a plane's camera
     *
     * @param color The color of the Flight to query
     * @return The aperture angle of a plane's camera
     */
    public int flightCamera(String color) throws IceepeeceeException {
        if (!flights.containsKey(color)) throw new IceepeeceeException(IceepeeceeException.FLIGHT_NOT_FOUND);
        return flights.get(color).getAngle();
    }

    /**
     * Get the colors of all islands
     *
     * @return An array with the colors of all islands
     */
    public String[] islands() {
        return islands.keySet().toArray(new String[0]);
    }

    /**
     * Get the colors of all flights
     *
     * @return An array with the colors of all flights
     */
    public String[] flights() {
        return flights.keySet().toArray(new String[0]);
    }

    /**
     * Get the colors of all islands that are currently being fully observed by at least one flight
     *
     * @return A String array
     */
    public String[] observedIslands() {
        ArrayList<String> observed = new ArrayList<>();

        for (Flight f : flights.values()) {
            String[] observedByF = f.observedIslands(islands.values().toArray(new Island[0]));

            for (String i : observedByF) {
                if (!observed.contains(i)) observed.add(i);
            }
        }

        return observed.toArray(new String[0]);
    }

    public String[] observedIslands(String color) {
        ArrayList<String> observed = new ArrayList<>();
        Flight f = loadFlight(color);
        String[] observedByF = f.observedIslands(islands.values().toArray(new Island[0]));
        for (String i : observedByF) {
            if (!observed.contains(i)) observed.add(i);
        }
        return observed.toArray(new String[0]);
    }

    /**
     * Get the colors of all flights that are not fully covering any island
     *
     * @return A String array
     */
    public String[] uselessFlights() {
        ArrayList<String> useless = new ArrayList<>();
        for (Flight f : flights.values()) {
            if (f.isUseless(islands.values().toArray(new Island[0]))) useless.add(f.getColor());
        }
        return useless.toArray(new String[0]);
    }

    /**
     * Make the map and all contents visible
     */
    public void makeVisible() {
        this.isVisible = true;
        for (Flight f : flights.values()) f.makeVisible();
        for (Island i : islands.values()) i.makeVisible();
    }

    /**
     * Make the map and all contents invisible
     */
    public void makeInvisible() {
        this.isVisible = false;
        for (Flight f : flights.values()) f.makeInvisible();
        for (Island i : islands.values()) i.makeInvisible();
    }

    /**
     * Terminate the program
     */
    public void finish() {
        System.exit(0);
    }

    /**
     * See if the last operation was successful
     *
     * @return A boolean
     */
    public boolean ok() {
        return false;
    }

    public Island loadIsland(String color) {
        return islands.get(color);
    }

    public Flight loadFlight(String color) {
        return flights.get(color);
    }

    private void buildIslands(int[][][] islands) throws IceepeeceeException {
        for (int[][] points : islands) {
            addIsland(chooseRandomColor(true), points);
        }
    }

    private void buildFlights(int[][][] flights) throws IceepeeceeException {
        for (int[][] points : flights) {
            addFlight(chooseRandomColor(false), points[0], points[1]);
        }
    }

    private boolean validIslandPoints(int[][] polygon) {
        for (int[] point : polygon) {
            if (point[0] > width || point[0] < 0) return false;
            if (point[1] > length || point[1] < 0) return false;
        }
        return true;
    }

    private boolean validPointsForFlight(int[] from, int[] to) {
        if (from[0] < 0 || from[1] > width) return false;
        if (from[1] < 0 || from[1] > length) return false;

        if (to[0] < 0 || to[0] > width) return false;

        return to[1] >= 0 && to[1] <= width;
    }

    private String chooseRandomColor(boolean forIsland) {
        String color = "blue";
        if (forIsland) {
            while (islands.containsKey(color)) color = Canvas.randomColor();
        } else {
            while (flights.containsKey(color)) color = Canvas.randomColor();
        }
        return color;
    }

    public void displayError(String message) {
        JOptionPane.showConfirmDialog(null, message);
    }
}