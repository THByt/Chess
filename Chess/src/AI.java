import java.util.ArrayList;

// Class: AI
// Written by: Ethan Frank
// Date: Dec 25, 2017
// Description: This is a chess AI
public class AI {
	private Move choice; // Used to store which move the AI is making while calculating optimal move
	private int player; //  Which player the AI is
	
	public AI(int player){
		this.player = player;
		this.choice = new Move();
	}
	
	// Method: getMove
	// Description: Returns the move the AI would make
	// Params: Piece[][] b: the board that the AI will make a move for
	// Returns: void
	public Move getMove(Piece[][] board){
		//System.out.println(getMoves(board));
		long time = System.currentTimeMillis();
		int pieceScore = 0;
		for(int c = 0; c <8; c++){ //See if that piece can make any valid moves
			for (int r = 0; r<8; r++){
				if(board[r][c]!=null){
					pieceScore+=board[r][c].getValue()*100*(board[r][c].getPlayer()==1?1:-1);
				}
			}
		}
		minimax(board, player, 0, Integer.MIN_VALUE,Integer.MAX_VALUE, pieceScore);
		System.out.println(System.currentTimeMillis()-time);
		return choice;
	}
	
	public int minimax(Piece[][] board, int player, int d, int alpha, int beta, int pieceScore){
		//Do thing where it doesn't calculate move list each time
		int depthLimit = 4;
		int winner = -1; //Who won, used to do less calculations
		
		if(d>=depthLimit){
			winner = 0;
		}else if(GraphicsPanel.isInCheckMate(player, board)){
			winner = 3-player;
		}

		if(winner>=0) {return score(board,player,d,winner, pieceScore);}
		
		ArrayList<Integer> scores = new ArrayList<Integer>();
		ArrayList<Move> moves = getMoves(board, player);
		
		//for every move
		for(Move move: moves){
			
			//copy board
			Piece[][] newBoard = new Piece[8][8];
			for(int c = 0; c <8; c++){
				for (int r = 0; r<8; r++){
					if(board[r][c]!=null){
						try {
							newBoard[r][c] = (Piece) board[r][c].clone();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			//generate board with new move
			makeMove(newBoard, move);
			if(Piece.getPieceAtLocation(move.getTo(), board)!=null){
				pieceScore-=Piece.getPieceAtLocation(move.getTo(), board).getValue()*100*(player==2?1:-1);
			}
			int value = minimax(newBoard,3-player,d+1,alpha,beta, pieceScore);
			//prune based on alpha beta values
			if(player == 1 && value > alpha){
				alpha = value;
			}else if(player == 2 && value < beta){
				beta = value;
			}
			if(beta<=alpha){
				return value;
			}
			scores.add(value);
		}
		if(d==0)System.out.println(scores.toString());
		
		//find max or min score(player 1 wants max, 2 min)
		//set choice equal to that move, when everything ends this is the move the AI will make
		//Just look up minimax algorithm, it will help better then my comments
		//Or maybe I should just get better at describing
		if(player == 1){
			int num = 0;
			num = scores.stream().reduce(Integer.MIN_VALUE, (x,y)->y>x?y:x);//max
			choice = moves.get(scores.indexOf(num));
			return num;
		}else{
			int num = 0;
			num = scores.stream().reduce(Integer.MAX_VALUE, (x,y)->y<x?y:x);//min
			choice = moves.get(scores.indexOf(num));
			return num;
		}	
	}
	

	// Method: score
	// Description: Scores the board. If the board favors player 1, the number will be high. If it favors player two, the number will be low
	// Params:
	// Returns: int
	private int score(Piece[][] board, int player, int d, int winner, int pieceScore){
		int score = 0;
		
		if(winner!=0){ //If they win give them a very large score so this move is chosen
			return 1000000*(winner==1?1:-1)+(d*player==1?-10:10);
		}
		
		if(GraphicsPanel.isInCheck(2, board)){
			score+=50000;
			if(GraphicsPanel.isInCheckMate(2, board)){
				score+=100000;
			}
		}else if(GraphicsPanel.isInCheck(1, board)){
			score+=-50000;
			if(GraphicsPanel.isInCheckMate(1, board)){
				score+=-100000;
			}
		}
		
//		for(int c = 0; c <8; c++){ //See if that piece can make any valid moves
//			for (int r = 0; r<8; r++){
//				if(board[r][c]!=null){
//					score+=board[r][c].getValue()*100*(board[r][c].getPlayer()==1?1:-1);
//				}
//			}
//		}
		score+=pieceScore;
		score+=getMoves(board, 3-player).size()*(player==1?-1:1); //Punish opponent having lots of mobility
		score+=(d*player==1?-10:10);//Penalize taking longer to win
		return score;
	}
	
	// Method: getMoves
	// Description: Returns an ArrayList of all valid moves
	// Params: Piece[][] board: the board
	// Returns: ArrayList<Move>
	private ArrayList<Move> getMoves(Piece[][] board, int player){
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int c1 = 0; c1 <8; c1++){ //Look through all squares
			for (int r1 = 0; r1<8; r1++){
				Location from = new Location(r1,c1);
				Piece p = Piece.getPieceAtLocation(from, board);
				if(p!=null && p.getPlayer()==player){ //If there is a piece on player's team
					for(int c2 = 0; c2 <8; c2++){ //See if that piece can make any valid moves
						for (int r2 = 0; r2<8; r2++){
							if(p.isValidMove(from, new Location(r2,c2), board)){//If it can add it to the list
								moves.add(new Move(from, new Location(r2,c2)));
							}
						}
					}
				}
			}
		}
		return moves;
	}
	
	// Method: makeMove
	// Description: Moves a piece
	// Params:Move m: the move to make, Piece[][] board: 2 dimensional array of pieces to make the move on
	// Returns: void
	public static void makeMove(Piece[][] board, Move m){
		board[m.getTo().getRow()][m.getTo().getColumn()] = Piece.getPieceAtLocation(m.getFrom(), board);//move piece there (captures by overriding)
		board[m.getFrom().getRow()][m.getFrom().getColumn()] = null;	//remove piece from where it was
	}
}
