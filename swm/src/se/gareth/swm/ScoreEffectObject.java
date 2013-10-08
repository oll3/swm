package se.gareth.swm;

import android.graphics.Canvas;

public class ScoreEffectObject extends ActiveObject {
		
    private final TextDrawable mText;

	public ScoreEffectObject(GameBase gameBase,
				double x, double y, double size, int color, int score) {
		super(gameBase);
        
    	mText = new TextDrawable(gameBase.gameView.font, super.mPaint);
    	mText.setTextSize(size, false);
        mText.setColor(color);
        mText.setTextAlign(TextDrawable.Align.CENTER);
		mText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
        mText.setText("" + score);

        setPosition(x, y);
        setVelocity(Vector2D.DIRECTION_UP - 1.0, 20.0);
		applyEffect(new FadeDestroyEffect(1.0));

       	setDrawOrder(30);
	}

	public void changeScoreValue(int score) {
		mText.setText("" + score);
	}
		 
	@Override
	public void setAlpha(int alpha) {
		super.setAlpha(alpha);
		mText.forceRedraw();
	}
	
	@Override
	public void update(double frameTime) {
		super.update(frameTime);
		mText.setPosition(getX(), getY());		
	}
	
	@Override
	public void draw(Canvas canvas) {
		mText.draw(canvas);
	}
}
