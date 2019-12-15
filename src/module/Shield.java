package module;

import display.StdDraw;
import display.Vector2;

/**
 * A shield is a circle protecting the ship from incoming shots.
 * It is defined by its position and radius.
 * it can have two levels of improvement, which will change its reactivation time after getting hit.
 */
public class Shield {
    private Vector2<Double> pos; //the position of the center of the shield
    private Double radius; //the radius of the shield
    private boolean isUpgraded; //weather it has been upgraded or not
    private boolean isActive; //weather it is currently active or not
    private Double timeSinceDeactivated; //the time in ms since it has been deactivated
    private Double ionTime;

    /**
     * Creates a shield around the position given, with a given radius
     * @param pos the center of the shield
     * @param radius the radius of it
     */
    public Shield(Vector2<Double> pos, Double radius) {
        this.pos = pos;
        this.radius = radius;
        this.isUpgraded = false; //by default, it is not upgraded
        this.isActive = true; //is active by default
        this.timeSinceDeactivated = 0.0; //default value
        this.ionTime = 0.0;
    }

    public Vector2<Double> getPos() {
        return pos;
    }

    public void setPos(Vector2<Double> pos) {
        this.pos = pos;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public boolean isUpgraded() {
        return isUpgraded;
    }

    public void upgrade() {
        isUpgraded = true;
    }

    public void downgrade() {
        isUpgraded = false;
    }

    /**
     * Draws the shield.
     * The colour is determined based on upgrade level (for visual clarity)
     */
    public void draw(){
        if (this.isUpgraded()) {
            StdDraw.setPenColor(StdDraw.ORANGE);
        } else StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.002);
        StdDraw.circle(this.pos.getX(), this.pos.getY(), this.radius);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
    }

    /**
     * Checks if a given position is in the radius of the shield
     * @param pos the position to check
     * @return weather or not the projectile is in radius.
     */
    public boolean isInRadius(Vector2<Double> pos) {
        Double distance = Math.sqrt( //Pythagorean theorem
                ((Math.abs(pos.getX()-this.pos.getX()))*(Math.abs(pos.getX()-this.pos.getX())))
                        + ((Math.abs(pos.getY()-this.pos.getY()))*(Math.abs(pos.getY()-this.pos.getY()))));
        Double margin = radius - 0.007; //margin of error, to prevent fast projectile to go through shields.
        return (distance <= radius && distance >= margin);
    }

    /**
     * Deactivates a Shield
     */
    public void deactivate() {
        this.isActive = false;
        this.ionTime = 0.0;//reputs this to 0 as a safety
        this.timeSinceDeactivated = 0.0;
    }

    public void deactivateByIon(Double time) {
        this.isActive = false;
        this.ionTime = time;
        this.timeSinceDeactivated = 0.0;
    }

    /**
     * Activates a shield
     */
    public void activate() {
        this.isActive = true;
        this.timeSinceDeactivated = 0.0;
        this.ionTime = 0.0;
    }

    public boolean isActive() {return this.isActive;}

    /**
     * checks if the shield has been recharged enough, depending on weather it ha been upgraded or not
     * If it has been deactivated by Ion for less than its normal time,
     * it will be deactivated for the normal amount of time (2/1.5s)
     * @return if the shield has been recharged
     */
    public boolean isRecharged() {
        return (this.timeSinceDeactivated > ionTime &&
                (this.timeSinceDeactivated > 2.0 || (this.timeSinceDeactivated > 1.5 && this.isUpgraded())));
    }

    /**
     * recharges a shield by the amount of time (in s)
     * @param time the time to recharge the shield by
     */
    public void recharge(Double time) {
        this.timeSinceDeactivated += time;
    }

}
