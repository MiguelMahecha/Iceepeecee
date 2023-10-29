package shapes;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * shapes.Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose shapes.Canvas, specially made for
 * the BlueJ "shapes" example.
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 * @version: 1.6 (shapes)
 */
public class Canvas {
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 300, 300,
                    Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    /**
     * Factory method to get the canvas singleton object.
     *
     * @param height The canvas' height
     * @param width  The canvas' width
     */
    public static Canvas getCanvas(int width, int height) {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", width, height,
                    Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----

    private final JFrame frame;
    private final CanvasPane canvas;
    private Graphics2D graphic;
    private final Color backgroundColour;
    private Image canvasImage;
    private final List<Object> objects;
    private final HashMap<Object, ShapeDescription> shapes;

    /**
     * Create a shapes.Canvas.
     *
     * @param title    title to appear in shapes.Canvas Frame
     * @param width    the desired width for the canvas
     * @param height   the desired height for the canvas
     * @param bgColour the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<>();
        shapes = new HashMap<>();
    }

    public static String randomColor() {
        String[] colors = new String[]{"red", "blue", "yellow", "green", "magenta", "white",
                "black", "cyan", "darkGray", "gray", "lightGray", "orange", "pink"};

        int choice = new Random().nextInt(colors.length);

        return colors[choice];
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     *
     * @param visible boolean value representing the desired visibility of
     *                the canvas (true or false)
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     *
     * @param referenceObject an object to define identity for this shape
     * @param color           the color of the shape
     * @param shape           the shape object to be drawn on the canvas
     */
    // Note: this is a slightly backwards way of maintaining the shape
    // objects. It is carefully designed to keep the visible shape interfaces
    // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Draw a given shape onto the canvas.
     *
     * @param referenceObject an object to define identity for this shape
     * @param color           the color of the shape
     * @param shape           the shape object to be drawn on the canvas
     */
    // Note: this is a slightly backwards way of maintaining the shape
    // objects. It is carefully designed to keep the visible shape interfaces
    // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape, boolean filled) {
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color, filled));
        redraw();
    }

    /**
     * Erase a given shape's from the screen.
     *
     * @param referenceObject the shape object to be erased
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);   // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the shapes.Canvas.
     *
     * @param colorString the new colour for the foreground of the shapes.Canvas
     */
    public void setForegroundColor(String colorString) {
        switch (colorString) {
            case "red" -> graphic.setColor(Color.RED);
            case "blue" -> graphic.setColor(Color.BLUE);
            case "yellow" -> graphic.setColor(Color.YELLOW);
            case "green" -> graphic.setColor(Color.GREEN);
            case "magenta" -> graphic.setColor(Color.MAGENTA);
            case "white" -> graphic.setColor(Color.WHITE);
            case "cyan" -> graphic.setColor(Color.CYAN);
            case "darkGray" -> graphic.setColor(Color.DARK_GRAY);
            case "gray" -> graphic.setColor(Color.GRAY);
            case "lightGray" -> graphic.setColor(Color.LIGHT_GRAY);
            case "orange" -> graphic.setColor(Color.ORANGE);
            case "pink" -> graphic.setColor(Color.PINK);
            default -> graphic.setColor(Color.BLACK);
        }
    }


    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     *
     * @param milliseconds the number
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // ignoring exception at the moment
        }
    }

    /**
     * Redraw all shapes currently on the shapes.Canvas.
     */
    private void redraw() {
        erase();
        for (Object object : objects) {
            shapes.get(object).draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }


    /********
     * Inner class CanvasPane - the actual canvas component contained in the
     * shapes.Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /********
     * Inner class CanvasPane - the actual canvas component contained in the
     * shapes.Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class ShapeDescription {
        private final Shape shape;
        private final String colorString;
        private final boolean filled;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
            this.filled = true;
        }

        public ShapeDescription(Shape shape, String color, boolean filled) {
            this.shape = shape;
            colorString = color;
            this.filled = filled;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            if (filled)
                graphic.fill(shape);
        }
    }

}