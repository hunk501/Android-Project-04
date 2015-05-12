package com.ebookapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.ebookapp.Database.DBHelper;
import com.ebookapp.Webview.CallBack;
import com.ebookapp.Webview.Progress;
import com.ebookapp.assetManager.AssetFolder;

public class NextPage extends Activity implements TextToSpeech.OnInitListener {

	private WebView webview;
	private SimpleDateFormat sdf = new SimpleDateFormat("M/d/y hh:mm:ss a");
	private DBHelper helper;
	private Handler handler;
	private Calendar calendar = Calendar.getInstance();
	
	private boolean hasBookmarkPassed = false;
	private String chapter_number;
	private String chapter_id;
	private int scrollx;
	private int scrolly;
	
	private Clipboard clipboard;
	private ArrayList<String> markedList = null;
	private TextToSpeech tts;
	private boolean speaking = false;
	private final String current_page = "introduction.html"; // filename of the current page
	private Progress progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_page);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		// Initialize Database
		helper = new DBHelper(this);
		
		// Get Extra String
		Bundle bundle = getIntent().getExtras();
		//String chapter_decription = bundle.getString("_chapter_description");
		setTitle(R.string.lbl_introduction);

		// Check if has bookmark extra
		String load_bookmark = bundle.getString("_load_bookmark");

		// Set Chapters Info
		chapter_number = bundle.getString("_chapter_number");
		chapter_id = bundle.getString("_chapter_id");

		// Get Bookmark
		ArrayList<BeanBookmark> bookmarkList = helper.getBookmarkList();
		for (BeanBookmark bb : bookmarkList) {
			if (bb.getChapterId().equalsIgnoreCase(chapter_id)) {
				Log.e("[Bookmark ID]", "MATCHED: " + bb.getChapterId()
						+ " | X:" + bb.getScrollX() + " | Y:" + bb.getScrollY());
				scrollx = (bb.getScrollX() != null) ? Integer.parseInt(bb
						.getScrollX()) : 0;
				scrolly = (bb.getScrollY() != null) ? Integer.parseInt(bb
						.getScrollY()) : 0;
			} else {
				Log.e("[Bookmark ID]", "NO: " + bb.getChapterId() + " | X:"
						+ bb.getScrollX() + " | Y:" + bb.getScrollY());
			}
		}
		
		// initialize progress Dialog
		progress = new Progress(this, "Loading", "Loading Page please wait....");
		
		webview = (WebView) findViewById(R.id.webview1);
		webview.setWebViewClient(new CallBack(progress, webview));
		webview.setHorizontalScrollBarEnabled(false);
		// Check if this page has an marked list
		markedList = HighlightText.getMarkText(chapter_id, helper);
		if(markedList != null){
			String str_files = AssetFolder.openFile(this, current_page, markedList);
			webview.loadDataWithBaseURL("file:///android_asset/", str_files, "text/html", "UTF-8", null);
		} else {
			webview.loadUrl("file:///android_asset/"+ current_page);
		}
		
		// Check if there is an existing book marked
		hasBookmarkPassed = (scrollx > 0 || scrolly > 0 && load_bookmark != null) ? true : false;

		handler = new Handler();
		
		// Initialize Text to Speech Object
		tts = new TextToSpeech(this, this);
		
		// Initialize ClipboardManager
		clipboard = new Clipboard(this, null);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		/**
		 * Scroll to bookmark page
		 */
		if(hasBookmarkPassed){
			Thread t = new Thread(new MyThread());
			t.start();
			
		} else {
			Log.e("[onStart]", "No bookmark passed.!");
		}
	}

	/**
	 * Goto Chapter Home
	 */
	private void gotoChapterHome() {
		Intent i = new Intent(NextPage.this, ChaptersHome.class);
		finish();
		startActivity(i);
	}

	/**
	 * goto Bookmark list
	 */
	private void gotoBookmark() {
		Intent i = new Intent(this, BookmarkList.class);
		finish();
		startActivity(i);
	}

	/**
	 * Goto Chapter 1
	 */
	private void gotoChapter1() {
		Intent i = new Intent(this, ChaptersPage.class);
		// Put Extra String
		i.putExtra("_chapter_id", "chap1");
		i.putExtra("_chapter_description", "Chapter 1");
		i.putExtra("_chapter_number", "1");
		// start
		finish();
		startActivity(i);
	}
	
	/**
	 * Goto Home
	 */
	private void gotoHome(){
		Intent i = new Intent(this, MainActivity.class);
		finish();
		startActivity(i);
	}

	/**
	 * Book mark, for this Introduction Only
	 */
	private void insertBookmark() {
		// Get ScrollBar Position
		int x = webview.getScrollX();
		int y = webview.getScrollY();

		String _date = sdf.format(calendar.getTimeInMillis());

		// Set Columns
		String[] column = { "_chapter_id", "_scrollPosX", "_scrollPosY",
				"_chapter_title", "_date", "_chapter_number" };
		String[] value = { "introduction", String.valueOf(x),
				String.valueOf(y), "Introduction", _date, "0" };

		helper.insertData("tbl_bookmark", column, value);

		displayToast("Bookmark has been Saved.!");
	}

	/**
	 * Display Message
	 * 
	 * @param msg
	 */
	private void displayToast(String msg) {
		handler.post(new MyLoader(msg));
	}
	
	/**
	 * Speak will be fired when the Speech Menu was selected
	 */
	private void speak(String chapter){
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
	 * This will create a Handler Thread to Display a Toast.
	 * 
	 * @author Dennis
	 *
	 */
	private class MyLoader implements Runnable {

		private String msg;

		public MyLoader(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}

	}
	
	/**
	 * Thread for Handling Bookmark Scrolling
	 * @author Dennis
	 *
	 */
	private class MyThread implements Runnable{
		
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				
				webview.setScrollX(scrollx);
				webview.setScrollY(scrolly);
				
				displayToast("Bookmark loaded complete.!");
			} catch (Exception e) {
				Log.e("[MyThread]", e.getMessage());
			}
		}		
	}
	
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			
			case android.R.id.home:
				gotoHome();
				break;
				
			case R.id.action_settings:
				gotoHome();
				break;
	
			case R.id.menu_bookmark:
				gotoBookmark();
				break;
			
			case R.id.menu_speak:
				speak(chapter_id);
				break;
	
			case R.id.menu_chapter:
				gotoChapterHome();
				break;
	
			case R.id.menu_next:
				gotoChapter1();
				break;
	
			case R.id.menu_add_bookmark:
				insertBookmark();
				break;
				
			case R.id.menu_mark_text:
				String str = clipboard.paste();
				//str = str.replace("'", ""); // clean string
				String[] column = {"_chapter_id","_chapter_number","_clip_text"};
				String[] values = {chapter_id, chapter_number, str};
				HighlightText.saveMarkText(column, values, helper);
				displayToast("Text has been marked.!");
				// reload
				finish();
				startActivity(getIntent());
				break;
				
			case R.id.menu_clear_all:
				helper.query("Delete from tbl_marktext Where _chapter_id='"+ chapter_id +"' ");
				Log.e("[ClearAll]", "Marked Texts has been removed.!");
				displayToast("All marked text has been cleared.!");
				// reload
				finish();
				startActivity(getIntent());
				break;
		}
		return super.onOptionsItemSelected(item);
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
