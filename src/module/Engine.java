package module;

import display.Vector2;

public class Engine extends Module {

    /**
     * Construct a module owned by the player or the opponent.
     * The module's tile is drawn at the location given in tilePos.
     * The module's HUD is drawn at the location given in hudPos.
     *
     * @param hudPos   position at which to draw the HUD
     * @param tilePos  position at which to draw the tile
     * @param isPlayer whether it belongs to the player
     * @param initialLevel initial amount of energywhich can be allocated
     */
    public Engine(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer, int initialLevel) {
        super(hudPos, tilePos, isPlayer);
        name = "Engine";
        maxLevel = 8;
        currentLevel = initialLevel;
        allocatedEnergy = 0;
        amountDamage = 0;
        canBeManned = true;
    }

    /**
     * gives the dodging percentage
     * Improved by crewmembers
     * @return the dodging percentage (ex, 15 means 15% chance to dodge)
     */
    public int getDodge() {
        return ((5 * (this.getUsableEnergy() + nbCrewMembers())));
    }
}
