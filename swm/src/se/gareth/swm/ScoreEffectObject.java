package se.gareth.swm;

import android.graphics.Canvas;

public class ScoreEffectObject extends ActiveText {
		
	public ScoreEffectObject(GameBase gameBase,
				double x, double y, double size, int color, int score) {
		super(gameBase, x, y, size, "" + score);
        
    	setColor(color);
		mText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
        setPosition(x, y);
        setVelocity(Vector2D.DIRECTION_UP - 1.0, 20.0);
		applyEffect(new FadeDestroyEffect(1.0));
	}

	public void changeScoreValue(int score) {
		setText("" + score);
	}
		 
	@Override
	public void update(double frameTime) {
		super.update(frameTime);
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}
}
