
// Class: King
// Written by: Ethan Frank
// Date: Dec 21, 2017
// Description: Kings can only move one space in any direction
public class King extends Piece{
	public King(int player){
		super("king", player);
	}
	
	//Kings move one space any direction. This means delta y and delta x need to have magnitude less than or equal to 1
	@Override
	public boolean isValidMoveSpecific(Location from, Location to, Piece[][] b) {
		Location rookLocation = new Location(7,7,player);
		
		return (Math.abs(from.getRow()-to.getRow())<=1
					&&	Math.abs(from.getColumn()-to.getColumn())<=1)
					&& (Piece.getPieceAtLocation(to, b)==null || Piece.getPieceAtLocation(to, b).getPlayer()!=player) ||
					(moved==0 && rookLocation.equals(to) &&
					GraphicsPanel.canACertainPlayerCastleButItOnlyChecksCertainConditionsBecauseOthersAreCheckedElsewhere(b, player) &&
					Piece.getPieceAtLocation(to, b) instanceof Rook && Piece.getPieceAtLocation(to, b).getMoved() == 0);
	}
}
