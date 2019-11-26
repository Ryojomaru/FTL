package weapon;

import display.StdDraw;
import display.Vector2;

public class DummyLaserGun extends Weapon {
    private class DummyLaserShot extends LaserShot{
        public DummyLaserShot(Vector2<Double> pos, Vector2<Double> dir) {
            super(0.005);
            this.x = pos.getX();
            this.y = pos.getY();
            this.startX = x;
            this.startY = y;
            this.cSpeed = 0.6;
            this.xSpeed = dir.getX()*cSpeed;
            this.ySpeed = dir.getY()*cSpeed;
            this.damage = shotDamage;
            this.color = StdDraw.CYAN;
        }


    }

    public DummyLaserGun() {
        name = "DummyLaserGun";
        chargeTime = 5;
        shotDamage = 4; //TODO: change so it depends on the number of charges used
        shotPerCharge = 1;
        requiredPower = 1;
    }

    /**
     * Shots a dummy laser
     * @see weapon.Weapon#shot(display.Vector2, display.Vector2)
     */
    @Override
    public Projectile shot(Vector2<Double> pos, Vector2<Double> dir) {
        /**DummyLaserShot dls = new DummyLaserShot();
        dls.setPosition(pos.getX(),pos.getY());
        dls.setTargetY(dir.getX()*1000, dir.getY()*1000);
        return dls;*/
        return new DummyLaserShot(pos, dir);
    }
}
