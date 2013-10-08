package se.gareth.swm;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class GameBase {

	private static final String TAG = GameBase.class.getName();

	class Forces {
		
		private Vector2D mGravityForce;
		
		public Forces(Vector2D gravity) {
			mGravityForce = gravity;
		}
		
		public Vector2D getGravity() {
			return mGravityForce;
		}
	
	}


    protected final GameView gameView;
    
    
    public final SharedPreferences settings;
    public final SharedPreferences.Editor settingsEditor;

    private static final String STORAGE_FILE_NAME = "StorageFile";
	
    public final GameStage gameStage;
    public final LevelStage levelStage;
    public final PauseStage pauseStage;
    public final WorldSelectStage worldSelectStage;
    public final Resources res;
    public final Forces forces;

    public final ItemBar itemBar;
    public final HealthObject health;
    public final ScoreIcon score;
	
    public final Sounds sounds;
	
    private int mScreenWidth, mScreenHeight;
    
    private final Bitmap mWorldBackground;
    
    public final ArrayList<Class<? extends ItemBaseObject>> itemTypes;
    public final ArrayList<Class<? extends Creature>> creatureTypes;
    public final ArrayList<Class<? extends Background>> backgroundTypes;
	
    public GameBase(GameView view) {
		gameView = view;
		res = gameView.getResources();
		
		mWorldBackground = Sprite.loadFitBitmap(res, R.drawable.world_background1, view.mWidth, view.mHeight);
		
		forces = new Forces(new Vector2D(0.0, 1000.0));
			
		itemBar = new ItemBar(this, 10);		
		health = new HealthObject(this);
		score = new ScoreIcon(this);
    	itemBar.setPositionOffset(1.0, 1.0);
    	health.setPositionOffset(1.35, 0.0);
    	score.setPositionOffset(-0.35, 0.0);
		
    	settings = view.getContext().getSharedPreferences(STORAGE_FILE_NAME, 0);
    	settingsEditor = settings.edit();
    	    
    	
		/* Create the game stage */
		gameStage = new GameStage(this);
			
		/* Create the level stage */
		levelStage = new LevelStage(this);
	
		/* Create the pause stage */
		pauseStage = new PauseStage(this);
	
		
		sounds = new Sounds(this);
		
		itemTypes = new ArrayList<Class<? extends ItemBaseObject>>();
		creatureTypes = new ArrayList<Class<? extends Creature>>();
		backgroundTypes = new ArrayList<Class<? extends Background>>();
		
		/* Add all item types to list */
		itemTypes.add(BombItem.class);
		itemTypes.add(ExtraLifeItem.class);
		itemTypes.add(FreezeItem.class);
		itemTypes.add(FireItem.class);
		
		
		/* Add all creatures types to list */
		creatureTypes.add(Bird1.class);
		creatureTypes.add(Hen1.class);
		creatureTypes.add(Hummingbird1.class);
		creatureTypes.add(Owl1.class);
		creatureTypes.add(BirdWithCat.class);
		
		/* Add all background types to list */
		backgroundTypes.add(Background1.class);
		
		setPlayfieldSize(view.mWidth, view.mHeight);
		
		worldSelectStage = new WorldSelectStage(this);
		
		setStage(worldSelectStage);
    }
    
    public int convertPixelToDp(int input) { 
    	final float scale = res.getDisplayMetrics().density;
    	return (int) (input * scale + 0.5f); 
    }
	
	public void setPlayfieldSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
	}
	
	public int getScreenWidth() {
		return mScreenWidth;
	}
	
	public int getScreenHeight() {
		return mScreenHeight;
	}
	
	public int getPlayfieldHeight() {
		return mScreenHeight - (int)(res.getDimension(R.dimen.ItemBarHeight) + 0.5);
	}

	public void setStage(Stage stage) {
		gameView.setStage(stage);
	}

	public Stage getStage() {
		return gameView.getStage();
	}
	
	public void drawWorldBackground(Canvas canvas) {
		canvas.drawBitmap(mWorldBackground, 0, 0, null);
	}
}

