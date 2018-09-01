package se.gareth.swm;

import java.util.ArrayList;

public class LevelDescriptor {

    private static final String TAG = LevelDescriptor.class.getName();

    enum Type {
        Regular,
            Bonus}

    class Hitable {
        public final String type;
        public final int level;
        public final double chance;
        public final int copies;
        public final boolean mustBeKilled;

        public Hitable(String hitableType, int hitableLevel, double hitableChance, int hitableCopies, boolean hitableMustBeKilled) {
            type = hitableType;
            level = hitableLevel;
            chance = hitableChance;
            copies = hitableCopies;
            mustBeKilled = hitableMustBeKilled;
        }
    }

    public String description;
    public Type type;
    public ArrayList<Hitable> hitableList;
    public final int creaturesAlive;
    public final long creaturesAliveMinInterval;
    public final long creaturesAliveMaxInterval;

    public LevelDescriptor(Type levelType, int creaturesAlive,
                           long creaturesAliveMinInterval, long creaturesAliveMaxInterval) {
        type = levelType;
        description = "";
        hitableList = new ArrayList<Hitable>();
        this.creaturesAlive = creaturesAlive;
        this.creaturesAliveMinInterval = creaturesAliveMinInterval;
        this.creaturesAliveMaxInterval = creaturesAliveMaxInterval;
        SLog.d(TAG, "Level name=" +", type=" + type + " created");
    }

    public void setDesctiption(String text) {
        description = text;
    }

    public void addHitable(String hitableType, int hitableLevel, double hitableChance, int hitableCopies, boolean hitableMustBeKilled) {
        hitableList.add(new Hitable(hitableType, hitableLevel, hitableChance, hitableCopies, hitableMustBeKilled));
        SLog.d(TAG, "New Hitable=" + hitableType +", level=" + hitableLevel + ", chance=" + hitableChance + ", copies=" + hitableCopies);
    }

}
