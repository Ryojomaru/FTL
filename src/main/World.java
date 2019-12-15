package main;
import java.util.Collection;
import java.util.Random;

import display.Button;
import display.StdDraw;
import display.Vector2;
import module.Module;
import module.Shield;
import ship.*;
import weapon.DummyGun;
import weapon.IonCannon;
import weapon.MissileLauncher;
import weapon.Projectile;

/**
 * The world contains the ships and draws them to screen.
 */
public class World {
	
	private Bindings 	bind;		// The bindings of the game.
	private long 		time;		// The current time
	private boolean		canContinue;// Player have to choose reward for winning
	private Ship[] ennemies = new Ship[3];
	private int ennemyIndex;
	
	Ship player;				// The ship of the player
	Ship opponent;				// The ship of the opponent
	
	/**
	 * Creates the world with the bindings, the player ship
	 * and the opponent ship.
	 */
	public World() {
		ennemies[0] = new DummyShip(false, new Vector2<Double>(0.8, 0.5));
		ennemies[1] = new SecondShip(false, new Vector2<Double>(0.8, 0.5));
		ennemies[2] = new BigShip(false, new Vector2<Double>(0.8, 0.5));
		ennemyIndex = 0;
		bind = new Bindings(this);
		player = new DummyShip(true, new Vector2<Double>(0.3, 0.5));
		opponent = ennemies[ennemyIndex];
		time = System.currentTimeMillis();
		canContinue = true;
		hasChosenRandomReward = false;
		hasChosenReward = false;
	}
	
	/**
	 * Processes the key pressed.
	 */
	public void processKey(){
		this.bind.processKey();
	}
	
	/**
	 * Makes a step in the world.
	 */
	public void step() {
		if (canContinue) {
			player.step(((double) (System.currentTimeMillis() - time)) / 1000);
			opponent.step(((double) (System.currentTimeMillis() - time)) / 1000);

			opponent.ai(player);

			processHit(player.getProjectiles(), true);
			processHit(opponent.getProjectiles(), false);

			time = System.currentTimeMillis();
		}
		if (opponent.getCurrentHull() == 0){
			if (opponent.getTarget() != null){
				opponent.getTarget().unmarkTarget();
			}
			opponent.setTarget(null);
			player.setTarget(null);
			canContinue = false;
			StdDraw.clear();
			player.getProjectiles().clear();
			draw();
			showRewardScreen();
			if (canContinue){
				hasChosenReward = false;
				hasChosenRandomReward = false;
				nextEnnemy();
				opponent = ennemies[ennemyIndex];
			}
		}

		if (player.getCurrentHull() == 0){
			System.exit(0);
		}
	}
	
	/**
	 * Processes the projectiles hit
	 * @param projectiles collection of projectiles to check for hit
	 * @param isPlayer whether the own of the projectiles is the player
	 */
	private void processHit(Collection<Projectile> projectiles, boolean isPlayer) {
		if (!(isPlayer)){
			processShieldHit(projectiles, player);
			processDamageHit(projectiles, player);
		} else {
			processShieldHit(projectiles, opponent);
			processDamageHit(projectiles, opponent);
		}
	}

	/**
	 * Tests if a shield is hit by an enemy projectile.
	 * If yes, deactivates the shield and make the projectile disappear.
	 * @param projectiles collection of projectiles to check for hit
	 * @param ship ship to hit
	 */
	private void processShieldHit(Collection<Projectile> projectiles, Ship ship) {
		for(Projectile p: projectiles) {
			for(Shield s: ship.getShield().getShields()) {
				if(s.isInRadius(p.getPosition()) && s.isActive()) {
					if(p instanceof IonCannon.IonProjectile) {
						ship.getShield().deactivateShieldByIon(s,((IonCannon.IonProjectile) p).getTime());
					}
					else if (!(p instanceof MissileLauncher.Missile)) { //missiles go through shields
						ship.getShield().deactivateShield(s);
						projectiles.remove(p);
					}

					return;
				}
			}
		}
	}

	/**
	 * Test if the ship is hit by projectiles
	 * @param projectiles collection of projectiles to check for hit
	 * @param ship ship to hit
	 */
	private void processDamageHit(Collection<Projectile> projectiles, Ship ship){
		for (Projectile p: projectiles) {
			for (Tile t: ship.getLayout()) {
				if (p != null) {
					if (p.isOutOfScreen()){
						projectiles.remove(p);
						return;
					}
					if (!(p.isOutOfRectangle(t.getCenterPosition().getX(), t.getCenterPosition().getY(), 0.01, 0.01))) {
						Random rn = new Random();
						int luck = rn.nextInt(100) + 1;
						if(luck < ship.getEngine().getDodge()) {
							System.out.println("You evaded a shot !");
						} else {
							if(t instanceof module.Module) {
								if(p instanceof IonCannon.IonProjectile) {
									((Module) t).deactivate(((IonCannon.IonProjectile) p).getTime());
								}
								((Module) t).damage(p.getDamage());
							}
							ship.applyDamage(p);
						}
						projectiles.remove(p);
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Draws the ships and HUDs.
	 */
	public void draw() {
		player.draw();
		player.drawHUD();
		
		opponent.draw();
		opponent.drawHUD();
		
	}

	/**
	 * increments the index for the next ennemy.
	 */
	private void nextEnnemy(){
		this.ennemyIndex++;
		if(this.ennemyIndex > 2) System.exit(0);
	}


	/**
	 * All that follows is only useful for the reward screen.
	 */


	private boolean hasChosenReward;
	private boolean hasChosenRandomReward;

	private class RewardButton extends Button {
		private int effect;

		private RewardButton(Vector2<Double> pos, Vector2<Double> dim,int effect) {
			super(pos, dim, true);
			this.effect = effect;
		}


		@Override
		protected void onLeftClick() {
			switch (this.effect) {
				case 0:
					player.getWeaponControl().levelUp();
					hasChosenReward = true;
					canContinue = true;
					break;
				case 2:
					player.getReactor().levelUp();
					hasChosenReward = true;
					canContinue = true;
					break;
				case 3:
					player.getShield().levelUp();
					hasChosenReward = true;
					canContinue = true;
					break;
				case 4:
					player.getEngine().levelUp();
					hasChosenReward = true;
					canContinue = true;
					break;
				default: //1 is put as default as a safety measure
					player.getReactor().increaseEnergy(3);
					hasChosenReward = true;
					canContinue = true;
					break;

			}
		}

		@Override
		protected void onRightClick() {
			this.onLeftClick();
		}

		@Override
		protected void onMiddleClick() {
			this.onLeftClick();
		}


	}

	/**
	 * Show reward buttons and give a random reward if it was not given yet
	 */

	public void showRewardScreen() {
		Button b0 = new RewardButton(new Vector2<Double>(0.1,0.65),new Vector2<Double>(0.075,0.075),0);
		Button b1 = new RewardButton(new Vector2<Double>(0.3,0.65),new Vector2<Double>(0.075,0.075),1);
		Button b2 = new RewardButton(new Vector2<Double>(0.5,0.65),new Vector2<Double>(0.075,0.075),2);
		Button b3 = new RewardButton(new Vector2<Double>(0.7,0.65),new Vector2<Double>(0.075,0.075),3);
		Button b4 = new RewardButton(new Vector2<Double>(0.9,0.65),new Vector2<Double>(0.075,0.075),4);
		if (!canContinue){

			if (!hasChosenRandomReward){
				randomReward();
				hasChosenRandomReward = true;
			}

			b0.draw();
			b1.draw();
			b2.draw();
			b3.draw();
			b4.draw();

			StdDraw.text(0.1,0.65,"Weapon lvl up");
			StdDraw.text(0.3,0.65,"Energy +3");
			StdDraw.text(0.5,0.65,"Reactor lvl up");
			StdDraw.text(0.7,0.65,"Shields lvl up");
			StdDraw.text(0.9,0.65,"Engine lvl up");

		}

		if(canContinue){
			StdDraw.clear();
			b0.destroy();
			b1.destroy();
			b2.destroy();
			b3.destroy();
			b4.destroy();
		}
	}

	/**
	 * Give the player a random reward
	 */

	public void randomReward(){
		boolean hasPicked = false;
		while(!hasPicked) {
			Random rn = new Random();
			int lottery = rn.nextInt(4);
			switch (lottery) {
				case 0:
					DummyGun weap = new DummyGun(); //todo:change to a new weapon
					if (!player.getWeaponControl().hasWeapon(weap)) {
						hasPicked = true;
						player.getEngine().setWeapon(weap);
						StdDraw.text(0.8,0.1,"You got a new Weapon !");
					}
					break;
				case 1:
					player.getWeaponControl().addMissile();
					StdDraw.text(0.8,0.1,"You got a new Missile !");
					hasPicked = true;
					break;
				case 2:
					CrewMember newGuy = new CrewMember("John Doe");
					StdDraw.text(0.8,0.1,"You got a new Crew member !");
					player.addCrewMember(newGuy);
					hasPicked = true;
					break;
				default:
					lottery = rn.nextInt(5)+1;
					player.repairHull(lottery);
					hasPicked = true;
					break;

			}
		}
	}
}
