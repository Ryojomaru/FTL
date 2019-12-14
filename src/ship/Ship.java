package ship;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import display.StdDraw;
import display.Vector2;
import main.World;
import module.*;
import weapon.Projectile;
import weapon.Weapon;

/**
 * A ship is the composed of modules, tiles and crew members.
 * The ship has a hull that can be damaged by an opponent's ship.
 */
public abstract class Ship {
	
	protected Vector2<Double>			position;		// The position of the ship
	protected int						totalHull;		// The total hull integrity of the ship
	protected int						currentHull;	// The current hull integrity of the ship
	
	protected Reactor					reactor;		// The reactor of the ship
	protected WeaponControl				weaponControl;	// The weapon control system
	protected Engine					engine;			// Engine system
	protected Shield					shield;			// Shield system

	protected Collection<CrewMember> 	crew;			// The crew members in the ship
	protected Collection<Tile>			layout;			// The layout of the ship
	protected boolean					isPlayer;		// Whether this ship is owned by the player
	protected Module[]					modules;		// The modules on the ship
	protected Collection<Projectile>	projectiles;	// The projectiles shot by the ship
	protected Tile						target;			// The targeted tile of the enemy ship
	protected CrewMember				selectedMember; // The currently selected crew member

	protected Tile 						selectedTile;	// The Tile where the currently selected crew memebr is
	protected DecimalFormat 			df = new DecimalFormat("#.##"); // To round integer to the right format
	/**
	 * Creates a Ship for the player or the opponent at the provided position.
	 * @param isPlayer whether the ship is for the player
	 * @param position the location to create it
	 */
	public Ship(boolean isPlayer, Vector2<Double> position) {
		this.isPlayer = isPlayer;
		this.position = position;
		crew = new ArrayList<CrewMember>();
		projectiles = new ArrayList<Projectile>();
		layout = new ArrayList<Tile>();
	}
	
	// Main Methods
	
	/**
	 * TODO
	 * Processes the action of the AI.
	 * @param player the enemy of the AI
	 */
	public void ai(Ship player) {
		Random r = new Random();
		boolean buffer = r.nextBoolean();
		if (isPlayer)

			if (shield.getAllocatedEnergy() == 0){
				shield.addEnergy();
			}
			if (engine.getAllocatedEnergy() == 0){
				shield.addEnergy();
			}
			if (weaponControl.getAllocatedEnergy() == 0){
				weaponControl.addEnergy();
				weaponControl.activeWeapon(1);
			}

			if (weaponControl.getAllocatedEnergy() > 0 && weaponControl.getWeapon(1).isCharged()){
				if (r.nextInt(100) > 98){
					if (buffer){
						aimLeft(player);
						shotWeapon(1);
					} else {
						aimDown(player);
						shotWeapon(1);
					}
				}
			}
	}
	
	/**
	 * Processes the time elapsed between the steps.
	 * @param elapsedTime the time elapsed since the last step
	 */
	public void step(double elapsedTime) {
		chargeWeapons(elapsedTime);
		processProjectiles(elapsedTime);
	}
	
	// Drawing Methods
	
	/**
	 * Draws the ship and all its components.
	 */
	public void draw() {
		drawTiles();
		for (Projectile p : projectiles)
			p.draw();
	}

	/**
	 * Draw the tiles of the ship.
	 */
	private void drawTiles() {
		for (Tile t : layout)
			t.draw();
	}

	/**
	 * Draws the HUD of the ship.
	 */
	public void drawHUD() {
		if (isPlayer)
			drawPlayerHUD();
		else
			drawOpponentHUD();
	}
	
	/**
	 * Draws the HUD of the player.
	 */
	private void drawPlayerHUD() {
		// Modules
		for (Module m : modules)
			m.drawHud();
		// Hull
		StdDraw.text(0.025, 0.97, "Hull");
		int j = currentHull;
		for (int i = 1; i <= totalHull; i++)
			if (j > 0) {
				StdDraw.filledRectangle(0.05+(i*0.015), 0.97, 0.005, 0.015);
				j--;
			} else
				StdDraw.rectangle(0.05+(i*0.015), 0.97, 0.005, 0.015);
		
	}
	
	/**
	 * Draws the HUD of the opponent.
	 */
	private void drawOpponentHUD() {
		int j = currentHull;
		for (int i = 1; i <= totalHull; i++)
			if (j > 0) {
				StdDraw.filledRectangle(0.67+(i*0.0075), 0.75, 0.0025, 0.0075);
				j--;
			} else
				StdDraw.rectangle(0.67+(i*0.0075), 0.75, 0.0025, 0.0075);
	}
	
	// Crew Methods
	
	/**
	 * Check if a crew member is currently selected.
	 * @return whether a crew member is selected
	 */
	public boolean isCrewMemberSelected() {
		return selectedMember != null;
	}

	/**
	 * Unselects the selected crew member.
	 */
	public void unselectCrewMember() {
		if (!isCrewMemberSelected())
			return;
		selectedMember.unselect();
		selectedMember = null;
	}

	/**
	 * Selects the crew member.
	 * @param i -th crew member
	 */
	public void selectMember(int i) {
		int j = 0;
		for (CrewMember m : crew)
			if (j++ == i) {
				selectedMember = m;
				selectedTile = getSelectedCrewTile(m);
				selectedMember.select();
				return;
			}
				
	}
	
	/**
	 * Adds a crew member to the ship.
	 * @param member the crew member to add
	 */
	public void addCrewMember(CrewMember member) {
		Tile t = getEmptyTile();
		if (t == null) {
			System.err.println("The ship is full ! Sorry " + member.getName() + "...");
			return;
		}
		crew.add(member);
		t.setCrewMember(member);
	}
	
	// Layout Methods
	
	/**
	 * Adds a tile to the ship.
	 * @param t the tile to add
	 */
	protected void addTile(Tile t) {
		layout.add(t);
	}
	
	/**
	 * Gives an empty tile of the ship
	 * @return a tile empty of crew member
	 */
	private Tile getEmptyTile() {
		Iterator<Tile> it = layout.iterator();
		while(it.hasNext()) {
			Tile t = it.next();
			if (!t.hasCrewMember())
				return t;
		}
		return null;
	}
	
	/**
	 * Gives the first tile of the ship.
	 * @return the first tile of the ship
	 */
	private Tile getFirstTile() {
		return layout.iterator().next();
	}

	private Tile getSelectedCrewTile(CrewMember m) {
		Iterator<Tile> it = layout.iterator();
		while (it.hasNext()) {
			Tile t = it.next();
			if(t.isCrewMember(m)) return t;
		}
		return null;
	}

	public Tile getNextUnselectedTile(Tile current){
	    Iterator<Tile> it = layout.iterator();
	    boolean a = false;
	    while (it.hasNext()){
	        Tile t = it.next();
			if (a){
				return t;
			}
	        if (t == current) a = true;
        }
	    return layout.iterator().next();
    }
	// Energy Methods

	/**
	 * Decreased the energy allocated in the module. 
	 * @param module the module to remove energy from
	 */
	public void removeEnergy(int module) {
		if (module >= modules.length)
			return;
		if (modules[module].removeEnergy())
			reactor.addEnergy();
	}
	
	/**
	 * Increases the energy allocated in the module.
	 * @param module the module to add energy to
	 */
	public void addEnergy(int module) {
		if (module >= modules.length)
			return;
		if (reactor.getAllocatedEnergy() > 0 && modules[module].addEnergy()) 
			reactor.removeEnergy();
		
	}
	
	// Weapon Methods
	
	/**
	 * Deactivates a weapon. 
	 * @param weapon the weapon to deactivate
	 */
	public void deactiveWeapon(int weapon) {
		weaponControl.deactiveWeapon(weapon);
	}
	
	/**
	 * Activates a weapon. 
	 * @param weapon the weapon to activate
	 */
	public void activeWeapon(int weapon) {
		weaponControl.activeWeapon(weapon);
	}
	
	/**
	 * Charges the weapons by the time provided
	 * @param time the time to charge the weapons by
	 */
	public void chargeWeapons(double time) {
		weaponControl.chargeTime(time);
	}

	/**
	 * Shots a weapon.
	 * @param weapon the weapon to shot
	 */
	public void shotWeapon(int weapon) {
	    Projectile p = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)),
				new Vector2<Double>(
						target.getCenterPosition().getX() -
								getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getX(),
						target.getCenterPosition().getY() -
								getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getY()));
		if (p != null)
			projectiles.add(p);
	}

	public void missShotWeapon(int weapon){
		Random r = new Random();
		if (r.nextBoolean()){
			Projectile p = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)),
					new Vector2<Double>(
							target.getCenterPosition().getX() -
									getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getX() * 0.5,
							target.getCenterPosition().getY() -
									getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getY() * 0.5));
			if (p != null)
				projectiles.add(p);
		} else {
			Projectile p = weaponControl.shotWeapon(weapon, getWeaponTile(weaponControl.getWeapon(weapon)),
					new Vector2<Double>(
							target.getCenterPosition().getX() -
									getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getX() * 2,
							target.getCenterPosition().getY() -
									getWeaponTile(weaponControl.getWeapon(weapon)).getWeaponPosition().getY() * 2));
			if (p != null)
				projectiles.add(p);
		}
	}
	/**
	 * Gives the tile on which the weapon is.
	 * @param w the weapon to get the tile from
	 * @return the tile on which the weapon is attached
	 */
	private Tile getWeaponTile(Weapon w) {
		for (Tile t : layout)
			if (t.getWeapon() == w)
				return t;
		return null;
	}

	public boolean isWeaponShotCorrectly(Ship opponent){
		Random r = new Random();
		return r.nextInt(100) >= opponent.getEngine().getDodge();
	}
	// Projectile Methods
	
	/**
	 * Processes the projectiles with the time elapsed since
	 * the last processing.
	 * @param elapsedTime the time elapsed since the last call
	 */
	private void processProjectiles(double elapsedTime) {
		for (Projectile p : projectiles){
			p.step(elapsedTime);
		}
	}
	
	/**
	 * Gives the projectiles shot by the ship.
	 * @return A collection of projectiles
	 */
	public Collection<Projectile> getProjectiles(){
		return projectiles;
	}

	/**
	 * Applies the damage a projectile did.
	 * @param p the projectile to process
	 */
	public void applyDamage(Projectile p) {
		if (this.currentHull - p.getDamage() < 0) this.currentHull = 0;
		else this.currentHull -= p.getDamage();
	}
	
	// Aiming Methods
	
	/**
	 * Aims the guns up.
	 * @param opponent the ship to aim at
	 */
	public void aimUp(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		Tile nextTile = new Tile(new Vector2<>(target.getPosition().getX(),target.getPosition().getY()
				+ 0.02), false);
		changeAim(opponent, nextTile);
	}
	
	/**
	 * Aims the guns down.
	 * @param opponent the ship to aim at
	 */
	public void aimDown(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		Tile nextTile = new Tile(new Vector2<>(target.getPosition().getX(),target.getPosition().getY()
				- 0.02), false);
		changeAim(opponent, nextTile);
	}
	
	/**
	 * Aims the guns left.
	 * @param opponent the ship to aim at
	 */
	public void aimLeft(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		Tile nextTile = new Tile(new Vector2<>(target.getPosition().getX() - 0.02,
				target.getPosition().getY()), false);
		changeAim(opponent, nextTile);
	}

	/**
	 * Aims the guns right.
	 * @param opponent the ship to aim at
	 */
	public void aimRight(Ship opponent) {
		if (target == null) {
			target = opponent.getFirstTile();
			target.markTarget();
			return;
		}
		Tile nextTile = new Tile(new Vector2<>(target.getPosition().getX() + 0.02,
				target.getPosition().getY() + 0.01), false);
		changeAim(opponent, nextTile);
	}

	/**
	 * Test if nextTile is in opponent layout, if true change target to nextTile
	 * @param opponent the ship to aim at
	 * @param nextTile the tile to test
	 */
	private void changeAim(Ship opponent, Tile nextTile){
		for (Tile t: opponent.layout) {
			if (df.format(t.getPosition().getX()).equals(df.format(nextTile.getPosition().getX())) &&
					df.format(t.getPosition().getY()).equals(df.format(nextTile.getPosition().getY()))){
				target.unmarkTarget();
				target = t;
				t.markTarget();
			}
		}
	}

	public Collection<Tile> getLayout() {
		return layout;
	}

    public CrewMember getSelectedMember() {
        return selectedMember;
    }

	public int getCurrentHull() {
		return currentHull;
	}

	public Tile getSelectedTile() {
		return selectedTile;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setProjectiles(Collection<Projectile> projectiles) {
		this.projectiles = projectiles;
	}
}
