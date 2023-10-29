import iceepeecee.IceepeeceeContest;

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
            IceepeeceeContest contest = new IceepeeceeContest();
            contest.solve(testIslands, testFlights);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }
}