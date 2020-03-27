package com.example.SimpleMusicPlayer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackReceiver extends BroadcastReceiver 
{
    //全局变量
	private MusicApplication mApplication;
	//判读字符串
	private String ctrl_code;
	//音乐播放类
	private IMusic music;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//实例化Application对象
		mApplication=(MusicApplication) context.getApplicationContext();
		//获取判断字符串
		ctrl_code=intent.getAction();
		//获取音乐播放类
		music=mApplication.music;
		
		//播放操作
		if(ctrl_code.equals("play"))
		{
			//播放操作
			music.playSong();
		}
		
		//前进操作
		if(ctrl_code.equals("next"))
		{
			//播放下一首音乐
			music.moveon();
		}
		
		//取消操作
		if(ctrl_code.equals("cancel"))
		{
			//撤销通知
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) mApplication.getSystemService(ns);
			nMgr.cancel(0);
			//退出程序
			System.exit(0);
		}
	}
}
