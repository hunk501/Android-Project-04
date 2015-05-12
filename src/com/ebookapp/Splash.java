package com.ebookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class Splash extends Activity {
	
	private TextView txt;
	private boolean keepGoing = true;
	private Handler handler;
	private int lop = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		handler = new Handler();
		
		txt = (TextView) findViewById(R.id.textview1);
		
		// Start Thread
		Thread t = new Thread(new Mythread());
		t.start();
	}
	
	private void gotoMain(){
		Intent i = new Intent(this, MainActivity.class);
		finish();
		startActivity(i);
	}
	
	private class UpdateGUI implements Runnable{

		@Override
		public void run() {
			txt.setText(lop+"% Complete....");
			if(lop >= 100){
				keepGoing = false;
				lop = 0;
				// Goto Main
				gotoMain();
			}
			lop+= 2;
		}
		
	}
	
	private class Mythread implements Runnable{
		
		public Mythread(){
			Log.e("[MyThread]", "Started.");
		}
		
		@Override
		public void run() {
			while(keepGoing){
				try {
					
					// Update GUI
					handler.post(new UpdateGUI());
					
					// Sleep
					Thread.sleep(100);
					
				} catch (Exception e) {
					Log.e("[MyThread]", e.getMessage());
				}
			}
			Log.e("[MyThread]", "Done.");
		}
		
	}
}
