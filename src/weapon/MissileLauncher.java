package weapon;

import display.StdDraw;
import display.Vector2;

public class MissileLauncher extends Weapon {

    public class Missile extends Projectile {

        /**
         * Creates a Missile of the provided dimensions.
         *
         * @param pos of the projectile
         * @param dir of the missile once shot
         */
        protected Missile(Vector2<Double> pos, Vector2<Double> dir) {
            super(0.02, 0.015);
            this.x = pos.getX();
            this.y = pos.getY();
            this.cSpeed = 0.15;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.color = StdDraw.MAGENTA;
            this.damage = shotDamage;
        }

        /**
         * Draws the missile.
         */
        @Override
        public void draw() {
            StdDraw.setPenColor(color);
            StdDraw.filledEllipse(x, y, width/2, height/2);
            StdDraw.setPenColor(StdDraw.BLACK);
        }
    }

    protected int maxCapacity; //the maximum number of missiles that can be stocked
    protected int missilesLeft; // the amount of missiles left to shoot.

    public MissileLauncher() {
        name = "Missile";
        chargeTime = 5;
        shotDamage = 4;
        shotPerCharge = 1;
        requiredPower = 1;
        missilesLeft = 3;
    }

    public int getMissilesLeft() {
        return missilesLeft;
    }

    @Override
    public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
        if(missilesLeft > 0) {
            missilesLeft--;
            return new Missile(pos, dir);
        } else return null;
    }

    public void addMissile() {missilesLeft++;}
}
