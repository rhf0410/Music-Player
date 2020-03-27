package com.example.SimpleMusicPlayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.display.CharacterParser;
import com.example.display.Music;
import com.example.display.MusicAdapter;
import com.example.kmp.KMP;

public class SearchActivity extends Activity 
{
	private AutoCompleteTextView music_search;
	private ImageView delete;
	private Button search;
    //音乐列表
    private ListView music_list;
    private ListView search_list;
    private MusicApplication mApplication;
    private List<Music>musics;
    private List<String>search_musics;
    private List<Music>result_musics;
    private ArrayAdapter searchadapter;
    private MusicAdapter musicadapter;
    private CharacterParser parse;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_search);
    	findView();
		setListener();
    }
	
    @Override
    protected void onStart() 
    {
    	for(Music music:musics)
    	{
    		search_musics.add(music.getName()+" "+music.getArtist());
    	}
    	searchadapter.notifyDataSetChanged();
    	super.onStart();
    }
    
	private void findView() 
	{
		music_search=(AutoCompleteTextView) findViewById(R.id.search_et_input);
		delete=(ImageView) findViewById(R.id.search_iv_delete);
		search=(Button) findViewById(R.id.search_btn);
		music_list=(ListView) findViewById(R.id.search_result);
		mApplication=(MusicApplication) this.getApplication();
		musics=mApplication.getMusics();
		search_musics=new ArrayList<String>();
		result_musics=new ArrayList<Music>();
		searchadapter=new ArrayAdapter(SearchActivity.this, R.layout.item, search_musics);
		musicadapter=new MusicAdapter(SearchActivity.this, R.layout.latestmusic, result_musics);
		music_search.setAdapter(searchadapter);
		music_list.setAdapter(musicadapter);
		searchadapter.notifyDataSetChanged();
		musicadapter.notifyDataSetChanged();
		parse=CharacterParser.getInstance();
	}

	private void setListener() 
	{
		//为搜索按钮添加点击事件
		search.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				changeDisplay(music_search.getText().toString());
			}
		});
		
		//为搜索音乐列表添加点击事件
		music_list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				//跳转操作
				Intent intent=new Intent();
				intent.putExtra("name", result_musics.get(arg2).getName());
				intent.putExtra("artist", result_musics.get(arg2).getArtist());
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		//为清空按钮添加点击事件
		delete.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				music_search.setText("");
				music_list.setVisibility(View.INVISIBLE);
			}
		});
	}

	private String transfer(String name,String artist)
	{
		return parse.getSelling(name).trim()+parse.getSelling(artist).trim();
	}
	
	private void changeDisplay(String s)
	{
		result_musics.clear();
		String ref=s.replace(" ", "");
		String regex = "[\u4E00-\u9FA5]+";
		if(s.matches(regex))
		{
			for(Music music:musics)
			{
				String Ref=music.getName()+music.getArtist();
				if(Ref.contains(ref))
				{
					result_musics.add(music);
				}
			}
		}
		else
		{
			for(Music music:musics)
			{
				if(KMP.Kmp_Matching(transfer(music.getName(),music.getArtist()).toCharArray(), ref.toCharArray()))
				{
					result_musics.add(music);
				}
			}
		}
		music_list.setVisibility(View.VISIBLE);
		musicadapter.notifyDataSetChanged();
	}
}
