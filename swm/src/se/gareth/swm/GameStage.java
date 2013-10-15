package se.gareth.swm;

import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class GameStage extends Stage {

	private static final String TAG = GameStage.class.getName();
	    
    /* Active Objects to update and draw each frame */
    private final LinkedList<GraphicObject> mGraphicObjectsAdd;
    protected final LinkedList<GraphicObject> mGraphicObjects;
    protected final LinkedList<HitableObject> mHitableObjects;
    
    private final BonusBar mBonusBar;
    
    private Background mBackground;
    
    private long mLevelFinishedTS;
    
    private Level mCurrentLevel;
    
    
    public GameStage(GameBase gameBase) {
    	super(gameBase);
    	
        mGraphicObjects = new LinkedList<GraphicObject>();
        mGraphicObjectsAdd = new LinkedList<GraphicObject>();  	 
        mHitableObjects = new LinkedList<HitableObject>();
        mBackground = null;
        
        mBonusBar = new BonusBar(gameBase);
    }

    /*
     * Reset game stage
     */
    public void setLevel(Level level) {
    	mCurrentLevel = level;
        mGraphicObjects.clear();
        mGraphicObjectsAdd.clear();
        mHitableObjects.clear();
        mLevelFinishedTS = 0;
        game.score.setScore(0);
        mBonusBar.reset();
    }
    
    public Level getLevel() {
    	return mCurrentLevel;
    }
    
    public void setBackground(Background background) {
    	mBackground = background;
    }

    @Override
    public void activated(Stage previousStage) {
    	
    }
    
    @Override
    public void deactivated(Stage nextStage) {

    }
    
    public void addCreature(HitableObject hitableObject) {
    	hitableObject.setFreeStanding(false);
    	mGraphicObjectsAdd.add(hitableObject);
    	hitableObject.isAlive();
    	mCurrentLevel.creatureIsAlive(hitableObject);
    }
    
    public void addActiveObject(ActiveObject ao) {
    	mGraphicObjectsAdd.add(ao);
		ao.isAlive();
    }
     
	
	private void addScore(int points, double x, double y) {

		int bonusPoints = mBonusBar.calcBonus(points);
		
		if (points > 0) {
			/* Positive points */
			mCurrentLevel.addHitPoints(points);

			int color = Color.rgb(0x50, 
					Math.min(0xcf + (bonusPoints*2), 255), 
					Math.min(0x79 + (bonusPoints*2), 255));
			points += bonusPoints;
			addActiveObject(new ScoreEffectObject(game, x, y,
					game.res.getDimension(R.dimen.NormalFontSize)  * (1.0 + Math.min(bonusPoints / 100.0, 0.5)), 
							color, points));
			
			mCurrentLevel.addBonusPoints(bonusPoints);
		}
		else if (points < 0) {
			/* Negative points */
			
			if ((mCurrentLevel.getScore() + points) < 0) {
				points = -mCurrentLevel.getScore();
			}
			if (points < 0) {
				int color = Color.argb(255, 255, 0, 0);
		    	addActiveObject(new ScoreEffectObject(game, x, y, 
		    			game.res.getDimension(R.dimen.NormalFontSize)  * (1.0 + Math.min(bonusPoints / 100.0, 0.5)), 
		    			color, points));
		    	
		    	mCurrentLevel.addLostPoints(points);
			}
		}
		mCurrentLevel.addScore(points);
		game.score.addScore(points);		
	}
    

    public void hitObject(HitableObject ho, int damage) {
		int hitScore = ho.handleHit(damage);
		if (hitScore > 0) {
			/* Hit gave score points */
	    	addScore(hitScore, ho.getX(), ho.getY());
		}
    }
    
    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {

		int damagePoints = 150;
		int hits = 0;
		for (TouchEvent touchEvent: touchEventList) {
			if (touchEvent.type == TouchEvent.TouchType.Down) {
				int x = (int)touchEvent.x;
				int y = (int)touchEvent.y;
	    	
				if (mLevelFinishedTS != 0) {
					return ;
				}
			
				if (game.itemBar.testIfSelected(touchEvent.x, touchEvent.y)) {
					
				}
				else {
					int score = 0;
					
					Shot1 shot1 = new Shot1(game);
					shot1.setPosition(x, y);
					
					Iterator<HitableObject> aoitr = mHitableObjects.descendingIterator();
					while (aoitr.hasNext()) {
						HitableObject hitableObject = aoitr.next();
						/* Test if shot is a hit */
						int hitScore = (int)(hitableObject.isHit(shot1) * (double)damagePoints);
						if (hitScore > 0) {
							/* Object was hit! */
							score += hitableObject.handleHit(damagePoints);
							hits ++;
							damagePoints /= 2; /* half the damage of shot */
						}
					}
		
		    
					if (hits == 0) {
						/* Shot missed */
						game.sounds.play(Sounds.Sound.Miss1);
						addScore(-100, x, y);
					}
					else {
						/* Hit gave score points */
						addScore(score, touchEvent.x, touchEvent.y);
					}
				}
			}
		}
    }
	
	@Override
	public void update(final TimeStep timeStep) {
        
    	if (mLevelFinishedTS == 0) {
    		
        	/* Create a new bird if it's time */
    		if (mCurrentLevel.timeForNextCreature()) {
    			HitableObject creature = mCurrentLevel.getNext();
    			addCreature(creature);
    		}
    		
	    	/* Insert newly added active objects into the sorted list */
	    	if (mGraphicObjectsAdd.size() > 0) {
	
	    		for (GraphicObject ao: mGraphicObjectsAdd) {
	    			if (mGraphicObjects.size() > 0) {
	    				GraphicObject first = mGraphicObjects.get(0);
	    				GraphicObject last = mGraphicObjects.get(mGraphicObjects.size() - 1);
		    			last = mGraphicObjects.get(mGraphicObjects.size() - 1);
	    				if (ao.getDrawOrder() < first.getDrawOrder()) {
	    					mGraphicObjects.add(0, ao);
	    				}
	    				else if (ao.getDrawOrder() >= last.getDrawOrder()) {
	    					mGraphicObjects.add(ao);
	    				}
	    				else {
	    					int index = 0;
	        		    	Iterator<GraphicObject> itr = mGraphicObjects.iterator();
	        		    	while (itr.hasNext()) {
	        		    		GraphicObject nextObject = itr.next();
	        		    		if (ao.getDrawOrder() < nextObject.getDrawOrder()) {
	        		    			mGraphicObjects.add(index, ao);
	        		    			break;
	        		    		}
	        		    		index ++;
	        		    	}
	    				}
	    			}
	    			else {
	    				mGraphicObjects.add(ao);
	    			}
	    			
		           	if (ao instanceof HitableObject) {
		           		mHitableObjects.add((HitableObject)ao);
		           	}
	    		}
	    		mGraphicObjectsAdd.clear();
	    	}
    	}
    	
    	/* Run all graphic objects update() function */
       	Iterator<GraphicObject> goitr = mGraphicObjects.iterator();
    	while (goitr.hasNext()) {
    		GraphicObject graphicObject = goitr.next();
    		graphicObject.update(timeStep);
    	}

    	
    	/* Test if an graphic object should be deleted */
       	goitr = mGraphicObjects.iterator();
    	while (goitr.hasNext()) {
    		GraphicObject graphicObject = goitr.next();
    		if (graphicObject.isToBeDeleted() == true) {
    			
    			if (graphicObject instanceof HitableObject) {
    				HitableObject hitableObject = (HitableObject)graphicObject;
    				
    				if (hitableObject.isFreeStanding() == false) {
    					mCurrentLevel.addFinishedObject(hitableObject);
    				}
			    	mHitableObjects.remove(hitableObject);
    			}
    			
    			goitr.remove();
				//Log.d(TAG, "Deleted graphic object " + graphicObject);
    		}
    	}
    	
    	if (mBackground != null) {
    		mBackground.update(timeStep);
    	}
    	
    	game.itemBar.update(timeStep);
    	game.health.update(timeStep);
    	game.score.update(timeStep);
    	
    	mBonusBar.update(timeStep);

    	if (mLevelFinishedTS == 0) {
    		if (mCurrentLevel.hasFailed() || mCurrentLevel.hasFinished()) {
    			mLevelFinishedTS = System.currentTimeMillis();
    		}
    	}
    	else if ((mLevelFinishedTS + 1000) < System.currentTimeMillis()) {
    		game.setStage(game.levelStage);
    	}
	}

	
	@Override
	public void draw(Canvas canvas) {

	    /* 
	     * Do drawing 
	     */
		if (mBackground != null) {
			mBackground.draw(canvas);
		}
		else {
			canvas.drawARGB(0xff, 0xff, 0xff, 0xff);
		}


	    /* Draw health and score icons behind all */
	    game.health.draw(canvas);
	    game.score.draw(canvas);
	    mBonusBar.draw(canvas);

	    Iterator<GraphicObject> aoitr = mGraphicObjects.iterator();
	    while (aoitr.hasNext()) {
	    	GraphicObject ao = aoitr.next();
	    	ao.draw(canvas);
	    }
            	
	    game.itemBar.draw(canvas);
            	
	    if (mLevelFinishedTS > 0) {
			long fade = System.currentTimeMillis() - mLevelFinishedTS;
			fade = (255 * fade) / 1000;
			if (fade > 255) fade = 255;
	            		
			if (mCurrentLevel.hasFailed()) {
			    /* Level has failed - Fade out */
			    int color = game.res.getColor(R.color.FailColor);
			    canvas.drawColor(Color.argb((int)fade, Color.red(color), Color.green(color), Color.blue(color)));
			}
			else {
			    /* Level was finished - Fade out */
			    int color = game.res.getColor(R.color.NormalBackground);
			    canvas.drawColor(Color.argb((int)fade, Color.red(color), Color.green(color), Color.blue(color)));
			}
	    }
	}


	@Override
	public void playfieldSizeChanged(int width, int height) {
    	game.itemBar.setPosition(0, 0.5, height, 0.5);
    	game.health.setPosition(width, -0.9, 0, 0.5);
    	game.score.setPosition(0, 0.9, 0, 0.5);
	}
	
	@Override
	public void onPause() {
		game.setStage(game.pauseStage);
	}
	
	@Override
	public void onStop() {
		game.setStage(game.pauseStage);
	}

}
