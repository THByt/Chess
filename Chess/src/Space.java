
// Class: Space
// Written by: Ethan Frank
// Date: Dec 25, 2017
// Description: An Space is what is on a square with no real piece on it.
public class Space extends Piece{

	public Space(){
		super("space" , 0); //spaces don't belong to a player
	}
	
	@Override
	public boolean isValidMove(Location from, Location to, Piece[][] b) {
		return false;
	}

}
