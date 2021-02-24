package application;


import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class HexMap extends Map{
	
	
	
	
	
	private final int[][] hexMapDirections = {
			{2, 0},
			{1, -1},
			{-1, -1},
			{-2, 0},
			{-1, 1},
			{1, 1}
	};
	
	
	
	
	
	
	
	
	
	
	//constructor
	public HexMap() {
		this.setTable(new ArrayList[9]);
		
		for(int i = 0; i < 9; i++) {
			this.getTable()[i] = new ArrayList<Tile>();
		}
		
		this.setMap(new Group());
		this.setCircles(new Group());
		this.setAllTroops(new Group());
		
		this.setAll(new Group());
		
		generate();
		linkTiles();
		
		
		getAll().getChildren().addAll(getMap(), getCircles(), getAllTroops());
		
		
		//FOR TESTING
		
		
		
		
		
		
		//IN THE END OF SampleController
		
		//map.randomPlayerDistribution(map.getSettings());
		//map.setCurrentPlayer(map.nextPlayer());
		//map.distributeInitialReiforcementTroops(map.getSettings());
		//map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " have " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
		//map.setCurrentPhase(new ReinforcementPhase(map));
		
		this.setCurrentPhase(new FortifyPhase(this));
	}
	
	
	
	
	
	@Override
	public void generate() {
		int i = 0;
		Group gBottomRight = addContinent(new Hex(5, 1), getTileColors()[i++]);
		Group gTopRight = addContinent(new Hex(4, -2), getTileColors()[i++]);
		Group gTop = addContinent(new Hex(-1, -3), getTileColors()[i++]);
		Group gTopLeft = addContinent(new Hex(-5, -1), getTileColors()[i++]);
		Group gBottomLeft = addContinent(new Hex(-4, 2), getTileColors()[i++]);
		Group gBottom = addContinent(new Hex(1, 3), getTileColors()[i++]);
		Group gCenter = addContinent(new Hex(0, 0), getTileColors()[i++]);
		
		getMap().getChildren().addAll(gTop, gBottom, gTopRight, gTopLeft, gBottomRight, gBottomLeft, gCenter);
	}
	
	
	
	@Override
	public void linkTiles() {
		for(int i = 0; i < this.getTable().length; i++) {
			for(Tile tile: this.getTable()[i]) {
				assignNeighbors(tile);
			}
				
		}
	}
	
	@Override
	public void assignNeighbors(Tile tile){
		
		int col = tile.getCol();
		int row = tile.getRow();
		
		if(row != -4) {
			
			for(Tile h: this.getTable()[row + 3]) {
				if(h.getCol() == col + 1) {
					tile.setNeighbor(h, 1);
				}
				
				else if(h.getCol() == col - 1) {
					tile.setNeighbor(h, 2);
				}
			}
		}
		
		for(Tile h: this.getTable()[row + 4]) {
			if(h.getCol() == col + 2) {
				tile.setNeighbor(h, 0);
			}
			
			else if(h.getCol() == col - 2) {
				tile.setNeighbor(h, 3);
			}
		}
		
		if(row != 4) {
			
			for(Tile h: this.getTable()[row + 5]) {
				if(h.getCol() == col + 1) {
					tile.setNeighbor(h, 5);
				}
				
				else if(h.getCol() == col - 1) {
					tile.setNeighbor(h, 4);
				}
			}
		}
	}
	
	@Override
	public Group addContinent(Hex hex, Color c){
		Group continent = new Group();
		
		hex.setTileColor(c);
		hex.setFill(c);
		
		continent.getChildren().add(hex);
		getCircles().getChildren().add(hex.getCircle());
		getAllTroops().getChildren().add(hex.getShowTroops());
		
		
		this.setNumberOfTiles(this.getNumberOfTiles() + 1);
		
		hex.setOnMouseClicked(e-> getCurrentPhase().onClick(hex, this));
		hex.getCircle().setOnMouseClicked(e-> getCurrentPhase().onClick(hex, this));
		hex.getShowTroops().setOnMouseClicked(e-> getCurrentPhase().onClick(hex, this));
		
		disableTileSelection(hex);
		
		this.getTable()[hex.getRow() + 4].add(hex);
		
		
		for(int i = 0; i < 6; i++) {
			int col = hex.getCol() + hexMapDirections[i][0];
			int row = hex.getRow() + hexMapDirections[i][1];
			Hex h = new Hex(col, row);
			
			
			h.setTileColor(c);
			h.setFill(c);
			
			continent.getChildren().add(h);
			getCircles().getChildren().add(h.getCircle());
			getAllTroops().getChildren().add(h.getShowTroops());
			
			
			this.setNumberOfTiles(this.getNumberOfTiles() + 1);
			
			h.setOnMouseClicked(e-> getCurrentPhase().onClick(h, this));
			h.getCircle().setOnMouseClicked(e-> getCurrentPhase().onClick(h, this));
			h.getShowTroops().setOnMouseClicked(e-> getCurrentPhase().onClick(h, this));
			
			disableTileSelection(h);
			
			this.getTable()[h.getRow() + 4].add(h);
		}
		return continent;
	}
	
	
	
	
	@Override
	public void addReinforcementTroopsFromContinents(Player player) {
		for(Node g: this.getMap().getChildren()) {
			int counter = 0;
			boolean center = false;
			if(g instanceof Group){
	        	for(Node tile: ((Group) g).getChildren()) {
	        		if(tile instanceof Tile) {
	        			if(((Tile) tile).getOwnership() == player.getId()) {
	        				counter++;
	        			}
	        			
	        			if(((Tile) tile).getCol() == 0 && ((Tile) tile).getRow() == 0) {
	        				center = true;
	        			}
	        		}
	        		if(counter == 7 && center) {
	        			player.setNumberOfReinforcementTroops(player.getNumberOfReinforcementTroops() + 7);
	        		}
	        		
	        		else if(counter == 7) {
	        			player.setNumberOfReinforcementTroops(player.getNumberOfReinforcementTroops() + 4);
	        		}
	        	}
	        }
		}
	}
	
	@Override
	public void addReinforcementTroopsFromOwnedTiles(Player player) {
		if(player.getNumberOfOwnedTiles() < 12) {
			player.setNumberOfReinforcementTroops(player.getNumberOfReinforcementTroops() + 3);
		}
		else {
			player.setNumberOfReinforcementTroops(player.getNumberOfReinforcementTroops() + player.getNumberOfOwnedTiles() / 3);
		}
	}
	
	
	@Override
	
	public boolean bfs(Tile source, Tile dest) {
		ArrayList<Tile> toClear = new ArrayList<Tile>();
		ArrayList<Tile> linkedFriendlyTiles = new ArrayList<Tile>();
		
		source.setVisited(2);
		toClear.add(source);
		linkedFriendlyTiles.add(source);
		
		while(linkedFriendlyTiles.size() != 0) {
			for(int i = 0; i< 6; i++) {
				if(linkedFriendlyTiles.get(0).getNeighbors()[i] != null && linkedFriendlyTiles.get(0).getOwnership() == linkedFriendlyTiles.get(0).getNeighbors()[i].getOwnership() && linkedFriendlyTiles.get(0).getNeighbors()[i].getVisited() == 0) {
					
					if(linkedFriendlyTiles.get(0).getNeighbors()[i].equals(dest)) {
						clearVisited(toClear);
						return true;
					}
					
					linkedFriendlyTiles.get(0).getNeighbors()[i].setVisited(1);
					toClear.add(linkedFriendlyTiles.get(0).getNeighbors()[i]);
					linkedFriendlyTiles.add(linkedFriendlyTiles.get(0).getNeighbors()[i]);
				}
				
			}
			linkedFriendlyTiles.get(0).setVisited(2);
			linkedFriendlyTiles.remove(0);
		}
		clearVisited(toClear);
		return false;
	}
	
	
	
	
	
	
	/*public void randomTile() {

		for(Node g:((Group)this.map).getChildren()){
            if(g instanceof Group){
            	((Group) g).getChildren().remove(new Random().nextInt(6));
            }
        }
		
	}*/
	
	
	
	
	
	
	//get /set



}
