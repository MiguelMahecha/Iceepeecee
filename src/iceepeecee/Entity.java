package iceepeecee;

import shapes.Canvas;

/**
 * Any class that can draw itself in the Iceepeecee's map extends Drawable
 */
public abstract class Entity {
    protected String color;
    protected boolean isVisible;

    /**
     * Get the Entity's color
     * @return The Entity's color
     */
    String getColor() {
        return color;
    }

    /**
     * Draw the Entity to the canvas
     */
    abstract protected void draw();

    /**
     * Erase the Entity from the canvas
     */
    protected void erase() {
        Canvas.getCanvas().erase(this);
    }

    /**
     * Make the Entity visible in the canvas
     */
    void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Make the Entity invisible in the canvas
     */
    void makeInvisible() {
        isVisible = false;
        erase();
    }
}
