package com.example.SimpleMusicPlayer;

import java.util.ArrayList;
import java.util.List;

import com.example.display.Music;
import com.example.file.Mp3Info;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;

public class MusicApplication extends Application 
{
	//音乐实体类
	private List<Music> musics;
	//音乐信息类
	private List<Mp3Info>mp3s;
	//当前播放位置
	private int current_location;
	//音乐播放模式
	private int status;
	//音乐播放接口
	public IMusic music;
	//播放进度
	public int play_proceed;
	//是否登录
	public boolean flag_login=false;
	//用户名
	public String username;
	//用户头像
	public Bitmap userimage;
	public Bundle bundle;
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		bundle=new Bundle();
	}
	
	//获取音乐
	public List<Music> getMusics() 
	{
		return musics;
	}
	
	//设置音乐
	public void setMusics(List<Music> musics) 
	{
		this.musics = musics;
	}
	
	//获取音乐信息
	public List<Mp3Info> getMp3s() 
	{
		return mp3s;
	}
	
	//设置音乐信息
	public void setMp3s(List<Mp3Info> mp3s) 
	{
		this.mp3s = mp3s;
	}
	
	//获取音乐当前播放位置
	public int getCurrent_location() 
	{
		return current_location;
	}
	
	//设置音乐当前播放位置
	public void setCurrent_location(int current_location)
	{
		this.current_location = current_location;
	}

	//获得音乐播放模式
	public int getStatus() 
	{
		return status;
	}

	//设置音乐播放模式
	public void setStatus(int status) 
	{
		this.status = status;
	}
}
