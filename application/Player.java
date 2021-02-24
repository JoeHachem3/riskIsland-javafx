package application;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Player {

	private String name;
	private int id;
	private Color color;
	private ArrayList<Tile> ownedTiles = new ArrayList<Tile>();
	private int numberOfOwnedTiles;
	private int numberOfReinforcementTroops;
	private boolean defeated = false;
	private ImageView ship = new ImageView();
	
	
	
	
	public Player(String name, Color color, int id) {
		this.name = name;
		this.color = color;
		this.id = id;
	}
	
	
	
	
	//get /set
	
	public ArrayList<Tile> getOwnedTiles() {
		return ownedTiles;
	}
	public void setOwnedTiles(ArrayList<Tile> ownedTiles) {
		this.ownedTiles = ownedTiles;
	}
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}



	
	public int getNumberOfReinforcementTroops() {
		return numberOfReinforcementTroops;
	}
	
	public void setNumberOfReinforcementTroops(int numberOfReinforcementTroops) {
		this.numberOfReinforcementTroops = numberOfReinforcementTroops;
	}

	public void setNumberOfReinforcementTroops(Map map) {
		
		map.addReinforcementTroopsFromOwnedTiles(this);
		map.addReinforcementTroopsFromContinents(this);
		
	}



	public boolean isDefeated() {
		return defeated;
	}




	public void setDefeated(boolean defeated) {
		this.defeated = defeated;
	}




	public int getNumberOfOwnedTiles() {
		return numberOfOwnedTiles;
	}




	public void setNumberOfOwnedTiles(int numberOfOwnedTiles) {
		this.numberOfOwnedTiles = numberOfOwnedTiles;
	}




	public ImageView getShip() {
		return ship;
	}




	public void setShip(ImageView ship) {
		this.ship = ship;
	}


}
