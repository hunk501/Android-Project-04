package com.ebookapp;

/**
 * 
 * @author Dennis
 *
 */
public class BeanChapter {
	
	private String description;
	private String id;
	private String number;
	private String header;
	private int page;
	
	
	// Getters
	public String getTitle(){ return this.description; }
	public String getId(){ return this.id; }
	public String getChapterNumber(){ return this.number; }
	public String toString(){ return this.description; }
	public String getHeader(){ return this.header; }
	public int getPageNumber(){ return this.page; }
	
	// Setters
	public void setTitle(String desc){
		this.description = desc;
	}
	
	public void setChapterId(String _id){
		this.id = _id;
	}
	
	public void setChapterNumber(String num){
		this.number = num;
	}
	
	public void setHeader(String h){
		this.header = h;
	}
	
	public void setPageNumber(int p){
		this.page = p;
	}
}
