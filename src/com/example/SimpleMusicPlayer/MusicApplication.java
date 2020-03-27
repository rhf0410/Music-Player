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
	//����ʵ����
	private List<Music> musics;
	//������Ϣ��
	private List<Mp3Info>mp3s;
	//��ǰ����λ��
	private int current_location;
	//���ֲ���ģʽ
	private int status;
	//���ֲ��Žӿ�
	public IMusic music;
	//���Ž���
	public int play_proceed;
	//�Ƿ��¼
	public boolean flag_login=false;
	//�û���
	public String username;
	//�û�ͷ��
	public Bitmap userimage;
	public Bundle bundle;
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		bundle=new Bundle();
	}
	
	//��ȡ����
	public List<Music> getMusics() 
	{
		return musics;
	}
	
	//��������
	public void setMusics(List<Music> musics) 
	{
		this.musics = musics;
	}
	
	//��ȡ������Ϣ
	public List<Mp3Info> getMp3s() 
	{
		return mp3s;
	}
	
	//����������Ϣ
	public void setMp3s(List<Mp3Info> mp3s) 
	{
		this.mp3s = mp3s;
	}
	
	//��ȡ���ֵ�ǰ����λ��
	public int getCurrent_location() 
	{
		return current_location;
	}
	
	//�������ֵ�ǰ����λ��
	public void setCurrent_location(int current_location)
	{
		this.current_location = current_location;
	}

	//������ֲ���ģʽ
	public int getStatus() 
	{
		return status;
	}

	//�������ֲ���ģʽ
	public void setStatus(int status) 
	{
		this.status = status;
	}
}
