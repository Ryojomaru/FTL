package weapon;

import display.StdDraw;
import display.Vector2;

/**
 * A dummy gun is an example of a gun which shots dummy pojectiles
 */
public class IonCannon extends Weapon {

    /**
     * A dummy projectile is an example of a projectile
     */
    private class IonProjectile extends Projectile {

        public IonProjectile(Vector2<Double> pos, Vector2<Double> dir) {
            super(0.03, 0.003);
            this.x = pos.getX();
            this.y = pos.getY();
            this.cSpeed = 1.2;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.color = StdDraw.RED;
            this.damage = shotDamage;
        }
    }

    protected int maxDeactivationTime; //the maximum amount of time (in s) the ion can deactivate a module or shield

    public void setDeactivationTime(int deactivationTime) {
        this.deactivationTime = deactivationTime;
    }

    protected int deactivationTime; //the actual amount of time (in s) the ion can deactivate a module or shield

    public int getMaxDeactivationTime() {
        return maxDeactivationTime;
    }

    /**
     * Creates a Ion Cannon
     */
    public IonCannon() {
        name = "Ion";
        requiredPower = 1;
        chargeTime = 1;
        shotDamage = 0;
        shotPerCharge = 1;
        deactivationTime = 0;
        maxDeactivationTime = 4;
    }

    /**
     * Shots a dummy projectile
     * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
     */
    @Override
    public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
        return new IonProjectile(pos, dir);
    }


}