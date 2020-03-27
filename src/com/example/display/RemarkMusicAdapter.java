package com.example.display;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.SimpleMusicPlayer.R;
import com.example.circleimageview.CircleImageView;
import com.example.display.MusicAdapter.ViewHolder;

public class RemarkMusicAdapter extends ArrayAdapter<RemarkObject> 
{

	private Context mContext;
	private List<RemarkObject> list = null;
    private int resourceId;
	
    public RemarkMusicAdapter(Context context, int textViewResourceId,List<RemarkObject> objects)
    {
    	super(context, textViewResourceId, objects);
		this.mContext=context;
		resourceId=textViewResourceId;
		this.list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder viewHolder = null;
		if (convertView == null) 
		{
			convertView=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.remark_image=(CircleImageView) convertView.findViewById(R.id.remark_image);
			viewHolder.remark_name=(TextView) convertView.findViewById(R.id.remark_user);
			viewHolder.remark_time=(TextView) convertView.findViewById(R.id.remark_time);
			viewHolder.remark_content=(TextView) convertView.findViewById(R.id.remark_content_of_user);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.remark_image.setImageBitmap(this.list.get(position).getRemark_image());
		viewHolder.remark_name.setText(this.list.get(position).getRemark_name());
		viewHolder.remark_time.setText(this.list.get(position).getRemark_time());
		viewHolder.remark_content.setText(this.list.get(position).getRemark_content());
		
		return convertView;
	}
	
	final static class ViewHolder 
	{
		CircleImageView remark_image;
		TextView remark_name;
		TextView remark_time;
		TextView remark_content;
	}
}
