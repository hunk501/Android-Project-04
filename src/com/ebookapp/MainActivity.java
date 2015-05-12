package com.ebookapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ebookapp.Database.DBHelper;
import com.ebookapp.assetManager.AssetFolder;
	
public class MainActivity extends Activity {
	
	private DBHelper helper;
	private CustomDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize Database
		helper = new DBHelper(this);
		// Check Chapters List
		helper.query("Select * from tbl_chapter");
		
	}
	
	/**
	 * Goto next page
	 */
	private void gotoNextPage(){
		Intent i = new Intent(MainActivity.this, NextPage.class);
		// Put Extra String
		i.putExtra("_chapter_id", "introduction");
		i.putExtra("_chapter_description", "Introduction");
		i.putExtra("_chapter_number", "0");
		
		finish();
		startActivity(i);
	}
	
	/**
	 * Goto Chapter Home
	 */
	private void gotoChapterHome(){
		Intent i = new Intent(MainActivity.this, ChaptersHome.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
			
			case R.id.menu_bookmark:
				gotoBookmark();
				break;
				
			case R.id.menu_chapter:
				gotoChapterHome();
				break;
			
			case R.id.action_settings:
				finish();
				break;
				
			case R.id.menu_next:
				gotoNextPage();
				break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * goto Bookmark list
	 */
	private void gotoBookmark(){
		Intent i = new Intent(this, BookmarkList.class);
		startActivity(i);
	}
}
