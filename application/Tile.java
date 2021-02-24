package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public abstract class Tile extends Polygon{
	private int col;
	private int row;
	private int size;
	private int ownership;
	private int troops = 1;
	private Color tileColor;
	private Color circleColor;
	private Tile[] neighbors;
	private Circle circle = new Circle();
	private Text showTroops = new Text("" + troops);
	
	private int visited = 0;
	
	public Tile(int col, int row, int size) {
		this.col = col;
		this.row = row;
		this.size = size;
	}
	
	

	public abstract void draw();
	
	public abstract boolean checkNeighbor(Tile tile);
	
	

	//get/ set
	
	
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	
	
	public int getOwnership() {
		return ownership;
	}

	public void setOwnership(int ownership) {
		this.ownership = ownership;
	}

	
	
	public int getTroops() {
		return troops;
	}

	public void setTroops(int troops) {
		this.troops = troops;
	}

	
	
	
	
	
	
	public Color getTileColor() {
		return tileColor;
	}



	public void setTileColor(Color tileColor) {
		this.tileColor = tileColor;
	}



	public Color getCircleColor() {
		return circleColor;
	}



	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}



	public Tile[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(Tile[] neighbors) {
		this.neighbors = neighbors;
	}
	
	public void setNeighbor(Tile neighbor, int i) {
		this.neighbors[i] = neighbor;
	}
	
	
	
	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}



	public Text getShowTroops() {
		return showTroops;
	}



	public void setShowTroops(Text showTroops) {
		this.showTroops = showTroops;
	}



	public int getVisited() {
		return visited;
	}



	public void setVisited(int visited) {
		this.visited = visited;
	}

	
	
	
}
