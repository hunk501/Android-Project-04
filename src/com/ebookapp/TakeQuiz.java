package com.ebookapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ebookapp.Xml.XmlUtils;
import com.ebookapp.assetManager.AssetFolder;

public class TakeQuiz extends Activity {

	private TableLayout main_table;
	private TableRow main_tr;
	private TableLayout tl;
	private int count;
	
	private int correct;
	private int ave;
	private String filename;
	private RadioGroup[] views;
	
	private ArrayList<BeanQuestions> alist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_quiz);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		// Get the quiz filename
		Bundle bundle = getIntent().getExtras();
		String chp = bundle.getString("_chapter");
		String quiz_title = bundle.getString("_title");
		setTitle(quiz_title);
		
		filename = chp+".xml";
		// check filename if exits in the asset folder
		boolean isExists = AssetFolder.fileExists(this, filename);
		if(isExists){
			Log.e("[Quiz]", "Filename is exits "+ filename);
			
			main_table = (TableLayout) findViewById(R.id.main_table);
			
			alist = XmlUtils.getChapterQuestion(this, filename);
			count = alist.size(); // get Total Questions
			views = new RadioGroup[count];
			int lop = 0;
			if(alist != null){
				// Loop Questions
				for(BeanQuestions list : alist){
					HashMap<String, String> ch = list.getChoices();
					// add To View
					add(list.getQuestionNumber(), lop, list.getQuestion(), ch.get("A"), ch.get("B"), ch.get("C"), ch.get("D"));
					lop++;
				}
			}
			
		} else {
			Log.e("[Quiz]", "Filename is Not exits "+ filename);
			Toast.makeText(getApplicationContext(), quiz_title +" was not found.!", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private void add(String number, int lop, String question, String choice_a, String choice_b, String choice_c, String choice_d){
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		main_tr = new TableRow(this);
		main_tr.setLayoutParams(lp);
		
		// Setup Question Body
		tl = new TableLayout(this);
		//tl.setLayoutParams(lp);
		
		/**
		 * Question Header
		 */
		TableRow tr_header = new TableRow(this);
		tr_header.setLayoutParams(lp);
		TextView txtview = new TextView(this);
		txtview.setText(number+". "+ question);
		txtview.setTextColor(Color.BLACK);
		txtview.setMaxWidth(450);
		txtview.setMinWidth(450);
		txtview.setBackgroundColor(Color.LTGRAY);
		tr_header.addView(txtview);
		tl.addView(tr_header);
		/**
		 * Question Choices
		 */
		TableRow tr_choices = new TableRow(this);
		LinearLayout tr_linear = new LinearLayout(this);
		
		RadioGroup choices_group = new RadioGroup(this);
		choices_group.setLayoutParams(lp);
		choices_group.setTag("question_"+number); // Set Tag ex. question_1, question_2  etc...
		choices_group.setId(Integer.parseInt(number));
		views[lop] = choices_group; // Add to views
		
		RadioButton radio_a = new RadioButton(this);
		radio_a.setMaxWidth(450);
		radio_a.setId(0);
		radio_a.setText(choice_a);
		radio_a.setTag("A");
		radio_a.setTextColor(Color.BLACK);
		radio_a.setTextSize(13f);
		
		RadioButton radio_b = new RadioButton(this);
		radio_b.setMaxWidth(450);
		radio_b.setText(choice_b);
		radio_b.setTag("B");
		radio_b.setTextColor(Color.BLACK);
		radio_b.setTextSize(13f);
		
		RadioButton radio_c = new RadioButton(this);
		radio_c.setMaxWidth(450);
		radio_c.setText(choice_c);
		radio_c.setTag("C");
		radio_c.setTextColor(Color.BLACK);
		radio_c.setTextSize(13f);
		
		RadioButton radio_d = new RadioButton(this);
		radio_d.setMaxWidth(450);
		radio_d.setText(choice_d);
		radio_d.setTag("D");
		radio_d.setTextColor(Color.BLACK);
		radio_d.setTextSize(13f);
		
		// Add choices to Radio Group
		choices_group.addView(radio_a);
		choices_group.addView(radio_b);
		choices_group.addView(radio_c);
		choices_group.addView(radio_d);
		// add radio group to Linear Layout
		tr_linear.addView(choices_group);
		// add to Table row
		tr_choices.addView(tr_linear);
		// add to table layout
		tl.addView(tr_choices);
		
		main_tr.addView(tl);
		
		main_table.addView(main_tr);
	}
	
	
	/**
	 * Submit All Answer's
	 */
	private void submitAnswer(){
		String output = "";
		int q_lop = 1;
		
		for(int i=0; i < views.length; i++){
			try {
				RadioGroup group = (RadioGroup) views[i];
				int selectedId = group.getCheckedRadioButtonId();
				//Log.e("[TAG]", "Group: "+ views[i].getId()+" | "+  views[i].getTag().toString());
				
				RadioButton radio_button = (RadioButton) findViewById(selectedId);
				String ans = radio_button.getTag().toString();
				//Log.e("[TAG]", "Question: "+ group.getTag().toString() +" | Answer: "+ ans);
				
				output += "No.: "+ q_lop +"\n";
				
				// Check if the user answer is correct
				BeanQuestions bq = alist.get(i);
				if(bq.getAnswer().equalsIgnoreCase(ans)){ // Correct answer
					output += "Your Answer: "+ ans +" tama\n";
					correct+= 1;
				} else { // wrong answer					
					output += "Your Answer: "+ ans +" mali\n";
					output += "Correct Answer: "+ bq.getAnswer() +"\n";
				}
				
				output += "\n";				
				q_lop++;
				
			} catch (Exception e) {
				Log.e("[SubmitAnswer]", "An error was encountered.!");
				BeanQuestions bq = alist.get(i);
				output += "No.: "+ q_lop +"\n";
				output += "Your Answer: (No Answer) mali\n";
				output += "Correct Answer: "+ bq.getAnswer() +"\n";
				output += "\n";
				q_lop++;
			}
		}
		
		views = null;
		
		//int total_items = alist.size();
		output += "Total Score: "+ correct +"/"+ count +"\n";
		ave = (int)(correct / count) * 100;
		
		// display
		display(output);
	}
	
	
	private void display(String msg){
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setIcon(R.drawable.info);
		d.setTitle("Result");
		d.setMessage(msg);
		d.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//finish();
				//startActivity(getIntent());
				gotoChapters();
			}
		});
		d.setCancelable(false);
		
		AlertDialog alert = d.create();
		alert.show();
	}
	
	private void gotoChapters(){
		Intent i = new Intent(this, ChaptersHome.class);
		finish();
		startActivity(i);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			views = null;
			main_table.removeAllViews();
			main_tr.removeAllViews();
			correct = 0;
			count = 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		//finish();
		Log.e("[onPause]", "onPause()");
		//gotoChapters();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//add();
			//Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
			gotoChapters();
			return true;
		} 
		else if(id == R.id.menu_submit_answer){
			submitAnswer();
			return true;
		}
		else if(id == android.R.id.home){
			gotoChapters();
		}
		return super.onOptionsItemSelected(item);
	}
}
