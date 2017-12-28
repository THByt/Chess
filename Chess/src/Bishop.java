import java.util.ArrayList;

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

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public ArrayList<Move> getMoves(Location from, Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		int upRight = from.getRow()+from.getColumn();
		int upLeft = from.getRow()-from.getColumn();
//		System.out.println("UR:"+upRight);
//		System.out.println("UL:"+upLeft);
		for(int r = from.getRow()+1, c = from.getColumn()+1; r<8 && c<8; r++, c++){
			if(Piece.getPieceAtLocation(new Location(r, c), board)==null){
				moves.add(new Move(from, new Location(r, c)));
			}else if(Piece.getPieceAtLocation(new Location(r, c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, c)));
				break;
			}else{
				break;
			}
		}
		
		for(int r = from.getRow()+1, c = from.getColumn()-1; r<8 && c>-1; r++, c--){
			if(Piece.getPieceAtLocation(new Location(r, c), board)==null){
				moves.add(new Move(from, new Location(r, c)));
			}else if(Piece.getPieceAtLocation(new Location(r, c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, c)));
				break;
			}else{
				break;
			}
		}
		
		for(int r = from.getRow()-1, c = from.getColumn()-1; r>-1 && c>-1; r--, c--){
			if(Piece.getPieceAtLocation(new Location(r, c), board)==null){
				moves.add(new Move(from, new Location(r, c)));
			}else if(Piece.getPieceAtLocation(new Location(r, c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, c)));
				break;
			}else{
				break;
			}
		}
		
		for(int r = from.getRow()-1, c = from.getColumn()+1; r>-1 && c<8; r--, c++){
			if(Piece.getPieceAtLocation(new Location(r, c), board)==null){
				moves.add(new Move(from, new Location(r, c)));
			}else if(Piece.getPieceAtLocation(new Location(r, c), board).getPlayer()==3-player){
				moves.add(new Move(from, new Location(r, c)));
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
