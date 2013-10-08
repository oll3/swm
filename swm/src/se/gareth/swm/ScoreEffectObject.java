package se.gareth.swm;

import android.graphics.Canvas;

public class ScoreEffectObject extends ActiveObject {
		
    private final TextDrawable mText;
	private float mAlpha;

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

       	setDrawOrder(30);

		mAlpha = 1.0f;
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
		mAlpha -= frameTime / 1.0;
		if (mAlpha > 0.0) {
			setAlpha((int)((double)255 * mAlpha));
		}
		else {
			deleteMe();
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		mText.draw(canvas);
	}
}
