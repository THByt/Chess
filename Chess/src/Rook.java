// Class: Rook
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: A rook is a piece that can only move left/right or up/down
public class Rook extends Piece{
	public Rook(int player){
		super("rook", player);
	}
	
	@Override
	public boolean isValidMove(Location from, Location to, Piece[][] b) {
		return (from.getRow()-to.getRow()==0||from.getColumn()-to.getColumn()==0) && !to.equals(from); 
	}
}
