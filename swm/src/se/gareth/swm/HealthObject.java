package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class HealthObject extends GraphicObject {
	
	private static Sprite mHealthSprite;
	private TextDrawable mLifeCounterText;
	private int mNumLifes;
	
	public HealthObject(GameBase gameBase) {
		super(gameBase);
		mLifeCounterText = new TextDrawable(gameBase.gameView.font);
		
		mLifeCounterText.setTextSize(game.res.getDimension(R.dimen.BigFontSize), false);
		mLifeCounterText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLifeCounterText.setColor(game.res.getColor(R.color.PanelFontColor));
		mLifeCounterText.setTextAlign(TextDrawable.Align.CENTER);
		
    	if (mHealthSprite == null) {
    		mHealthSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.health_icon1), 1);
    	}
    	setAnimation(new Animation(mHealthSprite, 0, 0));
    	mNumLifes = 0;
	}
	
	public void setLifes(int lifes) {
		mNumLifes = lifes;
		mLifeCounterText.setText("" + mNumLifes);
	}
	
	public void addLifes(int add) {
		setLifes(mNumLifes + add);
	}
	
	public int getLifes() {
		return mNumLifes;
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		mLifeCounterText.setPosition(posLeft + width / 2, posTop + height / 2 + 4);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mLifeCounterText.draw(canvas);
	}
}
