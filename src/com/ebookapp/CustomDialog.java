package com.ebookapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ebookapp.Database.DBHelper;

public class CustomDialog {

	private String title;
	private Activity activity;
	private final String selectedChapter;
	private final DBHelper db;
	
	public CustomDialog(Activity context, String title, DBHelper helper, String selected_chapter){
		this.activity = context;
		this.title = title;
		this.db = helper;
		this.selectedChapter = selected_chapter;
	}
	
	/**
	 * This will create a Chapter Dialog
	 */
	public void createChapterDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		dialog.setIcon(R.drawable.info);
		dialog.setTitle(title);
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.custom_dialog, null);
		
		// Get Chapter Lists
		final ArrayList<BeanChapter> alist = db.getChapterList();
		// Setup Listview Widget
		ListView listview = (ListView) view.findViewById(R.id.listView1);
		TableContentAdapter adapter = new TableContentAdapter(activity, alist, selectedChapter);
		// add Adapter
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				BeanChapter bc = alist.get(position);
				Log.e("[CustomeDialog]", ""+ bc.getTitle());
				Log.e("[CustomeDialog]", ""+ bc.getId());
				Log.e("[CustomeDialog]", ""+ bc.getChapterNumber());
				Log.e("[CustomeDialog]", ""+ bc.getHeader());
			}
		});
		
		
		// Create the Custom Dialog
		dialog.setView(view);
		dialog.setPositiveButton("Close", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	
	
	/**
	 * This will Create a Book mark Dialog
	 */
	public void createBookmarkDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		dialog.setIcon(R.drawable.info);
		dialog.setTitle(title);
		
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.custom_dialog, null);
		
		// Get Chapter Lists
		final ArrayList<BeanBookmark> alist = db.getBookmarkList();
		// Setup Listview Widget
		ListView listview = (ListView) view.findViewById(R.id.listView1);
		BookmarkAdapter adapter = new BookmarkAdapter(activity, alist);
		// add Adapter
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				BeanBookmark bc = alist.get(position);
				
				Intent i = new Intent(activity, ChaptersPage.class);
				i.putExtra("_chapter_id", bc.getChapterId());
				i.putExtra("_chapter_description", bc.getChapterTitle());
				i.putExtra("_chapter_number", bc.getChapterNumber());
				// Book mark details
				//i.putExtra("_bookmark_scrollx", bc.getScrollX());
				//i.putExtra("_bookmark_scrolly", bc.getScrollY());
				
				Log.e("[CustomeDialog]", ""+ bc.getDate());
				Log.e("[CustomeDialog]", ""+ bc.getChapterId());
				Log.e("[CustomeDialog]", ""+ bc.getChapterTitle());
				Log.e("[CustomeDialog]", ""+ bc.getScrollX());
				Log.e("[CustomeDialog]", ""+ bc.getScrollY());
				Log.e("[CustomeDialog]", ""+ bc.getChapterNumber());
				// Start Intent
				activity.finish();
				activity.startActivity(i);
			}
		});
		
		
		// Create the Custom Dialog
		dialog.setView(view);
		dialog.setPositiveButton("Clear All", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		dialog.setNegativeButton("Close", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		AlertDialog alert = dialog.create();
		alert.show();
	}
}




