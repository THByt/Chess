// Class: Piece
// Written by: Mr. Swope
// Date: 10/28/15
// Description: This class implements a Piece.  This Piece will be drawn onto a graphics panel. 

//Modified 12/20/17 by Ethan Frank
//Description
import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;

public class Piece {
	private ImageIcon image;			// The ImageIcon will be used to hold the Character's png.
	
	private int player;					// This int will represent which team the piece is, 1 for yellow team, 
									    // 2 for black team. 
	
	// method: Default constructor - see packed constructors comments for a description of parameters.
	public Piece(){
		this("pawn", 1);
	}
		
	// method: Piece's actually useful constructor
	// description: Initialize a new Piece object with a certain image
	// parameters: int player - should be either 1 or 2. 1 for yellow team, 2 for black team.
	//				String piece: the name of the piece
	public Piece(String piece, int player){
		String imagePath = "images2/" + piece + player + ".png";
		setImageIcon(imagePath);
		this.setPlayer(player);			
	}
	
	// method: Character's packed constructor
	// description: Initialize a new Character object.
	// parameters: int player - should be either 1 or 2. 1 for yellow team, 2 for black team.
	public Piece(int player, String imagePath){
		setImageIcon(imagePath);
		this.player = player;			
	}
	
	// Method: setImageIcon
	// Description: Instantiates image instance field with an image at the path imagePath
	// Params: String imagePath: path to image to instantiate
	// Returns: void
	protected void setImageIcon(String imagePath){
		ClassLoader cldr = this.getClass().getClassLoader();	
		
		URL imageURL = cldr.getResource(imagePath);				
        image = new ImageIcon(imageURL);
	}
	
	// method: isValidMove
	// description: This method checks to see if a move is valid.
	// Returns whether or not the attempted move is valid.
	// @param - Location from - the location that the piece will be moved from
	// @param - Location to - the location that the piece will be moved to
	// @param - Piece[][]b - the chess board.  a two dimensional array of pieces.
	// return - boolean - true if the move is valid 
	public boolean isValidMove(Location from, Location to, Piece[][]b){
		return false;
	}
	
	// method: draw
	// description: This method is used to draw the image onto the GraphicsPanel.  You shouldn't need to 
	//				modify this method.
	// parameters: Graphics g - this object draw's the image.
	//			   Component c - this is the component that the image will be drawn onto.
	//			   Location l - a Location that determines where to draw the piece.
	public void draw(Graphics g, Component c, Location l) {
        image.paintIcon(c, g, l.getColumn()*75, l.getRow()*90); // you'll need to update the last two parameters so that it will 
        											  // correctly draw the piece in the right location.
    }

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
}