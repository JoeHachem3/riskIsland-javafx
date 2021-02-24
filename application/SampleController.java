package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SampleController {
	@FXML
	private VBox container;
	@FXML
	private Pane playerBar;
	@FXML
	private Text editableText;
	@FXML
	private Button attackOrDoneButton;
	@FXML
	private Button retreatButton;
	@FXML
	private VBox troopSliderBox;
	@FXML
	private Slider troopSlider;
	@FXML
	private Button troopSliderMoveButton;
	@FXML
	private Button troopSliderCancelButton;
	@FXML
	private Text troopSliderText;
	@FXML
	private VBox ships;
	@FXML
	private BorderPane root;
	@FXML
	private HBox bottom;
	@FXML
	private Button exitButton;
	@FXML
	private VBox changeable;
	
	Map map;
	VBox mainMenu;
	HBox numberOfPlayersBox;
	Label playersLabel;
	Spinner<Integer> numberOfPlayers;
	VBox listOfPlayers;
	HBox player;
	TextField name;
	Region color;
	Button playButton;
	Text noName;
		
	ArrayList<ImageView> mainMenuShips;
	private int t;
	
	private TranslateTransition moveShip;
	
	private int[] shipDistance;
	
	public void initialize() {
	
		container.getChildren().clear();
		ships.getChildren().clear();
		
		ships.toBack();
		
		troopSliderBox.setOpacity(0.9);
		troopSliderBox.translateYProperty().bind((root.heightProperty().subtract(troopSliderBox.getHeight() + 200)).divide(2));
		
		playerBar.styleProperty().set("-fx-background-color: #b99361; -fx-background-radius: 5;");
		editableText.setText("");
		
		map = new HexMap();
		mainMenu = new VBox(10);
		numberOfPlayersBox = new HBox(10);
		playersLabel = new Label("players");
		numberOfPlayers = new Spinner<Integer>(2,6,2);
		listOfPlayers = new VBox(10);
		playButton = new Button();
		noName = new Text("you can't lead your army with no name...");
		noName.setStyle("-fx-fill: red; -fx-font-size: 14;");
		
		mainMenuShips = new ArrayList<ImageView>();
		
		
		container.setAlignment(Pos.CENTER);
		container.translateXProperty().bind(root.widthProperty().divide(2));
		container.translateYProperty().bind(root.heightProperty().divide(2));
		
		exitButton.setVisible(false);
		
		attackOrDoneButton.setVisible(false);
		
		retreatButton.setVisible(false);
		
		mainMenu.setAlignment(Pos.CENTER);
		
		numberOfPlayers.setMaxWidth(50);
		numberOfPlayers.setEditable(true);
		numberOfPlayersBox.setAlignment(Pos.CENTER);
		
		numberOfPlayersBox.getChildren().addAll(playersLabel, numberOfPlayers);
		
		
		ships.setTranslateX(0);
    	ships.setTranslateY(0);
    	
    	ships.layoutXProperty().bind(root.widthProperty());
    	ships.layoutYProperty().bind(root.heightProperty().divide(2).subtract(50));
		
		
    	
		
		
		for(int i = 0; i < numberOfPlayers.getValue(); i++) {
			player = new HBox(10);
			player.setAlignment(Pos.CENTER);
			color = new Region();
			color.setMaxWidth(30);
			color.setMaxHeight(30);
			color.setMinWidth(30);
			color.setMinHeight(30);
			color.setPrefWidth(30);
			color.setPrefHeight(30);
			color.styleProperty().set("-fx-background-color: #" + map.getCircleColors()[i].toString().substring(2, 8) + "; -fx-background-radius: 5;");
			name = new TextField();
			player.getChildren().addAll(color, name);
			listOfPlayers.getChildren().add(player);
			
			mainMenuShips.add(i, new ImageView(new Image("file:C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\img\\rotatedShip" + i + ".png"))); 
			ships.getChildren().add(mainMenuShips.get(i));
			
			Line path = new Line();
			path.setStartX(-2000);
			path.setStartY(-40 * i);
			path.setEndX(200 + 100 * i);
			path.setEndY(100 +70 * i);
			
			//Path path = new Path();
			//path.getElements().add(new MoveTo(20,20));
			//path.getElements().add(new CubicCurveTo(380, 0, 380, 500, 200, 500));
			//path.getElements().add(new CubicCurveTo(0, 500, 0, 240, 380, 240));
			
			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(new Random().nextInt(1000) + 10000));
			pathTransition.setNode(mainMenuShips.get(i));
			pathTransition.setPath(path);
			pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
			pathTransition.setCycleCount(Timeline.INDEFINITE);
			pathTransition.setAutoReverse(false);
			
		 
			pathTransition.play();
		     
		     
		}
		
		listOfPlayers.setMinHeight(225);
		
		
		playButton.setText("To Battle");
		playButton.setOnAction(e-> verify());
		playButton.setStyle("-fx-background-color: radial-gradient( focus-angle 0.0deg, focus-distance 0.0%, center 50.0% 50.0%, radius 100.0%, rgb(204,255,204) 0.0, rgb(0,51,0) 100.0 ); -fx-text-fill: black;");
		
		playButton.setOnMouseEntered(e-> playButton.setStyle("-fx-background-color: radial-gradient( focus-angle 0.0deg, focus-distance 0.0%, center 50.0% 50.0%, radius 100.0%, #ccffcc 0.0, #336633 100.0 );"));
		playButton.setOnMouseExited(e-> playButton.setStyle("-fx-background-color: radial-gradient( focus-angle 0.0deg, focus-distance 0.0%, center 50.0% 50.0%, radius 100.0%, rgb(204,255,204) 0.0, rgb(0,51,0) 100.0 ); -fx-text-fill: black;"));
		playButton.setCursor(Cursor.HAND);
		
		mainMenu.getChildren().addAll(numberOfPlayersBox, listOfPlayers, playButton);
		
		
		
		mainMenu.getChildren().add(mainMenu.getChildren().size()-1, noName);
		noName.setVisible(false);
		
		ChangeListener<Number> selectedNumberOfPlayers = new ChangeListener<Number>() {
        	
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				
				int difference = ((int) newValue) - ((int) oldValue);
				
				if(difference > 0) {
					for(int i = 0; i < difference; i++) {
						player = new HBox(10);
						player.setAlignment(Pos.CENTER);
						color = new Region();
						color.setMaxWidth(30);
						color.setMaxHeight(30);
						color.setMinWidth(30);
						color.setMinHeight(30);
						color.setPrefWidth(30);
						color.setPrefHeight(30);
						color.styleProperty().set("-fx-background-color: #" + map.getCircleColors()[((int) oldValue) + i].toString().substring(2, 8) + "; -fx-background-radius: 5;");
						name = new TextField();
						player.getChildren().addAll(color, name);
						listOfPlayers.getChildren().add(player);
						
						int n = ((int) oldValue) + i;
						
						mainMenuShips.add(n, new ImageView(new Image("file:C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\img\\rotatedShip" + n + ".png"))); 
						ships.getChildren().add(mainMenuShips.get(n));
						
						Line path = new Line();
						path.setEndX(-2000);
						path.setEndY(-20 * n);
						path.setStartX(1800 + 100 * n);
						path.setStartY(500 -7 * n);
						
						
						PathTransition pathTransition = new PathTransition();
						pathTransition.setDuration(Duration.millis(new Random().nextInt(1000) + 10000));
						pathTransition.setNode(mainMenuShips.get(n));
						pathTransition.setPath(path);
						pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
						pathTransition.setCycleCount(Timeline.INDEFINITE);
						pathTransition.setAutoReverse(false);
						
					 
						pathTransition.play();
						
					}
				}
					
				else {
					for(int i = 0; i < -difference; i++) {
						int n = ((int) newValue) + i;
						listOfPlayers.getChildren().remove(listOfPlayers.getChildren().size() - 1);
						ships.getChildren().remove(n);
						mainMenuShips.remove(n);
					}
				}
			}
			
		};
		numberOfPlayers.valueProperty().addListener(selectedNumberOfPlayers);
		
		container.getChildren().add(mainMenu);
		
		
		
		
		
		
		mainMenu.setTranslateY(-500);
		
		
		TranslateTransition mainMenuTransition = new TranslateTransition();
		mainMenuTransition.setNode(mainMenu);
		mainMenuTransition.setDuration(Duration.millis(250));
		mainMenuTransition.setDelay(Duration.seconds(1));
		mainMenuTransition.setToY(mainMenu.getTranslateY() + 500);
		mainMenuTransition.play();
		
	}
	
	public void verify() {
		HBox tester;
		TextField nameTester;
		Player[] p = new Player[listOfPlayers.getChildren().size()];
		for (int i = 0; i < listOfPlayers.getChildren().size(); i++) {
			tester = (HBox) listOfPlayers.getChildren().get(i);
			nameTester = (TextField) tester.getChildren().get(1);
			
			if(nameTester.getText().isEmpty()) {
				
				noName.setVisible(true);
				return;
			}
			else {
				p[i] = new Player(nameTester.getText(), map.getCircleColors()[i], i);
			}
		}
		
		TranslateTransition mainMenuTransition = new TranslateTransition();
		mainMenuTransition.setNode(mainMenu);
		mainMenuTransition.setDuration(Duration.millis(250));
		mainMenuTransition.setToY(mainMenu.getTranslateY() - 500);
		mainMenuTransition.play();
	        
		map.getSettings().setPlayers(p);
		
		play();
	}

	
	
    public void play(){
    	
    	container.getChildren().clear();
    	ships.getChildren().clear();
    	
    	exitButton.setVisible(true);
    	exitButton.setOnAction(e->initialize());
    	
    	
    	
    	
    	
    	container.translateXProperty().bind(root.widthProperty().divide(2).subtract(100));
		container.translateYProperty().bind(root.heightProperty().divide(2).subtract(20));
    	
    	
    	shipDistance = new int[map.getSettings().getPlayers().length];
    	for(int i = 0; i < map.getSettings().getPlayers().length; i++) {
    		
    		map.getSettings().getPlayers()[i].setShip(new ImageView(new Image("file:C:\\Users\\Hachem\\eclipse-workspace\\RiskIsland\\src\\img\\ship" + i + ".png")));
    		
    		shipDistance[i] = new Random().nextInt(100) + 100;
    		map.getSettings().getPlayers()[i].getShip().setTranslateX(map.getSettings().getPlayers()[i].getShip().getX() + shipDistance[i]);
    		
    		ships.getChildren().add(map.getSettings().getPlayers()[i].getShip());
    		
    		moveShip = new TranslateTransition();
	    	moveShip.setNode(map.getSettings().getPlayers()[i].getShip());
	    	moveShip.setDuration(Duration.seconds((shipDistance[i] + 100 )/ 20));
	    	moveShip.setToX(ships.getTranslateX() - 100);
	    	moveShip.play();
    	}
    	
        container.getChildren().add(map.getAll());
        
        ChangeListener<String> buttonState = new ChangeListener<String>() {
        	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue == "0") {
					attackOrDoneButton.setVisible(false);
					retreatButton.setVisible(false);
				
					
				}
				else if(newValue == "1") {
					attackOrDoneButton.setText("Done");
					attackOrDoneButton.setVisible(true);
					attackOrDoneButton.setOnAction(e-> map.getCurrentPhase().nextPhase(map));
					
					retreatButton.setVisible(false);
				
				}
				else if(newValue == "2"){
					attackOrDoneButton.setText("Attack");
					attackOrDoneButton.setVisible(true);
					attackOrDoneButton.setOnAction(e-> map.getCurrentPhase().attack(map));
					
					retreatButton.setVisible(true);
					retreatButton.setOnAction(e-> map.getCurrentPhase().retreat(map));
				}
				else {
					attackOrDoneButton.setText("New Game");
					map.getSlider().setText("2");
					attackOrDoneButton.setVisible(true);
					attackOrDoneButton.setOnAction(e-> initialize());
					
					retreatButton.setVisible(false);
					exitButton.setVisible(false);
				}
			}
			
		};
		map.getButtonState().textProperty().addListener(buttonState);
       
        
        ChangeListener<String> sliderVisibility = new ChangeListener<String>() {
        	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue == "1") {
					map.getCurrentPhase().setSliderProperty(map, troopSlider, troopSliderCancelButton);
				
					troopSliderBox.setVisible(true);
				}
				else {
					troopSliderBox.setVisible(false);
				}
			}
			
		};
		map.getSlider().textProperty().addListener(sliderVisibility);
		
		
		
		
		
		ChangeListener<Number> selectedTroops = new ChangeListener<Number>() {
        	
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				t = (int) Math.floor((double) newValue);
				troopSliderText.setText("" + t);
				
			}
			
		};
		troopSlider.valueProperty().addListener(selectedTroops);
		
		
		troopSliderCancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				map.getSlider().setText("0");
				map.getButtonState().setText("" + 1);
				map.getCurrentPhase().getFirstTile().setFill(map.getCurrentPhase().getFirstTile().getTileColor());
				map.getCurrentPhase().getSecondTile().setFill(map.getCurrentPhase().getSecondTile().getTileColor());
				map.getCurrentPhase().setFirstTile(null);
				map.getCurrentPhase().setSecondTile(null);
			}
		});
		
		troopSliderMoveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				map.getSlider().setText("0");
				map.getCurrentPhase().getFirstTile().setFill(map.getCurrentPhase().getFirstTile().getTileColor());
				map.getCurrentPhase().getSecondTile().setFill(map.getCurrentPhase().getSecondTile().getTileColor());
				map.getCurrentPhase().moveTroops(map, t);
			}
			
		});
		
		ChangeListener<String> changeBarColorAndMoveShip = new ChangeListener<String>() {
        	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				playerBar.styleProperty().set("-fx-background-color: #" + map.getCircleColors()[map.getCurrentPlayer().getId()].toString().substring(2, 8) + "; -fx-background-radius: 5;");
				moveShip = new TranslateTransition();
		    	moveShip.setNode(map.getCurrentPlayer().getShip());
		    	
		    	if(map.getPreviousPlayer() != null) {

			    	moveShip.setDuration(Duration.seconds(50/ 20));
			    	moveShip.setToX(ships.getTranslateX() - 150);
			    	moveShip.play();
		    		
		    		
			    	moveShip = new TranslateTransition();
			    	moveShip.setNode(map.getPreviousPlayer().getShip());
			    	moveShip.setDuration(Duration.seconds(50 / 20));
			    	moveShip.setToX(ships.getTranslateX() - 100);
			    	moveShip.play();
			    	
			    	
		    	}
		    	
		    	else {

			    	moveShip.setDuration(Duration.seconds((shipDistance[map.getCurrentPlayer().getId()] + 150 )/ 20));
			    	moveShip.setToX(ships.getTranslateX() - 150);
			    	moveShip.play();
		    	}
			}
			
		};
		map.getColorBar().textProperty().addListener(changeBarColorAndMoveShip);
		
        
		
		ChangeListener<String> changeText = new ChangeListener<String>() {
        	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				editableText.setText(map.getDisplayedText().getText());
				
			}
			
		};
		map.getDisplayedText().textProperty().addListener(changeText);
		
		
		ChangeListener<String> newPlayerDefeated = new ChangeListener<String>() {
        	
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				moveShip = new TranslateTransition();
		    	moveShip.setNode(map.getSettings().getPlayers()[Integer.parseInt(map.getDefeatedPlayer().getText())].getShip());
		    	
		    	moveShip.setDuration(Duration.seconds(200/ 20));
		    	moveShip.setToX(ships.getTranslateX() + 200);
		    	moveShip.play();
		    	
		    }
			
		};
		map.getDefeatedPlayer().textProperty().addListener(newPlayerDefeated);
		
		
		
		
		
		//not in map constructor just to initialize listeners before them
		
		map.randomPlayerDistribution(map.getSettings());
		map.setCurrentPlayer(map.nextPlayer());
		map.distributeInitialReiforcementTroops(map.getSettings());
		map.getDisplayedText().setText(map.getCurrentPlayer().getName() + " has " + map.getCurrentPlayer().getNumberOfReinforcementTroops() + " reinforcement troops to deploy");
		map.setCurrentPhase(new ReinforcementPhase(map));
		
    }
}
