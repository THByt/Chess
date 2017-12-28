
// Class: Location
// Written by: Mr Swop
// Date: Dec 20, 2017
// Description: A location has a row and a column

//Modified 12/20/17
//Author: Ethan Frank
//Description: Make instance fields private
public class Location {
	private int row;
	private int column;

	public Location(){
		this(0,0);
	}
	
	public Location(int r, int c){
		row = r;
		column = c;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public Location add(Location l){
		return new Location(row + l.row, column + l.column);
	}
	
	// Method: inBounds
	// Description: Returns true if a locaiton is on the board
	// Params: none
	// Returns: boolean: is the location on the board
	public boolean inBounds(){
		return row>-1 && row <8 && column<8 && column>-1;
	}
	
	public void setRow(int r){
		row = r;
	}
	
	public void setColumn(int c){
		column = c;
	}
	
	public boolean equals(Object o){
		Location l = (Location) o;
		return row==l.getRow() && column==l.getColumn();
	}
	
	public String toString(){
		return row + "," + column;
	}
}
