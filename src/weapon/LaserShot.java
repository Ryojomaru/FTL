package weapon;

import java.awt.Color;
import display.StdDraw;
import display.Vector2;

public abstract class LaserShot extends Projectile{

    protected double    startX;//the X position of the start
    protected double    startY;//the Y position of the start

    protected LaserShot(double width) {
        super(width, 0);
    }

    /**
     * Draws the laser.
     */
    @Override
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.setPenRadius(width);
        StdDraw.line(startX, startY, x, y);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
    }

    /**
     * Gives the number of damage the laser should do depending on the charges.
     * Just checks that's the number of charges used is between 1 and the max included.
     * @param max the maximum damage it can do
     * @param charges
     * @return
     */
    int calculateDamageFromCharges(int charges, int max) {
        if(charges < 1) return 1;
        if(charges > max) return 4;
        else return charges;
    }


}
