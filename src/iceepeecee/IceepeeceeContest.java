package iceepeecee;

import utilities.PolygonBuilder;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;

public class IceepeeceeContest {
    private Iceepeecee simulator;
    private HashMap<Integer, FlightVector> inputFlights;
    private HashMap<Integer, Polygon> inputIslands;

    /**
     * Solve the ICPC problem with the given input
     * @param islands The islands
     * @param flights The flights
     * @return The solution
     */
    public float solve(int[][][] islands, int[][][] flights) {
        inputFlights = buildFlightVectors(flights);
        inputIslands = buildIslandPolygons(islands);
        simulate(islands, flights);
        float high = 90.f;
        float low = 0.f;
        float mid = (high + low) / 2;
        float prevMid = mid;
        while (high >= low) {
            try {
                Thread.sleep(1000);
                updatePhoto(mid);
                if (isSurveyComplete(mid)) {
                    high = mid;
                    if (Math.abs(mid - prevMid) < 0.1f) break;
                } else {
                    low = mid;
                }
                prevMid = mid;
                mid = (high + low) / 2;
            } catch (Exception e) {
                simulator.displayError(e.getMessage());
            }
        }
        System.out.println(mid);
        return mid;
    }

    /**
     * Given the input, simulate the process of solving the problem
     * @param islands The islands
     * @param flights The flights
     */
    public void simulate(int[][][] islands, int[][][] flights) {
        try {
            simulator = new Iceepeecee(islands, flights);
            simulator.makeVisible();
        } catch (IceepeeceeException e) {
            simulator.displayError(e.getMessage());
        }
    }

    private HashMap<Integer, FlightVector> buildFlightVectors(int[][][] flights) {
        HashMap<Integer, FlightVector> output = new HashMap<>();
        int i = 0;
        for (int[][] flight : flights) {
            output.put(i, new FlightVector(flight[0], flight[1]));
            i++;
        }
        return output;
    }

    private HashMap<Integer, Polygon> buildIslandPolygons(int[][][] islands) {
        HashMap<Integer, Polygon> output = new HashMap<>();
        int i = 0;
        for (int[][] island : islands) {
            int[] xPoint = new int[island.length];
            int[] yPoint = new int[island.length];
            for (int j = 0; j < island.length; j++) {
                xPoint[j] = island[j][0];
                yPoint[j] = island[j][1];
            }
            output.put(i, new Polygon(xPoint, yPoint, island.length));
            i++;
        }
        return output;
    }

    private void updatePhoto(float theta) {
        if (simulator != null) {
            try {
                simulator.photograph(theta);
            } catch (IceepeeceeException e) {
                simulator.displayError(e.getMessage());
            }
        }
    }

    private boolean isSurveyComplete(float angle) {
        HashMap<Integer, Polygon> observedIslands = new HashMap<>();

        for (FlightVector vector : inputFlights.values()) {
            PolygonBuilder builder = new PolygonBuilder();
            int delta1 = builder.delta(vector.z1, angle);
            int delta2 = builder.delta(vector.z2, angle);
            Point p1 = new Point(vector.x1, vector.y1);
            Point p2 = new Point(vector.x2, vector.y2);
            Line2D trajectory = new Line2D.Double(p1, p2);
            Polygon flightPolygon = builder.buildPolygon(trajectory, delta1, delta2);

            for (Integer island : inputIslands.keySet()) {
                if (isContained(flightPolygon, inputIslands.get(island))) {
                    if (!observedIslands.containsKey(island)) observedIslands.put(island, inputIslands.get(island));
                }
            }
        }

        return observedIslands.size() == inputIslands.size();
    }

    private boolean isContained(Polygon flightPolygon, Polygon polygon) {
        boolean isContained = true;

        for (int i = 0; i < polygon.npoints; i++) {
            int x = polygon.xpoints[i];
            int y = polygon.ypoints[i];

            if (!flightPolygon.contains(x, y)) {
                isContained = false;
                break;
            }
        }

        return isContained;
    }
}

class FlightVector {
    public final int x1, y1, z1;
    public final int x2, y2, z2;
    public FlightVector(int[] origin, int[] dest) {
        x1 = origin[0];
        y1 = origin[1];
        z1 = origin[2];
        x2 = dest[0];
        y2 = dest[1];
        z2 = dest[2];
    }
}
