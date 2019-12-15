package weapon;

import display.StdDraw;
import display.Vector2;

public class LaserGun extends Weapon {

    private class LaserShot extends Projectile{

        protected double    startX;//the X position of the start
        protected double    startY;//the Y position of the start

        protected LaserShot(Vector2<Double> pos, Vector2<Double> dir) {
            super(0.005, 0);
            this.x = pos.getX();
            this.y = pos.getY();
            this.startX = x;
            this.startY = y;
            this.cSpeed = 0.50;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.damage = shotDamage;
            this.color = StdDraw.CYAN;
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


    }

    protected int maxDamage;//the maximum damage the laser can do

    public LaserGun() {
        name = "Laser";
        chargeTime = 1;
        shotDamage = 0;
        shotPerCharge = 1;
        requiredPower = 1;
        maxDamage = 4;
    }

    /**
     * Shots a dummy laser
     * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
     */
    @Override
    public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
        return new LaserShot(pos, dir);
    }

    public int getMaxDamage() {return this.maxDamage;}
}
