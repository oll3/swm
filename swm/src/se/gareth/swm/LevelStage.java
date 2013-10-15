package se.gareth.swm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import android.graphics.Canvas;
import android.util.Log;

import java.util.LinkedList;

public class LevelStage extends Stage {

	private static final String TAG = LevelStage.class.getName();

	enum LevelState {
		LevelDone,
		LevelFailed,
		StartLevel,
		Loading,
		WorldDone,
	}
	
	private Random mRandom;

	private int mLevelValue;
	private int mTotalScore;
	
	private int mHighscore;
    private final TextDrawable mLevelText;
    private final TextDrawable mWorldNameText;
    private final TextDrawable mLoadingText;
    
    private WorldDescriptor mWorldDescriptor;
    
    private final LevelScoreTable mScoreTable;
    
    private LevelState mLevelState;
        
    private final LeftArrow mExitArrow;
    private final RightArrow mNextArrow;
    
    private Background mCurrentBackground;
    	
	public LevelStage(GameBase gameBase) {
		super(gameBase);

		mLevelText = new TextDrawable(gameBase.gameView.font);
		mLevelText.setTextSize(game.res.getDimension(R.dimen.HeadingFontSize), true);
		mLevelText.setOutline(game.res.getDimension(R.dimen.BigFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLevelText.setTextAlign(TextDrawable.Align.CENTER);
		mLevelText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mLevelText.setText("Level 1");
		
		mWorldNameText = new TextDrawable(gameBase.gameView.font);
		mWorldNameText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mWorldNameText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mWorldNameText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mWorldNameText.setTextAlign(TextDrawable.Align.CENTER);
		
		mLoadingText = new TextDrawable(gameBase.gameView.font);
		mLoadingText.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mLoadingText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mLoadingText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mLoadingText.setTextAlign(TextDrawable.Align.CENTER);
		
		mRandom = new Random();
		
		mScoreTable = new LevelScoreTable(gameBase);

		mExitArrow = new LeftArrow(gameBase, "Menu");
		mNextArrow = new RightArrow(gameBase, "Next");
	}
	
	public WorldDescriptor getWorldDescriptor() {
		return mWorldDescriptor;
	}
	
	public void setWorld(WorldDescriptor worldDescriptor) {
		
		mWorldDescriptor = worldDescriptor;
		mWorldNameText.setText("[" + mWorldDescriptor.getName() + "]");
		mLevelValue = 1;
		mTotalScore = 0;
		int lifes = mWorldDescriptor.getHealth();
		game.itemBar.reset();
		
		if (mCurrentBackground != null) {
			mCurrentBackground.unload();
		}
		
		mCurrentBackground = mWorldDescriptor.getBackground();
		mCurrentBackground.load();
		
		/* Load the state of where player stopped playing this world */
		
		if (game.settings.getBoolean(mWorldDescriptor.getKey() + "AreaFinished", false) || 
				game.settings.getBoolean(mWorldDescriptor.getKey() + "AreaFailed", false)) {
			
			game.settingsEditor.putBoolean(mWorldDescriptor.getKey() + "AreaFinished", false);
			game.settingsEditor.putBoolean(mWorldDescriptor.getKey() + "AreaFailed", false);
			game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentHealth", mWorldDescriptor.getHealth());
			game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentLevel", mLevelValue);
			game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentScore", mTotalScore);
			game.settingsEditor.commit();
		}
		else {
			/* Continue a previous run */
			mLevelValue = game.settings.getInt(worldDescriptor.getKey() + "CurrentLevel", 1);
			mTotalScore = game.settings.getInt(worldDescriptor.getKey() + "CurrentScore", 0);
			lifes = game.settings.getInt(worldDescriptor.getKey() + "CurrentHealth", lifes);
			mScoreTable.setScores(0, 0, 0, 0, mTotalScore);
			
			for (int i = 0; i < game.itemBar.getMaxItems(); i ++) {
				String itemTypeName = game.settings.getString(worldDescriptor.getKey() + "Item" + i, "");
				
				for (Class<? extends ItemBaseObject> itemClass: game.itemTypes) {
					if (itemClass.getSimpleName().equals(itemTypeName)) {
						/* Instance item object */
						try {
							ItemBaseObject itemObject = itemClass.getConstructor(GameBase.class).newInstance(game);
							game.itemBar.addItem(itemObject, true);
							Log.i(TAG, "Restored item " + itemTypeName);
						} catch (NoSuchMethodException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (IllegalArgumentException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (InstantiationException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (IllegalAccessException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (InvocationTargetException e) {
							Log.e(TAG, e.getMessage(), e);
						}
					}
				}
			}
		}
		
		mHighscore = game.settings.getInt(mWorldDescriptor.getKey() + "Highscore", -1);
		game.health.setLifes(lifes);
		mScoreTable.reset();
		setLevelState(LevelState.StartLevel);
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		
		mScoreTable.update(timeStep);
		game.health.update(timeStep);
		
	    mExitArrow.update(timeStep);
	    mNextArrow.update(timeStep);
	    
	    if (mLevelState == LevelState.Loading) {
			
	    	if (mCurrentBackground == null) {
	    		mCurrentBackground = mWorldDescriptor.getBackground();
	    	}
	    		/*
	    		for (Class<? extends Background> backgroundClass: game.backgroundTypes) {
					if (backgroundClass.getSimpleName().equalsIgnoreCase(mWorldDescriptor.getBackground())) {
						try {
							mCurrentBackground = backgroundClass.getConstructor(GameBase.class, int.class).newInstance(game, mWorldDescriptor.getBackgroundColor());
						} catch (IllegalArgumentException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (InstantiationException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (IllegalAccessException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (InvocationTargetException e) {
							Log.e(TAG, e.getMessage(), e);
						} catch (NoSuchMethodException e) {
							Log.e(TAG, e.getMessage(), e);
						}
						break;
					}
	    		}
	    	}
	    	*/
	    	
	    	//mBackground = null;
	    	
			/* Generate the next level */
	    	Level level = generateNextLevel();
	    	
		    /* Run the level in game stage */
	    	game.gameStage.setBackground(mCurrentBackground);
		    game.gameStage.setLevel(level);
		    game.setStage(game.gameStage);
	    }
	    else if (mLevelState == LevelState.StartLevel) {
	    	setLevelState(LevelState.Loading);
	    	mLoadingText.setText("Loading...");
	    	mLevelText.setText("Level " + mLevelValue);
		}
	}
	
	public void setLevelState(LevelState state) {
		mLevelState = state;
	}

	@Override
	public void draw(Canvas canvas) {

	    /* 
	     * Do drawing 
	     */

	    /* Clear screen */
		Level level = game.gameStage.getLevel();
		if (level != null && level.hasFailed())
			canvas.drawColor(game.res.getColor(R.color.FailColor));
		else
			canvas.drawColor(game.res.getColor(R.color.NormalBackground));
		game.drawWorldBackground(canvas);
		
		mLevelText.draw(canvas);
		mWorldNameText.draw(canvas);
		game.health.draw(canvas);
		
		if (mLevelState == LevelState.StartLevel || mLevelState == LevelState.Loading) {
			mLoadingText.draw(canvas);
		}
		else {
		    mScoreTable.draw(canvas);
		    mExitArrow.draw(canvas);
		    mNextArrow.draw(canvas);
		}
	}

		
	/* 
	 * Called from view when this stage is first activated, that 
	 * is when the game stage has ended.
	 */
	@Override
	public void activated(Stage previousStage) {

		//mBackground = BitmapFactory.decodeResource(game.res, R.drawable.world_background1);
		
		if (mLevelState == LevelState.StartLevel) {
			/* Do nothing if level is set to start at activation (first run of world) */
			return ;
		}
		
		Level level = game.gameStage.getLevel();
		int levelScore = level.getScore();
		int hitPoints = 0;
		int bonusPoints = 0;
		int lostPoints = 0;
		
		mNextArrow.show();
		
		if (level.hasFailed()){
			game.health.addLifes(-1);
			
			mLevelText.setText("Level " + (mLevelValue) + " - Failed");
			setLevelState(LevelState.LevelFailed);
			mNextArrow.setText("Retry");
			levelScore = 0;
			
			if (game.health.getLifes() <= 0) {
				/* End of game */
				mLevelText.setText("Game Over");
				mNextArrow.hide();
				mLevelValue = 1;
				game.settingsEditor.putBoolean(mWorldDescriptor.getKey() + "AreaFailed", true);
				game.itemBar.reset();
			}
		}
		else if (level.hasFinished()) {
			/* Level was successfully finished */
			mTotalScore += level.getScore();
			hitPoints = level.getHitPoints();
			bonusPoints = level.getBonusPoints();
			lostPoints = level.getLostPoints();
			
			if (mLevelValue >= mWorldDescriptor.getNumLevels()) {
				/* World finished */
				setLevelState(LevelState.WorldDone);
				mLevelText.setText("Area Cleared!");
				mNextArrow.hide();
				game.settingsEditor.putBoolean(mWorldDescriptor.getKey() + "AreaCleared", true);
				game.settingsEditor.putBoolean(mWorldDescriptor.getKey() + "AreaFinished", true);
				if (mTotalScore > mHighscore) {
					game.settingsEditor.putInt(mWorldDescriptor.getKey() + "Highscore", mTotalScore);
				}
				
				game.itemBar.reset();
			}
			else {
				/* Level finished */
				setLevelState(LevelState.LevelDone);
				mLevelText.setText("Level " + (mLevelValue) + " - Done");
				mNextArrow.setText("Next");
			}
			mLevelValue ++;
		}
		
		/* Save current items in item bar to shared memory so they may be restored later */
		int itemIndex = 0;
		for (ItemBaseObject item: game.itemBar.getItemList()) {
			game.settingsEditor.putString(mWorldDescriptor.getKey() + "Item" + itemIndex, item.getClass().getSimpleName());
			itemIndex ++;
		}
		while (itemIndex < game.itemBar.getMaxItems()) {
			game.settingsEditor.remove(mWorldDescriptor.getKey() + "Item" + itemIndex);
			itemIndex ++;
		}
		
		game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentHealth", game.health.getLifes());
		game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentLevel", mLevelValue);
		game.settingsEditor.putInt(mWorldDescriptor.getKey() + "CurrentScore", mTotalScore);
		game.settingsEditor.commit();
		
		mScoreTable.setScores(levelScore, hitPoints, bonusPoints, lostPoints, mTotalScore);
	}
	
	
	/*
	 * Generate the next level to run (start it with startNextLevel()) 
	 */
	private Level generateNextLevel() {
		
		int interval = 800 - mLevelValue * 100;

		LevelDescriptor levelDesc = mWorldDescriptor.getLevelDescriptor(mLevelValue - 1);
		Level level = new Level(levelDesc.creaturesAlive, 
				levelDesc.creaturesAliveMinInterval, 
				levelDesc.creaturesAliveMaxInterval);
		
		for (LevelDescriptor.Hitable hitableDesc: levelDesc.hitableList) {
			/* Go through all descriptors of hitable objects of the level and generate
			 * the requested objects. */
			HitableObject hitableObject = null;
			
			for (int i = 0; i < hitableDesc.copies; i ++) {
				int chanceOfApperence = (int)(100.0/(hitableDesc.chance * 100.0) + 0.5);

				if (mRandom.nextInt(chanceOfApperence) == 0) {

					/* An hitable object should be created */
					
					try {
						for (Class<? extends Creature> creatureClass: game.creatureTypes) {
							if (creatureClass.getSimpleName().equalsIgnoreCase(hitableDesc.type)) {
								Log.d(TAG, "Create creature " + hitableDesc.type + " (level " + hitableDesc.level + ")...");
								Constructor<? extends Creature> creatureConstructor = creatureClass.getConstructor(GameBase.class, int.class);
								int creatureLevel = hitableDesc.level - 1;
								hitableObject = creatureConstructor.newInstance(game, creatureLevel);
							}
						}
						
						for (Class<? extends ItemBaseObject> itemClass: game.itemTypes) {
							if (itemClass.getSimpleName().equalsIgnoreCase(hitableDesc.type)) {
								Log.d(TAG, "Create item " + hitableDesc.type + "...");
								Constructor<? extends ItemBaseObject> itemConstructor = itemClass.getConstructor(GameBase.class);
								ItemBaseObject itemObject = itemConstructor.newInstance(game);
								hitableObject = new ItemBox(game, game.getScreenWidth() / 2, 0, itemObject);
							}
						}
					} catch (NoSuchMethodException e) {
						Log.e(TAG, e.getMessage(), e);
					} catch (IllegalArgumentException e) {
						Log.e(TAG, e.getMessage(), e);
					} catch (InstantiationException e) {
						Log.e(TAG, e.getMessage(), e);
					} catch (IllegalAccessException e) {
						Log.e(TAG, e.getMessage(), e);
					} catch (InvocationTargetException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
				if (hitableObject != null) {
					hitableObject.setMustBeKilled(hitableDesc.mustBeKilled);
					level.addObject(hitableObject, (int)(interval * (0.5 + mRandom.nextDouble())));
					
					//mHighestPossibleScore += hitableObject.getHitScore();
				}
			}
		}
		return level;
	}
	


    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {

    	for (TouchEvent touchEvent: touchEventList) {
    		if (mExitArrow.wasPressed(touchEvent)) {
	    		game.setStage(game.worldSelectStage);
    		}
	    	else if (mNextArrow.wasPressed(touchEvent)) {
    			if (mLevelValue <= mWorldDescriptor.getNumLevels()) {
    				setLevelState(LevelState.StartLevel);
    			}
    		}
    	}
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
    	mLevelText.setPosition(width/2, mLevelText.getHeight());
    	mWorldNameText.setPosition(width/2, height - mWorldNameText.getHeight());
    	mLoadingText.setPosition(width/2, height/2);
    	mScoreTable.setPosition(width/2, height/2);
    	game.health.setPosition(width, -0.9, 0, 0.5);
    	mExitArrow.setPosition(mExitArrow.getWidth() / 1.5, height - mExitArrow.getHeight() / 1.5);
    	mNextArrow.setPosition(width - mNextArrow.getWidth() / 1.5, height - mNextArrow.getHeight() / 1.5);
    }

}
