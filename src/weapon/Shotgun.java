package weapon;

import display.StdDraw;
import display.Vector2;

import java.util.Random;

public class Shotgun extends Weapon{
    /**
     * A shotgun projectile, very slow to move and recharge.
     * Does a fixed 3 damage.
     */
    private class ShotgunProjectile extends Projectile {
        private ShotgunProjectile(Vector2<Double> pos, Vector2<Double> dir) {
            super(0.03,0.03);
            this.setPosition(pos.getX(),pos.getY());
            this.cSpeed = 0.08;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.color = StdDraw.LIGHT_GRAY;
            this.damage = shotDamage;

        }
    }

    /**
     * Creates a dummy gun
     */
    public Shotgun() {
        name = "Shotgun";
        requiredPower = 1;
        chargeTime = 6;
        shotDamage = 3;
    }

    /**
     * Shots a shotgun projectile
     * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
     */
    @Override
    public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
        return new ShotgunProjectile(pos, dir);
    }

}
