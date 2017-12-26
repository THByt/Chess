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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.Timer;

import sun.net.www.content.audio.aiff;

enum State{
	START, PLAY, GAMEOVER
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
	private AI ai;							// An AI
	private Timer t;						// The timer that allows the AI to go after drawing the player's move
	private Move lastMove;					// The last move that was made, for visualization purposes	
	
	public GraphicsPanel(){
		setPreferredSize(new Dimension(SQUARE_WIDTH*8+2,SQUARE_WIDTH*8+2));
		board = new Piece[8][8];	//Initialize board
//
//		board[4][2] = new Rook(2);
//		board[1][5] = new Rook(2);
//		board[4][6] = new King(2);
//		board[6][3] = new King(1);
		
		for(int i = 0; i<6; i++){		
			 			double random = Math.random();		
			 			Location l = new Location((int) (Math.random()*8),(int) (Math.random()*8));		
			 			Piece p = null;		
			 			if(random<0.1 || i == 3){		
			 				p = new King(1);		
			 			}else if(random<0.4){		
			 				p = new Bishop(1);		
			 			}else if(random<0.6){	
			 				p = new Rook(1);		
			 			}else if(random<0.65){	
			 				p = new Queen(1);		
			 			}else if(random<1){	
			 				p = new Knight(1);		
			 			}		
			 			board[l.getRow()][l.getColumn()] = p;		
			 		}		
			 				
			 		for(int i = 0; i<6; i++){		
			 			double random = Math.random();		
			 			Location l = new Location((int) (Math.random()*8),(int) (Math.random()*8));		
			 			Piece p = null;		
			 			if(random<0.1 || i == 3){		
			 				p = new King(2);		
			 			}else if(random<0.4){		
			 				p = new Bishop(2);		
			 			}else if(random<0.6){	
			 				p = new Rook(2);		
			 			}else if(random<0.65){	
			 				p = new Queen(2);		
			 			}else if(random<1){	
			 				p = new Knight(2);		
			 			}		
			 			board[l.getRow()][l.getColumn()] = p;		
			 		}

		player = 1; //Player 1 starts
		state = State.START; //Start on opening screen
		ai = new AI(2);		//Create a new AI for player 2
		lastMove = new Move(new Location(-1,-1), new Location(-1,-1)); //At begining there was no last move;
		
		t = new Timer(0,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lastMove = ai.getMove(board);
				AI.makeMove(board,lastMove);//make the move
				repaint(); //redraw
				if(isInCheckMate(3-player, board)){//Check if game over
					state = State.GAMEOVER;
				}else{
					player = 3 - player;
				}
				
				t.stop();//end AI's turn
			}
		});
		
        this.setFocusable(true);					 // for keylistener
		this.addMouseListener(this);
	}
	
	
	// Method: isCheckMate
	// Description: Sees if a player has won, i.e., opponent is in checkmate
	// Params: int player: the player to check if they have won, Piece[][] board: the current board to check on
	// Returns: boolean: has that player won?
	public static boolean isInCheckMate(int player, Piece[][] board){
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
		g2.setColor(new Color(150,150,255)); 
		g2.fillOval(lastMove.getFrom().getColumn()*SQUARE_WIDTH+10,lastMove.getFrom().getRow()*SQUARE_WIDTH+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);
		g2.fillOval(lastMove.getTo().getColumn()*SQUARE_WIDTH+10,lastMove.getTo().getRow()*SQUARE_WIDTH+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);

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
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//do different stuff depending on state
		switch(state){
		case START:
			state = State.PLAY; //Start game on click
			this.repaint(); //clears text from screen
			break;
		case PLAY:
			int r = Math.max(Math.min((e.getY())/SQUARE_WIDTH, 7), 0); // use math to figure out the row and column that was clicked.
			int c = Math.max(Math.min((e.getX())/SQUARE_WIDTH, 7), 0);
			Piece p = Piece.getPieceAtLocation(new Location(r,c), board);
			lastClickTurnSwitch = false;
			lastMove = new Move(new Location(-1,-1), new Location(-1,-1)); //Make lastmove go away after one click
			if(!selected && p!=null ){//should select a piece //&& p.getPlayer()==player//TODO reenable
				from = new Location(r,c);
				selected = true;
			}else if(selected){//should select a place to go
				to = new Location(r,c);
				if(Piece.getPieceAtLocation(from, board).isValidMove(from, to, board)){//if valid move selected
					board[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, board);//move piece there (captures by overriding)
					board[from.getRow()][from.getColumn()] = null;	//remove piece from where it was
					lastMove = new Move(from, to);
					
					if(isInCheckMate(3-player, board)){//Check if game over
						state = State.GAMEOVER;
					}else{
						player = 3-player; //other player's turn
						lastClickTurnSwitch = true;
						this.repaint();
						t.start(); //Make the AI take it's turn
					}
					
				}
				selected = false;
			}
			this.repaint();
			break;
		case GAMEOVER:
			break;
		}
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
