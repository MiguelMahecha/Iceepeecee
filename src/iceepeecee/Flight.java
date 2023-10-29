package iceepeecee;

import shapes.Canvas;

import java.awt.*;
import java.awt.geom.Line2D;

public class Flight extends Entity {
    protected final int[][] location;
    protected final Line2D trajectory;
    protected final Camera camera;

    public Flight(String color, int[] from, int[] to) {
        this.color = color;
        this.location = new int[][] {from, to};
        this.isVisible = false;
        Point p1 = new Point(location[0][0], location[0][1]);
        Point p2 = new Point(location[1][0], location[1][1]);
        trajectory = new Line2D.Double(p1, p2);
        camera = new Camera(this);
    }

    public void takePhoto(int theta) throws IceepeeceeException {
        camera.takePhoto((float)theta);
    }

    public void takePhoto(float theta) throws IceepeeceeException {
        camera.takePhoto(theta);
    }

    public String[] observedIslands(Island[] islands) {
        return camera.observedIslands(islands);
    }

    public boolean isUseless(Island[] islands) {
        String[] observed = observedIslands(islands);

        return !(observed.length >= 1);
    }

    public int getAngle() {
        return camera != null ? camera.getAngle() : 0;
    }

    public int[][] getLocation() {
        return location;
    }

    public void makePhotoVisible() {
        camera.makeVisible();
    }

    public void makePhotoInvisible() {
        camera.makeInvisible();
    }

    @Override
    protected void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, trajectory);
        }
    }

    public Line2D getTrajectory() {
        return trajectory;
    }
}
