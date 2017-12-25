// Class: Knight
// Written by: Isaac Hu
// Date: Dec 21, 2017
// Description: A knight is a piece that can only move "some linear combination of two î and one ˆj" -Ethan Frank

//Modified 12/22/17 by Isaac Hu
//Adding all the functionality

public class Knight extends Piece{
	public Knight(int player){
		super("knight", player);
	}
	
	//A knight can only move "some linear combination of two î and one ˆj" -Ethan Frank, so one direction must be ± 2, and the other ±1
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		
		
		//Check if it is not within the range of the knight
		if(!((Math.abs(from.getColumn()-to.getColumn()) == 2 && Math.abs(from.getRow()-to.getRow()) == 1) || (Math.abs(from.getRow() - to.getRow()) == 2 && Math.abs(from.getColumn()-to.getColumn()) == 1))) {
			return false;
		}
		//Check if a piece is on the projected location
		return b[to.getRow()][to.getColumn()] == null || b[to.getRow()][to.getColumn()].getPlayer()!=player;
	}
		
}
