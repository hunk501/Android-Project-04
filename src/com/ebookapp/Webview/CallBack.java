package com.ebookapp.Webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * This will be Fired when the Page was Loaded
 * It's handle the Start and End Method
 * @author Dennis
 *
 */
public class CallBack extends WebViewClient {
	
	private Progress dialog;
	private WebView webview;
	private int X;
	private int Y;
	private boolean hasBookmark = false;
	
	public CallBack(Progress d, WebView webview){
		this.dialog = d;
		this.webview = webview;
		this.hasBookmark = false;
	}
	
	public CallBack(Progress d, WebView web, int velX, int velY){
		this.dialog = d;
		this.webview = web;
		this.X = velX;
		this.Y = velY;
		this.hasBookmark = true;
	}
	
	/**
	 * Scroll Bookmark
	 */
	private void scrollBookmark(){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					webview.scrollTo(X, Y);
					Log.e("[BookmarkThread]", "Done scrolled bookmark: "+ X +", "+ Y);
				} catch (Exception e) {
					Log.e("[BookmarkThread]", "Error: "+ e.getMessage());
				}
			}			
		});
		
		t.start();
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		Log.e("[Callback]", "onPageStarted()");
		dialog.show();
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		Log.e("[Callback]", "onPageFinish()");
		dialog.close();
		/**
		 * Check if there is an bookmark, then start a
		 * thread to scroll it
		 */
		if(hasBookmark){
			scrollBookmark();
		}
	}
	
}
