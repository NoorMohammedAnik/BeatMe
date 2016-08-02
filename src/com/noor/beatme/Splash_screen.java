package com.noor.beatme;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

//Use intent filter in android.xm file
public class Splash_screen extends Activity {
	
	private static int SplashTimeOut=6000;
	MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//java code for full screen
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_splash_screen);
		player = MediaPlayer.create(this, R.raw.music);
		
		//Post delayed for main activity
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						
						//After 3 seconds ,start this activity
					Intent intent=new Intent(Splash_screen.this,MainActivity.class);
					startActivity(intent);
					player.setLooping(true);
					player.start();
					
					//closing this activity.
					finish();
						
					}
				}, SplashTimeOut);
			}

	}




