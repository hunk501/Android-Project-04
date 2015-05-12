package com.ebookapp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ebookapp.R;
import com.ebookapp.Database.DBHelper;
import com.ebookapp.assetManager.AssetFolder;

public class ChaptersHome extends Activity {
	
	private ListView listview;
	private ArrayList<BeanChapter> alist = new ArrayList<BeanChapter>();
	private ArrayAdapter<BeanChapter> adapter;
	private TableContentAdapter table_contents;
	private Menu m;
	
	private DBHelper helper;
	private CustomDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapter_home);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// Initialize Database
		helper = new DBHelper(this);
		
		listview = (ListView) findViewById(R.id.listview1);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> viewadapter, View view, int pos, long arg3) {
				BeanChapter chap = alist.get(pos);
				/**
				 * Check the file is exists on the Asset Folder, 
				 * else throw error message
				 */
				String filename = chap.getId()+".html";
				boolean isFileExists = AssetFolder.fileExists(ChaptersHome.this, filename);
				if(isFileExists){
					// Load Intent
					Intent i = new Intent(ChaptersHome.this, ChaptersPage.class);
					// Put Extra String
					i.putExtra("_chapter_id", chap.getId());
					i.putExtra("_chapter_description", chap.getTitle());
					i.putExtra("_chapter_number", chap.getChapterNumber());
					// Start Intent Activity
					finish();
					startActivity(i);
					
				} else {
					ShowDialog.showError(ChaptersHome.this, "Chapter was not found.!");
				}
			}
		});
		
		alist = helper.getChapterList();
		Log.e("[Count]", ""+alist.size());
		table_contents = new TableContentAdapter(this, alist, null);
		
		//adapter = new ArrayAdapter<BeanChapter>(this, android.R.layout.simple_list_item_1, alist);
		
		listview.setAdapter(table_contents);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cover_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_home) {
			gotoHome();
			return true;
		}
		else if(id == R.id.menu_bookmark){
			// initialize
//			dialog = new CustomDialog(this, "Bookmark Lists", helper, null);
//			dialog.createBookmarkDialog();
			gotoBookmark();
		}
		else if(id == android.R.id.home){
			gotoHome();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Goto Home page
	 */
	private void gotoHome(){
		Intent i = new Intent(this, MainActivity.class);
		finish();
		startActivity(i);
	}
	
	/**
	 * goto Bookmark list
	 */
	private void gotoBookmark(){
		Intent i = new Intent(this, BookmarkList.class);
		finish();
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
