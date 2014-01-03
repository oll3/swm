package se.gareth.swm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.graphics.Canvas;

/* Object representing the bar with items */
public class ItemBar extends GraphicObject {

    private static final String TAG = ItemBar.class.getName();

    ArrayList<ItemBaseObject> mItemList;

    private final int mMaxItems;

    public ItemBar(GameBase gameBase, int maxItems) {
        super(gameBase);

        mItemList = new ArrayList<ItemBaseObject>();
        mMaxItems = maxItems;
    }

    public void reset() {
        mItemList.clear();
    }

    public int getMaxItems() {
        return mMaxItems;
    }

    public void saveItems(String key) {
    	int itemIndex = 0;
    	for (int i = 0; i < mItemList.size(); i ++) {
    		for (int c = 0; c < mItemList.get(i).getCount(); c ++) {
    			game.settingsEditor.putString(key + "Item" + itemIndex, mItemList.get(i).getClass().getSimpleName());
    			itemIndex ++;
    		}
    	}
        while (itemIndex < getMaxItems()) {
            game.settingsEditor.remove(key + "Item" + itemIndex);
            itemIndex ++;
        }
    	game.settingsEditor.commit();
    }
    
    public void loadItems(String key) {
        for (int i = 0; i < game.itemBar.getMaxItems(); i ++) {
            String itemTypeName = game.settings.getString(key + "Item" + i, "");

            for (Class<? extends ItemBaseObject> itemClass: game.itemTypes) {
                if (itemClass.getSimpleName().equals(itemTypeName)) {
                    /* Instance item object */
                    try {
                        ItemBaseObject itemObject = itemClass.getConstructor(GameBase.class).newInstance(game);
                        game.itemBar.addItem(itemObject, true);
                        SLog.i(TAG, "Restored item " + itemTypeName);
                    } catch (NoSuchMethodException e) {
                        SLog.pe(TAG, e.getMessage(), e);
                    } catch (IllegalArgumentException e) {
                        SLog.pe(TAG, e.getMessage(), e);
                    } catch (InstantiationException e) {
                        SLog.pe(TAG, e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        SLog.pe(TAG, e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        SLog.pe(TAG, e.getMessage(), e);
                    }
                }
            }
        }
    }
    
    
    /* Add an item to be displayed in the item bar */
    public void addItem(ItemBaseObject item, boolean direct) {
        int numItems = mItemList.size();
        item.inBar = false;
        
        if (numItems < mMaxItems) {
        	double x, y;
        	ItemBaseObject sameItem = null;
    	    for (int i = 0; i < mItemList.size(); i ++) {
        		if (mItemList.get(i).inBar && mItemList.get(i).getClass() == item.getClass()) {
        			sameItem = mItemList.get(i);
        			break;
        		}
        	}
        	
            mItemList.add(item);
            
            if (sameItem != null) {
                x = sameItem.getX();
                y = sameItem.getY();
            }
            else {
	            x = getX() + item.getWidth() * numItems + item.getWidth() / 2;
	            y = getY() - item.getHeight() / 2;
            }
            
            if (!direct) {
               item.addBehavior(new MoveToBehavior(game, x, y, 1000.0));
            }
            else {
                item.setPosition(x, y);
                item.displayIcon();
                item.hasStopped = true;
            }
        }
    }

    public void addItem(ItemBaseObject item) {
        addItem(item, false);
    }

    public boolean testIfSelected(double x, double y) {
        boolean wasSelected = false;

        for (int i = 0; i < mItemList.size(); i ++) {
            final ItemBaseObject item = mItemList.get(i);

            if (wasSelected) {
                /* Move items to the left */
                item.removeAllBehaviors();
                double newX = getX() + (item.getWidth() * i) + item.getWidth() / 2;
                double newY = getY() - item.getHeight() / 2;
                item.addBehavior(new MoveToBehavior(game, newX, newY, 500.0));
            }
            else if (item.inBar && item.containsPos(x, y)) {
            	item.decreaseCount();
            	if (item.getCount() <= 0) {
	                mItemList.remove(i);
	                i --;
            	}
            	wasSelected = true;
                item.useItem();
                game.sounds.play(Sounds.Sound.Click1);
            }
        }
        return wasSelected;
    }


    @Override
    public void update(final TimeStep timeStep) {
        for (int i = 0; i < mItemList.size(); i ++) {
        	
            final ItemBaseObject item = mItemList.get(i);
            item.update(timeStep);
            
            if (item.hasStopped && !item.inBar) {
            	ItemBaseObject sameItem = null;
                for (int j = 0; j < mItemList.size(); j ++) {
                	final ItemBaseObject tmpItem = mItemList.get(j);
                	if (tmpItem.inBar && tmpItem != item && tmpItem.getClass() == item.getClass()) {
                		/* An item of same kind is already in list */
                		sameItem = tmpItem;
                		break;
                	}
                }
            	if (sameItem != null) {
            		mItemList.remove(i);
            		i --;
            		sameItem.increaseCount();
            	}
            	else {
	                double x = getX() + item.getWidth() * i + item.getWidth() / 2;
	                double y = getY() - item.getHeight() / 2;
	                item.setPosition(x, y);
	                item.inBar = true;
            	}
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

        for (int i = 0; i < mItemList.size(); i ++) {
            mItemList.get(i).draw(canvas);
        }

    }
}
