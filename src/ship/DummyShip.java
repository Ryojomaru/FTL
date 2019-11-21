package ship;
import display.Vector2;
import module.Module;
import module.Reactor;
import module.WeaponControl;
import weapon.DummyGun;
import weapon.Weapon;

public class DummyShip extends Ship {
	
	public DummyShip(boolean isPlayer, Vector2<Double> position) {
		// Creates the ship
		super(isPlayer, position);
		
		// Sets the characteristics of the ship.
		totalHull 		= 30;
		currentHull		= 30;
		modules = new Module[2];
		
		// Creates the tiles for the layout of the ship
		Tile front = new Tile(getNextTilePosition(), isPlayer);
		addTile(front);
		
		reactor = new Reactor(new Vector2<Double>(0.025, 0.015), getNextTilePosition(),isPlayer, 8);
		addTile(reactor);
		
		Tile mid = new Tile(getNextTilePosition(), isPlayer);
		addTile(mid);
		
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), getNextTilePosition(), isPlayer, 1, 4);
		addTile(weaponControl);
		
		Tile back = new Tile(getNextTilePosition(), isPlayer);
		addTile(back);
		
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		
		// Creates the gun of the ship
		Weapon w = new DummyGun();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w);
		
		// Places the weapon at the front
		front.setWeapon(w);
		
		// Adds a crew member to the ship
		addCrewMember(new CrewMember("Jose"));
	}
	
	private Vector2<Double> getNextTilePosition() {
		if (isPlayer)
			return new Vector2<Double>(position.getX()-(layout.size()*0.02), position.getY()); 
		else
			return new Vector2<Double>(position.getX(), position.getY()-(layout.size()*0.02));
	}

}