package com.example.file;

//����ʵ��
public class Mp3Info 
{
    //������
	private String MusicName;
	//������Ϣ
	private String MusicInfo;
	//���ָ��
	private String lyrics;
	//����ͼƬ
	private String picture;
	private String sortLetters;
	
	//���캯��
	public Mp3Info() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public Mp3Info(String MusicName,String MusicInfo,String lyrics,String picture) 
	{
		this.MusicName=MusicName;
		this.MusicInfo=MusicInfo;
		this.lyrics=lyrics;
		this.picture=picture;
	}
	
	public String getMusicName() 
	{
		return MusicName;
	}
	
	public void setMusicName(String musicName)
	{
		MusicName = musicName;
	}
	
	public String getMusicInfo() 
	{
		return MusicInfo;
	}
	
	public void setMusicInfo(String musicInfo) 
	{
		MusicInfo = musicInfo;
	}
	
	public String getLyrics() 
	{
		return lyrics;
	}
	
	public void setLyrics(String lyrics)
	{
		this.lyrics = lyrics;
	}
	
	public String getPicture() 
	{
		return picture;
	}
	
	public void setPicture(String picture) 
	{
		this.picture = picture;
	}
	
	public String getSortLetters() 
	{
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) 
	{
		this.sortLetters = sortLetters;
	}
}
