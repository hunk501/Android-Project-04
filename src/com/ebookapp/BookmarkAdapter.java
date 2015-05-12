package com.ebookapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarkAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<BeanBookmark> alist = new ArrayList<BeanBookmark>();
	
	public BookmarkAdapter(Activity activity, ArrayList<BeanBookmark> al){
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.alist = al;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
			BeanBookmark bb = alist.get(position);
			// Set text
			holder.text1.setText(bb.getChapterTitle());
			holder.text2.setText("Date: "+bb.getDate());
		}
		
		return view;
	}

	
}
