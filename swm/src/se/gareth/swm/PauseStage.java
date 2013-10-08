package se.gareth.swm;

import android.graphics.Canvas;
import android.util.Log;

import java.util.LinkedList;


public class PauseStage extends Stage {

	private static final String TAG = PauseStage.class.getName();

    private final TextDrawable mPauseText;
    private final TextDrawable mInfoText;


    public PauseStage(GameBase gameBase) {
		super(gameBase);
	
		mPauseText = new TextDrawable(gameBase.gameView.font);
		mPauseText.setTextSize(game.res.getDimension(R.dimen.BigFontSize), true);
		mPauseText.setOutline(game.res.getDimension(R.dimen.BigFontOutline),
				      game.res.getColor(R.color.NormalOutlineColor));
		mPauseText.setTextAlign(TextDrawable.Align.CENTER);
		mPauseText.setColor(game.res.getColor(R.color.HeadingFontColor));
		
		mPauseText.setText("Pause");
		
		mInfoText = new TextDrawable(gameBase.gameView.font);
		mInfoText.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mInfoText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mInfoText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				      game.res.getColor(R.color.NormalOutlineColor));
		mInfoText.setTextAlign(TextDrawable.Align.CENTER);
		
		mInfoText.setText("Touch to continue");
    }

    @Override
    public void update(double frameTime) {

    }

    
    @Override
    public void draw(Canvas canvas) {
		canvas.drawColor(game.res.getColor(R.color.NormalBackground));
		mPauseText.draw(canvas);
		mInfoText.draw(canvas);
    }
    

    /* 
     * Called when this stage is activated.
     */
    @Override
    public void activated(Stage previousStage) {

	
    }


    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {
    	
    	for (TouchEvent touchEvent: touchEventList) {
	    	if (touchEvent.type == TouchEvent.TouchType.Up) {
	    		game.setStage(game.gameStage);
	    		break;
	    	}
    	}
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
    	mPauseText.setPosition(width/2, height/2 - 30);
    	mInfoText.setPosition(width/2, height/2 + 30);
    }
    
    @Override
    public void onStop() {
		/* If we are in the middle of a level and activity gets stopped then remove one life 
		 * to stop players from restarting at the same level without being punished*/
		WorldDescriptor worldDescriptor = game.levelStage.getWorldDescriptor();
		if (worldDescriptor != null) {
			Log.i(TAG, "Actvity stopped in middle of a level.");
			if (game.health.getLifes() > 1) {
				game.settingsEditor.putInt(worldDescriptor.getKey() + "CurrentHealth", game.health.getLifes() - 1);
			}
			else {
				Log.d(TAG, "Area failed as player run out of extra lifes upon stopping activity");
				game.settingsEditor.putBoolean(worldDescriptor.getKey() + "AreaFailed", true);
			}
			game.settingsEditor.commit();
		}
    }
};

