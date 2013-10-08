package se.gareth.swm;

import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.Canvas;


/* Object representing the bar with items */
public class ItemBar extends GraphicObject {
	
	LinkedList<ItemBaseObject> mItemList;
	
	private final int mMaxItems;
	
	public ItemBar(GameBase gameBase, int maxItems) {
		super(gameBase);
		
		mItemList = new LinkedList<ItemBaseObject>();
		mMaxItems = maxItems;
	}
	
	public void reset() {
		mItemList.clear();
	}
	
	public int getMaxItems() {
		return mMaxItems;
	}
	
	public final LinkedList<ItemBaseObject> getItemList() {
		return mItemList;
	}
	
	/* Add an item to be displayed in the item bar */
	public void addItem(ItemBaseObject item, boolean direct) {
		int numItems = mItemList.size();
		if (numItems < mMaxItems) {
			mItemList.add(item);
			double x = getX() + item.getWidth() * numItems + item.getWidth() / 2;
			double y = getY() - item.getHeight() / 2;
			if (!direct) {
				item.addBehavior(new MoveToBehavior(game, x, y, 1000.0));
			}
			else {
				item.setPosition(x, y);
				item.displayIcon();
			}
		}
	}
	
	public void addItem(ItemBaseObject item) {
		addItem(item, false);
	}
	
	public boolean testIfSelected(double x, double y) {
		boolean wasSelected = false;
		int itemIndex = 0;
		
		Iterator<ItemBaseObject> itemitr = mItemList.iterator();
	    while (itemitr.hasNext()) {
	    	ItemBaseObject item = itemitr.next();
		
			if (wasSelected) {
				/* Move items to the left */
				item.removeAllBehaviors();
				double newX = getX() + item.getWidth() * (itemIndex - 1) + item.getWidth() / 2;
				double newy = getY() - item.getHeight() / 2;
				item.addBehavior(new MoveToBehavior(game, newX, newy, 500.0));
			}
			else if (item.containsPos(x, y)) {
				itemitr.remove();
				wasSelected = true;
				item.useItem();
				game.sounds.play(Sounds.Sound.Click1);
			}
			itemIndex ++;
		}
		return wasSelected;
	}
	
	
	@Override
	public void update(double frameTime) {
		int i = 0;
		for (ItemBaseObject item: mItemList) {
			double x = getX() + item.getWidth() * i + item.getWidth() / 2;
			double y = getY() - item.getHeight() / 2;
			
			if (item.iconIsDisplayed()) {
				item.setPosition(x, y);
			}
			item.update(frameTime);
			i ++;
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		
		for (ItemBaseObject item: mItemList) {
			item.draw(canvas);
		}
		
	}
}
