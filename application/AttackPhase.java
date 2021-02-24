package application;

import java.util.Random;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AttackPhase extends Phase{
	
	public AttackPhase(Map map) {
		super();
		map.getButtonState().setText("1");
		map.getDisplayedText().setText("select a terrority to attack from");
	}
	

	@Override
	public void onClick(Tile tile, Map map) {
		
		if(getFirstTile() != null) {
			for(int i = 0; i< 6; i++) {
				if(getFirstTile().getNeighbors()[i] != null) {
					getFirstTile().getNeighbors()[i].setFill(getFirstTile().getNeighbors()[i].getTileColor());
				}
			}
		}
			
		if(getFirstTile() == null) {
			
			if(tile.getOwnership() == map.getCurrentPlayer().getId() && tile.getTroops() > 1) {
				
				map.highlightNeighboringEnemyTiles(tile);
				
				setFirstTile(tile);
				map.getDisplayedText().setText("select a neighboring enemy terrority to attack");
			}
			return;
		}
		
		
		
			if(tile.getOwnership() != map.getCurrentPlayer().getId() && tile.checkNeighbor(getFirstTile())) {
				map.getSlider().setText("2");
				setSecondTile(tile);
				tile.setFill(Color.DARKRED);
				map.getButtonState().setText("2");
				tile.toFront();
				
				//sound
				map.playSound(map.getFightSound());
				map.getDisplayedText().setText("");
			}
			else if(tile.equals(getFirstTile())){
				for(int i = 0; i< 6; i++) {
					if(tile.getNeighbors()[i] != null) {
						tile.getNeighbors()[i].setFill(tile.getNeighbors()[i].getTileColor());
					}
				}
				setFirstTile(null);
				map.getButtonState().setText("1");
				map.getDisplayedText().setText("select a terrority to attack from");
			}
			else if(tile.getOwnership() == map.getCurrentPlayer().getId() && tile.getTroops() > 1) {
				map.highlightNeighboringEnemyTiles(tile);
				setFirstTile(tile);
				map.getButtonState().setText("1");
				map.getDisplayedText().setText("select a neighboring enemy terrority to attack");
			}
			else if(!tile.checkNeighbor(getFirstTile()) || tile.getTroops() == 1){
				setFirstTile(null);
				map.getButtonState().setText("1");
				map.getDisplayedText().setText("select a terrority to attack from");
			}
	}
	

	@Override
	public void nextPhase(Map map) {
		map.setCurrentPhase(new FortifyPhase(map));
		if(getFirstTile() != null) {
			map.delightNeighboringEnemyTiles(getFirstTile());
		}
	}
	
	@Override
	public void attack(Map map) {
		int nbOfDices = getFirstTile().getTroops() / 20 + 1;
		
		if(getSecondTile().getTroops() < nbOfDices) {
			nbOfDices = getSecondTile().getTroops();
		}
		
		int[] attackerDice = new int[nbOfDices];
		int[] defenderDice = new int[nbOfDices];
		
		for(int i = 0; i < nbOfDices; i++) {
			attackerDice[i] = new Random().nextInt(6) + 1;
			defenderDice[i] = new Random().nextInt(6) + 1;
		}
		
		ScaleTransition t = new ScaleTransition();
		t.setByX(0.2);
		t.setByY(0.2);
		t.setDuration(Duration.millis(100));
		t.setCycleCount(2);
		t.setAutoReverse(true);
		
		for(int i = 0; i < nbOfDices; i++) {
			
			if(attackerDice[i] > defenderDice[i]) {
				getSecondTile().setTroops(getSecondTile().getTroops() - 1);
				
				
				t.setNode(getSecondTile().getCircle());
				t.play();
				
				
				
			}
			else {
				getFirstTile().setTroops(getFirstTile().getTroops() - 1);
				
				
				t.setNode(getFirstTile().getCircle());
				t.play();
				
			}
		}
		getSecondTile().getShowTroops().setText("" + getSecondTile().getTroops());
		getFirstTile().getShowTroops().setText("" + getFirstTile().getTroops());
		
		
		if(getSecondTile().getTroops() == 0) {
			
			map.getCurrentPlayer().getOwnedTiles().add(getSecondTile());
			map.getCurrentPlayer().setNumberOfOwnedTiles(map.getCurrentPlayer().getNumberOfOwnedTiles() + 1);
			
			map.getSettings().getPlayers()[getSecondTile().getOwnership()].getOwnedTiles().remove(getSecondTile());
			map.getSettings().getPlayers()[getSecondTile().getOwnership()].setNumberOfOwnedTiles(map.getSettings().getPlayers()[getSecondTile().getOwnership()].getNumberOfOwnedTiles() - 1);
			
			if(map.getSettings().getPlayers()[getSecondTile().getOwnership()].getNumberOfOwnedTiles() == 0) {
				map.getSettings().getPlayers()[getSecondTile().getOwnership()].setDefeated(true);
				map.getDefeatedPlayer().setText("" + getSecondTile().getOwnership());
			}
			
			getSecondTile().setOwnership(map.getCurrentPlayer().getId());
			
			if(map.getCurrentPlayer().getNumberOfOwnedTiles() == map.getNumberOfTiles()) {
				map.getDisplayedText().setText("The victory is yours!");
			}
			
			getSecondTile().setCircleColor(map.getCurrentPlayer().getColor());
			getSecondTile().getCircle().setFill(getSecondTile().getCircleColor());
			
			getSecondTile().setStrokeWidth(3);
			getSecondTile().setStroke(getSecondTile().getCircleColor());
			
			getSecondTile().setFill(getSecondTile().getTileColor());
			
			if(getFirstTile().getTroops() <= 4) {
				moveTroops(map, getFirstTile().getTroops() - 1);
				setFirstTile(null);
				setSecondTile(null);
			}
			else {
				map.getSlider().setText("1");
				
				//sound
				map.playSound(map.getVictorySound());
			}
			
			getFirstTile().getCircle().setScaleX(1);
			getFirstTile().getCircle().setScaleY(1);

			getSecondTile().getCircle().setScaleX(1);
			getSecondTile().getCircle().setScaleY(1);
			
			//sound
			map.stopSound(map.getFightSound());
		}
		
		else if(getFirstTile().getTroops() == 1){
			getSecondTile().setFill(getSecondTile().getTileColor());
			setFirstTile(null);
			setSecondTile(null);
			
			map.getButtonState().setText("1");
			map.getSlider().setText("0");
			map.stopSound(map.getFightSound());
		}
		return;
	}
	
	@Override
	public void retreat(Map map) {
		map.getSlider().setText("0");
		map.getButtonState().setText("1");
		
		map.stopSound(map.getFightSound());
		
		getSecondTile().setFill(getSecondTile().getTileColor());
		setFirstTile(null);
		setSecondTile(null);
	}
	
	


	@Override
	public void setSliderProperty(Map map, Slider slider, Button button) {
		slider.setMin(3);
		slider.setMax(getFirstTile().getTroops() - 1);
		button.setVisible(false);
		
		map.getButtonState().setText("0");
	}
	
	@Override
	public void moveTroops(Map map, int troops) {
		getSecondTile().setTroops(troops);
		getSecondTile().getShowTroops().setText("" + getSecondTile().getTroops());
		
		getFirstTile().setTroops(getFirstTile().getTroops() - troops);
		getFirstTile().getShowTroops().setText("" + getFirstTile().getTroops());
		
		map.getButtonState().setText("1");
		map.getSlider().setText("0");
		
		map.stopSound(map.getVictorySound());
		
		if(map.getCurrentPlayer().getNumberOfOwnedTiles() == map.getNumberOfTiles()) {
			map.getButtonState().setText("3");
		}
		
		
		setFirstTile(null);
		setSecondTile(null);
	}
	
	
}
