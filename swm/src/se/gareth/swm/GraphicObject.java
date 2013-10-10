package se.gareth.swm;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GraphicObject {

	/* The position of the object */
	protected double posX, posY;
    private double posLeft, posRight, posTop, posBottom;
	
	protected double mPositionOffsetX, mPositionOffsetY;
	private double mPositionOffsetScaleWidth, mPositionOffsetScaleHeight;
	
	private double width, height;
	
	private boolean mDoDrawArea;
	protected boolean mVisible;
	
	private boolean mToBeDeleted;
	
	private int mAlpha;
	
	protected Paint mPaint;
	
	private int mDrawOrder;
	
	/* Game object reference */
	protected GameBase game;
	
	/* Contains the animations of object */	
	private Animation mCurrentAnimation;
	
	private float mRotation;
	private float mRotationOffsetX, mRotationOffsetY;
	
	public GraphicObject(GameBase gameBase) {
		game = gameBase;
		mDrawOrder = 0;
	    posX = 0;
		posY = 0;
		width = 0;
		height = 0;
		mRotation = 0;
		mVisible = true;
		
		mDoDrawArea = false;
		mCurrentAnimation = null;
		
		mToBeDeleted = false;
		mPositionOffsetScaleWidth = 0.5;
		mPositionOffsetScaleHeight = 0.5;
		mRotationOffsetX = 0.5f;
		mRotationOffsetY = 0.5f;
		
		mPaint = new Paint();
	}
	
	public void hide() {
		mVisible = false;
	}
	
	public void show() {
		mVisible = true;
	}
	
	protected void setDrawOrder(int drawOrder) {
		mDrawOrder = drawOrder;		
	}
	
	public int getDrawOrder() {
		return mDrawOrder;
	}
	
	public void enableDrawArea(boolean enable) {
		mDoDrawArea = enable;
	}
	
	public void setPosition(double x, double y) {
		posX = x;
		posY = y;
		posLeft = posX - mPositionOffsetX;
		posRight = posLeft + width;
		posTop = posY - mPositionOffsetY;
		posBottom = posTop + height;
	}
	
	public void setX(double x) {
		posX = x;
		posLeft = posX - mPositionOffsetX;
		posRight = posLeft + width;
	}

	public void setY(double y) {
		posY = y;
		posTop = posY - mPositionOffsetY;
		posBottom = posTop + height;
	}

	public void addPosition(double relX, double relY) {
		posX += relX;
		posY += relY;

		posLeft = posX - mPositionOffsetX;
		posRight = posLeft + width;
		posTop = posY - mPositionOffsetY;
		posBottom = posTop + height;
	}

	public double getX(double scalar) {
		return posLeft + width * scalar;
	}

	public double getY(double scalar) {
		return posTop + height * scalar;
	}

	public double getX() {
		return posX;
	}
	
	public double getY() {
		return posY;
	}
	
	public double getLeft() {
		return posLeft;
	}
	
	public double getTop() {
		return posTop;
	}
	
	public double getRight() {
		return posRight;
	}
	
	public double getBottom() {
		return posBottom;
	}
	
	public double getCenterX() {
		return posLeft + width / 2.0;
	}
	
	public double getCenterY() {
		return posTop + height / 2.0;
	}

	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setAnimation(Animation animation) {
		mCurrentAnimation = animation;
		if (animation != null) {
			width = animation.getWidth();
			height = animation.getHeight();
		}
		else {
			width = 0;
			height = 0;
		}
		setPositionOffset(mPositionOffsetScaleWidth, mPositionOffsetScaleHeight);
	}
	

	/* Set where the object should be centered around. 
	 * A setting of 0.0, 0.0 is top left, and 1.0, 1.0 is bottom right */
	public void setPositionOffset(double widthScale, double heightScale) {
		mPositionOffsetScaleWidth = widthScale;
		mPositionOffsetScaleHeight = heightScale;
		mPositionOffsetX = width * mPositionOffsetScaleWidth;
		mPositionOffsetY = height * mPositionOffsetScaleHeight;

		posLeft = posX - mPositionOffsetX;
		posRight = posLeft + width;
		posTop = posY - mPositionOffsetY;
		posBottom = posTop + height;
	}
	
	public boolean containsPos(double x, double y) {
		if (mVisible)
			return (x >= posLeft && x < posRight && y >= posTop && y < posBottom);
		return false;
	}

	public Animation getCurrentAnimation() {
		return mCurrentAnimation;
	}
	
	public void setAlpha(int alpha) {
		if (alpha != mAlpha) {
			mAlpha = alpha;
			mPaint.setAlpha(mAlpha);
		}
	}
	
	public int getAlpha() {
		return mAlpha;
	}
	
	
	/* Tell game stage to stop using this object */
	protected void deleteMe() {
		mToBeDeleted = true;
	}
	
	/* Test if object should be deleted */
	public boolean isToBeDeleted() {
		return mToBeDeleted;
	}
	
	public void setRotationOffset(float widthScale, float heightScale) {
		mRotationOffsetX = widthScale;
		mRotationOffsetY = heightScale;
	}
	
	public void setRotation(float degrees) {
		mRotation = degrees;
	}
	
	public float getRotation() {
		return mRotation;
	}
	
	public void addRotation(float degrees) {
		mRotation += degrees;
	}
	
	public void draw(Canvas canvas) {

		if (mVisible) {
		
			if (mDoDrawArea) {
				/* Enable to draw the object area */
				Rect areaRect = new Rect();
				areaRect.left = (int)(posLeft + 0.5);
				areaRect.top = (int)(posTop + 0.5);
				areaRect.right = (int)(posRight + 0.5);
				areaRect.bottom = (int)(posBottom + 0.5);
				canvas.drawRect(areaRect, mPaint);
			}
			
			if (mCurrentAnimation != null) {
				mCurrentAnimation.draw(canvas, 
						posX - mPositionOffsetX, posY - mPositionOffsetY, 
						mRotation, (float)width * mRotationOffsetX, (float)height * mRotationOffsetY, 
						mPaint);
			}
		}
	}
	
	public void update(final TimeStep timeStep) {
		if (mCurrentAnimation != null) {
			mCurrentAnimation.update();
		}
	}
	

}
