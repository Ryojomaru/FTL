package module;

import display.StdDraw;
import display.Vector2;

public class ShieldModule extends Module{
    /**
     * Construct a module owned by the player or the opponent.
     * The module's tile is drawn at the location given in tilePos.
     * The module's HUD is drawn at the location given in hudPos.
     *
     * @param hudPos   position at which to draw the HUD
     * @param tilePos  position at which to draw the tile
     * @param isPlayer whether it belongs to the player
     */
    public ShieldModule(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
        super(hudPos, tilePos, isPlayer);
        name = "Shield Module";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = true;
    }

    @Override
    public void drawHud(){
        super.drawHud();
        Double x = tilePos.getX();
        Double y = tilePos.getY();
        if(allocatedEnergy > 0) {
            StdDraw.setPenColor(StdDraw.GRAY);
            StdDraw.setPenRadius(0.002);
            StdDraw.circle(x, y, 0.03);
            StdDraw.setPenRadius();
            StdDraw.setPenColor();
        }
    }
}
