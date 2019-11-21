package module;
import display.StdDraw;
import display.Vector2;
import ship.Tile;

/**
 * A module is an implementation of Tile which handles energy.
 * This tile has a HUD to display its current energy level.
 */
public abstract class Module extends Tile {

	protected	String				name;				// Name of the module
	protected	int 				maxLevel;			// Maximum level of the module
	protected 	int 				currentLevel;		// Current level of the module
	protected 	int 				allocatedEnergy;	// Amount of energy allocated to the module
	protected 	int					amountDamage;		// Amount of damage done to the module
	protected  	boolean 			canBeManned; 		// Can a crew member man this module
	protected 	Vector2<Double> 	hudPos;				// HUD position of the module
	
	/**
	 * Construct a module owned by the player or the opponent.
	 * The module's tile is drawn at the location given in tilePos.
	 * The module's HUD is drawn at the location given in hudPos.
	 * @param hudPos position at which to draw the HUD
	 * @param tilePos position at which to draw the tile
	 * @param isPlayer whether it belongs to the player
	 */
	public Module(Vector2<Double> hudPos, Vector2<Double> tilePos, boolean isPlayer) {
		super(tilePos, isPlayer);
		this.hudPos = hudPos;
	}
	
	/**
	 * Increments energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean addEnergy() {
		if (allocatedEnergy < currentLevel - amountDamage) {
			++allocatedEnergy;
			return true;
		}
		return false;
	}
	
	/**
	 * Decrements energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean removeEnergy() {
		if (allocatedEnergy > 0) {
			--allocatedEnergy;
			return true;
		}
		return false;
	}
	
	// Draw Methods
	
	/**
	 * Draw the module's tile. 
	 */
	@Override
	public void draw() {
		super.draw();	
		if (name != null && name.length() > 0)
			StdDraw.text(tilePos.getX()-0.01, tilePos.getY()-0.01, ""+name.charAt(0));
	}
	
	/**
	 * Draw the module's HUD.
	 */
	public void drawHud() {
		double x = hudPos.getX();
		double y = hudPos.getY();
		if (getName() != null)
			StdDraw.text(x, y, getName());
		int j = allocatedEnergy;
		int k = currentLevel - amountDamage;
		for (int i = 1; i <= currentLevel; i++)
			if(k-- <= 0) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.filledRectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
				StdDraw.setPenColor(StdDraw.BLACK);
			} else if (j-- > 0) {
				StdDraw.filledRectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
			} else
				StdDraw.rectangle(x, (y+0.01)+(i*0.015), 0.015, 0.005);
	}

	/**
	 * Gives the name of the module.
	 * @return name of the module
	 */
	public String getName() {
		return name;
	}

	/////////////
	// Getters //
	/////////////
	
	public int 		getMaxLevel() 			{ return maxLevel;			}
	public int 		getCurrentLevel()		{ return currentLevel; 		}
	public int		getAllocatedEnergy()	{ return allocatedEnergy;	}
	public int		getAmountDamage()		{ return amountDamage;		}
	public boolean 	getCanBeManned() 		{ return canBeManned; 		}
	
}
