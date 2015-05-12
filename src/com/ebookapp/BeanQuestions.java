package com.ebookapp;

import java.util.HashMap;

/**
 * 
 * @author Dennis
 *
 */
public class BeanQuestions {
	
	private String question_number;
	private String question;
	private HashMap<String, String> choices = new HashMap<String, String>();
	private String answer;
	
	// Setters
	public void setQuestionNumber(String num){ this.question_number = num; };
	
	public void setQuestion(String ques){ this.question = ques; };
	
	public void setChoices(String[] keys, String[] values){ 
		for(int i=0; i < keys.length; i++){
			choices.put(keys[i], values[i]);
		}
	};
	
	public void setAnswer(String ans){ this.answer = ans; }
	
	
	// Getters
	public String getQuestionNumber(){ return this.question_number; }
	public String getQuestion(){ return this.question; }
	public HashMap<String, String> getChoices(){ return this.choices; }
	public String getAnswer(){ return this.answer; }
	
}
