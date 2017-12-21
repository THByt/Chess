
// Class: Bishop
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: Bishops move diagonally and are Pieces
public class Bishop extends Piece{
	public Bishop(int player){
		super("bishop", player);
	}

	@Override
	public boolean isValidMove(Location from, Location to, Piece[][] b) {
		return (Math.abs(from.getRow()-to.getRow()) == Math.abs(from.getColumn()-to.getColumn()))
				&& !to.equals(from);
	}
}
