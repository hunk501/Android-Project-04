package com.ebookapp;

/**
 * 
 * @author Dennis
 *
 */
public class BeanBookmark {

	private String chapter_id;
	private String chapter_title;
	private String scrollX;
	private String scrollY;
	private String date;
	private String chapter_number;
	private String record_id;
	
	public void setChapterId(String id){
		this.chapter_id = id;
	}
	
	public void setScrollX(String x){
		this.scrollX = x;
	}
	
	public void setScrollY(String y){
		this.scrollY = y;
	}
	
	public void setDate(String d){
		this.date = d;
	}
	
	public void setChapterTitle(String title){
		this.chapter_title = title;
	}
	
	public void setChapterNumber(String num){
		this.chapter_number = num;
	}
	
	public void setRecordId(String id){
		this.record_id = id;
	}
	
	public String getChapterId(){ return chapter_id; }
	public String getScrollX(){ return scrollX; }
	public String getScrollY(){ return scrollY; }
	public String getDate(){ return date; }
	public String getChapterTitle(){ return chapter_title; }
	public String getChapterNumber(){ return chapter_number; }
	public String getRecordId(){ return record_id; }
	public String toString(){ return date; }
}
