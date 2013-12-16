package se.gareth.swm;

import android.graphics.Canvas;
import android.util.Log;

import java.util.LinkedList;


public class HelpStage extends Stage {


    private final TextDrawable mHelpText;
    private final TextDrawable mInfoText;

    private static final String mHelpString = "Make sure no creatures gets past your sight unnoticed, \nall animals should treated equally. " + 
    		"Let any creature escape your aim and you have lost the level and will have to replay it.\nNormally you have 3 retries in total for an area." +
    		"Shots are expensive, so do not miss. Firing at a faster pace will get your bonus up and \nincrease your score. " +
    		"Boxes contains (mostly) good to have items, collect and use them right.\n" +
    		"Now, enough with this... Go show those creatures some love, have fun and make Maud proud!\n";
    
    
    public HelpStage(GameBase gameBase) {
		super(gameBase);
	
		mHelpText = new TextDrawable(gameBase.gameView.font);
		mHelpText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), true);
		mHelpText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline),
				      game.res.getColor(R.color.NormalOutlineColor));
		mHelpText.setTextAlign(TextDrawable.Align.CENTER);
		mHelpText.setColor(game.res.getColor(R.color.HeadingFontColor));
		
		mHelpText.setText("Test1&#10;Test2");
		
		mInfoText = new TextDrawable(gameBase.gameView.font);
		mInfoText.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mInfoText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mInfoText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				      game.res.getColor(R.color.NormalOutlineColor));
		mInfoText.setTextAlign(TextDrawable.Align.LEFT);
		
		mInfoText.setText(mHelpString);
    }

    @Override
    public void update(final TimeStep timeStep) {

    }

    
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(game.res.getColor(R.color.NormalBackground));
        game.drawWorldBackground(canvas);
		//mHelpText.draw(canvas);
		//mInfoText.draw(canvas);
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
	    		game.setStage(game.worldSelectStage);
	    		break;
	    	}
    	}
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
    	mHelpText.setPosition(50, 50);
    	mInfoText.setPosition(width/2, height/2 + 30);
    }
};

