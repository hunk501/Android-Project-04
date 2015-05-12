package com.ebookapp.Database;

import java.util.ArrayList;

import com.ebookapp.BeanBookmark;
import com.ebookapp.BeanChapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DBHelper {
	
	private Context context;
	private OpenHelper helper;
	
	public DBHelper(Context context){
		this.context = context;
		// Initialize
		init();
	}
	
	/**
	 * Initialize first the Database Helper,
	 * to Load the Connection
	 */
	private void init(){
		helper = new OpenHelper(context);
	}
	
	/**
	 * Open the Database Connection
	 * @return
	 */
	private synchronized SQLiteDatabase open(){
		return helper.getWritableDatabase();
	}
	
	/**
	 *  CRUD
	 */
	
	public void insertData(String table, String[] column, String[] value){
		try {
			final SQLiteDatabase db = open();
			
			ContentValues content = new ContentValues();
			
			for(int i=0; i < column.length; i++){
				content.put(column[i], value[i]);
				Log.e("[insertData]", column[i] +" : "+ value[i]);
			}
			
			db.insert(table, null, content);
			// Close Connection
			db.close();
		} catch (Exception e) {
			displayError(e.getMessage());
		}
	}
	
	
	public ArrayList<BeanChapter> getChapterList(){
		ArrayList<BeanChapter> alist = new ArrayList<BeanChapter>();
		final SQLiteDatabase db = open();
		
		Cursor cursor = db.rawQuery("Select * from tbl_chapter order by _id ASC", null);

		try {
			if(cursor.moveToFirst()){
				int lop = 0;
				do{
					lop++;
					BeanChapter bc = new BeanChapter();
					bc.setChapterId(cursor.getString(1)); 		// Chapter Id
					bc.setTitle(cursor.getString(2));     		// Chapter Title
					bc.setChapterNumber(String.valueOf(lop));	// Chapter Number
					bc.setHeader(cursor.getString(3));			// Chapter Subtitle
					bc.setPageNumber(lop);
					
					alist.add(bc);
					//Log.e("[List]", bc.getTitle());
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			displayError(e.getMessage());
		}
		db.close();
		
		return alist;
	}
	
	
	public int getTotalChapters(){
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery("Select * from tbl_chapter", null);
		int lop = 0;
		try {
			if(cursor.moveToFirst()){
				do {
					lop++;
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			displayError(e.getMessage());
		}
		db.close();
		
		return lop;
	}
	
	
	public ArrayList<BeanBookmark> getBookmarkList(){
		ArrayList<BeanBookmark> alist = new ArrayList<BeanBookmark>();
		final SQLiteDatabase db = open();
		Cursor cursor = db.rawQuery("Select * from tbl_bookmark order by _id DESC", null);
		
		try {
			if(cursor.moveToFirst()){
				do{
					BeanBookmark bb = new BeanBookmark();
					bb.setRecordId(cursor.getString(0));     // Record Id
					bb.setDate(cursor.getString(1)); 		 // Date
					bb.setChapterId(cursor.getString(2));    // Chapter Id
					bb.setScrollX(cursor.getString(3));	     // ScrollX
					bb.setScrollY(cursor.getString(4));      // ScrollY
					bb.setChapterTitle(cursor.getString(5)); // Chapter title				
					bb.setChapterNumber(cursor.getString(6));// Chapter Number 
					
					alist.add(bb);
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			displayError(e.getMessage());
		}
		db.close();
		
		return alist;
	}
	
	public void query(String sql){
		final SQLiteDatabase db = open();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				do{
					Log.d("[Data]", cursor.getString(0) +" : "+ cursor.getString(1));
				}while(cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
			displayError(e.getMessage());
		}
	}
	
	
	public ArrayList<String> getRecords(String sql){
		final SQLiteDatabase db = open();
		ArrayList<String> alist = new ArrayList<String>();
		try {
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				do{
					for(int i=0; i < cursor.getColumnCount(); i++){
						String val = cursor.getString(i);
						alist.add(val);
						Log.e("[getRecords]", val);
					}
				} while(cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("[getRecords]", "Error: "+ e.getMessage());
		}
		
		return alist;
	}
	
	
	public void deleteBookmark(String[] values){
		final SQLiteDatabase db = open();
		try {
			db.delete("tbl_bookmark", "_id=?", values);
			db.close();
		} catch (Exception e) {
			displayError(e.getMessage());
		}
	}
	
	
	/**
	 *  End CRUD
	 */
	
	
	/**
	 * Display Error
	 * @param msg
	 */
	private void displayError(String msg){
		Log.e("[Database]", "Error: "+ msg);
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}




