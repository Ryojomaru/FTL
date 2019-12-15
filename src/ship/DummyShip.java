package ship;
import display.Vector2;
import module.*;
import module.Module;
import weapon.*;

public class DummyShip extends Ship {
	
	public DummyShip(boolean isPlayer, Vector2<Double> position) {
		// Creates the ship
		super(isPlayer, position);
		
		// Sets the characteristics of the ship.
		totalHull 		= 30;
		currentHull		= 30;
		modules = new Module[4];
		
		// Creates the tiles for the layout of the ship
		Tile front = new Tile(getNextTilePosition(), isPlayer);
		addTile(front);
		
		reactor = new Reactor(new Vector2<Double>(0.025, 0.015), getNextTilePosition(),isPlayer, 3);
		addTile(reactor);
		
		engine = new Engine(new Vector2<Double>(0.7,0.015), getNextTilePosition(), isPlayer,1);
		addTile(engine);
		
		weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), getNextTilePosition(), isPlayer, 4, 4);
		addTile(weaponControl);
		
		shield = new ShieldModule(new Vector2<Double>(0.6,0.015), getNextTilePosition(), isPlayer,1);
		addTile(shield);
		
		// Assigns the modules

		modules[0] = reactor;
		modules[1] = weaponControl;
		modules[2] = engine;
		modules[3] = shield;
		
		// Creates the gun of the ship
		Weapon w2 = new LaserGun();
		Weapon w3 = new MissileLauncher();
		Weapon w4 = new IonCannon();
		// Assigns the gun to the weapon control
		weaponControl.addWeapon(w2);
		weaponControl.addWeapon(w4);
		weaponControl.addWeapon(w3);



		
		// Places the weapon at the front
		front.setWeapon(w2);
		shield.setWeapon(w3);
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