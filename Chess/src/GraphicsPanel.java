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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel implements MouseListener{
	private final int SQUARE_WIDTH = 90;    // The width of one space on the board.  Constant used for drawing board.
	private final int OFFSET = 30;
	private Location from;   			    // holds the coordinates of the square that the user would like to move from.
	private Location to;   				    // holds the coordinates of the square that the user would like to move to.
	private boolean selected;   			// false until the game has started by somebody clicking on the board.  should also be set to false
	                         				// after an attempted move.
	private Piece[][] board; 				// an 8x8 board of 'Pieces'.  Each spot should be filled by one of the chess pieces or a 'space'. 
	
	public GraphicsPanel(){
		setPreferredSize(new Dimension(SQUARE_WIDTH*8+OFFSET+2,SQUARE_WIDTH*8+OFFSET+2));
		board = new Piece[8][8];
		board[3][4] = new Rook(2);
		board[7][0] = new Rook(1);
		board[2][4] = new Bishop(2);
		board[5][7] = new King(1);
		
        this.setFocusable(true);					 // for keylistener
		this.addMouseListener(this);
	}
	
	// method: paintComponent
	// description: This method will paint the items onto the graphics panel.  This method is called when the panel is
	//   			first rendered.  It can also be called by this.repaint()
	// parameters: Graphics g - This object is used to draw your images onto the graphics panel.
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw the board
		g2.setColor(Color.gray);
		g2.drawLine(SQUARE_WIDTH*8+OFFSET, OFFSET, SQUARE_WIDTH*8+OFFSET, SQUARE_WIDTH*8+OFFSET);
		g2.drawLine(OFFSET, SQUARE_WIDTH*8+OFFSET, SQUARE_WIDTH*8+OFFSET, SQUARE_WIDTH*8+OFFSET);
		g2.drawLine(OFFSET, OFFSET, SQUARE_WIDTH*8+OFFSET, 0+OFFSET);
		g2.drawLine(OFFSET, OFFSET, OFFSET, SQUARE_WIDTH*8+OFFSET);
		
		for(int c = 0; c <8; c+=2){
			for (int r = 0; r<8; r+=2){
				g2.setColor(Color.gray);
				g2.fillRect(c*SQUARE_WIDTH+OFFSET,r*SQUARE_WIDTH+OFFSET,SQUARE_WIDTH,SQUARE_WIDTH);
			}
		}
		
		for(int c = 1; c<8; c+=2){
			for (int r = 1; r<8; r+=2){
				g2.setColor(Color.gray);
				g2.fillRect(c*SQUARE_WIDTH+OFFSET,r*SQUARE_WIDTH+OFFSET,SQUARE_WIDTH,SQUARE_WIDTH);
			}
		}
		
		if(selected){
			for(int c = 0; c <8; c++){
				for (int r = 0; r<8; r++){
					Piece p = Piece.getPieceAtLocation(from, board);
					to = new Location(r,c);
					if(p!=null && p.isValidMove(from, to, board)){
						g2.setColor(new Color(100,255,100));
						g2.fillOval(c*SQUARE_WIDTH+OFFSET+10,r*SQUARE_WIDTH+OFFSET+10,SQUARE_WIDTH-20,SQUARE_WIDTH-20);
					}
				}
			}
		}

		// instead of drawing a single piece you should loop through the two-dimensional array and draw each piece except for 
		// empty spaces.
		//p.draw(g2, this, new Location(4,6));
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
		int r = Math.max(Math.min((e.getY()-OFFSET)/SQUARE_WIDTH, 7), 0); // use math to figure out the row and column that was clicked.
		int c = Math.max(Math.min((e.getX()-OFFSET)/SQUARE_WIDTH, 7), 0);
		System.out.println("r = " + r);
		System.out.println("c = " + c);
		selected = true; //TODO make this function properly
		from = new Location(r,c);
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
