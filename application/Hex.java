package application;

import javafx.scene.paint.Color;

public class Hex extends Tile{
	
	private final double hexUnitX = this.getSize() * Math.sqrt(3) / 2 + 0.5;
	private final double hexUnitY = this.getSize() * 3 / 2 + 0.5;


	
	
	public Hex(int col, int row) {
		super(col, row, 30);
		this.setNeighbors(new Hex[6]);
		draw();
	}
	
	@Override
	public void draw() {
		double posX = this.getCol() * hexUnitX + this.getCol();
		double posY = this.getRow() * hexUnitY + this.getRow();
		Double[] corners = new Double[12];
	
		for(int i = 0; i < 6; i++) {
			double a = 60 * i -30;
			a = Math.PI * a /180;
			corners[2 * i] = posX + getSize() * Math.cos(a);
			corners[2 * i + 1] = posY + getSize() * Math.sin(a);
		}
		
		this.getPoints().addAll(corners);
		
		this.setStroke(Color.BLACK);
		
		
		
		getCircle().setCenterX(posX);
		getCircle().setCenterY(posY);
		getCircle().setRadius(8);		
		
		getShowTroops().setX(posX - getCircle().getRadius() / 2);
		getShowTroops().setY(posY + getCircle().getRadius() / 2);
		
	}

	
	
	@Override
	public boolean checkNeighbor(Tile tile) {
		for(int i = 0; i < 6; i++) {
			if(tile.equals(this.getNeighbors()[i])) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	//get /set
	
	

	public double getHexUnitX() {
		return hexUnitX;
	}

	public double getHexUnitY() {
		return hexUnitY;
	}

}
