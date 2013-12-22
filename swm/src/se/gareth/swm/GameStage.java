package se.gareth.swm;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class GameStage extends Stage {

    private static final String TAG = GameStage.class.getName();

    /* Active Objects to update and draw each frame */
    private final ArrayList<GraphicObject> mGraphicObjectsAdd;
    protected final SortedArrayList<GraphicObject> mGraphicObjects;
    protected final SortedArrayList<HitableObject> mHitableObjects;
    private final ArrayList<ActiveObject> mCollisionObjects;

    private final BonusBar mBonusBar;

    private Background mBackground;

    private long mLevelFinishedTS;

    private Level mCurrentLevel;

    private class ComparatorGraphicObject implements Comparator<GraphicObject> {
        @Override
        public int compare(GraphicObject o1, GraphicObject o2) {
            return o1.getDrawOrder() - o2.getDrawOrder();
        }
    }

    private class ComparatorHitableObject implements Comparator<HitableObject> {
        @Override
        public int compare(HitableObject o1, HitableObject o2) {
            return o2.getDrawOrder() - o1.getDrawOrder();
        }
    }

    public GameStage(GameBase gameBase) {
        super(gameBase);

        mGraphicObjects = new SortedArrayList<GraphicObject>(new ComparatorGraphicObject());
        mGraphicObjectsAdd = new ArrayList<GraphicObject>(50);
        mHitableObjects = new SortedArrayList<HitableObject>(new ComparatorHitableObject());
        mCollisionObjects = new ArrayList<ActiveObject>(50);
        mBackground = null;

        mBonusBar = new BonusBar(gameBase);
    }

    /*
     * Reset game stage
     */
    public void setLevel(Level level, Background background) {
        mCurrentLevel = level;
        mGraphicObjects.clear();
        mGraphicObjectsAdd.clear();
        mHitableObjects.clear();
        mCollisionObjects.clear();
        mLevelFinishedTS = 0;
        game.score.setScore(0);
        mBonusBar.reset();
        mBackground = background;
    }

    public Level getLevel() {
        return mCurrentLevel;
    }

    @Override
    public void activated(Stage previousStage) {
    	if (previousStage != game.pauseStage) {
	    	mBackground.load();
	        mBackground.prepare(this);
	        addActiveObject(mBonusBar);
    	}
    }

    @Override
    public void deactivated(Stage nextStage) {
    	if (nextStage != game.pauseStage) {
    		mBackground.unload();
    	}
    }

    public void addCreature(final HitableObject hitableObject) {
        hitableObject.setFreeStanding(false);
        mGraphicObjectsAdd.add(hitableObject);
        hitableObject.isAlive();
        mCurrentLevel.creatureIsAlive(hitableObject);
    }
    
    public void addGraphicObject(final GraphicObject graphicObject) {
    	mGraphicObjectsAdd.add(graphicObject);
    }

    public void addActiveObject(final ActiveObject activeObject) {
        mGraphicObjectsAdd.add(activeObject);
        activeObject.isAlive();
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

                    game.sounds.play(Sounds.Sound.Miss1);

                    final Shot1 shot1 = new Shot1(game);
                    shot1.setPosition(x, y);

                    for (int i = 0; damagePoints > 0 && i < mHitableObjects.size(); i ++) {

                        final HitableObject hitableObject = mHitableObjects.get(i);


                        /* Test if shot is a hit */
                        double hit = hitableObject.isHit(shot1);

                        if (hit > 0.1) {

                            /* Object was hit! */
                            int hitPoints = (int)(hit * damagePoints);
                            int objectHitPoints = hitableObject.getHitPoints();
                            score += hitableObject.handleHit(hitPoints);
                            if (score > 0)
                                hits ++;

                            if (objectHitPoints > hitPoints)
                                damagePoints -= hitPoints;
                            else
                                damagePoints -= objectHitPoints;
                        }
                    }


                    if (hits == 0) {
                        /* Shot missed */
                        //game.sounds.play(Sounds.Sound.Miss1);
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
    
    private void collisionTest(final TimeStep timeStep) {
        
        for (int i = 0; i < mCollisionObjects.size(); i ++) {
        	final ActiveObject activeObject1 = mCollisionObjects.get(i);
            final int collisionId1 = activeObject1.getCollisionId();
            if (collisionId1 > 0) {
            	for (int j = (i + 1); j < mCollisionObjects.size(); j ++) {
                    final ActiveObject activeObject2 = mCollisionObjects.get(j);
                    final int collisionId2 = activeObject2.getCollisionId();
                    
                    if (collisionId2 == collisionId1) {
                        /* Test objects for collision */
                        if (ActiveObject.testCollision(activeObject1, activeObject2) == true) {
                            activeObject1.handleCollision(activeObject2);
                            activeObject2.handleCollision(activeObject1);
                        }
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
                final HitableObject creature = mCurrentLevel.getNext();
                addCreature(creature);
            }

            /* Insert newly added active objects into the sorted list */
            while (mGraphicObjectsAdd.size() > 0) {
                final GraphicObject graphicObject = mGraphicObjectsAdd.remove(mGraphicObjectsAdd.size() - 1);
                mGraphicObjects.addSort(graphicObject);
                if (graphicObject instanceof HitableObject) {
                    mHitableObjects.addSort((HitableObject)graphicObject);
                }
                if (graphicObject instanceof ActiveObject) {
                	final ActiveObject activeObject = (ActiveObject)graphicObject;
                	if (activeObject.getCollisionId() > 0) {
                		mCollisionObjects.add(activeObject);
                	}
                }
            }
        }

        /* Run all graphic objects update() function */
        for (int i = 0; i < mGraphicObjects.size(); i ++) {
            final GraphicObject graphicObject = mGraphicObjects.get(i);
            graphicObject.update(timeStep);
        }


        /* Test if an graphic object should be deleted */
        for (int i = 0; i < mGraphicObjects.size(); i ++) {
            final GraphicObject graphicObject = mGraphicObjects.get(i);
            if (graphicObject.isToBeDeleted() == true) {

                if (graphicObject instanceof HitableObject) {
                    final HitableObject hitableObject = (HitableObject)graphicObject;

                    if (hitableObject.isFreeStanding() == false) {
                        mCurrentLevel.addFinishedObject(hitableObject);
                    }
                    mHitableObjects.remove(hitableObject);
                }
                if (graphicObject instanceof ActiveObject) {
                    final ActiveObject activeObject = (ActiveObject)graphicObject;
                    if (activeObject.getCollisionId() > 0) {
                    	mCollisionObjects.remove(activeObject);
                    }
                }

                mGraphicObjects.remove(i);
                i --;
            }
        }

        collisionTest(timeStep);

        if (mBackground != null) {
            mBackground.update(timeStep);
        }

        game.itemBar.update(timeStep);
        game.health.update(timeStep);
        game.score.update(timeStep);

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
            mBackground.drawBackground(canvas);
        }
        else {
            canvas.drawARGB(0xff, 0xff, 0xff, 0xff);
        }


        /* Draw health and score icons behind all */
        game.health.draw(canvas);
        game.score.draw(canvas);

        for (int i = 0; i < mGraphicObjects.size(); i ++) {
            final GraphicObject graphicObject = mGraphicObjects.get(i);
            graphicObject.draw(canvas);
        }

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

        game.itemBar.draw(canvas);
    }


    @Override
    public void playfieldSizeChanged(int width, int height) {
        game.itemBar.setPosition(0, 0.5, height, 0.5);
        game.health.setPosition(width, -0.9, 0, 0.7);
        game.score.setPosition(0, 0.9, 0, 0.7);
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
