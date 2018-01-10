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
	public boolean isValidMove(Location from, Location to, Piece[][] b) {
		
		if(!((from.getRow()-to.getRow()==0||from.getColumn()-to.getColumn()==0) && !to.equals(from))) return false; //can't move there
		
		//Checking if the piece may move forward twice due to its first time being moved
		if(!hasMoved) {
			hasMoved = true;
			if(from.getColumn() == to.getColumn() && from.getRow() == (to.getRow() + (((-player + 1.5)*4))) //Checks if the 'to' is the double move position
					&& ((b[(int) (to.getRow() + (((-player + 1.5)*4)))][from.getColumn()] == null)))
					return true;
		}
		
		//Checking if it is attempting to move forward once
		if(from.getColumn() == to.getColumn() && from.getRow() == (to.getRow() + (((-player + 1.5)*2))) //Checks if the 'to' is the double move position
				&& ((b[(int) (to.getRow() + (((-player + 1.5)*2)))][from.getColumn()] == null)))
				return true;
		
		//Checking if the piece is attempting to move diagonally to capture
		for(int i : new int[] {-1, 1}) {
			if(from.getColumn() == to.getColumn()+i && from.getRow() == (to.getRow() + (((-player + 1.5)*2))) //Checks if the 'to' is the double move position
					&& ((b[(int) (to.getRow() + (((-player + 1.5)*2)))][from.getColumn()] != null)) && ((b[(int) (to.getRow() + (((-player + 1.5)*2)))][from.getColumn()].getPlayer()!=player)))
					return true;
		}
		return false;
		
		
		
	}
		
}
