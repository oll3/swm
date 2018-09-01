package se.gareth.swm;

import android.graphics.Canvas;

import java.util.LinkedList;


public class PauseStage extends Stage {

    private static final String TAG = PauseStage.class.getName();

    private final TextDrawable mPauseText, mInfoText;
    
    private final LeftArrow mMenuArrow;
    private int mExitCounter;

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

        mMenuArrow = new LeftArrow(gameBase, "Menu");
    }

    @Override
    public void update(final TimeStep timeStep) {
        mMenuArrow.update(timeStep);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(game.res.getColor(R.color.NormalBackground));
        game.drawWorldBackground(canvas);
        mPauseText.draw(canvas);
        mInfoText.draw(canvas);
        mMenuArrow.draw(canvas);
    }


    /*
     * Called when this stage is activated.
     */
    @Override
    public void activated(Stage previousStage) {
    	mMenuArrow.setText("Menu");
    	mExitCounter = 0;
    }


    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {

        for (TouchEvent touchEvent: touchEventList) {
        	
        	if (mMenuArrow.wasPressed(touchEvent)) {
        		if (mExitCounter > 0) {
        			game.setStage(game.worldSelectStage);
        			punishExit();
        		}
        		else {
        			mMenuArrow.setText("Sure?");
        			mExitCounter ++;
        		}
        		break;
        	}
        	else if (touchEvent.type == TouchEvent.TouchType.Up) {
                game.setStage(game.gameStage);
                break;
        	}
        }
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
        mPauseText.setPosition(width/2, height/2 - 30);
        mInfoText.setPosition(width/2, height/2 + 30);
        mMenuArrow.setPosition(mMenuArrow.getWidth() / 1.75, height - mMenuArrow.getHeight() / 1.5);
    }
    
    private void punishExit() {
        WorldDescriptor worldDescriptor = game.levelStage.getWorldDescriptor();
        if (worldDescriptor != null) {
	        if (game.health.getLifes() > 1) {
	        	game.itemBar.saveItems(worldDescriptor.getKey());
	            game.settingsEditor.putInt(worldDescriptor.getKey() + "CurrentHealth", game.health.getLifes() - 1);
	        }
	        else {
	            SLog.d(TAG, "Area failed as player run out of extra lifes upon stopping activity");
	            game.settingsEditor.putBoolean(worldDescriptor.getKey() + "AreaFailed", true);
	        }
	        game.settingsEditor.commit();
        }
    }

    @Override
    public void onStop() {
        /* If we are in the middle of a level and activity gets stopped then remove one life
         * to stop players from restarting at the same level without being punished*/
        WorldDescriptor worldDescriptor = game.levelStage.getWorldDescriptor();
        if (worldDescriptor != null) {
            SLog.i(TAG, "Actvity stopped in middle of a level.");
            punishExit();
        }
    }
};

