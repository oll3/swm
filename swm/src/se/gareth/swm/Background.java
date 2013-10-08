package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
	
	protected final GameBase game;
	protected final int backgroundColor;
	
	Background(GameBase gameBase, int colorParam) {
		backgroundColor = colorParam;
		game = gameBase;
	}
	
	protected int getBackgroundColor() {
		return backgroundColor;
	}
	
	protected void load() {

	}
	
	protected void unload() {

	}
	
	protected Bitmap getThumbnail() {
		return null;
	}
	
	public void update(double frameTime) {
		
	}
	
	public void draw(Canvas canvas) {
		
	}
}
