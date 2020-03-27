package com.example.display;

import java.io.File;
import java.util.List;

import com.example.SimpleMusicPlayer.R;
import com.example.SimpleMusicPlayer.R.id;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<Music> implements SectionIndexer
{
	private Context mContext;
	private List<Music> list = null;
    private int resourceId;
	public MusicAdapter(Context context, int textViewResourceId,List<Music> objects) 
	{
		super(context, textViewResourceId, objects);
		this.mContext=context;
		resourceId=textViewResourceId;
		this.list = objects;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Music> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}
	
	public int getCount() 
	{
		return this.list.size();
	}
	
	public Music getItem(int position) 
	{
		return list.get(position);
	}
	
	public long getItemId(int position) 
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder = null;
		final Music music = list.get(position);
		if (convertView == null) 
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.song_list, null);
			viewHolder.music_name = (TextView) convertView.findViewById(R.id.song_list_name);
			viewHolder.music_artist = (TextView) convertView.findViewById(R.id.song_list_artist);
			viewHolder.music_image=(ImageView) convertView.findViewById(R.id.song_list_picture);
			viewHolder.music_sort_letter=(TextView) convertView.findViewById(R.id.catalog);
			convertView.setTag(viewHolder);
		} 
		else 
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
				
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section))
		{
			viewHolder.music_sort_letter.setVisibility(View.VISIBLE);
			viewHolder.music_sort_letter.setText(music.getSortLetters());
		}else
		{
			viewHolder.music_sort_letter.setVisibility(View.GONE);
		}
			
		viewHolder.music_name.setText(this.list.get(position).getName());
		viewHolder.music_artist.setText(this.list.get(position).getArtist());
		viewHolder.music_image.setImageBitmap(this.list.get(position).getImage());
		
		return convertView;
	}

	final static class ViewHolder 
	{
		ImageView music_image;
		TextView music_name;
		TextView music_artist;
		TextView music_sort_letter;
	}
	
	@Override
	public int getPositionForSection(int section)
	{
		for (int i = 0; i < getCount(); i++) 
		{
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) 
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) 
	{
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	 private String getAlpha(String str) 
	 {
		 String  sortStr = str.trim().substring(0, 1).toUpperCase();
		 // 正则表达式，判断首字母是否是英文字母
		 if (sortStr.matches("[A-Z]")) 
		 {
			 return sortStr;
		 } 
		 else 
		 {
			 return "#";
		 }
	 }
	
	@Override
	public Object[] getSections() 
	{
		return null;
	}

}
