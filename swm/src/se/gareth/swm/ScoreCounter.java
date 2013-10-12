package se.gareth.swm;

import android.graphics.Canvas;


public class ScoreCounter extends GraphicObject {
	
	protected TextDrawable text;
	private int mScoreCounter;
	private int mIncreaseStep;
	private int mCurrentDisplayedScore;
	
	public ScoreCounter(GameBase gameBase) {
		super(gameBase);
		text = new TextDrawable(gameBase.gameView.font);
		text.setTextSize(game.res.getDimension(R.dimen.BigFontSize), false);
		text.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		text.setColor(game.res.getColor(R.color.PanelFontColor));
		text.setTextAlign(TextDrawable.Align.CENTER);
    	setScore(0);
	}
	
	public void setScore(int scoreValue) {
		mScoreCounter = scoreValue;
		mCurrentDisplayedScore = mScoreCounter;
		mIncreaseStep = 0;
		text.setText("" + mCurrentDisplayedScore);
	}
	
	public void updateScore(int newScore) {
		int prevScore = mScoreCounter;
		mScoreCounter = newScore;
		if (mScoreCounter != prevScore) {
			mIncreaseStep = Math.abs((mScoreCounter - mCurrentDisplayedScore)/30) + 1;
		}
	}
	
	public void addScore(int add) {
		updateScore(mScoreCounter + add);
	}
	
	public int getScore() {
		return mScoreCounter;
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		text.setPosition(getX(), getY());
		
		if (mCurrentDisplayedScore != mScoreCounter) {
		
			if (mIncreaseStep < 1)
				mIncreaseStep = 1;
			
			if (mCurrentDisplayedScore < mScoreCounter) {
				mCurrentDisplayedScore = Math.min(mScoreCounter, mCurrentDisplayedScore + mIncreaseStep);
			}
			else if (mCurrentDisplayedScore > mScoreCounter) {
				mCurrentDisplayedScore = Math.max(mScoreCounter, mCurrentDisplayedScore - mIncreaseStep);
			}
			text.setText("" + mCurrentDisplayedScore);
		}
		else {
			mIncreaseStep = 0;
		}		
	}
	
	@Override
	public void draw(Canvas canvas) {
		text.draw(canvas);
	}
}
