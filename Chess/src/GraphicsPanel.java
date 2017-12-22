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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GraphicsPanel extends JPanel implements MouseListener{
	private final int SQUARE_WIDTH = 90;    // The width of one space on the board.  Constant used for drawing board.
	private Location from;   			    // holds the coordinates of the square that the user would like to move from.
	private Location to;   				    // holds the coordinates of the square that the user would like to move to.
	private boolean selected;   			// false until the game has started by somebody clicking on the board.  should also be set to false
	                         				// after an attempted move.
	private Piece[][] board; 				// an 8x8 board of 'Pieces'.  Each spot should be filled by one of the chess pieces or a 'space'. 
	private int player;						// current player (1 or 2)
	private double boardAngle;
	Timer t;
	
	public GraphicsPanel(){
		setPreferredSize(new Dimension(SQUARE_WIDTH*8+2,SQUARE_WIDTH*8+2));
		board = new Piece[8][8];	//Initialize board
		board[2][5] = new King(1);
		board[2][4] = new King(2);
		board[6][3] = new King(1);
		board[0][7] = new King(2);
		board[4][3] = new Rook(1);
		board[3][4] = new Bishop(2);
		player = 1;
		boardAngle = 0;
		
		t = new Timer(25, new ClockListener(this));  	// t is a timer.  This object will call the ClockListener's
													 	// action performed method every 25 milliseconds once the 
													 	// timer is started.
		
		
        this.setFocusable(true);					 // for keylistener
		this.addMouseListener(this);
	}
	
	// method: paintComponent
	// description: This method will paint the items onto the graphics panel.  This method is called when the panel is
	//   			first rendered.  It can also be called by this.repaint()
	// parameters: Graphics g - This object is used to draw your images onto the graphics panel.
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(boardAngle, SQUARE_WIDTH*4, SQUARE_WIDTH*4);
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
					board[r][c].draw(g2, this, new Location(r,c), boardAngle);
				}
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int r = Math.max(Math.min((e.getY())/SQUARE_WIDTH, 7), 0); // use math to figure out the row and column that was clicked.
		int c = Math.max(Math.min((e.getX())/SQUARE_WIDTH, 7), 0);
		if(player==2){//the board is rotated so the row and column are rotated to
			r = 7-r;
			c = 7-c;
		}
		Piece p = Piece.getPieceAtLocation(new Location(r,c), board);
		if(!selected && p!=null && p.getPlayer()==player){//should select a piece
			from = new Location(r,c);
			selected = true;
		}else if(selected){//should select a place to go
			to = new Location(r,c);
			if(Piece.getPieceAtLocation(from, board).isValidMove(from, to, board)){//if valid move selected
				board[to.getRow()][to.getColumn()] = Piece.getPieceAtLocation(from, board);//move piece there (captures by overriding)
				board[from.getRow()][from.getColumn()] = null;	//remove piece from where it was
				player = 3-player; //other player's turn
				t.start();			//Start rotate animation
			}
			selected = false;
		}
		this.repaint();
	}
	
	public void clock(){
		boardAngle+=0.55; //rotate board
		if(player == 2 && boardAngle%(2*Math.PI)>Math.PI){//Player two's turn, stop at their view
			boardAngle = Math.PI;
			t.stop();
		}else if(player == 1 && boardAngle%(2*Math.PI)<0.56){//Player one's turn, stop at their view.
			boardAngle = 0;
			t.stop();
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
