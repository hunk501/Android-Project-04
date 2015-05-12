package com.ebookapp.Database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ebookapp.BeanChapter;
import com.ebookapp.Xml.XmlUtils;

public class OpenHelper extends SQLiteOpenHelper {
	
	private final String TAG = "[DatabaseHelper]";
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "mydb.db";
	private Context context;
	
	//   For Table: tbl_bookmark
	private final String query01 = "CREATE TABLE tbl_bookmark("
			+ "_id integer primary key autoincrement,"
			+ "_date text,"
			+ "_chapter_id text,"
			+ "_scrollPosX text,"
			+ "_scrollPosY text,"
			+ "_chapter_title text,"
			+ "_chapter_number text);";
	
	private final String query02 = "CREATE TABLE tbl_chapter("
			+ "_id integer primary key autoincrement,"
			+ "_chapter_id text,"
			+ "_chapter_title text,"
			+ "_chapter_header text);";
	
	private final String query03 = "CREATE TABLE tbl_marktext("
			+ "_id integer primary key autoincrement,"
			+ "_chapter_id text,"
			+ "_chapter_number text,"
			+ "_clip_text text);";
	
	public OpenHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			Log.e(TAG, "onCreate: Creating Databse "+ DB_NAME);
			db.execSQL(query01);
			db.execSQL(query02);
			db.execSQL(query03);
			Log.e(TAG, "onCreate: Done Creating Databse "+ DB_NAME);
			
			ArrayList<BeanChapter> alist = XmlUtils.getChapterList(context, "chapterLists.xml");
			
			for(BeanChapter list : alist){
				db.execSQL("INSERT INTO tbl_chapter (_chapter_id,_chapter_title,_chapter_header) "
						+ "VALUES('"+ list.getId() +"','"+ list.getTitle() +"','"+ list.getHeader() +"')");
				Log.e("_chapter_id", list.getTitle() +" : "+ list.getHeader());
			}
			
		} catch (Exception e) {
			Log.e(TAG, "onCreate: "+ e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
		try {
			Log.e(TAG, "onUpgrade: Deleting table tbl_bookmark");
			db.execSQL("DROP TABLE IF EXISTS tbl_bookmark");
			db.execSQL("DROP TABLE IF EXISTS tbl_chapter");
			db.execSQL("DROP TABLE IF EXISTS tbl_marktext");
			Log.e(TAG, "onUpgrade: Done Deleting table tbl_bookmark");
			
			onCreate(db);
			
		} catch (Exception e) {
			Log.e(TAG, "onUpgrade: "+ e.getMessage());
		}
	}

	
}
