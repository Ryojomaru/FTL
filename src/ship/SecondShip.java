package ship;
import display.Vector2;
import module.*;
import module.Module;
import weapon.*;

/**
 * Another, U-shaped ship for ennemies only.
 * Less resistant but more offensive than the base ship.
 */

public class SecondShip extends Ship {

    public SecondShip(boolean isPlayer, Vector2<Double> position) {
        // Creates the ship
        super(isPlayer, position);

        // Sets the characteristics of the ship.
        totalHull 		= 20;
        currentHull		= 20;
        modules = new Module[4];

        // Creates the tiles for the layout of the ship
        Tile left = new Tile(new Vector2<Double>(position.getX(), position.getY()), isPlayer);
        addTile(left);

        reactor = new Reactor(new Vector2<Double>(0.025, 0.015), new Vector2<Double>(position.getX(), position.getY()-0.02),isPlayer, 6);
        addTile(reactor);

        engine = new Engine(new Vector2<Double>(0.7,0.015), new Vector2<Double>(position.getX()+0.02, position.getY()-0.02), isPlayer,1);
        addTile(engine);

        weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), new Vector2<Double>(position.getX()+0.04, position.getY()-0.02), isPlayer, 3, 4);
        addTile(weaponControl);

        shield = new ShieldModule(new Vector2<Double>(0.6,0.015), new Vector2<Double>(position.getX()+0.06, position.getY()-0.02), isPlayer,2);
        addTile(shield);

        Tile right = new Tile(new Vector2<Double>(position.getX()+0.06, position.getY()), isPlayer);
        addTile(right);

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
        left.setWeapon(w2);
        right.setWeapon(w3);
        weaponControl.setWeapon(w4);

        // Adds a crew member to the ship
        addCrewMember(new CrewMember("Jose"));
        addCrewMember(new CrewMember("Mr Cheminade"));
    }


}
