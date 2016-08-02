package com.noor.beatme;




import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
    ImageView start,about,exit;
    MediaPlayer sound,music;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		start=(ImageView)findViewById(R.id.button1);
		about=(ImageView)findViewById(R.id.button2);
		exit=(ImageView)findViewById(R.id.button3);
		
		sound = MediaPlayer.create(this, R.raw.click);
		GameActivity game=new GameActivity();
		
	

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(MainActivity.this,
						GameActivity.class);
				sound.start();
				startActivity(intent);

			}
		});

		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(MainActivity.this,AboutActivity.class);
				startActivity(intent);
				sound.start();
				
			}
		});
		
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		      sound.start();
			  finish();
			 
			}
		});

	}
	
	

}