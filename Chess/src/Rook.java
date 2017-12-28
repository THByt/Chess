import java.util.ArrayList;

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
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		if(!(from.getRow()-to.getRow()==0||from.getColumn()-to.getColumn()==0)) return false; //can't move there
		
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

	@Override
	public int getValue() {
		return 5;
	}

	@Override
	public ArrayList<Move> getMoves(Location from, Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int r = from.getRow()+1; r<8; r++){
			
			if(Piece.getPieceAtLocation(new Location(r, from.getColumn()), board)==null){
				moves.add(new Move(from, new Location(r, from.getColumn())));
			}else if(Piece.getPieceAtLocation(new Location(r, from.getColumn()), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, from.getColumn())));
				break;
			}else{
				break;
			}
		}
		for(int r = from.getRow()-1; r>-1; r--){
			if(Piece.getPieceAtLocation(new Location(r, from.getColumn()), board)==null){
				moves.add(new Move(from, new Location(r, from.getColumn())));
			}else if(Piece.getPieceAtLocation(new Location(r, from.getColumn()), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, from.getColumn())));
				break;
			}else{
				break;
			}
		}
		for(int c = from.getColumn()-1; c>-1; c--){
			if(Piece.getPieceAtLocation(new Location(from.getRow(), c), board)==null){
				moves.add(new Move(from, new Location(from.getRow(), c)));
			}else if(Piece.getPieceAtLocation(new Location(from.getRow(), c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(from.getRow(), c)));
				break;
			}else{
				break;
			}
		}
		for(int c = from.getColumn()+1; c<8; c++){
			if(Piece.getPieceAtLocation(new Location(from.getRow(), c), board)==null){
				moves.add(new Move(from, new Location(from.getRow(), c)));
			}else if(Piece.getPieceAtLocation(new Location(from.getRow(), c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(from.getRow(), c)));
				break;
			}else{
				break;
			}
		}
		
		for(int i = moves.size()-1; i>-1; i--){
			Location to = moves.get(i).getTo();
			Piece[][] boardCopy = new Piece[8][8];
			for(int c = 0; c <8; c++){
				for (int r = 0; r<8; r++){
					if(board[r][c]!=null){
						try {
							boardCopy[r][c] = (Piece) board[r][c].clone();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			//Make the move (virtually) to test it
			boardCopy[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, boardCopy);
			boardCopy[from.getRow()][from.getColumn()] = null;

			if(!(!GraphicsPanel.isInCheck(player, boardCopy) || //If they are still in check the move is not valid 
				(Piece.getPieceAtLocation(to, board) instanceof King && //Unless the move captures the king. That is always valid. (see comment below)
						Piece.getPieceAtLocation(to, board).getPlayer()==3-player))){
				moves.remove(i);
			}
		}
		return moves;
	}
}
