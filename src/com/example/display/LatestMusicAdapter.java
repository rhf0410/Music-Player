package com.example.display;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.SimpleMusicPlayer.R;

public class LatestMusicAdapter extends ArrayAdapter<Music> 
{

	private int resourceId;
	public LatestMusicAdapter(Context context, int textViewResourceId,LinkedList<Music> objects) 
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Music music=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView musicpicture=(ImageView) view.findViewById(R.id.latest_song_list_picture);
		TextView musicname=(TextView) view.findViewById(R.id.latest_song_list_name);
		TextView musicartist=(TextView) view.findViewById(R.id.latest_song_list_artist);
		
		musicpicture.setImageBitmap(music.getImage());
		musicname.setText(music.getName());
		musicartist.setText(music.getArtist());
		return view;
	}
}
