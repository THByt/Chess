// Class: GraphicsPanel
// Written by: Mr. Swope
// Date: 12/2/15
// Description: This class is the main class for this project.  It extends the Jpanel class and will be drawn on
// 				on the JPanel in the GraphicsMain class.  

//Modified 12/21/17 by Ethan Frank
// change click to selected
// make selected true when mouse is clicked
// create valid move viewer
// draw all pieces in array
// add current player variable	
// implement moving pieces 

import static java.lang.System.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JPanel;
import javazoom.jl.decoder.JavaLayerException;

enum State{
	START, PLAY, GAMEOVER, CHOOSEPIECE
}

// Class: GraphicsPanel
// Written by: Ethan Frank
// Date: Dec 24, 2017
// Description:

public class GraphicsPanel extends JPanel implements MouseListener{
	private final int SQUARE_WIDTH = 90;    // The width of one space on the board.  Constant used for drawing board.
	private static Location from;   			    // holds the coordinates of the square that the user would like to move from.
	private static Location to;   				    // holds the coordinates of the square that the user would like to move to.
	private boolean selected;   			// false until the game has started by somebody clicking on the board.  should also be set to false
	                         				// after an attempted move.
	private Piece[][] board; 				// an 8x8 board of 'Pieces'.  Each spot should be filled by one of the chess pieces or a 'space'. 
	private int player;						// current player (1 or 2)
	private State state; 					// Current game state
	private boolean lastClickTurnSwitch; 	// True if the last click moved a piece. Used to draw "check" until they click again
	private static Move lastMove;					// The last move made. For visualization purposes.
	
	public GraphicsPanel(){
		setPreferredSize(new Dimension(SQUARE_WIDTH*8+2,SQUARE_WIDTH*8+2));
		board = new Piece[8][8];	//Initialize board

		//Black non-pawns
		board[0][0] = new Rook(2);
		board[0][1] = new Knight(2);
		board[0][2] = new Bishop(2);
		board[0][3] = new Queen(2);
		board[0][4] = new King(2);
		board[0][5] = new Bishop(2);
		board[0][6] = new Knight(2);
		board[0][7] = new Rook(2);
	
//		//Black pawns
		for(int i = 0; i < 8; i++) board[1][i] = new Pawn(2);
//		
//		//White pawns
		for(int i = 0; i < 8; i++) board[6][i] = new Pawn(1);
		board[1][6] = new Pawn(1);
		
		//White non-pawns
		board[7][0] = new Rook(1);
		board[7][1] = new Knight(1);
		board[7][2] = new Bishop(1);
		board[7][3] = new Queen(1);
		board[7][4] = new King(1);
		board[7][5] = new Bishop(1);
		board[7][6] = new Knight(1);
		board[7][7] = new Rook(1);

		player = 1;
		state = State.START;
		lastMove = new Move(new Location(-1, -1), new Location(-1, -1)); // no last mvoe on start
		
		//play music
		new Thread(new Runnable(){ 
			javazoom.jl.player.Player player;
			@Override
			public void run() {
				while(true){
					try{
						player = new javazoom.jl.player.Player(getClass().getResource("sounds/background_music.mp3").openStream());
						player.play();
					} catch (JavaLayerException | IOException e) {
						e.printStackTrace();
					}
				}
			}	
		}).start();
		
        this.setFocusable(true);					 // for keylistener
		this.addMouseListener(this);
		
	}
	
	
	// Method: isCheckMate
	// Description: Checks to see if a certain player has won, i.e., opponent is in checkmate
	// Params: int player: the player to check if they have won, Piece[][] board: the current board to check on
	// Returns: boolean: has that player won?
	public static boolean isCheckMate(int player, Piece[][] board){
		//Check to see if there are any moves the player can make to get out of check. If not, they are in checkmate and the they lose
		
		for(int c1 = 0; c1 <8; c1++){ //Look through all squares
			for (int r1 = 0; r1<8; r1++){
				Location from = new Location(r1,c1);
				Piece p = Piece.getPieceAtLocation(from, board);
				if(p!=null && p.getPlayer()==player){ //If there is a piece on player's team
					for(int c2 = 0; c2 <8; c2++){ //See if that piece can make any valid moves
						for (int r2 = 0; r2<8; r2++){
							if(p.isValidMove(from, new Location(r2,c2), board)){
								return false; //If they can make a valid move they are not in checkMate
							}
						}
					}
				}
			}
		}
		
		return true; //otherwise they are
	}
	
	// Method: isInCheck
	// Description: Checks if a player is in check
	// Params: int player: the player to see if is in check, Piece[][] board: the current board
	// Returns: boolean: true if they are in check
	public static boolean isInCheck(int player,  Piece[][] board){
		Location kingLocation = new Location(); //the location of the player's king
		
		//find location of king
		for(int c = 0; c <8; c++){	
			for (int r = 0; r<8; r++){
				if(board[r][c] instanceof King && board[r][c].getPlayer()==player){//null instanceof King is false
					kingLocation = new Location(r,c);
				}
			}
		}
		
		//See if any of the opponent's pieces can capture the king
		for(int c = 0; c <8; c++){	
			for (int r = 0; r<8; r++){
				if(board[r][c] != null && board[r][c].getPlayer() == 3-player && //If there is a piece and it is the opponet's and it can move to the king
						board[r][c].isValidMove(new Location(r,c), kingLocation, board)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Method: canACertainPlayerCastleButItOnlyChecksCertainConditionsBecauseOthersAreCheckedElsewhere
	// Description: Says if a player can castle
	// Params: Piece[][] board: the playing board, int player: the player to chekc to castle with
	// Returns: boolean: can that player castle
	public static boolean canACertainPlayerCastleButItOnlyChecksCertainConditionsBecauseOthersAreCheckedElsewhere(Piece[][] board, int player){
		Location inbetweenPieceLocation1 = new Location(7,5, player);
		Location inbetweenPieceLocation2 = new Location(7,6, player);
		boolean squaresInbetweenInAttack = false;

		boolean cantCastle = Piece.getPieceAtLocation(inbetweenPieceLocation1, board) != null ||
				Piece.getPieceAtLocation(inbetweenPieceLocation2, board) != null || isInCheck(player, board);
		
		for(int c = 0; c <8; c++){	
			for (int r = 0; r<8; r++){
				if(board[r][c]!=null && board[r][c].getPlayer()!=player){//opponent's piece
					if(board[r][c].isValidMove(new Location(r,c), inbetweenPieceLocation1, board) ||
							board[r][c].isValidMove(new Location(r,c), inbetweenPieceLocation2, board)){
						squaresInbetweenInAttack = true;
						break;
					}
				}
			}
		}
		
		cantCastle|=squaresInbetweenInAttack;
		return !cantCastle;//That double negative though👌👌👌	
	}
	
	// Method: movePiece
	// Description: Moves a piece. Handles castling too.
	// Params: Location from: location to move piece from. Location to: location to move piece to. Piece[][] board: board of pieces. 
	// Returns: void
	public static void movePiece(Location from, Location to, Piece[][] board){
		int player = Piece.getPieceAtLocation(from, board).getPlayer();
		Location kingLocation = new Location(7,4);
		Location rookLocation = new Location(7,7);
		Location kingLocationAfterMovingIfCastle = new Location(7,6);
		Location rookLocationAfterMovingIfCastle = new Location(7,5);
		
		if(player==2){
			kingLocation.flip();
			rookLocation.flip();
			kingLocationAfterMovingIfCastle.flip();
			rookLocationAfterMovingIfCastle.flip();
		}
		
		if(Piece.getPieceAtLocation(to, board)!=null && Piece.getPieceAtLocation(to, board).getPlayer()!=player){
			SoundEffects.EXPLOSION.play();
		}
		
		lastMove = new Move(from, to);
		
		boolean isCastle = from.equals(kingLocation) && to.equals(rookLocation);
		board[from.getRow()][from.getColumn()].move(); //Add one to 'moved' variable
		if(isCastle){
			board[kingLocationAfterMovingIfCastle.getRow()][kingLocationAfterMovingIfCastle.getColumn()] = Piece.getPieceAtLocation(kingLocation, board);
			board[rookLocationAfterMovingIfCastle.getRow()][rookLocationAfterMovingIfCastle.getColumn()] = Piece.getPieceAtLocation(rookLocation, board);
			board[from.getRow()][from.getColumn()] = null;	//remove piece from where it was
			board[to.getRow()][to.getColumn()] = null;	//remove piece from where it was
		}else{
			board[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, board);//move piece there (captures by overwriting)
			board[from.getRow()][from.getColumn()] = null;	//remove piece from where it was
		}
		
	}
	
	// method: paintComponent
	// description: This method will paint the items onto the graphics panel.  This method is called when the panel is
	//   			first rendered.  It can also be called by this.repaint()
	// parameters: Graphics g - This object is used to draw your images onto the graphics panel.
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		// Draw the board
		g2.setColor(Color.gray);
		g2.drawLine(SQUARE_WIDTH*8, 0, SQUARE_WIDTH*8, SQUARE_WIDTH*8);
		g2.drawLine(0, SQUARE_WIDTH*8, SQUARE_WIDTH*8, SQUARE_WIDTH*8);
		g2.drawLine(0, 0, SQUARE_WIDTH*8, 0);
		g2.drawLine(0, 0, 0, SQUARE_WIDTH*8);
		
		for(int c = 0; c <8; c+=2){
			for (int r = 0; r<8; r+=2){
				g2.setColor(Color.gray);
				g2.fillRect(c*SQUARE_WIDTH,r*SQUARE_WIDTH,SQUARE_WIDTH,SQUARE_WIDTH);
			}
		}
		
		for(int c = 1; c<8; c+=2){
			for (int r = 1; r<8; r+=2){
				g2.setColor(Color.gray);
				g2.fillRect(c*SQUARE_WIDTH,r*SQUARE_WIDTH,SQUARE_WIDTH,SQUARE_WIDTH);
			}
		}
		
		//Draw the last move
		if(lastClickTurnSwitch){
			g2.setColor(new Color(150,150,255)); 
			g2.fillOval(lastMove.getFrom().getColumn()*SQUARE_WIDTH+10,lastMove.getFrom().getRow()*SQUARE_WIDTH+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);
			g2.fillOval(lastMove.getTo().getColumn()*SQUARE_WIDTH+10,lastMove.getTo().getRow()*SQUARE_WIDTH+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);
		}	
		
		if(selected){
			for(int c = 0; c <8; c++){
				for (int r = 0; r<8; r++){
					Piece p = Piece.getPieceAtLocation(from, board);
					to = new Location(r,c);
					if(p!=null && p.isValidMove(from, to, board)){
						g2.setColor(new Color(100,255,100));
						g2.fillOval(c*SQUARE_WIDTH+10,r*SQUARE_WIDTH+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);
					}
				}
			}
		}

		//Draw the pieces
		for(int c = 0; c <8; c++){
			for (int r = 0; r<8; r++){
				if(board[r][c]!=null){
					board[r][c].draw(g2, this, new Location(r,c));
				}
			}
		}
		
		//Draw different stuff depending on state
		switch(state){
		case START:
			drawCenteredText(g2, "CHESS", SQUARE_WIDTH*4-20, 240, Color.BLACK);
			break;
		case PLAY:
			
			if(lastClickTurnSwitch && isInCheck(player, board)){
				drawCenteredText(g2, "CHECK", SQUARE_WIDTH*4-20, 240, Color.BLACK);
			}
			break;
		case GAMEOVER:
			Color color = Color.BLACK;//player==1?Color.WHITE:
			drawCenteredText(g2, "PLAYER " + player, SQUARE_WIDTH*4-60, 140, color);
			drawCenteredText(g2, "WINS!", SQUARE_WIDTH*4+60, 140, color);
			break;
			
		case CHOOSEPIECE:
			
			//Print green pieces for selection
			g2.translate(5*SQUARE_WIDTH, 3*SQUARE_WIDTH);
			g2.scale(2, 2);
			out.println("IS RUNIN");
			new Queen(3).draw(g2, this, new Location());
			g2.scale(0.5, 0.5);
			g2.translate(-5*SQUARE_WIDTH, -3*SQUARE_WIDTH);
			
			g2.translate(1*SQUARE_WIDTH, 3*SQUARE_WIDTH);
			g2.scale(2, 2);
			new Knight(3).draw(g2, this, new Location());
			g2.scale(0.5, 0.5);
			g2.translate(-1*SQUARE_WIDTH, -3*SQUARE_WIDTH);
			break;
		}
	}

	//Method: mouseClicked
	//Description: 
	//Params:
	//Returns: 
	@Override
	public void mouseClicked(MouseEvent e) {
		lastClickTurnSwitch = false;
		switch(state){
		case START:
			state = State.PLAY;
			break;
		case PLAY:
			int r = Math.max(Math.min((e.getY())/SQUARE_WIDTH, 7), 0); // use math to figure out the row and column that was clicked.
			int c = Math.max(Math.min((e.getX())/SQUARE_WIDTH, 7), 0);
			Piece p = Piece.getPieceAtLocation(new Location(r,c), board);
			if(!selected && p!=null && p.getPlayer()==player){//should select a piece
				from = new Location(r,c);
				selected = true;
			}else if(!selected){//They did not click on a valid piece
				SoundEffects.ERROR.play();
			}else if(selected){//should select a place to go
				to = new Location(r,c);
				if(Piece.getPieceAtLocation(from, board).isValidMove(from, to, board)){//if valid move selected
					movePiece(from, to, board);//move the piece there
					//Check if a pawn must be changed into a queen
					for(int i = 0; i < 8; i++) {
						if(board[0][i] != null 
						&& board[0][i].getPlayer()==1
						&& board[0][i] instanceof Pawn) {
							state = State.CHOOSEPIECE;
						}
					}
					for(int i = 0; i < 8; i++) {
						if(board[7][i] != null 
						&& board[7][i].getPlayer()==2
						&& board[7][i] instanceof Pawn) {
							state = State.CHOOSEPIECE;
						}
					}
					

					state = State.PLAY;
			        if(isCheckMate(3-player, board)){//Check if game over
			          state = State.GAMEOVER;
			        }else{
			          player = 3-player; //other player's turn
			          lastClickTurnSwitch = true;
			        }
			        selected = false;
				}else if(p!=null && p.getPlayer()==player && !from.equals(to)){//If they click another one of their pieces
					from = new Location(r,c); //select it
				}else if(!from.equals(to)){//If invalid move selected that is not clicking the selected piece
					SoundEffects.ERROR.play(); //play error sound
					selected = false;		   // unselect it
				}else{ //they click their piece again
					selected = false; //unselect it
				}
				break;	
			}
			
		case CHOOSEPIECE:
			
			switch(player) {
			case 1:
				for(int i = 0; i < 8; i++) {
					if(board[7][i] != null 
					&& board[7][i].getPlayer()==1
					&& board[7][i] instanceof Pawn) {
						board[7][i] = (e.getX() < (SQUARE_WIDTH*4)+2) ? new Knight(2) : new Queen(2);
					}
				}
				state = State.PLAY;
				break;
				
			case 2:
				for(int i = 0; i < 8; i++) {
					if(board[0][i] != null 
					&& board[0][i].getPlayer()==1
					&& board[0][i] instanceof Pawn) {
						board[0][i] = (e.getX() < (SQUARE_WIDTH*4)+2) ? new Knight(1) : new Queen(1);
					}
				}
				state = State.PLAY;
				break;
				
			}
			
		case GAMEOVER:
			break;
		}
		repaint();
	}
	
	// Method: drawCenteredText
	// Description: Draws text centered at x position
	// Params: Graphics2D g2: the graphics to use to draw
				// String text: the text to draw
				// int y: the y coordinate to draw the text at
				// int size: the size of the text
				// int x: x coordinate to center on
				// Color c: color of text
	// Returns: void
	private void drawCenteredText(Graphics2D g2, String text, int y, int size, int x, Color color){
		Font font = new Font("Courier", 1, size); //Create new font of the size
		FontMetrics metrics = g2.getFontMetrics(font); // Get the font metrics
		g2.setColor(color);
		g2.setFont(font);
		g2.drawString(text, x-metrics.stringWidth(text)/2, y); // Draw the centered text
	}
	
	// Method: drawCenteredText
	// Description: Draws text centered in screen at certain color
	// Params: @see drawCenteredText(Graphics2D g2, String text, int y, int size, int x, Color color) for description of params
	// Returns: void
	private void drawCenteredText(Graphics2D g2, String text, int y, int size, Color color){
		drawCenteredText(g2, text, y, size, SQUARE_WIDTH*4, color);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
