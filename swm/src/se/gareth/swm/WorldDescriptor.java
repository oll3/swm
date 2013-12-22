package se.gareth.swm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class WorldDescriptor {

    private static final String TAG = WorldDescriptor.class.getName();

    private final GameBase game;

    public final ArrayList<LevelDescriptor> levelList;
    private final String mName;

    /* The key value identifies this world object over time. As default this
     * is the value of the order of appearance of the world (from top to bottom
     * in the world xml file) but can be overridden if we in the future changes
     * the order. */
    private int mKey;

    /* Number of lifes */
    private int mHealth;

    //private String mBackgroundName;
    //private int mBackgroundColorParam;
    private Background mBackground;

    public WorldDescriptor(GameBase gameBase, String name, int key) {
        game = gameBase;
        mName = name;
        levelList = new ArrayList<LevelDescriptor>();
        mKey = key;
        mHealth = 3;
        SLog.d(TAG, "World=" + mName + " created");
        mBackground = null;
    }

    public void setHealth(int health) {
        mHealth = health;
    }

    public void setBackground(String backgroundName, int colorParam) {

        for (Class<? extends Background> backgroundClass: game.backgroundTypes) {
            if (backgroundClass.getSimpleName().equalsIgnoreCase(backgroundName)) {
                try {
                    mBackground = backgroundClass.getConstructor(GameBase.class, int.class).newInstance(game, colorParam);
                } catch (IllegalArgumentException e) {
                    SLog.pe(TAG, e.getMessage(), e);
                } catch (InstantiationException e) {
                    SLog.pe(TAG, e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    SLog.pe(TAG, e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    SLog.pe(TAG, e.getMessage(), e);
                } catch (NoSuchMethodException e) {
                    SLog.pe(TAG, e.getMessage(), e);
                }
                break;
            }
        }
    }

    public Background getBackground() {
        return mBackground;
    }


    public String getKey() {
        return "World" + mKey;
    }

    public void addLevelDescriptor(LevelDescriptor level) {
        levelList.add(level);
    }

    public LevelDescriptor getLevelDescriptor(int index) {
        return levelList.get(index);
    }

    public int getNumLevels() {
        return levelList.size();
    }

    public String getName() {
        return mName;
    }

    public int getHealth() {
        return mHealth;
    }
}
