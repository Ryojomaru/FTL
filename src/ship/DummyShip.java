package ship;
import display.Vector2;
import module.Module;
import module.Reactor;
import module.WeaponControl;
import weapon.*;

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
		
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), getNextTilePosition(), isPlayer, 5, 4);
		addTile(weaponControl);
		
		Tile back = new Tile(getNextTilePosition(), isPlayer);
		addTile(back);
		
		// Assigns the modules
		modules[0] = reactor;
		modules[1] = weaponControl;
		
		// Creates the gun of the ship
		Weapon w = new DummyGun();
		Weapon w2 = new LaserGun();
		Weapon w3 = new MissileLauncher();
		Weapon w4 = new IonCannon();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w2);
		weaponControl.addWeapon(w);
		weaponControl.addWeapon(w4);
		weaponControl.addWeapon(w3);



		
		// Places the weapon at the front
		front.setWeapon(w);
		back.setWeapon(w2);
		mid.setWeapon(w3);
		weaponControl.setWeapon(w4);
		
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