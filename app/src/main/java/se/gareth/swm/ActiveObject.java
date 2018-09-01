package se.gareth.swm;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

public class ActiveObject extends GraphicObject {

    private static final String TAG = ActiveObject.class.getName();

    /* ID value for active object */
    public final int id;

    private static int mIdCnt;

    /* Random object shared for all active objects */
    protected static final Random mRandom = new Random();

    protected double mCollisionRadius; /* The calculated collision radius of the object */

    /* Velocity of object */
    protected final Vector2D mVelocity;
    private double mSpeed, mDirection;
    private final double mSpeedMultiple;

    /* Used for calculate sum of all forces acting on the object */
    private final Vector2D mForcesSum;

    /* Location and velocity before last update */
    protected final Vector2D mLastLocation;
    protected final Vector2D mLastVelocity;


    /* Collision ID (object collides with other objects with same ID) */
    protected int mCollisionId;

    /* Decides how much impact the drag has on the object */
    private double mDensityInverted;

    /* Is movement enabled/disabled */
    private boolean mMovementDisabled;

    /* List of forces which operate on our velocity */
    private final ArrayList<Vector2D> mForces;


    private ArrayList<Behavior> mBehaviorList;

    public ActiveObject(GameBase gameBase) {
        super(gameBase);
        id = mIdCnt;
        mIdCnt ++;
        mCollisionRadius = 0.0;
        mVelocity = new Vector2D();
        mForcesSum = new Vector2D();
        mLastLocation = new Vector2D();
        mLastVelocity = new Vector2D();
        mCollisionId = 0;
        mSpeed = 0.0;
        mDirection = 0.0;
        mForces = new ArrayList<Vector2D>();
        mBehaviorList = new ArrayList<Behavior>();
        mMovementDisabled = false;
        mSpeedMultiple = game.getSpeedMultiple();
        setDensity(200);
    }

    /*
     * Apply a force to act upon the objects velocity
     */
    public void applyForce(Vector2D vector) {
        mForces.add(vector);
    }

    public void removeForce(Vector2D vector) {
        mForces.remove(vector);
    }

    public void removeAllForces() {
        mForces.clear();
    }

    public void addBehavior(Behavior behavior) {
        mBehaviorList.add(behavior);
        behavior.wasAdded(this);
    }

    /*
     * Called when a behavior has finished acting on our object
     */
    protected void behaviorFinished(Behavior behavior) {

    }

    public void removeBehavior(Behavior behavior) {
        mBehaviorList.remove(behavior);
        behaviorFinished(behavior);
    }

    public void removeAllBehaviors() {
        mBehaviorList.clear();
    }

    public void setVelocity(double direction, double speed) {
        mSpeed = speed;
        mDirection = direction;
        mVelocity.setDirectionMagnitude(mDirection, mSpeed);
    }

    public void addVelocity(Vector2D velocityVector) {
        mVelocity.add(velocityVector);
    }

    /*
     * Set speed (without changing the direction)
     */
    public void setSpeed(double speed) {
        mSpeed = speed;
        mDirection = mVelocity.getDirection();
        mVelocity.setDirectionMagnitude(mDirection, mSpeed);
    }


    /*
     * Disable object from moving
     */
    public void disableMovement(boolean disable) {
        mMovementDisabled = disable;
    }


    /*
     * Get movement disable flag
     */
    public boolean movementIsDisabled() {
        return mMovementDisabled;
    }


    /*
     * Set direction (without changing the speed)
     */
    public void setDirection(double direction) {
        mDirection = direction;
        mSpeed = mVelocity.getMagnitude();
        mVelocity.setDirectionMagnitude(mDirection, mSpeed);
    }


    /*
     * Set object density (used for drag calculation).
     * The higher the density the lesser the impact of drag is.
     */
    public void setDensity(double density) {
        if (density > 0) {
            mDensityInverted = 1.0/density;
        }
    }

    /*
     * make object look in the direction of given position
     */
    public void lookAt(double x, double y) {
        mSpeed = mVelocity.getMagnitude();
        mVelocity.set(x - getX(), y - getY());
        mDirection = mVelocity.getDirection();
        mVelocity.setDirectionMagnitude(mDirection, mSpeed);
    }

    public void setAnimation(Animation animation) {
        super.setAnimation(animation);
        mCollisionRadius = (double)(Math.min(getWidth(), getHeight()))/2.5;
    }


    protected static boolean circleIntersect(double x1, double y1, double r1, double x2, double y2, double r2) {
        final double r = r1 + r2;
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        return (r*r) > (dx*dx) + (dy*dy);
    }

    public static boolean testCollision(ActiveObject object1, ActiveObject object2) {
        return circleIntersect(object1.getX(), object1.getY(), object1.mCollisionRadius,
                               object2.getX(), object2.getY(), object2.mCollisionRadius);
    }


    public void handleCollision(ActiveObject object) {
        /* Do nothing */
    }

    /* Set collision ID. Object will collide with other objects with same ID (if ID != 0) */
    public void setCollisionId(int id) {
        mCollisionId = id;
    }

    /* Get collision ID */
    public int getCollisionId() {
        return mCollisionId;
    }

    /*
     * Called when object is alive (added to list of objects to
     * be updated). Override to detect the event.
     */
    protected void isAlive() {

    }


    public void draw(Canvas canvas) {

        super.draw(canvas);

        /* Apply behaviors upon object */
        for (int i = 0; i < mBehaviorList.size(); i ++) {
            mBehaviorList.get(i).draw(this, canvas);
        }

        if (false) {
            canvas.drawCircle((float)getX(), (float)getY(), (float)mCollisionRadius, mPaint);
        }
    }

    public void update(final TimeStep timeStep) {

        /* Save current location and velocity */
        mLastLocation.set(location);
        mLastVelocity.set(mVelocity);

        if (!mMovementDisabled) {

            /* Calculate the total force acting upon our object */
            if (mForces.size() > 0) {

                final double v = mVelocity.getMagnitude();

                mForcesSum.set(0, 0);
                mForcesSum.add(mForces);
                mForcesSum.mulAdd(mVelocity, -v * mDensityInverted);
            }

            /* Move the object according to our velocity */
            mVelocity.mulAdd(mForcesSum, timeStep.get());
            location.mulAdd(mVelocity, timeStep.get() * mSpeedMultiple);
            location.mulAdd(mForcesSum, -timeStep.getSquared()/2.0 * mSpeedMultiple);
        }


        /*
         * Apply behaviors upon object
         */
        /* Only run if there are behaviors in list */
        for (int i = 0; i < mBehaviorList.size(); i ++) {
            Behavior behavior = mBehaviorList.get(i);
            behavior.update(this, timeStep);
            if (behavior.isDone()) {
                behaviorFinished(behavior);
                mBehaviorList.remove(i);
                i --;
            }
        }

        super.update(timeStep);
    }


    @Override
    public String toString() {
        return "" + getClass() + " (" + id + ")";
    }
}
