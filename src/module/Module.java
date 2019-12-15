package module;
import display.StdDraw;
import display.Vector2;
import ship.Tile;

/**
 * A module is an implementation of Tile which handles energy.
 * This tile has a HUD to display its current energy level.
 *
 * Modules are slightly improved by crewmembers manning them :
 * Each Crewmember in the shield module makes the shield recharging time faster by 5%
 * Each Crewmember in the weapon control module makes the weapon charging time faster by 5%
 * Each Crewmember in the engine makes the dodging probability greater by 5%
 *
 * /!\ Please note one thing about how damage affects energy:
 * /!\ A module can use every energy allocated to it to the fulles,
 * /!\ as long as it doesn't surpass (currentLevel - amountDamage)
 * /!\For example, a level 4 module with 1 damage can still use 3 energy to the max
 */
public abstract class Module extends Tile {

	protected	String				name;				// Name of the module
	protected	int 				maxLevel;			// Maximum level of the module
	protected 	int 				currentLevel;		// Current level of the module
	protected 	int 				allocatedEnergy;	// Amount of energy allocated to the module
	protected 	int					amountDamage;		// Amount of damage done to the module
	protected  	boolean 			canBeManned; 		// Can a crew member man this module
	protected 	Vector2<Double> 	hudPos;				// HUD position of the module
	protected 	Double 				timeRepaired;		// time the module has been being repaired by crewmembers.
	protected 	boolean 			isActive;			//weather the module is active or not.
	protected 	Double 				deactivationTime;   //the time it'll be deactivated.
	protected 	Double				timeSinceDeactivation;//the time gone since it was deactivated.
	
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
		this.timeRepaired = 0.0; //default value
		this.isActive = true; //default value
	}
	
	/**
	 * Increments energy allocated to the module.
	 * @return whether the energy has been added or not
	 */
	public boolean addEnergy() {
		if (allocatedEnergy < currentLevel - amountDamage && isActive) {
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
		int k = amountDamage;
		for (int i = 1; i <= currentLevel; i++)
			if(k-- > 0) {
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

	/**
	 * does damage to the module
	 * @param dmg the damage to be done
	 */
	public void damage(int dmg) {
		this.amountDamage = Math.min(currentLevel,this.amountDamage+dmg);
		while(this.allocatedEnergy > this.getUsableEnergy()) {
			this.removeEnergy();
		}
		if(this.getName() != null) {
			System.out.println("The " +this.getName()+ " was hit !");
		}
	}

	/**
	 * Repairs the module for the given amount of time.
	 * @param time the amount of time to repair the module for
	 */
	public void repair(Double time) {
		this.timeRepaired += time;
		if(this.timeRepaired >= (2.0 / ((double) this.nbCrewMembers()))) {
			this.amountDamage--;
			this.timeRepaired = 0.0;
			if(this.getName() != null) {
				System.out.println(this.getName() + " was repaired by 1 !");
			}
		}
	}

	/**
	 * deactivates the module for a certain time
	 * @param time the amount of time to deactivate it.
	 */
	public void deactivate(Double time) {
		for(int i = 0; i < getAllocatedEnergy(); i++) {
			this.removeEnergy();
		}
		isActive = false;
		deactivationTime = time;
		timeSinceDeactivation = 0.0;
	}

	/**
	 * advances module reactivation by given time
	 * @param time advancement in reactivation
	 */
	public void reactivate(Double time) {
		if(!isActive){
			timeSinceDeactivation+=time;
			if(timeSinceDeactivation>=deactivationTime) {
				isActive = true;
				deactivationTime = 0.0;
				timeSinceDeactivation = 0.0;
			}
		}
	}

	/**
	 * levels up by one.
	 */
	public void levelUp() {
		if (this.getCurrentLevel() < this.maxLevel) this.currentLevel++;
	}

	/////////////
	// Getters //
	/////////////
	
	public int 		getMaxLevel() 			{ return maxLevel;			}
	public int 		getCurrentLevel()		{ return currentLevel; 		}
	public int		getAllocatedEnergy()	{ return allocatedEnergy;	}
	public int		getAmountDamage()		{ return amountDamage;		}
	public boolean 	getCanBeManned() 		{ return canBeManned; 		}

	/**
	 * Returns the amount of energy currently usable.
	 * Takes into account the amount of energy allocated, the maximal possible power and the damage taken.
	 * @return the energy th module can use.
	 */
	public int 		getUsableEnergy()		{ return Math.min(this.getAllocatedEnergy(),
														this.currentLevel- this.amountDamage);}
	
}
