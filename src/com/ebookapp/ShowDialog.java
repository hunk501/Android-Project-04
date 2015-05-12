package com.ebookapp;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * This will Show a Message Dialog
 * @author Dennis
 *
 */
public class ShowDialog {
	
	/**
	 * Show Error Message Dialog
	 * @param context
	 * @param msg
	 */
	public static void showError(Context context, String msg){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setIcon(R.drawable.ic_delete);
		dialog.setTitle("Error");
		dialog.setMessage(msg);
		
		dialog.setPositiveButton("Okay", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	
	/**
	 * Show Information Message Dialog
	 * @param context
	 * @param msg
	 */
	public static void showInfo(Context context, String msg){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setIcon(R.drawable.ic_dialog_info);
		dialog.setTitle("Message");
		dialog.setMessage(msg);
		
		dialog.setPositiveButton("Okay", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		AlertDialog alert = dialog.create();
		alert.show();
	}
}









