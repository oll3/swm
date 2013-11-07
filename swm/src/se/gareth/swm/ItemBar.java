package se.gareth.swm;

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

    public final ArrayList<ItemBaseObject> getItemList() {
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

        for (int i = 0; i < mItemList.size(); i ++) {
            final ItemBaseObject item = mItemList.get(i);

            if (wasSelected) {
                /* Move items to the left */
                item.removeAllBehaviors();
                double newX = getX() + (item.getWidth() * i) + item.getWidth() / 2;
                double newY = getY() - item.getHeight() / 2;
                item.addBehavior(new MoveToBehavior(game, newX, newY, 500.0));
            }
            else if (item.containsPos(x, y)) {
                mItemList.remove(i);
                i --;
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
            double x = getX() + item.getWidth() * i + item.getWidth() / 2;
            double y = getY() - item.getHeight() / 2;

            if (item.iconIsDisplayed()) {
                item.setPosition(x, y);
            }
            item.update(timeStep);
        }
    }

    @Override
    public void draw(Canvas canvas) {

        for (int i = 0; i < mItemList.size(); i ++) {
            mItemList.get(i).draw(canvas);
        }

    }
}
