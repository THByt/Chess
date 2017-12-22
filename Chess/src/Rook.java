// Class: Rook
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: A rook is a piece that can only move left/right or up/down

//Modified 12/21/17 by Ethan Frank
// Make rook take into account pieces in the way

//Modified 12/22/17 by Ethan Frank
// Fix not saying that own square is invalid move
public class Rook extends Piece{
	public Rook(int player){
		super("rook", player);
	}
	
	//Rooks can only move left/right or up/down. This means at least one of deltaX or deltaY needs to be 0
	@Override
	public boolean isValidMove(Location from, Location to, Piece[][] b) {
		if(!((from.getRow()-to.getRow()==0||from.getColumn()-to.getColumn()==0) && !to.equals(from))) return false; //can't move there
		
		int x_way = (int) Math.signum(to.getColumn()-from.getColumn()); //which direction to go in the x direction to get from here to there
		int y_way = (int) Math.signum(to.getRow()-from.getRow());		//which direction to go in the y direction
		
		//see if pieces are in the way

		if(y_way == 0){//go in x direction
			for(int c = from.getColumn()+x_way; c!=to.getColumn()+x_way; c+=x_way){
				if(Piece.getPieceAtLocation(new Location(from.getRow(), c), b)!=null){
					return Piece.getPieceAtLocation(new Location(from.getRow(), c), b).getPlayer()!=player
							&& to.equals(new Location(from.getRow(),c));
				}//Returns true because it stops at the piece
			}
		}else{//go in y direction
			for(int r = from.getRow()+y_way; r!=to.getRow()+y_way; r+=y_way){
				if(Piece.getPieceAtLocation(new Location(r, from.getColumn()), b)!=null){
					return Piece.getPieceAtLocation(new Location(r, from.getColumn()), b).getPlayer()!=player
							&& to.equals(new Location(r,from.getColumn()));
				}
			}
		}
		return true;

	}
}
