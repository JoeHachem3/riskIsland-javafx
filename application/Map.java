package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public abstract class Map {
	
	private Group map;
	private Group circles;
	private Group allTroops;
	
	private Group all;
	
	private ArrayList<Tile>[] table;
	
	private Player currentPlayer;
	private Player previousPlayer;
	
	private Settings settings = new Settings();
	
	private Phase currentPhase;
	private int numberOfTiles = 0;
	
	private boolean initialDistributionDone = false;
	
	
	private Color[] circleColors = {Color.web("ff0000"),Color.web("0000ff"),Color.web("29ab87"),Color.web("ffff00"),Color.web("5d34a5"),Color.web("ff7f00")};
	private Color[] tileColors = {Color.web("e1d5a6"),Color.web("a3876a"),Color.web("bd8f76"),Color.web("a15f3b"),Color.web("997962"),Color.web("edc9af"),Color.web("c8b7a6"),};
	
	
	
	
	
	
	
	
	//for listeners
	
	
	private int counter = 0;
	
	private Text slider = new Text("0");		//slider visibility property
	private Text colorBar = new Text("-1");		//color bar background color property //ship to move
	private Text displayedText = new Text();	//displayed text property
	private Text buttonState = new Text();		//attack/retreat/done/hidden
	private Text defeatedPlayer = new Text();	//move ship outside the screen
	private Text end = new Text();				//end of game
	
	
	
	//sounds
	private Media m1 = new Media(new File("C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\sounds\\victorySound.mp3").toURI().toString());	
	private MediaPlayer victorySound = new MediaPlayer(m1);
	
	private Media m2 = new Media(new File("C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\sounds\\fightSound.mp3").toURI().toString());	
	private MediaPlayer fightSound = new MediaPlayer(m2);
	
	//private Media m3 = new Media(new File("C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\sounds\\sailingSound.mp3").toURI().toString());	
	//private MediaPlayer sailingSound = new MediaPlayer(m3);
	
	
	
	
	public Map() {
			fightSound.setVolume(0.1);
			//sailingSound.setVolume(0.5);
	}
	
	public void disableTileSelection(Tile tile) {
		ChangeListener<String> disableEnabletileSelection = new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue == "1" || newValue == "2") {
					tile.setOnMouseClicked(null);
					tile.getCircle().setOnMouseClicked(null);
					tile.getShowTroops().setOnMouseClicked(null);
				}
				else{
					tile.setOnMouseClicked(e-> getCurrentPhase().onClick(tile, Map.this));
					tile.getCircle().setOnMouseClicked(e-> getCurrentPhase().onClick(tile, Map.this));
					tile.getShowTroops().setOnMouseClicked(e-> getCurrentPhase().onClick(tile, Map.this));
				}
			}
			
		};
		getSlider().textProperty().addListener(disableEnabletileSelection);
	}
	
	public void randomPlayerDistribution(Settings s) {
		int nb = s.getPlayers().length;
		int[] temp = new int[nb];
		int sum = 0;
		
		int i;
		for(i = 0; i < nb; i++) {
			temp[i] = this.getNumberOfTiles() / nb;
			sum += temp[i];
		}
		
		i = 0;
		
		while(sum != this.getNumberOfTiles()) {
			temp[i]++;
			sum++;
			i++;
		}
		
		
		for(i = 0; i < this.getTable().length; i++) {
			for(Tile tile: this.getTable()[i]) {
				int j = new Random().nextInt(nb);
				
				while(s.getPlayers()[j].getNumberOfOwnedTiles() == temp[j]) {
					if(j< nb-1) {
						j++;
					}
					else {
						j = 0;
					}
				}
					
				tile.setOwnership(s.getPlayers()[j].getId());
				s.getPlayers()[j].getOwnedTiles().add(tile);
				s.getPlayers()[j].setNumberOfOwnedTiles(s.getPlayers()[j].getNumberOfOwnedTiles() + 1);
				tile.getCircle().setFill(this.getCircleColors()[j]);
				tile.setCircleColor(this.getCircleColors()[j]);
				
			}
		}
	}
	
	
	public void distributeInitialReiforcementTroops(Settings s) {
		for(int i = 0; i < s.getPlayers().length; i++) {
			s.getPlayers()[i].setNumberOfReinforcementTroops(50 - s.getPlayers().length * 5 - s.getPlayers()[i].getNumberOfOwnedTiles());
		}
	}
	
	
	public Player nextPlayer() {
		//stopSound(getSailingSound());
		if(currentPlayer == null) {
			currentPlayer = settings.getPlayers()[new Random().nextInt(settings.getPlayers().length - 1)];
			
			highlightOwnedTiles();
			getColorBar().setText("" + getCurrentPlayer().getId());
			return currentPlayer;
		}
		counter = 0;
		
		do {
			counter++;
			
			previousPlayer = currentPlayer;
			
			if(currentPlayer.getId() == (settings.getPlayers().length - 1)) {
				currentPlayer = settings.getPlayers()[0];
			}
			else {
				currentPlayer = settings.getPlayers()[currentPlayer.getId() + 1];
			}
			
			
		}while((currentPlayer.isDefeated() || currentPlayer.getNumberOfReinforcementTroops() == 0) && counter <= this.getSettings().getPlayers().length);
		//playSound(getSailingSound());
		highlightOwnedTiles();
		getColorBar().setText("" + getCurrentPlayer().getId());
		return currentPlayer;
	}
	
	
	public void highlightOwnedTiles() {
		for(int i = 0; i < table.length; i++) {
			for(Tile tile: this.table[i]){
				if(tile.getOwnership() == this.currentPlayer.getId()) {
					tile.setStrokeWidth(3);
					tile.setStroke(tile.getCircleColor());
					tile.toFront();
				}
				else {
					tile.setStrokeWidth(1);
					tile.setStroke(Color.BLACK);
					tile.toBack();
				}
			}
		}
	}
	
	public void highlightNeighboringEnemyTiles(Tile tile) {
		for(int i = 0; i< 6; i++) {
	
			if(tile.getNeighbors()[i] != null && tile.getOwnership() != tile.getNeighbors()[i].getOwnership()) {
				tile.getNeighbors()[i].setFill(Color.DARKRED);
			}
		}
	}
	
	public void delightNeighboringEnemyTiles(Tile tile) {
		for(int i = 0; i< 6; i++) {
			if(tile.getNeighbors()[i] != null) {
				tile.getNeighbors()[i].setFill(tile.getNeighbors()[i].getTileColor());
			}
		}
	}
	
	

	public void clearVisited(ArrayList<Tile> toClear) {
		for (Tile tile: toClear) {
			tile.setVisited(0);
		}
	}
	
	

	//sounds
	
	public void playSound(MediaPlayer mediaPlayer) {
		mediaPlayer.play();
	}
	
	public void stopSound(MediaPlayer mediaPlayer) {
		mediaPlayer.stop();
	}
	
	
	
	public abstract void addReinforcementTroopsFromOwnedTiles(Player player);
	
	public abstract void addReinforcementTroopsFromContinents(Player player);
	
	public abstract Group addContinent(Hex hex, Color c);
	
	public abstract void assignNeighbors(Tile tile);
	
	public abstract void linkTiles();
	
	public abstract void generate();
	
	public abstract boolean bfs(Tile source, Tile dest);

	
	
//get/ set
	public int getNumberOfTiles() {
		return numberOfTiles;
	}
	
	public void setNumberOfTiles(int numberOfTiles) {
		this.numberOfTiles = numberOfTiles;
	}

	public Group getMap() {
		return map;
	}

	public void setMap(Group map) {
		this.map = map;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public Phase getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}
	
	public Group getCircles() {
		return circles;
	}

	public void setCircles(Group circles) {
		this.circles = circles;
	}

	public Group getAllTroops() {
		return allTroops;
	}

	public ArrayList<Tile>[] getTable() {
		return table;
	}

	public void setTable(ArrayList<Tile>[] table) {
		this.table = table;
	}

	public void setAllTroops(Group allTroops) {
		this.allTroops = allTroops;
	}

	public Group getAll() {
		return all;
	}

	public void setAll(Group all) {
		this.all = all;
	}

	public Color[] getCircleColors() {
		return circleColors;
	}

	public void setCircleColors(Color[] circleColors) {
		this.circleColors = circleColors;
	}

	public Color[] getTileColors() {
		return tileColors;
	}

	public void setTileColors(Color[] tileColors) {
		this.tileColors = tileColors;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isInitialDistributionDone() {
		return initialDistributionDone;
	}

	public void setInitialDistributionDone(boolean initialDistributionDone) {
		this.initialDistributionDone = initialDistributionDone;
	}

	public Text getSlider() {
		return slider;
	}

	public void setSlider(Text slider) {
		this.slider = slider;
	}

	public Text getColorBar() {
		return colorBar;
	}

	public void setColorBar(Text colorBar) {
		this.colorBar = colorBar;
	}

	public Text getDisplayedText() {
		return displayedText;
	}

	public void setDisplayedText(Text displayedText) {
		this.displayedText = displayedText;
	}

	public Player getPreviousPlayer() {
		return previousPlayer;
	}

	public void setPreviousPlayer(Player previousPlayer) {
		this.previousPlayer = previousPlayer;
	}

	public Text getButtonState() {
		return buttonState;
	}

	public void setButtonState(Text buttonState) {
		this.buttonState = buttonState;
	}

	public Text getDefeatedPlayer() {
		return defeatedPlayer;
	}

	public void setDefeatedPlayer(Text defeatedPlayer) {
		this.defeatedPlayer = defeatedPlayer;
	}

	public MediaPlayer getVictorySound() {
		return victorySound;
	}

	public void setVictorySound(MediaPlayer victorySound) {
		this.victorySound = victorySound;
	}

	public MediaPlayer getFightSound() {
		return fightSound;
	}

	public void setFightSound(MediaPlayer fightSound) {
		this.fightSound = fightSound;
	}

	public Text getEnd() {
		return end;
	}

	public void setEnd(Text end) {
		this.end = end;
	}

	/*public MediaPlayer getSailingSound() {
		return sailingSound;
	}

	public void setSailingSound(MediaPlayer sailingSound) {
		this.sailingSound = sailingSound;
	}*/
}
