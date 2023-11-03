import iceepeecee.Iceepeecee;
import iceepeecee.IceepeeceeContest;
import iceepeecee.Island;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        int[][][] testFlights = new int[][][]{
                {{0, 30, 20}, {78, 70, 5}},
                {{55, 0, 20}, {70, 60, 10}}
        };

        int[][][] testIslands = new int[][][]{
                {{20, 30}, {50, 50}, {10, 50}},
                {{40, 20}, {60, 10}, {75, 20}, {60, 30}},
                {{45, 60}, {55, 55}, {60, 60}, {55, 65}}
        };

        try {
//            ICEEPEECEE With error messages
            Iceepeecee simulator = new Iceepeecee(testIslands, testFlights);
            simulator.makeVisible();

//            Take photographs
            Thread.sleep(1000);
            simulator.photograph(40.f);
            Thread.sleep(1000);
            simulator.photograph(30.f);
            Thread.sleep(1000);
//            Surprise Island
            List<String> islands = Arrays.stream(simulator.islands()).toList();
            simulator.addIsland("Surprising", "grey", new int[][]{{80, 60}, {100, 50}, {115, 60}, {100, 70}});
            Thread.sleep(1000);
            System.out.println(Arrays.deepToString(simulator.islandLocation("blue")));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }
}