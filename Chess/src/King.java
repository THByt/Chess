import java.util.ArrayList;

// Class: King
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: Kings can only move one space in any direction
public class King extends Piece{
	private static Location[] locations = new Location[] {
			new Location(1, 0),
			new Location(1, 1),
			new Location(0, 1),
			new Location(-1, 1),
			new Location(-1, 0),
			new Location(-1, -1),
			new Location(0, -1),
			new Location(1, -1),
		};
		
	public King(int player){
		super("king", player);
	}
	
	//Kings move one space any direction. This means delta y and delta x need to have magnitude less than or equal to 1
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		return (Math.abs(from.getRow()-to.getRow())<=1
					&&	Math.abs(from.getColumn()-to.getColumn())<=1)
					&& (Piece.getPieceAtLocation(to, b)==null || Piece.getPieceAtLocation(to, b).getPlayer()!=player);
	}

	@Override
	public int getValue() {
		return 100;
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
				if(!GraphicsPanel.isInCheck(player, boardCopy) || //If they are still in check the move is not valid 
					(Piece.getPieceAtLocation(to, board) instanceof King && //Unless the move captures the king. That is always valid. (see comment below)
							Piece.getPieceAtLocation(to, board).getPlayer()==3-player)){
	
					moves.add(new Move(from, to));
					
				}
//				board[from.getRow()][from.getColumn()] = Piece.getPieceAtLocation(to, board);
//				board[to.getRow()][to.getColumn()] = taken;
				
			}
		}
		return moves;
	}
}
