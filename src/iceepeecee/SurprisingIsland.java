package iceepeecee;

import java.awt.*;

public class SurprisingIsland extends Island{
    /**
     * Create a new island
     *
     * @param color    The island's color
     * @param polygons The island's shape
     */
    public SurprisingIsland(String color, int[][] polygons) {
        super(color, polygons);
        this.location = polygons;
    }

    /**
     * Get the island's location
     * @return The island location
     */
    public int[][] getLocation() {
        int[][] location2 = new int[this.location.length - 1][];

        System.arraycopy(this.location, 1, location2, 0, this.location.length - 1);

        this.location = location2;

        int[] xPoints = new int[location.length];
        int[] yPoints = new int[location.length];

        for (int i = 0; i < location.length; i++) {
            xPoints[i] = location[i][0];
            yPoints[i] = location[i][1];
        }

        this.shape = new Polygon(xPoints, yPoints, location.length);

        this.makeInvisible();
        this.makeVisible();

        return location;
    }
}
