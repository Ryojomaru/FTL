package module;

import display.Vector2;

public class Shield extends Module{
    /**
     * Construct a module owned by the player or the opponent.
     * The module's tile is drawn at the location given in tilePos.
     * The module's HUD is drawn at the location given in hudPos.
     *
     * @param hudPos   position at which to draw the HUD
     * @param tilePos  position at which to draw the tile
     * @param isPlayer whether it belongs to the player
     */
    public Shield(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
        super(hudPos, tilePos, isPlayer);
        name = "Shield";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = true;
    }
}
