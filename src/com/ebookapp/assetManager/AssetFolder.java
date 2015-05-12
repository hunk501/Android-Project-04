package com.ebookapp.assetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetFolder {

	
	/**
	 * Check File from Asset Folder if Exists.?
	 * @param filename
	 * @return
	 */
	public static boolean fileExists(Context context,String filename){
		InputStream input = null;
		try {
			
			AssetManager asset = context.getAssets();
			input = asset.open(filename);
			
			return (input != null) ? true : false;
		} catch (Exception e) {
			return false;
		} 
		finally{
			try {
				input.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	
	
	public static String openFile(Context context, String filename, ArrayList<String> list){
		InputStream input = null;
		BufferedReader br = null;
		ArrayList<String> matchList = list;
		StringBuilder sb = new StringBuilder();
		try {
			AssetManager manager = context.getAssets();
			input = manager.open(filename);
			if(input != null){
				Log.e("[OpenFile]", "file was found '"+ filename +"' ");
				Log.e("[OpenFile]", "List Size: "+ list.size());
				InputStreamReader reader = new InputStreamReader(input);
				br = new BufferedReader(reader);
				
				String line;
				while((line = br.readLine()) != null){
					/**
					 * Check if there is a matched String in the current lines
					 */
					for(String str : list){
						if(line.matches("(.*)"+ str +"(.*)")){							
							line = line.replace(str, "<span style='background:yellow;margin:auto;color:#333;'>"+str+"</span>");
							Log.e("[OpenFile]", "String was Matched.");
						} else {
							//line = line.replace(str, "<span style='background:yellow;margin:auto;color:#333;'>"+str+"</span>");
						}
					}
					sb.append(line);
				}	
				
				//Log.e("[OpenFile]", sb.toString());
				
			} else {
				Log.e("[OpenFile]", "file was not found");
			}
		} catch (Exception e) {
			Log.e("[OpenFile]", "Error: "+ e.getMessage());
		} finally {
			try {
				input.close();
				br.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return sb.toString();
	}

}
