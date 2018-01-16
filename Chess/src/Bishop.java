
// Class: Bishop
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: Bishops move diagonally and are Pieces
public class Bishop extends Piece{
	public Bishop(int player){
		super("bishop", player);
	}

	//Bishops move diagonally. This means abs(delta X) = abs(delta Y) 
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		if(!(Math.abs(from.getRow()-to.getRow()) == Math.abs(from.getColumn()-to.getColumn()))){return false;} //can't move there

		int x_way = (int) Math.signum(to.getColumn()-from.getColumn()); //which direction to go in the x direction to get from here to there
		int y_way = (int) Math.signum(to.getRow()-from.getRow());		//which direction to go in the y direction
		
		//see if pieces are in the way
		for(int i = 1; i<Math.abs(from.getRow()-to.getRow())+1; i++){
			if(Piece.getPieceAtLocation(new Location(from.getRow()+i*y_way, from.getColumn()+i*x_way), b)!=null){//If there is a piece in the path from from to to
				return Piece.getPieceAtLocation(new Location(from.getRow()+i*y_way, from.getColumn()+i*x_way), b).getPlayer()!=player//If that piece is on the other team
						&& to.equals(new Location(from.getRow()+i*y_way, from.getColumn()+i*x_way));	//and we are checking the to location
																					//We can capture it and can move there, but otherwise it is blocking
			}
		}
		return true;
	}
}
