package se.gareth.swm;

import android.graphics.Canvas;

public class Button extends GraphicObject {
	
	private boolean mEnable;
	private int mPressedId;
	
	public Button(GameBase gameBase) {
		super(gameBase);
		mPressedId = -1;
		mEnable = true;
	}
	
	public void enable() {
		mEnable = true;
	}
	
	public void disable() {
		mEnable = false;
	}
	
	public boolean isEnable() {
		return mEnable;
	}
	
	public boolean wasPressed(TouchEvent event) {
		if (mEnable && this.mVisible) {
			if (event.type == TouchEvent.TouchType.Down) {
				if (this.containsPos(event.x, event.y)) {
					mPressedId = event.id;
					onDown();
				}
			}
			else if (event.type == TouchEvent.TouchType.Up && mPressedId == event.id) {
				mPressedId = -1;
				onReleased();
				if (this.containsPos(event.x, event.y)) {
					onUp();
					game.sounds.play(Sounds.Sound.Click1);
					return true;
				}
			}
			else if (event.type == TouchEvent.TouchType.Move && mPressedId == event.id) {
				/* User moves the touching finger outside button */
				if (!this.containsPos(event.x, event.y)) {
					mPressedId = -1;
					onReleased();
				}
			}
		}
		return false;
	}
	
	public void onDown() {
		/* override to catch button down event */
	}
	
	public void onUp() {
		/* override to catch button up event */
	}
	
	public void onReleased() {
		/* override to catch button released event */
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}
}
