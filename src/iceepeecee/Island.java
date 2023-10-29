package iceepeecee;

import shapes.Canvas;

import java.awt.*;

public class Island extends Entity{
    protected int[][] location;
    protected Polygon shape;
    protected boolean isObserved;

    /**
     * Create a new island
     * @param color The island's color
     * @param polygons The island's shape
     */
    public Island(String color, int[][] polygons) {
        this.color = color;
        this.isVisible = false;
        this.location = polygons;

        int[] xPoints = new int[polygons.length];
        int[] yPoints = new int[polygons.length];

        for (int i = 0; i < polygons.length; i++) {
            xPoints[i] = polygons[i][0];
            yPoints[i] = polygons[i][1];
        }

        this.shape = new Polygon(xPoints, yPoints, polygons.length);
    }

    /**
     * Return the location of all the island's points
     * @return A 2D array
     */
    public int[][] getLocation() {
        return location;
    }

    /**
     * Return the island's shape
     * @return A polygon
     */
    public Polygon getShape() {
        return shape;
    }

    /**
     * Check if the island is being observed
     * @return Whether the island is being observed or not
     */
    public boolean isObserved() {
        return isObserved;
    }

    /**
     * Set whether the island is being observed or not
     * @param flag True if the island is being observed, false otherwise
     */
    public void isObserved(boolean flag) {
        this.isObserved = flag;
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, this.color, this.shape, isObserved);
        }
    }

}
