package application;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class FortifyPhase extends Phase{
	
	public FortifyPhase(Map map) {
		super();

		map.getDisplayedText().setText("select a terrority to move troops from");
	}
	
	@Override
	public void onClick(Tile tile, Map map) {
		if(getFirstTile() == null) {
			
			if(tile.getOwnership() == map.getCurrentPlayer().getId() && tile.getTroops() > 1) {
				
			//	highlightNeighboringOwnedTiles(tile, map);
				tile.setFill(Color.GOLD);
				setFirstTile(tile);
			}
			map.getDisplayedText().setText("select a terrority to move troops to");
			return;
		}
		
		
		
		
		if(map.bfs(getFirstTile(), tile)){
			tile.setFill(Color.GOLD);
			
			setSecondTile(tile);
			map.getSlider().textProperty().set("1");
			map.getDisplayedText().setText("");
		}
		
		else if(tile.equals(getFirstTile())){
			getFirstTile().setFill(getFirstTile().getTileColor());
			
			map.getDisplayedText().setText("select a terrority to move troops from");
			setFirstTile(null);
		}
		
		else if(tile.getOwnership() == map.getCurrentPlayer().getId() && tile.getTroops() > 1) {
			getFirstTile().setFill(getFirstTile().getTileColor());
			tile.setFill(Color.GOLD);
			map.getDisplayedText().setText("select a terrority to move troops to");
			setFirstTile(tile);
		}
		
		else {
			map.getDisplayedText().setText("select a terrority to move troops from");
			getFirstTile().setFill(getFirstTile().getTileColor());
			setFirstTile(null);
		}
	}

	
	@Override
	public void attack(Map map) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void retreat(Map map) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void nextPhase(Map map) {
		map.setCurrentPhase(new ReinforcementPhase(map));
	}
	
	
	
	@Override
	public void setSliderProperty(Map map, Slider slider, Button button) {
		slider.setMin(1);
		slider.setMax(getFirstTile().getTroops() - 1);
		button.setVisible(true);
		map.getButtonState().setText("0");
	}

	
	@Override
	public void moveTroops(Map map, int troops) {
		getSecondTile().setTroops(getSecondTile().getTroops() + troops);
		getSecondTile().getShowTroops().setText("" + getSecondTile().getTroops());
		
		getFirstTile().setTroops(getFirstTile().getTroops() - troops);
		getFirstTile().getShowTroops().setText("" + getFirstTile().getTroops());
		
		map.setCurrentPhase(new ReinforcementPhase(map));
	}
	
	
	
	
	/*public void highlightNeighboringOwnedTiles(Tile tile, Map map) {
		for(int i = 0; i< 6; i++) {
			
			if(tile.getNeighbors()[i] != null && tile.getOwnership() == tile.getNeighbors()[i].getOwnership()) {
				tile.getNeighbors()[i].setFill(Color.GOLD);
			}
			
		}
	}*/


	
	
}
