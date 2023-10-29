package utilities;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class PolygonBuilder {
    public int delta(int height, float theta) {
        return (int) (height * Math.tan(Math.toRadians(theta)));
    }

    public Polygon buildPolygon(Line2D trajectory, int delta1, int delta2) {
        return buildPhotographPolygon(trajectory, delta1, delta2);
    }

    private Polygon buildPhotographPolygon(Line2D trajectory, float viewRangeForPoint1, float viewRangeForPoint2) {
        Point2D[] startPoints = this.getPerpendicularPoints(trajectory, viewRangeForPoint1, trajectory.getP1());
        Point2D[] endPoints = this.getPerpendicularPoints(trajectory, viewRangeForPoint2, trajectory.getP2());

        int[] xPoints = new int[]{(int) startPoints[0].getX(), (int) startPoints[1].getX(), (int) endPoints[0].getX(), (int) endPoints[1].getX()};

        int[] yPoints = new int[]{(int) startPoints[0].getY(), (int) startPoints[1].getY(), (int) endPoints[0].getY(), (int) endPoints[1].getY()};

        return new Polygon(xPoints, yPoints, 4);
    }

    private Point2D[] getPerpendicularPoints(Line2D line, double x, Point2D point1) {
        double lineLength = line.getP1().distance(line.getP2());
        double dx, dy;
        if (point1.equals(line.getP1())) {
            dx = (line.getX2() - line.getX1()) / lineLength;
            dy = (line.getY2() - line.getY1()) / lineLength;
        } else {
            dx = (line.getX1() - line.getX2()) / lineLength;
            dy = (line.getY1() - line.getY2()) / lineLength;
        }

        double dirXLeft = -dy;
        double dirYRight = -dx;

        double newXLeft = point1.getX() + (dirXLeft * x);
        double newYLeft = point1.getY() + (dx * x);

        double newXRight = point1.getX() + (dy * x);
        double newYRight = point1.getY() + (dirYRight * x);

        Point2D pointLeft = new Point2D.Double(newXLeft, newYLeft);
        Point2D pointRight = new Point2D.Double(newXRight, newYRight);

        return new Point2D[]{pointLeft, pointRight};
    }

}
