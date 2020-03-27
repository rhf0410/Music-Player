package com.example.SimpleMusicPlayer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BackReceiver extends BroadcastReceiver 
{
    //ȫ�ֱ���
	private MusicApplication mApplication;
	//�ж��ַ���
	private String ctrl_code;
	//���ֲ�����
	private IMusic music;
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//ʵ����Application����
		mApplication=(MusicApplication) context.getApplicationContext();
		//��ȡ�ж��ַ���
		ctrl_code=intent.getAction();
		//��ȡ���ֲ�����
		music=mApplication.music;
		
		//���Ų���
		if(ctrl_code.equals("play"))
		{
			//���Ų���
			music.playSong();
		}
		
		//ǰ������
		if(ctrl_code.equals("next"))
		{
			//������һ������
			music.moveon();
		}
		
		//ȡ������
		if(ctrl_code.equals("cancel"))
		{
			//����֪ͨ
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) mApplication.getSystemService(ns);
			nMgr.cancel(0);
			//�˳�����
			System.exit(0);
		}
	}
}
