package se.gareth.swm;

import android.graphics.Canvas;

public class Behavior {

	protected GameBase game;
	protected boolean done;
	
	public Behavior(GameBase gameBase) {
		game = gameBase;
		done = false;
	}
	

	protected void setDone() {
		done = true;
	}

	public boolean isDone() {
		return done;
	}
	
	/*
	 * Called when behavior is added to active object
	 */
	public void wasAdded(ActiveObject activeObject) {
		
	}
	
	
	/* 
	 * Can be overridden
	 */
	public void update(ActiveObject activeObject, double frameTime) {
		
	}
	

	/* 
	 * Can be overridden
	 */
	public void draw(ActiveObject activeObject, Canvas canvas) {
		
	}
}
