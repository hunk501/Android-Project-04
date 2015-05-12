package com.ebookapp;

import java.util.ArrayList;

import android.util.Log;

import com.ebookapp.Database.DBHelper;

/**
 * This will save the Selected Text into Database
 * for later use And Also get the Records in the Database
 * 
 * Table Used: tbl_marktext
 * 
 * @author Dennis
 *
 */
public class HighlightText {

	/**
	 * Save Text into Database
	 * @param text
	 * @param helper
	 */
	public static void saveMarkText(String[] column, String[] values, DBHelper helper){
		try {
			helper.insertData("tbl_marktext", column, values);
		} catch (Exception e) {
			Log.e("[saveMarkText]", "Error: "+ e.getMessage());
		}
	}
	
	
	/**
	 * Get the Selected Text from Database
	 * @param chapter_id
	 * @param helper
	 * @return
	 */
	public static ArrayList<String> getMarkText(String chapter_id, DBHelper helper){
		String sql = "Select _clip_text from tbl_marktext Where _chapter_id='"+ chapter_id +"'";
		ArrayList<String> alist = null;
		try {
			alist = helper.getRecords(sql);
		} catch (Exception e) {
			Log.e("[getMarkText]", "Error: "+ e.getMessage());
		}
		return alist;
	}
}





