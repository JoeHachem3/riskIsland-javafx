package application;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public abstract class Phase {
	
	private Tile firstTile = null;
	private Tile secondTile = null;
	
	

	public abstract void onClick(Tile tile, Map map);
	
	public abstract void nextPhase(Map map);
	
	public abstract void setSliderProperty(Map map,  Slider slider, Button button);
	
	public abstract void moveTroops(Map map, int troops);
	
	public abstract void attack(Map map);
	
	public abstract void retreat(Map map);
	
	

	
//get /set

	public Tile getFirstTile() {
		return firstTile;
	}

	public void setFirstTile(Tile firstTile) {
		this.firstTile = firstTile;
	}

	public Tile getSecondTile() {
		return secondTile;
	}

	public void setSecondTile(Tile secondTile) {
		this.secondTile = secondTile;
	}

	
}