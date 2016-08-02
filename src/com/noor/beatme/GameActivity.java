package com.noor.beatme;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.scene.CameraScene;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.R.integer;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Toast;

public class GameActivity extends BaseGameActivity implements IOnSceneTouchListener{
   //For load AndEngine camera
	private Camera mCamera;
	private Scene mMainScene;
	
	int win=1;
	
	 int minDuration = 4; //  speedup 2= 3,4  next =2,6 last 1,7
	 int maxDuration = 6;

	//For player
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mPlayerTextureRegion;
	private Sprite player;
	
	//For target
	private TextureRegion mTargetTextureRegion;
	private LinkedList targetLL;
	private LinkedList TargetsToBeAdded;
	
	//For shot 
	private LinkedList projectileLL;
	private LinkedList projectilesToBeAdded;
	private TextureRegion mProjectileTextureRegion;
	
	//for music
	private Sound shootingSound;
	private Music backgroundMusic;
	
	//For Pause screen
	private TextureRegion mPausedTextureRegion;
	private CameraScene mPauseScene;
	
	
	//For win or fail
	private CameraScene mResultScene;
	private boolean runningFlag = false;
	private boolean pauseFlag = false;
	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	private ChangeableText score;
	public int hitCount;
	private int maxScore = 10;
	private Sprite winSprite;
	private Sprite failSprite;
	private TextureRegion mWinTextureRegion;
	private TextureRegion mFailTextureRegion;
	
	

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public Engine onLoadEngine() {

		final Display display = getWindowManager().getDefaultDisplay();
		int cameraWidth = display.getWidth();
		int cameraHeight = display.getHeight();
		mCamera = new Camera(0, 0, cameraWidth, cameraHeight);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
			    new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera)
			    .setNeedsMusic(true).setNeedsSound(true));
	}
	
	
	

	@Override
	public void onLoadResources() {


		mBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "Hero.png",
						0, 0);
		
		
		

		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
    
		  
    	   mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
			    .createFromAsset(this.mBitmapTextureAtlas, this, "target2.png",
			    128, 0);
		
       
       
      
       
       
		//for projectitle
		mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
			    .createFromAsset(this.mBitmapTextureAtlas, this,
			    "Projectile.png", 64, 0);
		
		//For sound
		SoundFactory.setAssetBasePath("mfx/");
		try {
		    shootingSound = SoundFactory.createSoundFromAsset(mEngine
		        .getSoundManager(), this, "pew_pew_lei.wav");
		} catch (IllegalStateException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		MusicFactory.setAssetBasePath("mfx/");
		 
		try {
		    backgroundMusic = MusicFactory.createMusicFromAsset(mEngine
		        .getMusicManager(), this, "background_music.wav");
		    backgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		//For pause screen
		mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory
			    .createFromAsset(this.mBitmapTextureAtlas, this, "paused.png",
			    0, 64);
		
		//For win or lose
		mWinTextureRegion = BitmapTextureAtlasTextureRegionFactory
			    .createFromAsset(this.mBitmapTextureAtlas, this, "win.png", 0,
			    128);
			mFailTextureRegion = BitmapTextureAtlasTextureRegionFactory
			    .createFromAsset(this.mBitmapTextureAtlas, this, "fail.png", 0,
			    256);
			 
			mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
			mFontTexture = new BitmapTextureAtlas(256, 256,
			    TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT,
			    Typeface.BOLD), 40, true, Color.BLACK);
			mEngine.getTextureManager().loadTexture(mFontTexture);
			mEngine.getFontManager().loadFont(mFont);
			
			
		
	}

	@Override
	public Scene onLoadScene() {

		mEngine.registerUpdateHandler(new FPSLogger());
		mMainScene = new Scene();
	
		mMainScene
				.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

		
		

		
		
		
		
		
		
		final int PlayerX = this.mPlayerTextureRegion.getWidth() / 2;
		final int PlayerY = (int) ((mCamera.getHeight() - mPlayerTextureRegion
				.getHeight()) / 2);

		player = new Sprite(PlayerX, PlayerY, mPlayerTextureRegion);
		player.setScale(2);
		mMainScene.attachChild(player);
		targetLL = new LinkedList();
		TargetsToBeAdded = new LinkedList();
		
		
		
		projectileLL = new LinkedList();
		projectilesToBeAdded = new LinkedList();
		
		mMainScene.setOnSceneTouchListener(this);
		
		//Call to play music
		 backgroundMusic.play();
		 //Do for pause
		 mPauseScene = new CameraScene(mCamera);
		 final int x = (int) (mCamera.getWidth() / 2 - mPausedTextureRegion
		     .getWidth() / 2);
		 final int y = (int) (mCamera.getHeight() / 2 - mPausedTextureRegion
		     .getHeight() / 2);
		 final Sprite pausedSprite = new Sprite(x, y, mPausedTextureRegion);
		 mPauseScene.attachChild(pausedSprite);
		 mPauseScene.setBackgroundEnabled(false);
		
		 //For win or lose
		 mResultScene = new CameraScene(mCamera);
		 winSprite = new Sprite(x, y, mWinTextureRegion);
		 failSprite = new Sprite(x, y, mFailTextureRegion);
		 mResultScene.attachChild(winSprite);
		 mResultScene.attachChild(failSprite);
		 mResultScene.setBackgroundEnabled(false);
		  
		 winSprite.setVisible(false);
		 failSprite.setVisible(false);
		 
		 score = new ChangeableText(0, 0, mFont, String.valueOf(maxScore));
		 score.setPosition(mCamera.getWidth() - score.getWidth() - 5, 5);
		 
		    createSpriteSpawnTimeHandler();
			mMainScene.registerUpdateHandler(detect);

		 
		 restart();
		 
		return mMainScene;

	}
	

	public void addTarget() {
	    Random rand = new Random();
	 
	    int x = (int) mCamera.getWidth() + mTargetTextureRegion.getWidth();
	    int minY = mTargetTextureRegion.getHeight();
	    int maxY = (int) (mCamera.getHeight() - mTargetTextureRegion
	        .getHeight());
	    int rangeY = maxY - minY;
	    int y = rand.nextInt(rangeY) + minY;
	 
	    Sprite target = new Sprite(x, y, mTargetTextureRegion.deepCopy());
	    mMainScene.attachChild(target);
	 
	    //change speed **********************************
	    //*************************************
	   // int minDuration = 4;
	    //int maxDuration = 6; //4
	    
	    if(maxScore==10)
	    	win=1;
	    
	    if(win==1)
	    {
	    	minDuration = 4;
	    	 maxDuration = 6;
	    }
	    
	    else if(win==2)
	    {
	    	 minDuration = 3;
	    	 maxDuration = 4;
	    }
	    
	    
	    else if(win==3)
	    {
	           minDuration = 2;
	    	   maxDuration = 6;
	    }
	    
	    else if(win>3)
	    {
	    	    minDuration = 1;
	    	    maxDuration = 7;
	    }
	    
	    int rangeDuration = maxDuration - minDuration;
	    int actualDuration = rand.nextInt(rangeDuration) + minDuration;
	 
	    MoveXModifier mod = new MoveXModifier(actualDuration, target.getX(),
	        -target.getWidth());
	    target.registerEntityModifier(mod.deepCopy());
	 
	    TargetsToBeAdded.add(target);
	 
	}
	
	//This method for peridically call for target.
	private void createSpriteSpawnTimeHandler() {
	    TimerHandler spriteTimerHandler;
	    float mEffectSpawnDelay = 1f;
	 
	    spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
	    new ITimerCallback() {
	 
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
	            addTarget();
	        }
	    });
	 
	    getEngine().registerUpdateHandler(spriteTimerHandler);
	}
	
	//Method for delete or remove sprite
	public void removeSprite(final Sprite _sprite, Iterator it) {
	    runOnUpdateThread(new Runnable() {
	 
	        @Override
	        public void run() {
	            mMainScene.detachChild(_sprite);
	        }
	    });
	    it.remove();
	}
	
	//For detecting sprite
	// TimerHandler for collision detection and cleaning up
		IUpdateHandler detect = new IUpdateHandler() {
			@Override
			public void reset() {
				
				
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {

				Iterator<Sprite> targets = targetLL.iterator();
				Sprite _target;
				boolean hit = false;

				// iterating over the targets
				while (targets.hasNext()) {
					_target = targets.next();

					// if target passed the left edge of the screen, then remove it
					// and call a fail
					if (_target.getX() <= -_target.getWidth()) {
						removeSprite(_target, targets);
						
						fail();
					
						maxScore=10;
						win =1;
						
						break;
					}
					Iterator<Sprite> projectiles = projectileLL.iterator();
					Sprite _projectile;
					// iterating over all the projectiles (bullets)
					while (projectiles.hasNext()) {
						_projectile = projectiles.next();

						// in case the projectile left the screen
						if (_projectile.getX() >= mCamera.getWidth()
								|| _projectile.getY() >= mCamera.getHeight()
										+ _projectile.getHeight()
								|| _projectile.getY() <= -_projectile.getHeight()) {
							removeSprite(_projectile, projectiles);
							continue;
						}

						// if the targets collides with a projectile, remove the
						// projectile and set the hit flag to true
						if (_target.collidesWith(_projectile)) {
							removeSprite(_projectile, projectiles);
							hit = true;
							break;
						}
					}

					// if a projectile hit the target, remove the target, increment
					// the hit count, and update the score
					if (hit) {
						removeSprite(_target, targets);
						hit = false;
						hitCount++;
						score.setText(String.valueOf(hitCount));
						
						
						
					}
				}

				// if max score , then we are done
				
				if (hitCount >= maxScore) {
					win();
					win++;
					//onLoadResources();
					maxScore=maxScore+10;
					
					
					
					
					
//					
				}

				// a work around to avoid ConcurrentAccessException
				projectileLL.addAll(projectilesToBeAdded);
				projectilesToBeAdded.clear();

				targetLL.addAll(TargetsToBeAdded);
				TargetsToBeAdded.clear();
			}
		};

	//This method for shot
	private void shootProjectile(final float pX, final float pY) {
		 
	    int offX = (int) (pX - player.getX());
	    int offY = (int) (pY - player.getY());
	    if (offX <= 0)
	        return;
	 
	    final Sprite projectile;
	    projectile = new Sprite(player.getX(), player.getY(),
	    mProjectileTextureRegion.deepCopy());
	    mMainScene.attachChild(projectile, 1);
	 
	    int realX = (int) (mCamera.getWidth() + projectile.getWidth() / 2.0f);
	    float ratio = (float) offY / (float) offX;
	    int realY = (int) ((realX * ratio) + projectile.getY());
	 
	    int offRealX = (int) (realX - projectile.getX());
	    int offRealY = (int) (realY - projectile.getY());
	    float length = (float) Math.sqrt((offRealX * offRealX)
	        + (offRealY * offRealY));
	    float velocity = 480.0f / 1.0f; // 480 pixels / 1 sec
	    float realMoveDuration = length / velocity;
	 
	    MoveModifier mod = new MoveModifier(realMoveDuration,
	    projectile.getX(), realX, projectile.getY(), realY);
	    projectile.registerEntityModifier(mod.deepCopy());
	 
	    projectilesToBeAdded.add(projectile);
	    shootingSound.play();
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		 
	    if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
	        final float touchX = pSceneTouchEvent.getX();
	        final float touchY = pSceneTouchEvent.getY();
	        shootProjectile(touchX, touchY);
	        return true;
	    }
	    return false;
	}
	
	//For pause
	public void pauseGame() {
	    mMainScene.setChildScene(mPauseScene, false, true, true);
	    mEngine.stop();
	}
	 
	public void unPauseGame() {
	    mMainScene.clearChildScene();
	    mEngine.start();
	}
	 
	public void pauseMusic() {
	    if (runningFlag)
	        if (backgroundMusic.isPlaying())
	            backgroundMusic.pause();
	}
	 
	public void resumeMusic() {
	    if (runningFlag)
	        if (!backgroundMusic.isPlaying())
	            backgroundMusic.resume();
	}
	
	//Touch by hand & pause screen
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
	    if (pKeyCode == KeyEvent.KEYCODE_MENU
	        && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	        if (mEngine.isRunning() && backgroundMusic.isPlaying()) {
	            pauseMusic();
	            pauseFlag = true;
	            pauseGame();
	            Toast.makeText(this, "Menu button to resume",
	                Toast.LENGTH_SHORT).show();
	        } else {
	            if (!backgroundMusic.isPlaying()) {
	                unPauseGame();
	                pauseFlag = false;
	                resumeMusic();
	                mEngine.start();
	            }
	            return true;
	        }
	    } else if (pKeyCode == KeyEvent.KEYCODE_BACK
	            && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
	 
	        if (!mEngine.isRunning() && backgroundMusic.isPlaying()) {
	            mMainScene.clearChildScene();
	            mEngine.start();
	            restart();
	            return true;
	        }
	        return super.onKeyDown(pKeyCode, pEvent);
	    }
	    return super.onKeyDown(pKeyCode, pEvent);
	}
	
	//Method for restart and every thing clear
	public void restart() {
		 
	    runOnUpdateThread(new Runnable() {
	 
	        @Override
	        public void run() {
	            mMainScene.detachChildren();
	            mMainScene.attachChild(player, 0);
	            mMainScene.attachChild(score);
	        }
	    });
	 
	    hitCount = 0;
	    score.setText(String.valueOf(hitCount));
	    projectileLL.clear();
	    projectilesToBeAdded.clear();
	    TargetsToBeAdded.clear();
	    targetLL.clear();
	}
	
	public void fail() {
		   if (mEngine.isRunning()) {
			   
			   win=1;
			   maxScore=10;
			   
			   mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(this.mBitmapTextureAtlas, this, "target2.png",
					    128, 0);
			   
		        winSprite.setVisible(false);
		        failSprite.setVisible(true);
		        mMainScene.setChildScene(mResultScene, false, true, true);
		        mEngine.stop();
		        
		    }
		}
		 
		public void win() {
		    if (mEngine.isRunning()) {
		        failSprite.setVisible(false);
		        winSprite.setVisible(true);
		        mMainScene.setChildScene(mResultScene, false, true, true);
		        mEngine.stop();
		     
		       
		        if(win==2)
		        {
		        mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(this.mBitmapTextureAtlas, this, "target3.png",
					    128, 0);
		        }
		       
		        
		        else if(win==3)
		        {
		        mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(this.mBitmapTextureAtlas, this, "Target.png",
					    128, 0);
		        }
		        
		        
		        else if (win==4)
		        {
		        mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(this.mBitmapTextureAtlas, this, "mytarget.png",
					    128, 0);
		        } 
		        
		        else if(win>=5)
		        {
		        mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
					    .createFromAsset(this.mBitmapTextureAtlas, this, "mysnake3.png",
					    128, 0);
		        } 
	
		        
		    }
		    
		    
		}
		
		
		//On resume
		@Override
		public void onResumeGame() {
		    super.onResumeGame();
		    if (runningFlag) {
		        if (pauseFlag) {
		            pauseFlag = false;
		            Toast.makeText(this, "Menu button to resume",
		            Toast.LENGTH_SHORT).show();
		        } else {
		            resumeMusic();
		            mEngine.stop();
		        }
		    } else {
		        runningFlag = true;
		    }
		}
	
		
		@Override
		protected void onPause() {
		    if (runningFlag) {
		        pauseMusic();
		        if (mEngine.isRunning()) {
		            pauseGame();
		            pauseFlag = true;
		        }
		    }
		    super.onPause();
		}
	
		
		
}
