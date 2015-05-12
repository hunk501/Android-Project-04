package com.ebookapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ebookapp.Database.DBHelper;
import com.ebookapp.Webview.CallBack;
import com.ebookapp.Webview.Progress;
import com.ebookapp.Xml.XmlUtils;
import com.ebookapp.assetManager.AssetFolder;

public class ChaptersPage extends Activity implements TextToSpeech.OnInitListener {

	private WebView webview;
	
	private String chapter_number;
	private String chapter_id;
	private String current_page = null;

	private int scrollx;
	private int scrolly;
	boolean hasBookmarkPassed;
	
	private DBHelper helper;
	private CustomDialog dialog;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("M/d/y hh:mm:ss a");
	private Calendar calendar = Calendar.getInstance();
	
	private Handler handler;
	private TextToSpeech tts;
	private boolean speaking = false;
	
	private Clipboard clipboard;
	ArrayList<String> markedList = null;
	
	private Progress progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chapters_page);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// Initialize Database
		helper = new DBHelper(this);
		
		// Get Extra String
		Bundle bundle = getIntent().getExtras();
		String chapter_decription = bundle.getString("_chapter_description");
		setTitle(chapter_decription + "/" + helper.getTotalChapters());
		
		// Check if has bookmark extra
		String load_bookmark = bundle.getString("_load_bookmark");
		
		// Set Chapters Info
		chapter_number = bundle.getString("_chapter_number");
		chapter_id = bundle.getString("_chapter_id");
		
		// Get Bookmark
		ArrayList<BeanBookmark> bookmarkList = helper.getBookmarkList();
		for(BeanBookmark bb : bookmarkList){
			if(bb.getChapterId().equalsIgnoreCase(chapter_id)){
				Log.e("[Bookmark ID]", "MATCHED: "+  bb.getChapterId() +" | X:"+ bb.getScrollX() +" | Y:"+ bb.getScrollY());
				scrollx = (bb.getScrollX() != null) ? Integer.parseInt(bb.getScrollX()) : 0;
				scrolly = (bb.getScrollY() != null) ? Integer.parseInt(bb.getScrollY()) : 0;
			} else {
				Log.e("[Bookmark ID]", "NO: "+ bb.getChapterId() +" | X:"+ bb.getScrollX() +" | Y:"+ bb.getScrollY());
			}
		}
		
		progress = new Progress(this, "Loading", "Loading Page please wait....");
		
		// Check if there is an existing book marked
		hasBookmarkPassed = (scrollx > 0 || scrolly > 0 && load_bookmark != null) ? true : false;
		
		webview = (WebView) findViewById(R.id.webview1);
		/**
		 * Check if there is an Bookmark, then
		 * assign a custom callback thread to handle it.
		 */
		if(hasBookmarkPassed){
			webview.setWebViewClient(new CallBack(progress, webview, scrollx, scrolly));
		} else {
			webview.setWebViewClient(new CallBack(progress, webview));
		}
		webview.setHorizontalScrollBarEnabled(false);
		
		current_page = "chap"+ chapter_number +".html";
		
		markedList = HighlightText.getMarkText(chapter_id, helper);
		if(markedList != null){
			String str_files = AssetFolder.openFile(this, current_page, markedList);
			webview.loadDataWithBaseURL("file:///android_asset/", str_files, "text/html", "UTF-8", null);
		} else {
			//webview.loadUrl("File:///android_asset/"+ current_page);
			String str_files = AssetFolder.openFile(this, current_page, markedList);
			webview.loadDataWithBaseURL("file:///android_asset/", str_files, "text/html", "UTF-8", null);
		}
		
		handler = new Handler();
		// Initialize Text to Speech Object
		tts = new TextToSpeech(this, this);
		// Initialize ClipboardManager
		clipboard = new Clipboard(this, null);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(tts != null){
			tts.stop();
			tts.shutdown();
			Log.e("[onPause]", "Text to Speech has been closed.!");
		}
		super.onPause();
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		/**
		 * Scroll to bookmark page
		 */
//		if(hasBookmarkPassed){
//			Thread t = new Thread(new MyThread());
//			t.start();
//			
//		} else {
//			Log.e("[onStart]", "No bookmark passed.!");
//		}
	}
	
	/**
	 * Previous Page
	 */
	private void prevPage(){
		int page = Integer.parseInt(chapter_number);
		page--;
		// Check the file name if Exists.?
		String filename = "chap"+ page +".html";
		boolean fileExists = AssetFolder.fileExists(ChaptersPage.this, filename);
		if(fileExists){
			//loadPage(filename, page);
			Intent i = new Intent(this, ChaptersPage.class);
			// Put Extra String
			i.putExtra("_chapter_id", "chap"+ page);
			i.putExtra("_chapter_description", "Chapter "+ page);
			i.putExtra("_chapter_number", String.valueOf(page));
			// start
			finish();
			startActivity(i);
		} else {
			ShowDialog.showError(ChaptersPage.this, "Chapter "+ page +" was not found.!");
		}
	}
	
	/**
	 * Next Page
	 */
	private void nextPage(){
		int page = Integer.parseInt(chapter_number);
		page++;
		// Check the file name if Exists.?
		String filename = "chap"+ page +".html";
		boolean fileExists = AssetFolder.fileExists(ChaptersPage.this, filename);
		if(fileExists){
			//loadPage(filename, page);
			Intent i = new Intent(this, ChaptersPage.class);
			// Put Extra String
			i.putExtra("_chapter_id", "chap"+ page);
			i.putExtra("_chapter_description", "Chapter "+ page);
			i.putExtra("_chapter_number", String.valueOf(page));
			// start
			finish();
			startActivity(i);
		} else {
			ShowDialog.showError(ChaptersPage.this, "Chapter "+ page +" was not found.!");
		}
	}
	
	/**
	 * Reload the Page
	 */
	private void reload(){
		finish();
		startActivity(getIntent());
	}
	
	/**
	 * Start a Quiz
	 */
	private void startQuiz(){
		Intent i = new Intent(this, TakeQuiz.class);
		i.putExtra("_title", "Chapter "+ chapter_number +" Quiz");
		i.putExtra("_chapter", "question_"+ chapter_number); // set the filename of Quiz
		startActivity(i);
		// close 
		finish();
	}
	
	
	/**
	 * Speak will be fired when the Speech Menu was selected
	 */
	private void speak(String chapter){
		//String output = XmlUtils.parseXml(ChaptersPage.this, "chapterLists.xml", chapter);
		String output = clipboard.paste();
		/**
		 * Check if already running
		 */
		if(speaking){ // Stop
			speaking = false;
			try {
				tts.stop();
				displayToast("Speech has been Stop.!");
			} catch (Exception e) {
				displayToast(e.getMessage());
			}
		} 
		else { // Start
			if(output != null){
				speaking = true;
				displayToast("Starting Speech.!");
				try {
					tts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
					// Run background thread
					Thread t = new Thread(new SpeechThread(true));
					t.start();
				} catch (Exception e) {
					displayToast(e.getMessage());
				}
			} else {
				Log.e("[Chapter]", "NULL");
			}
		}
	}
	
	
	/**
	 * Goto Home
	 */
	private void gotoHome(){
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	/**
	 * Goto Chapter Home
	 */
	private void gotoChapterHome(){
		Intent i = new Intent(this, ChaptersHome.class);
		startActivity(i);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chapters_page, menu);
		
		/**
		 * Check the Page Numbers
		 */
		int page = Integer.parseInt(chapter_number);
		int chap = helper.getTotalChapters();
		
		if(page == 1){
			menu.removeItem(R.id.menu_prev);
		} else {
			if(page >= chap){
				menu.removeItem(R.id.menu_next);
			}
		}
		
		Log.e("[Page]", ""+page);
		Log.e("[ChapterNumber]", ""+helper.getTotalChapters());
		
		return true;
	}
	
	
	/**
	 * Book mark
	 */
	private void insertBookmark(){
		// Get ScrollBar Position
		int x = webview.getScrollX();
		int y = webview.getScrollY();
		
		String _date = sdf.format(calendar.getTimeInMillis());
		
		// Set Columns
		String[] column = {"_chapter_id","_scrollPosX","_scrollPosY","_chapter_title","_date","_chapter_number"};
		String[] value = {chapter_id, String.valueOf(x), String.valueOf(y), "Chapter "+ chapter_number, _date, chapter_number};
		
		helper.insertData("tbl_bookmark", column, value);
		
		displayToast("Bookmark has been Saved.!");
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id){
			case R.id.menu_prev:
				prevPage();
				break;
			
			case R.id.menu_next:				
				nextPage();
				break;
				
			case R.id.menu_bookmark:
				insertBookmark();
				break;
			
			case R.id.menu_speak:
				speak(chapter_id);
				break;
				
			case R.id.menu_home:
				finish();
				gotoHome();
				break;
			
			case android.R.id.home:
				finish();
				gotoHome();
				break;
				
			case R.id.menu_chapter:
				finish();
				gotoChapterHome();
				break;
			
			case R.id.menu_mark_text:
				String str = clipboard.paste();
				//str = str.replace("'", ""); // clean string
				String[] column = {"_chapter_id","_chapter_number","_clip_text"};
				String[] values = {chapter_id, chapter_number, str};
				HighlightText.saveMarkText(column, values, helper);
				displayToast("Text has been marked.!");
				// reload
				reload();
				break;
			
			case R.id.menu_clear_all:
				helper.query("Delete from tbl_marktext Where _chapter_id='"+ chapter_id +"' ");
				Log.e("[ClearAll]", "Marked Texts has been removed.!");
				displayToast("All marked text has been cleared.!");
				// reload
				reload();
				break;
				
			case R.id.menu_take_quiz:
				startQuiz();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Display Message
	 * @param msg
	 */
	private void displayToast(String msg){
		handler.post(new MyLoader(msg));
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(tts != null){
			tts.shutdown();
		}
		Log.e("[onDestory]", "Page "+ chapter_number +" was closed.!");
		super.onDestroy();
	}
	
	/**
	 * This will create a Handler Thread to Display a Toast.
	 * @author Dennis
	 *
	 */
	private class MyLoader implements Runnable{
		
		private String msg;
		public MyLoader(String msg){
			this.msg = msg;
		}
		
		@Override
		public void run() {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Thread for Handling Bookmark Scrolling
	 * @author Dennis
	 *
	 */
//	private class MyThread implements Runnable{
//		
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(1000);
//				
//				webview.setScrollX(scrollx);
//				webview.setScrollY(scrolly);
//				
//				displayToast("Bookmark loaded complete.!");
//			} catch (Exception e) {
//				Log.e("[MyThread]", e.getMessage());
//			}
//		}		
//	}
	
	
	/**
	 * Thread for Speech Object
	 * @author Dennis
	 *
	 */
	private class SpeechThread implements Runnable{
		private boolean keepGoing = false;
		public SpeechThread(boolean b){
			this.keepGoing = b;
		}
		
		@Override
		public void run() {
			while(keepGoing){
				try {
					if(!tts.isSpeaking()){
						this.keepGoing = false;
						speaking = false;
						Log.e("[SpeechThread]", "Done");
						//displayToast("Speech Done.!");
					}
					// Sleep
					Thread.sleep(1500);
				} catch (Exception e) {
					Log.e("[SpeechThread]", "Error: "+ e.getMessage());
				}
			}
		}
		
	}

	
	@Override
	public void onInit(int status) {
		if(status == TextToSpeech.SUCCESS){
			int result = tts.setLanguage(Locale.US);
			if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
				Log.e("[onCreate]", "Text to Speech is not Supported.!");
			} else {
				Log.e("[onCreate]", "Text to Speech has been initialized.!");
				Log.e("[onCreate]", "Text to Speech is now ready to use.");
			}
		} 
		else {
			displayToast("Unable to Start Text to Speech.!");
		}
	}
	
}
