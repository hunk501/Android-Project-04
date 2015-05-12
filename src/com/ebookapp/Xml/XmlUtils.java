package com.ebookapp.Xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.ebookapp.BeanChapter;
import com.ebookapp.BeanQuestions;
import com.ebookapp.assetManager.AssetFolder;

public class XmlUtils {
	
	public static String parseXml(Context context, String filename, String chapterName){
		String output = null;
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			
			InputStream input = context.getAssets().open(filename);
			
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(input, null);
			
			output = getData(parser, chapterName);
			
		} catch (Exception e) {
			Log.e("[XmlUtils]", "Error: "+ e.getMessage());
		}
		
		return output;
	}
	
	
	private static String getData(XmlPullParser parser, String chapterName){
		String output = null;
		try {
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				
				case XmlPullParser.START_DOCUMENT:
					Log.e("[XmlUtils]", "START_DOCUMENT: "+ parser.getName());
					break;
					
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if(name.equalsIgnoreCase(chapterName)){
						output = parser.nextText();
					}
					Log.e("[XmlUtils]", "START_DOCUMENT: "+ name);
					break;
				}
				
				eventType = parser.next();
			}
		} catch (Exception e) {
			Log.e("[getData]", "Error: "+ e.getMessage());
		}
		
		return output;
	}
	
	
	
	/**
	 * Get the Chapter List from XML file
	 * @param context
	 * @param filename
	 * @return
	 */
	public static ArrayList<BeanChapter> getChapterList(Context context, String filename){
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			
			InputStream input = context.getAssets().open(filename);
			
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(input, null);
			
			return getChapter(parser);
			
		} catch (Exception e) {
			System.out.println("[getChapterList]: "+ e.getMessage());
		}
		return null;
	}
	
	
	private static ArrayList<BeanChapter> getChapter(XmlPullParser parser){
		ArrayList<BeanChapter> alist = new ArrayList<BeanChapter>();
		try {
			int eventType = parser.getEventType();
			int lop = 0;
			while(eventType != parser.END_DOCUMENT){
				switch(eventType){
					case XmlPullParser.START_DOCUMENT:
						break;
						
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if(!name.equalsIgnoreCase("chapters")){
							lop++;
							// Get Chapter Header
							String header = parser.getAttributeValue(null, "header");
							// Save to Bean Object
							BeanChapter bc = new BeanChapter();
							bc.setChapterId(name);
							bc.setChapterNumber(String.valueOf(lop));
							bc.setTitle("Chapter "+ lop);
							bc.setHeader(header);
							// Add To list
							alist.add(bc);
						}
						break;
				}
				
				eventType = parser.next();
			}
		} catch (Exception e) {
			System.out.println("[getChapter]: "+ e.getMessage());
		}
		
		return alist;
	}
	
	
	/**
	 * Get the List of Question Choices and Answer in the specified Chapters
	 * from the asset folder
	 * @param context
	 * @param chapter_filename
	 * @return
	 */
	public static ArrayList<BeanQuestions> getChapterQuestion(Context context, String chapter_filename){
		XmlPullParserFactory factory;
		ArrayList<BeanQuestions> alist = null;
		BeanQuestions question = null;
		
		String[] keys = null;
		String[] values = null;
		
		try {
			factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			// Check file if exists
			boolean isFileExits = AssetFolder.fileExists(context, chapter_filename);
			if(isFileExits){
				InputStream input = context.getAssets().open(chapter_filename);
				
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
				parser.setInput(input, null);
				
				/**
				 * Parse the data
				 */
				try {
					int eventType = parser.getEventType();
					while(eventType != XmlPullParser.END_DOCUMENT){
						String element = null; // set the Element Name
						switch(eventType){
							
							case XmlPullParser.START_DOCUMENT:
								alist = new ArrayList<BeanQuestions>();
								break;
								
							case XmlPullParser.START_TAG:
								// reference the current element
								element = parser.getName();
								
								if(element.equalsIgnoreCase("question")){
									question = new BeanQuestions();
									keys = new String[4];
									values = new String[4];
								} 
								else if(question != null){
									// Number
									if(element.equalsIgnoreCase("number")){
										question.setQuestionNumber(parser.nextText());
									}
									// Description
									else if(element.equalsIgnoreCase("description")){
										question.setQuestion(parser.nextText());
									}
									// Choices
									else if(element.equalsIgnoreCase("choices_a")){									
										keys[0] = "A";
										values[0] = parser.nextText();
									} else if(element.equalsIgnoreCase("choices_b")){
										keys[1] = "B";
										values[1] = parser.nextText();
									} else if(element.equalsIgnoreCase("choices_c")){
										keys[2] = "C";
										values[2] = parser.nextText();
									} else if(element.equalsIgnoreCase("choices_d")){
										keys[3] = "D";
										values[3] = parser.nextText();
										// add all choices
										question.setChoices(keys, values);
									}
									// Answer
									else if(element.equalsIgnoreCase("answer")){
										question.setAnswer(parser.nextText());
									}
									
								}
								break;
								
							case XmlPullParser.END_TAG:
								// Reference the current element
								element = parser.getName();
								// Check If end for the question tag and BeanQuestion is not Null
								if(element.equalsIgnoreCase("question") && question != null){
									alist.add(question);
								}
								break;
						}
						
						// Next Element
						eventType = parser.next();
					}
					
					
				} catch (Exception e) {
					System.out.println("ReadingElement: "+ e.getMessage());
				}
			} 
			else {
				System.out.println(chapter_filename +" was not found in Asset Folder.!");
			}
		} catch (Exception e) {
			System.out.println("GetChapterQuestion(): "+ e.getMessage());
		}
		
		return alist;
	}
}





