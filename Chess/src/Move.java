
// Class: Move
// Written by: Ethan Frank
// Date: Dec 25, 2017
// Description: Represents a move that a player can do
public class Move {
	private Location from;
	private Location to;
	
	public Move(){
		this(new Location(), new Location());
	}
	
	public Move(Location from, Location to){
		this.from = from;
		this.to = to;
	}
	
	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}
	
	public String toString(){
		return "("+from + "_" + to+")";
	}
	
	
}
