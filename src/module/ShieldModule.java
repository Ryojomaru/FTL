package module;

import display.StdDraw;
import display.Vector2;

import java.util.ArrayList;

public class ShieldModule extends Module{

    private ArrayList<Shield> shields;
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
        shields = new ArrayList<Shield>();
    }

    /**
     * Increments energy allocated to the module.
     * Either adds a new shield or upgrades the outer one.
     * @return whether the energy has been added or not
     */
    @Override
    public boolean addEnergy() {
        if (allocatedEnergy < currentLevel - amountDamage && isActive) {
            int currentEnergy = this.getUsableEnergy();
            ++allocatedEnergy;
            if(currentEnergy != this.getUsableEnergy()) {
                if (this.allocatedEnergy % 2 == 1) addShield();
                else this.shields.get(this.shields.size() - 1).upgrade();
            }
            return true;
        }
        return false;
    }

    /**
     * Decrements energy allocated to the module.
     * Either removes the outer shield or downgrades it, depending on energy left.
     * @return whether the energy has been added or not
     */
    @Override
    public boolean removeEnergy() {
        if (allocatedEnergy > 0) {
            int currentEnergy = this.getUsableEnergy();
            --allocatedEnergy;
            if(currentEnergy != this.getUsableEnergy()) {
                if (this.allocatedEnergy % 2 == 0) removeShield();
                else this.shields.get(this.shields.size() - 1).downgrade();
            }
            return true;
        }
        return false;
    }

    /**
     * removes the outermost shield.
     */
    public void removeShield() {
        this.shields.remove(this.shields.size() - 1);
    }

    /**
     * adds a new outer shield
     */
    public void addShield() {
        this.shields.add(new Shield(this.getCenterPosition(),0.15 + (0.005*this.shields.size())));
    }

    /**
     * Deactivates the shield with the given index
     * Also deactivates (as a safety measure) all shields outside of the indexed shield.
     * Resets all deactivation times from outer shields.
     * @param s the innermost shield to deactivate
     */
    public void deactivateShield(Shield s) {
        int index = this.shields.indexOf(s);
        for(int i = index; i < this.shields.size(); i++) { //deactivates the outer ones normally if for some reason they aren't already
            if(this.shields.get(i).isActive()) {this.shields.get(i).deactivate();}
        }
    }

    /**
     * Deactivates the single shield by the amount of time decided by the Ion
     * @param s the shield to dactivate
     * @param time the time of the ion deactivation
     */
    public void deactivateShieldByIon(Shield s, Double time) {
        int index = this.shields.indexOf(s);
        this.shields.get(index).deactivateByIon(time);
        for(int i = index+1; i < this.shields.size(); i++) { //deactivates the outer ones normally if for some reason they aren't already
            if(this.shields.get(i).isActive()) {this.shields.get(i).deactivate();}
        }
    }

    public ArrayList<Shield> getShields() {
        return shields;
    }

    /**
     * Draws the active shields of the module.
     */
    public void drawShields(){
        for(Shield s : this.shields) {
            if (s.isActive()) {s.draw();}
        }
    }

    /**
     * returns the index in the list of the innermost deactivated shield.
     * @return the index of the shield, or -1 if all shields are active
     */
    public int getFirstDeactivated() {
        for(int i = 0; i < this.shields.size(); i++) {
            if(!this.shields.get(i).isActive()) {return i;}
        }

        return -1;
    }

    /**
     * recharges Shields by the given time
     * Boosted by crewmembers.
     * @param time the time to recharge by
     */
    public void rechargeShields(double time) {
        int f = this.getFirstDeactivated();
        if(f != -1) {
            this.shields.get(f).recharge(time);
            if(this.shields.get(f).isRecharged()) {
                this.shields.get(f).activate();
            }
        }
    }
}
