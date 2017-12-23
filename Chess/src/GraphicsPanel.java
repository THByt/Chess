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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;

public class GraphicsPanel extends JPanel implements MouseListener{
	private final int SQUARE_WIDTH = 90;    // The width of one space on the board.  Constant used for drawing board.
	private Location from;   			    // holds the coordinates of the square that the user would like to move from.
	private Location to;   				    // holds the coordinates of the square that the user would like to move to.
	private boolean selected;   			// false until the game has started by somebody clicking on the board.  should also be set to false
	                         				// after an attempted move.
	private Piece[][] board; 				// an 8x8 board of 'Pieces'.  Each spot should be filled by one of the chess pieces or a 'space'. 
	private int player;						// current player (1 or 2)
	
	public GraphicsPanel(){
		setPreferredSize(new Dimension(SQUARE_WIDTH*8+2,SQUARE_WIDTH*8+2));
		board = new Piece[8][8];	//Initialize board

		for(int i = 0; i<12; i++){		
			double random = Math.random();		
			Location l = new Location((int) (Math.random()*8),(int) (Math.random()*8));		
			Piece p = null;		
			if(random<0.2){		
				p = new King(1);		
			}else if(random<0.4){		
				p = new Bishop(1);		
			}else if(random<0.6){		
				p = new Rook(1);		
			}else if(random<0.8){		
				p = new Knight(1);		
			}else if(random<1){		
				p = new Queen(1);		
			}		
			
			board[l.getRow()][l.getColumn()] = p;		
		}		
				
		for(int i = 0; i<12; i++){		
			double random = Math.random();		
			Location l = new Location((int) (Math.random()*8),(int) (Math.random()*8));		
			Piece p = null;		
			if(random<0.2){		
				p = new King(2);		
			}else if(random<0.4){		
				p = new Bishop(2);		
			}else if(random<0.6){		
				p = new Rook(2);		
			}else if(random<0.8){		
				p = new Knight(2);		
			}else if(random<1){		
				p = new Queen(2);		
			}	
			board[l.getRow()][l.getColumn()] = p;		
		}
		player = 1;
		
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
	
	
	public static boolean playerWon(int player, Piece[][] board){
		for(int c = 0; c <8; c++){
			for (int r = 0; r<8; r++){//check every square
				if(board[r][c]!= null && board[r][c] instanceof King && board[r][c].getPlayer()==(3-player)){//If there is a piece and it is a king of the other player's type they have a king
					return false;//they have a king so you have not won
				}
			}
		}
		return true; //they don't have a king you won
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
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(playerWon(1, board));
		System.out.println(playerWon(2, board));
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
				board[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, board);//move piece there (captures by overriding)
				board[from.getRow()][from.getColumn()] = null;	//remove piece from where it was
				player = 3-player; //other player's turn
			}else if(!from.equals(to)){
				SoundEffects.ERROR.play();
			}
			selected = false;
		}
		this.repaint();
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
