
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
	
	public Location(int r, int c, int player){
		row = r;
		column = c;
		if(player==2)this.flip();
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public void setRow(int r){
		row = r;
	}
	
	public void setColumn(int c){
		column = c;
	}
	
	public void flip(){
		row = 7-row;
		//column = 7-column; CHESS IS NOT ROTATIONALLY SYMETRIC
	}
	
	public boolean equals(Object o){
		Location l = (Location) o;
		return row==l.getRow() && column==l.getColumn();
	}
	
	public String toString(){
		return row + ", " + column;
	}
}
