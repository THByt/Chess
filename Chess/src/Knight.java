import java.util.ArrayList;

// Class: Knight
// Written by: Isaac Hu
// Date: Dec 21, 2017
// Description: A knight is a piece that can only move "some linear combination of two î and one ˆj" -Ethan Frank

//Modified 12/22/17 by Isaac Hu
//Adding all the functionality

public class Knight extends Piece{
	private static Location[] locations = new Location[] {
			new Location(2, 1),
			new Location(1, 2),
			new Location(-2, 1),
			new Location(1, -2),
			new Location(-1, -2),
			new Location(-2, -1),
			new Location(-1, 2),
			new Location(2, -1),
		};
	
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

	@Override
	public int getValue() {
		return 3;
	}
	
	@Override
	public ArrayList<Move> getMoves(Location from, Piece[][] board) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(Location l : locations){
			Location to = from.add(l);
			if(to.inBounds() &&(Piece.getPieceAtLocation(to, board)==null || Piece.getPieceAtLocation(to, board).getPlayer()!=player)){
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
//				Piece taken = null;
//				try {
//					if(board[to.getRow()][to.getColumn()]!=null){
//						taken = (Piece) board[to.getRow()][to.getColumn()].clone();
//					}
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//				}
//				board[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, board);
//				board[from.getRow()][from.getColumn()] = null;
				if((!GraphicsPanel.isInCheck(player, boardCopy) || //If they are still in check the move is not valid 
					(Piece.getPieceAtLocation(to, board) instanceof King && //Unless the move captures the king. That is always valid. (see comment below)
							Piece.getPieceAtLocation(to, board).getPlayer()==3-player))){
	
					moves.add(new Move(from, to));
					
				}
//				board[from.getRow()][from.getColumn()] = Piece.getPieceAtLocation(to, board);
//				board[to.getRow()][to.getColumn()] = taken;
				
			}
		}
		return moves;
	}
		
}
