// Class: Pawn
// Written by: Isaac Hu
// Date: Dec 21, 2017
// Description: A Pawn is a piece that can only move forward if there is no piece blocking, forward twice if the above conditions are met, and it is the first turn in which it is moving, and diagonally forward if it is capturing a piece.

//Modified 12/22/17 by Isaac Hu
//Adding all the functionality

public class Pawn extends Piece{
	public Pawn(int player){
		super("pawn", player);
	}
	
	// Description: A Pawn can only move forward if there is no piece blocking, forward twice if the above conditions are met, and it is the first turn in which it is moving, and diagonally forward if it is capturing a piece.
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		if(!((from.getRow()-to.getRow()==0||from.getColumn()-to.getColumn()==0) && !to.equals(from))) return false; //can't move there
		
		//Checking if the piece may move forward twice due to its first time being moved
		System.out.println(player);
		if(moved == 0
				&& from.getColumn() == to.getColumn() && from.getRow() == (to.getRow() + (((-player + 1.5)*4)))
				&& (((b[(from.getRow() + to.getRow())/2][to.getColumn()] == null)))
				&& (((b[to.getRow()][to.getColumn()] == null)))) {
					return true;
		}
		
		//Checking if it is attempting to move forward once
		if(from.getColumn() == to.getColumn() && from.getRow() == (to.getRow() + (((-player + 1.5)*2))) //Checks if the 'to' is the single move position
				&& ((b[to.getRow()][from.getColumn()] == null))) {
				return true;
		}
		
		//Checking if the piece is attempting to move diagonally to capture
		if(Math.abs(to.getColumn()-from.getColumn()) == 1
				&& from.getRow() == to.getRow() + ((-player + 1.5)*2)
				&& b[to.getRow()][to.getColumn()] != null
				&& b[to.getRow()][to.getColumn()].getPlayer()!=player)
					return true;
		return false;
		
	}
		
}
