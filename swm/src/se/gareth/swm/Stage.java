package se.gareth.swm;

import android.graphics.Canvas;
import java.util.LinkedList;

class Stage {
	
	protected GameBase game;
	
	public Stage(GameBase gameBase) {
		game = gameBase;
	}
	
	public void activated(Stage previousStage) {
		/* Called when stage is activated */		
	}
	
	public void deactivated(Stage nextStage) {
		/* Called when stage is deactivated */
	}


	public void onPause() {
		/* Called when activity get onPause call */
	}
	
	public void onStop() {
		/* Called when activity get onStop call */
	}
	
	public void update(final TimeStep timeStep) {
		
		
	}
	
	public void draw(Canvas canvas) {
		
		
	}
	
	public void onTouch(LinkedList<TouchEvent> touchEventList) {
		
	}
	
	public void playfieldSizeChanged(int width, int height) {

	}
	
}
