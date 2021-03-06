package se.gareth.swm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Canvas;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class WorldSelectStage extends Stage {

    private static final String TAG = WorldSelectStage.class.getName();

    private final UpArrow mScrollUpArrow;
    private final DownArrow mScrollDownArrow;
    private final LeftArrow mExitArrow;
    private final ArrayList<WorldIcon> mWorldIconList;
    protected ArrayList<WorldDescriptor> mWorldList;
    private int mScrollPosition;

    private final TextDrawable mVersionText;

    private int mTouchScrollId;
    private double mTouchScrollStart;
    private double mTouchScrollY;
    
    private final SmallButton mHelpButton;

    public WorldSelectStage(GameBase gameBase) {
        super(gameBase);
        mExitArrow = new LeftArrow(gameBase, "Exit");
        mScrollUpArrow = new UpArrow(gameBase, "");
        mScrollDownArrow = new DownArrow(gameBase, "");
        mWorldIconList = new ArrayList<WorldIcon>();


        mVersionText = new TextDrawable(gameBase.gameView.font);
        mVersionText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize) * 0.75, false);
        mVersionText.setColor(game.res.getColor(R.color.LightFontColor));
        mVersionText.setTextAlign(TextDrawable.Align.RIGHT);
        Context context =  game.gameView.getContext();
        PackageInfo pkgInfo;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            mVersionText.setText("v " + pkgInfo.versionName);
        } catch (NameNotFoundException e) {
            SLog.pe(TAG, e.getMessage(), e);
        }

        mVersionText.setAlpha(200);

        SLog.i(TAG, "Parse world file...");
        try {
            AssetManager assets = game.gameView.getContext().getAssets();
            InputStream worldInput = assets.open("World1.xml");
            mWorldList = WorldParser.parseXML(gameBase, worldInput);
            SLog.i(TAG, "Parsed world file!");
        }
        catch (Exception e) {
        	SLog.pe(TAG, e.getMessage(), e);
        }

        for (WorldDescriptor worldDescriptor: mWorldList) {
            mWorldIconList.add(new WorldIcon(gameBase, worldDescriptor));
        }
        mScrollPosition = game.settings.getInt("WorldSelectScrollPosition", 0);
        mTouchScrollId = -1;
        
        mHelpButton = new SmallButton(gameBase, "?");
    }


    @Override
    public void update(final TimeStep timeStep) {

        /* Limit scroll position */
        if (mScrollPosition <= 0) {
            mScrollPosition = 0;
            mScrollUpArrow.hide();
        }
        else {
            mScrollUpArrow.show();
        }
        if (mScrollPosition >= (mWorldIconList.size() - 1)) {
            mScrollPosition = mWorldIconList.size() - 1;
            mScrollDownArrow.hide();
        }
        else {
            mScrollDownArrow.show();
        }

        WorldIcon firstIcon = mWorldIconList.get(0);
        double iconOffset = firstIcon.getHeight() * 1.2;

        firstIcon.setX(game.getScreenWidth() / 2.0);

        if (mTouchScrollId < 0) {
            /* User is NOT trying to scroll the screen */
            double shouldBeAtPosY = (game.getScreenHeight()/2.0) - (mScrollPosition * iconOffset);
            if (firstIcon.getY() != shouldBeAtPosY) {
                /* Move the list until is has been positioned as expected according to the current scroll position */
                double diff = firstIcon.getY() - shouldBeAtPosY;
                double moveY = 10.0 * timeStep.get() * (Math.abs(diff) + 1.0);
                if (diff > 1.0) {
                    firstIcon.addPosition(0.0, -moveY);
                    if ((firstIcon.getY() - shouldBeAtPosY) < 0) {
                        firstIcon.setY(shouldBeAtPosY);
                    }
                }
                else if (diff < 0.1){
                    firstIcon.addPosition(0.0, moveY);
                    if ((firstIcon.getY() - shouldBeAtPosY) > 0) {
                        firstIcon.setY(shouldBeAtPosY);
                    }
                }
            }
        }
        else {
            /* User is trying to scroll the list by sliding finger */
            double shouldBeAtPosY = mTouchScrollStart + mTouchScrollY;
            firstIcon.setY(shouldBeAtPosY);
            mScrollPosition = (int)(((game.getScreenHeight()/2.0) - shouldBeAtPosY) / iconOffset + 0.5);
        }

        for (int i = 0; i < mWorldIconList.size(); i ++) {
            WorldIcon worldIcon = mWorldIconList.get(i);
            if (i > 0) {
                worldIcon.setPosition(mWorldIconList.get(i-1).getX(), mWorldIconList.get(i-1).getY() + iconOffset);
            }

            if (worldIcon.getBottom() > mScrollDownArrow.getTop()) {
                worldIcon.setAlpha(128);
            }
            else if (worldIcon.getTop() < mScrollUpArrow.getBottom()) {
                worldIcon.setAlpha(128);
            }
            else {
                worldIcon.setAlpha(255);
            }

            worldIcon.update(timeStep);
        }

        mExitArrow.update(timeStep);
        mScrollUpArrow.update(timeStep);
        mScrollDownArrow.update(timeStep);
    	mHelpButton.update(timeStep);
    }


    @Override
    public void draw(Canvas canvas) {
    	
        canvas.drawColor(game.res.getColor(R.color.NormalBackground));
        game.drawWorldBackground(canvas);

        mVersionText.draw(canvas);

        for (WorldIcon worldIcon: mWorldIconList) {
            worldIcon.draw(canvas);
        }

        mScrollDownArrow.draw(canvas);
        mScrollUpArrow.draw(canvas);
        mExitArrow.draw(canvas);
		mHelpButton.draw(canvas);
    }


    @Override
    public void activated(Stage previousStage) {
        int lastAreaCleared = -1;
        int areaIndex = 0;

        for (WorldIcon worldIcon: mWorldIconList) {

            WorldDescriptor wd = worldIcon.getDescriptor();
            boolean disableArea = true;
            int highscore = game.settings.getInt(wd.getKey() + "Highscore", -1);
            if (highscore >= 0) {
                disableArea = false;
                lastAreaCleared = areaIndex;
            }
            else if (lastAreaCleared == (areaIndex - 1)) {
                disableArea = false;
            }
            
            if (BuildConfig.DEBUG) {
            	disableArea = false;
            }
            worldIcon.setStats(disableArea, highscore,
                               game.settings.getInt(wd.getKey() + "CurrentLevel", 1) - 1);
            areaIndex ++;
        }
    }

    @Override
    public void onPause() {
        /* Store current scroll position */
        game.settingsEditor.putInt("WorldSelectScrollPosition", mScrollPosition);
        game.settingsEditor.commit();
    }


    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {

        for (TouchEvent touchEvent: touchEventList) {


            if (mScrollUpArrow.wasPressed(touchEvent)) {
                mScrollPosition -= 1;
            }
            else if (mScrollDownArrow.wasPressed(touchEvent)) {
                mScrollPosition += 1;
            }
            else if (mHelpButton.wasPressed(touchEvent)) {
            	game.setStage(game.helpStage);
            }

            else if (mExitArrow.wasPressed(touchEvent)) {
                Activity activity = (Activity)game.gameView.getContext();
                activity.moveTaskToBack(true);
                return ;
            }
            else {
                for (WorldIcon worldIcon: mWorldIconList) {
                    if (worldIcon.getAlpha() == 255 && worldIcon.wasPressed(touchEvent)) {
                        game.levelStage.setWorld(worldIcon.getDescriptor());
                        game.setStage(game.levelStage);
                        return ;
                    }
                }
            }

            if (touchEvent.type == TouchEvent.TouchType.Down) {
                mTouchScrollId = touchEvent.id;
                mTouchScrollStart = mWorldIconList.get(0).getY() - touchEvent.y;
                mTouchScrollY = touchEvent.y;
            }
            else if (touchEvent.type == TouchEvent.TouchType.Move) {
                mTouchScrollY = touchEvent.y;
            }
            else if (touchEvent.type == TouchEvent.TouchType.Up) {
                mTouchScrollId = -1;
            }
        }
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
        mVersionText.setPosition(width * 0.98, mVersionText.getHeight() / 1.75);
        mExitArrow.setPosition(mExitArrow.getWidth() / 1.75, height - mExitArrow.getHeight() / 1.5);
        mScrollUpArrow.setPosition(width/2, mExitArrow.getHeight() / 1.5);
        mScrollDownArrow.setPosition(width/2, height - mExitArrow.getHeight() / 1.5);
    	mHelpButton.setPosition(width - mHelpButton.getWidth() / 1.5, height - mHelpButton.getHeight() / 1.5);
    }
}
