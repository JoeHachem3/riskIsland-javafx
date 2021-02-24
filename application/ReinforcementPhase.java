package application;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class ReinforcementPhase extends Phase{
	
	public ReinforcementPhase(Map map) {
		super();
		
		if(map.isInitialDistributionDone()) {
			map.setCurrentPlayer(map.nextPlayer());
			map.getCurrentPlayer().setNumberOfReinforcementTroops(map);
			map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " has " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
		}
		map.getButtonState().setText("0");
	}
	
	
	@Override
	public void onClick(Tile tile, Map map) {
		//map.stopSound(map.getSailingSound());
		if(tile.getOwnership() == map.getCurrentPlayer().getId()) {
			tile.setTroops(tile.getTroops() + 1);
			tile.getShowTroops().setText("" + tile.getTroops());
			
			map.getCurrentPlayer().setNumberOfReinforcementTroops(map.getCurrentPlayer().getNumberOfReinforcementTroops() - 1);
			map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " has " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
			
			if(!map.isInitialDistributionDone()) {
				map.setCurrentPlayer(map.nextPlayer());
				map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " has " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
				if(map.getCounter() > map.getSettings().getPlayers().length) {
					map.setInitialDistributionDone(true);
					map.getCurrentPlayer().setNumberOfReinforcementTroops(map);
					map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " has " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
				}
			}
			else {
				if(map.getCurrentPlayer().getNumberOfReinforcementTroops() == 0) {
					map.getDisplayedText().setText("");
					map.setCurrentPhase(new AttackPhase(map));
				}
			}
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
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setSliderProperty(Map map, Slider slider, Button button) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void moveTroops(Map map, int Troops) {
		// TODO Auto-generated method stub
	}
	
}
