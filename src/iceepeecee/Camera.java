package iceepeecee;

import shapes.Canvas;
import utilities.PolygonBuilder;

import java.awt.*;
import java.util.ArrayList;

public class Camera extends Entity {
    private final Flight owner;
    private float angle;
    private Polygon photo;

    public Camera(Flight owner) {
        this.owner = owner;
        color = owner.getColor();
    }

    @Override
    protected void draw() {
        if (isVisible) {
            shapes.Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, photo, false);
        }
    }

    @Override
    void makeVisible() {
        if (photo != null) {
            isVisible = true;
            draw();
        }
    }

    public void takePhoto(float theta) throws IceepeeceeException {
        if (theta > 180.0f || theta < 0.0f) throw new IceepeeceeException(IceepeeceeException.INVALID_ANGLE);
        angle = theta;
        PolygonBuilder builder = new PolygonBuilder();
        int delta1 = builder.delta(owner.getLocation()[0][2], theta);
        int delta2 = builder.delta(owner.getLocation()[1][2], theta);
        photo = builder.buildPolygon(owner.getTrajectory(), delta1, delta2);
    }

    /**
     * Given an array of islands, get the colors of the islands contained within the fieldOfView
     *
     * @param islands The array of islands
     * @return The islands contained within the field of view
     */
    public String[] observedIslands(Island[] islands) {
        ArrayList<String> observedIslandColors = new ArrayList<>();

        for (Island island : islands) {
            Polygon islandPolygon = island.getShape();

            boolean isContained = true;

            for (int i = 0; i < islandPolygon.npoints; i++) {
                int x = islandPolygon.xpoints[i];
                int y = islandPolygon.ypoints[i];

                if (!photo.contains(x, y)) {
                    isContained = false;
                    break;
                }
            }

            if (isContained) {
                observedIslandColors.add(island.getColor());
            }
        }

        String[] observedIslandColorsArray = new String[observedIslandColors.size()];
        observedIslandColorsArray = observedIslandColors.toArray(observedIslandColorsArray);

        return observedIslandColorsArray;
    }

    public int getAngle() {
        return (int)angle;
    }

    public Polygon getPhoto() {
        return photo;
    }
}
