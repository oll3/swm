package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class LevelScoreTable extends GraphicObject {
	
	private static Sprite mIconSprite;
	private final TextDrawable mHitScoreText;
	private final TextDrawable mLevelScoreText;
	private final TextDrawable mBonusScoreText;
	private final TextDrawable mTotalScoreText;
	private final TextDrawable mLostScoreText;
	
	private final ScoreCounter mHitScoreCounter;
	private final ScoreCounter mLevelScoreCounter;
	private final ScoreCounter mBonusScoreCounter;
	private final ScoreCounter mTotalScoreCounter;
	private final ScoreCounter mLostScoreCounter;
	
	public LevelScoreTable(GameBase gameBase) {
		super(gameBase);
		
		mHitScoreText = new TextDrawable(gameBase.gameView.font);
		mHitScoreText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mHitScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mHitScoreText.setColor(game.res.getColor(R.color.PanelFontColor));
		mHitScoreText.setTextAlign(TextDrawable.Align.LEFT);
		mHitScoreText.setText("HIT POINTS:");
		
		mBonusScoreText = new TextDrawable(gameBase.gameView.font);
		mBonusScoreText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mBonusScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mBonusScoreText.setColor(game.res.getColor(R.color.SuccessColor));
		mBonusScoreText.setTextAlign(TextDrawable.Align.LEFT);
		mBonusScoreText.setText("BONUS POINTS:");
		
		mLostScoreText = new TextDrawable(gameBase.gameView.font);
		mLostScoreText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mLostScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLostScoreText.setColor(game.res.getColor(R.color.FailColor));
		mLostScoreText.setTextAlign(TextDrawable.Align.LEFT);
		mLostScoreText.setText("LOST POINTS:");
		
		mLevelScoreText = new TextDrawable(gameBase.gameView.font);
		mLevelScoreText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize) * 1.2, true);
		mLevelScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLevelScoreText.setColor(game.res.getColor(R.color.PanelFontColor));
		mLevelScoreText.setTextAlign(TextDrawable.Align.LEFT);
		mLevelScoreText.setText("LEVEL SCORE:");

		mTotalScoreText = new TextDrawable(gameBase.gameView.font);
		mTotalScoreText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize) * 1.2, false);
		mTotalScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mTotalScoreText.setColor(game.res.getColor(R.color.PanelFontColor));
		mTotalScoreText.setTextAlign(TextDrawable.Align.LEFT);
		mTotalScoreText.setText("TOTAL SCORE:");
		
		mHitScoreCounter = new ScoreCounter(gameBase);
		mLevelScoreCounter = new ScoreCounter(gameBase);
		mBonusScoreCounter = new ScoreCounter(gameBase);
		mTotalScoreCounter = new ScoreCounter(gameBase);
		mLostScoreCounter = new ScoreCounter(gameBase);
		
		mHitScoreCounter.text.setTextAlign(TextDrawable.Align.RIGHT);
		mLevelScoreCounter.text.setTextAlign(TextDrawable.Align.RIGHT);
		mBonusScoreCounter.text.setTextAlign(TextDrawable.Align.RIGHT);
		mTotalScoreCounter.text.setTextAlign(TextDrawable.Align.RIGHT);
		mLostScoreCounter.text.setTextAlign(TextDrawable.Align.RIGHT);
		
		mHitScoreCounter.text.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mBonusScoreCounter.text.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mLostScoreCounter.text.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mLevelScoreCounter.text.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), true);
		mTotalScoreCounter.text.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		
		mHitScoreCounter.text.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLevelScoreCounter.text.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mBonusScoreCounter.text.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mTotalScoreCounter.text.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLostScoreCounter.text.setOutline(game.res.getDimension(R.dimen.SmallFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		
		mHitScoreCounter.text.setColor(game.res.getColor(R.color.PanelFontColor));
		mBonusScoreCounter.text.setColor(game.res.getColor(R.color.SuccessColor));
		mLostScoreCounter.text.setColor(game.res.getColor(R.color.FailColor));
		mLevelScoreCounter.text.setColor(game.res.getColor(R.color.PanelFontColor));
		mTotalScoreCounter.text.setColor(game.res.getColor(R.color.PanelFontColor));
		
		
    	if (mIconSprite == null) {
    		mIconSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.level_scoretable), 1);
    	}
    	setAnimation(new Animation(mIconSprite, 0, 0));
	}

	public void reset() {
		mHitScoreCounter.setScore(0);
		mLevelScoreCounter.setScore(0);
		mBonusScoreCounter.setScore(0);
		mTotalScoreCounter.setScore(0);
		mLostScoreCounter.setScore(0);
	}
	
	public void setScores(int levelScore, int hitPoints, int bonusScore, int lostScore, int totalScore) {
		mHitScoreCounter.setScore(0);
		mLevelScoreCounter.setScore(0);
		mBonusScoreCounter.setScore(0);
		mLostScoreCounter.setScore(0);
		mHitScoreCounter.addScore(hitPoints);
		mLevelScoreCounter.addScore(levelScore);
		mBonusScoreCounter.addScore(bonusScore);
		mLostScoreCounter.addScore(lostScore);
		mTotalScoreCounter.updateScore(totalScore);
	}

	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		double x = getX(-0.45);
		double y = getY(-0.49);
		double vdiv = getHeight() / 5;
		mHitScoreText.setPosition(x, y + vdiv * 0.5);
		mBonusScoreText.setPosition(x, y + vdiv * 1.5);
		mLostScoreText.setPosition(x, y + vdiv * 2.5);
		mLevelScoreText.setPosition(x, y + vdiv * 3.5);
		mTotalScoreText.setPosition(x, y + vdiv * 4.5);
		
		x = getX(0.45);
		mHitScoreCounter.setPosition(x, y + vdiv * 0.5);
		mBonusScoreCounter.setPosition(x, y + vdiv * 1.5);
		mLostScoreCounter.setPosition(x, y + vdiv * 2.5);
		mLevelScoreCounter.setPosition(x, y + vdiv * 3.5);
		mTotalScoreCounter.setPosition(x, y + vdiv * 4.5);
		
		mHitScoreCounter.update(timeStep);
		mBonusScoreCounter.update(timeStep);
		mLostScoreCounter.update(timeStep);
		mLevelScoreCounter.update(timeStep);
		mTotalScoreCounter.update(timeStep);		
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mVisible) {
			mHitScoreText.draw(canvas);
			mBonusScoreText.draw(canvas);
			mLostScoreText.draw(canvas);
			mLevelScoreText.draw(canvas);
			mTotalScoreText.draw(canvas);

			mHitScoreCounter.draw(canvas);
			mLevelScoreCounter.draw(canvas);
			mBonusScoreCounter.draw(canvas);
			mLostScoreCounter.draw(canvas);
			mTotalScoreCounter.draw(canvas);
			
			canvas.drawLine((float)getLeft(), (float)(getY(0.31)), 
							(float)getRight(), (float)(getY(0.31)), new Paint());
		}
	}
}
