package com.ebookapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class Clipboard {

	private String text;
	private ClipboardManager clipManager;
	private Context context;
	
	public Clipboard(Context context, String txt){
		this.text = txt;
		this.context = context;
		// initialize ClipboardManager
		clipManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
	}
	
	
	/**
	 * Copy to Clip board
	 */
	public void copy(){
		ClipData clipData = ClipData.newPlainText("text", "Sample text here.");
		clipManager.setPrimaryClip(clipData);
		Toast.makeText(context, "Copy to Clipboard", Toast.LENGTH_LONG).show();
	}
	
	
	/**
	 * Paste the Clip board
	 * @return
	 */
	public String paste(){
		String clipText = "Please! copy a text to the clipboard!";
		// check if there is existing in the clip board
		if(clipManager.hasPrimaryClip()){
			ClipData data = clipManager.getPrimaryClip();
			ClipData.Item items = data.getItemAt(0);
			clipText = items.getText().toString();
		}
		
		return clipText;
	}
	
	
}
