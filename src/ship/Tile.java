package ship;
import display.StdDraw;
import display.Vector2;
import weapon.Weapon;

import java.util.ArrayList;

/**
 * A tile is a cell of the ship's layout.
 * A weapon can be attached to the tile and a crew member 
 * can be on the tile.
 */
public class Tile {
	
	private 		Weapon 			weapon;		// The weapon assigned to the tile
	private         ArrayList<CrewMember> members;		// The crew member on the tile
	private 		boolean 		isAimed;	// Whether the tile aimed at
	private 		boolean 		isPlayer;	// Whether the tile is owned by the player
	protected final Vector2<Double> tilePos;	// The position of the tile
	
	/**
	 * Creates a tile for the player of the opponent
	 * which is drawn at the given position.
	 * @param position location to draw the tile
	 * @param isPlayer whether it is owned by the player ship
	 */
	public Tile(Vector2<Double> position, boolean isPlayer) {
		this.tilePos = position;
		this.isPlayer = isPlayer;
		this.members = new ArrayList<CrewMember>();
	}
	
	/**
	 * Checks whether a crew member is inside the tile.
	 * @return whether the tile has a crew member
	 */
	public boolean hasCrewMember() {
		return !members.isEmpty() ;
	}
	
	/**
	 * Sets the given crew member has inside the tile.
	 * @param member the crew member to put inside the tile
	 */
	public void setCrewMember(CrewMember member) {
        for (CrewMember c: members) {
            if (c == member) return;
        }
        members.add(member);
	}
	
	/**
	 * Draws the tile, the member inside and the weapon.
	 */
	public void draw() {
		if (tilePos == null)
			return;
		
		double x = tilePos.getX();
		double y = tilePos.getY();
		if (isAimed) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.filledRectangle(x-0.01, y-0.01, 0.01, 0.01);
			StdDraw.setPenColor(StdDraw.BLACK);
		}
		if (weapon != null)
			if (isPlayer)
				drawWeaponHorizontal(x, y);
			else
				drawWeaponVertical(x, y);
		drawHorizontalWall(x,y);
		y-=0.02;
		drawVerticalWall(x,y);
		drawHorizontalWall(x,y);
		x-=0.02;
		drawVerticalWall(x,y);
		if(!members.isEmpty()){
            for (CrewMember c: members) {
                c.draw(getCenterPosition());
            }
        }
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * Draws a wall of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawHorizontalWall(double x, double y) {
			StdDraw.line(x-0.005, y, x-0.015, y);
			StdDraw.line(x, y, x-0.005, y);
			StdDraw.line(x-0.015, y, x-0.02, y);
	}
	
	/**
	 * Draws a wall of the tile vertically.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawVerticalWall(double x, double y) {
			StdDraw.line(x, y+0.005, x, y+0.015);
			StdDraw.line(x, y, x, y+0.005);
			StdDraw.line(x, y+0.015, x, y+0.02);
	}
	
	/**
	 * Draws the weapon of the tile horizontally.
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponHorizontal(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x+0.01, y-0.01, 0.01, 0.005);
		StdDraw.setPenColor(StdDraw.BLACK);
	}
	
	/**
	 * Draws the weapon of the tile vertically
	 * @param x X start position
	 * @param y Y start position
	 */
	private void drawWeaponVertical(double x, double y) {
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(x-0.01, y+0.01, 0.005, 0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
	}

	/**
	 * Assigns a weapon to the tile.
	 * @param w the weapon to assign
	 */
	public void setWeapon(Weapon w) {
		if (weapon == null)
			weapon = w;
	}

	/**
	 * Gives the assigned weapon.
	 * @return the weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Gives the horizontal position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponHorizontalPosition() {
		return new Vector2<Double>(tilePos.getX()+0.01, tilePos.getY()-0.01);
	}
	
	/**
	 * Gives the vertical position of the weapon.
	 * @return the position
	 */
	private Vector2<Double> getWeaponVerticalPosition() {
		return new Vector2<Double>(tilePos.getX()-0.01, tilePos.getY()+0.01);
	}
	
	/**
	 * Gives the position of the weapon.
	 * @return the position
	 */
	public Vector2<Double> getWeaponPosition() {
		if (isPlayer)
			return getWeaponHorizontalPosition();
		else	
			return getWeaponVerticalPosition();
	}

	/**
	 * Gives the position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getPosition() {
		return tilePos;
	}
	
	/**
	 * Gives the center position of the tile.
	 * @return the position
	 */
	public Vector2<Double> getCenterPosition() {
		return new Vector2<Double>(tilePos.getX()-0.01, tilePos.getY()-0.01);
	}
	
	/**
	 * Marks the tile as targeted.
	 */
	public void markTarget() {
		isAimed = true;
	}
	
	/**
	 * Unmarks the tile as targeted.
	 */
	public void unmarkTarget() {
		isAimed = false;
	}
	
	/**
	 * Checks whether the given crew member is the on in the tile.
	 * @param member the crew member to compare it to
	 * @return whether the crew member is the one in the tile
	 */
	public boolean isCrewMember(CrewMember member) {
        for (CrewMember c: members) {
            if (c == member) return true;
        }
        return false;
	}

	/**
	 * Removes the crew member of the tile.
	 */
	public void removeCrewMember() {
	    members.clear();
	}

    public boolean isAimed() {
        return isAimed;
    }
}
