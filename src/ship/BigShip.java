package ship;
import display.Vector2;
import module.*;
import module.Module;
import weapon.*;

/**
 * An almost unkillable ship using a shotgun for ennemies only.
 */
public class BigShip extends Ship {

    public BigShip(boolean isPlayer, Vector2<Double> position) {
        // Creates the ship
        super(isPlayer, position);

        // Sets the characteristics of the ship.
        totalHull 		= 40;
        currentHull		= 40;
        modules = new Module[4];

        // Creates the tiles for the layout of the ship
        Tile front = new Tile(new Vector2<Double>(position.getX(), position.getY()+0.02), isPlayer);
        addTile(front);
        Tile frontLeft = new Tile(new Vector2<Double>(position.getX()-0.02, position.getY()+0.02), isPlayer);
        addTile(frontLeft);
        Tile frontRight = new Tile(new Vector2<Double>(position.getX()+0.02, position.getY()+0.02), isPlayer);
        addTile(frontRight);
        Tile backLeft = new Tile(new Vector2<Double>(position.getX()-0.02, position.getY()-0.02), isPlayer);
        addTile(front);
        Tile backRight = new Tile(new Vector2<Double>(position.getX()+0.02, position.getY()-0.02), isPlayer);
        addTile(front);


        reactor = new Reactor(new Vector2<Double>(0.025, 0.015), new Vector2<Double>(position.getX(), position.getY()-0.02),isPlayer, 16);
        addTile(reactor);

        engine = new Engine(new Vector2<Double>(0.7,0.015), new Vector2<Double>(position.getX()+0.02, position.getY()), isPlayer,1);
        addTile(engine);

        weaponControl = new WeaponControl(new Vector2<Double>(0.08, 0.015), new Vector2<Double>(position.getX()-0.02, position.getY()), isPlayer, 4, 4);
        addTile(weaponControl);

        shield = new ShieldModule(new Vector2<Double>(0.6,0.015), new Vector2<Double>(position.getX(), position.getY()), isPlayer,5);
        addTile(shield);

        // Assigns the modules

        modules[0] = reactor;
        modules[1] = weaponControl;
        modules[2] = engine;
        modules[3] = shield;

        // Creates the gun of the ship
        Weapon w1 = new Shotgun();
        Weapon w2 = new LaserGun();
        Weapon w3 = new MissileLauncher();
        Weapon w4 = new IonCannon();
        // Assigns the gun to the weapon control
        weaponControl.addWeapon(w1);
        weaponControl.addWeapon(w2);
        weaponControl.addWeapon(w4);
        weaponControl.addWeapon(w3);




        // Places the weapon at the front
        front.setWeapon(w1);
        frontLeft.setWeapon(w2);
        shield.setWeapon(w3);
        frontRight.setWeapon(w4);

        // Adds a crew member to the ship
        addCrewMember(new CrewMember("RIRI"));
        addCrewMember(new CrewMember("FIFI"));
        addCrewMember(new CrewMember("LOULOU"));
        addCrewMember(new CrewMember("Donald"));
    }

}