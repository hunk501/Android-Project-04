package com.ebookapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TableContentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<BeanChapter> alist = new ArrayList<BeanChapter>();
	private String selected_chapter = null;
	
	public TableContentAdapter(Activity activity, ArrayList<BeanChapter> al, String selectedChapter){
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.alist = al;
		this.selected_chapter = selectedChapter;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alist.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}
	
	
	
	private class ViewHolder{
		public TextView text1;
		public TextView text2;
		public ImageView image1;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		ViewHolder holder = new ViewHolder();
		
		if(convertView == null){
			view = inflater.inflate(R.layout.chapter_list_item, null);
			
			holder.text1 = (TextView) view.findViewById(R.id.textview1);
			holder.text2 = (TextView) view.findViewById(R.id.textview2);
			holder.image1 = (ImageView) view.findViewById(R.id.imageview1);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		// Check if ArrayList is null
		if(alist != null){
			BeanChapter chapter = alist.get(position);
			// Set text
			holder.text1.setText(chapter.getTitle());
			holder.text2.setText(chapter.getHeader());
			
			
			// Check if has selected Chapter
//			if(selected_chapter != null){
//				if(chapter.getPageNumber() == 4){
//					holder.image1.setImageResource(R.drawable.check);
//				}
//			}
		}
		
		return view;
	}

	
}
