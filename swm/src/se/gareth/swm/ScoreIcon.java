package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class ScoreIcon extends GraphicObject {
	
	private static Sprite mScoreSprite;
	private final ScoreCounter mScoreCounter;
	
	public ScoreIcon(GameBase gameBase) {
		super(gameBase);
		mScoreCounter = new ScoreCounter(gameBase);
		
    	if (mScoreSprite == null) {
    		mScoreSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.score_icon1), 1);
    	}
    	setAnimation(new Animation(mScoreSprite, 0, 0));
    	setScore(0);
	}
	
	public void setScore(int scoreValue) {
		mScoreCounter.setScore(scoreValue);
	}
	
	public void addScore(int add) {
		mScoreCounter.addScore(add);
	}
	
	public int getScore() {
		return mScoreCounter.getScore();
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		mScoreCounter.setPosition(posLeft + width/2, posTop + height / 2);
		mScoreCounter.update(timeStep);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mScoreCounter.draw(canvas);
	}
}
