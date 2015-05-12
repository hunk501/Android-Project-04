package com.ebookapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ebookapp.Database.DBHelper;

public class BookmarkList extends Activity {

	private ListView listview;
	private DBHelper helper;
	private ArrayList<BeanBookmark> alist;
	private ArrayAdapter<BeanBookmark> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark_list);
		
		// Initialize Database
		helper = new DBHelper(this);
		
		// Get Bookmark Lists
		alist = helper.getBookmarkList();
		
		listview = (ListView) findViewById(R.id.listView1);
		
		//BookmarkAdapter adapter = new BookmarkAdapter(this, alist);
		
		arrayAdapter = new ArrayAdapter<BeanBookmark>(this, android.R.layout.simple_list_item_multiple_choice, alist);
		
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setAdapter(arrayAdapter);
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				BeanBookmark bc = alist.get(position);
				
				// Check the chapter Id
				if(bc.getChapterId().equalsIgnoreCase("introduction")){
					Intent i = new Intent(BookmarkList.this, NextPage.class);
					// Put Extra String
					i.putExtra("_chapter_id", bc.getChapterId());
					i.putExtra("_chapter_description", bc.getChapterTitle());
					i.putExtra("_chapter_number", bc.getChapterNumber());
					// extra string for bookmark
					i.putExtra("_load_bookmark", bc.getChapterId());
					
					// Start Intent Activity
					finish();
					startActivity(i);
				} else {
					Intent i = new Intent(BookmarkList.this, ChaptersPage.class);
					// Put Extra String
					i.putExtra("_chapter_id", bc.getChapterId());
					i.putExtra("_chapter_description", bc.getChapterTitle());
					i.putExtra("_chapter_number", bc.getChapterNumber());
					// extra string for bookmark
					i.putExtra("_load_bookmark", bc.getChapterId());
					
					// Start Intent Activity
					finish();
					startActivity(i);
				}
								
				return false;
			}
		});
	}
	
	
	private void gotoHome(){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	/**
	 * Delete Selected Bookmark
	 */
	private void deleteBookmark(){
		boolean isOk = false;
		int count = listview.getCount();
		for(int i=0; i < count; i++){
			if(listview.isItemChecked(i)){
				BeanBookmark bb = alist.get(i);
				// Delete
				helper.deleteBookmark(new String[]{ bb.getRecordId() });
				isOk = true;
			}
		}
		
		String result = (isOk == true) ? "Bookmark has been Deleted.!" : "No Bookmark has been Deleted.!";
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		// reload
		finish();
		startActivity(getIntent());
	}
	
	/**
	 * Show a Confirm Dialog
	 */
	private void confirmDelete(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setIcon(R.drawable.warning);
		dialog.setTitle("Confirm");
		dialog.setMessage("Are you sure you want to Delete Selected Bookmark(s).?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteBookmark();
			}
		});
		dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		// Pack and show
		AlertDialog alert = dialog.create();
		alert.show();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bookmark_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
			case R.id.menu_back:
				finish();
				gotoHome();
				break;
				
			case R.id.menu_delete:
				confirmDelete();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
