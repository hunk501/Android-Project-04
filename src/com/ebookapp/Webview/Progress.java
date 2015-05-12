package com.ebookapp.Webview;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress {
	
	private Context context;
	private ProgressDialog dialog;
	
	public Progress(Context con, String title, String message){
		this.context = con;
		// initialize Dialog
		dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
	}
	
	/**
	 * Show Dialog
	 */
	public void show(){
		dialog.show();
	}
	
	/**
	 * Close Dialog
	 */
	public void close(){
		dialog.dismiss();
	}
	
}
