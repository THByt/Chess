import java.util.ArrayList;

// Class: Queen
// Written by: Isaac Hu
// Date: Dec 21, 2017
// Description: A Queen is a piece that can only move within the specifications of a rook or a bishop

//Modified 12/22/17 by Isaac Hu
//Adding all the functionality

public class Queen extends Piece{
	public Queen(int player){
		super("queen", player);
	}
	
	//A Queen can only move within the specifications of a rook or a bishop
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		//Combining the functionality of a rook and a bishop
		return (new Rook(player).isValidMoveSpecific(from, to, b)) || (new Bishop(player).isValidMoveSpecific(from, to, b));
	}

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public ArrayList<Move> getMoves(Location from, Piece[][] board) {
		ArrayList<Move> moves = new Rook(player).getMoves(from, board);
		moves.addAll(new Bishop(player).getMoves(from, board));
		return moves;
	}
		
}
