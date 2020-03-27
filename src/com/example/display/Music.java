package com.example.display;

import android.graphics.Bitmap;

public class Music 
{
    private Bitmap image;
    private String name;
    private String artist;
    private String sortLetters;
    public boolean flag=false;
    
    public Music()
    {
    	
    }
    
	public Music(Bitmap image,String name,String artist) 
    {
		this.image=image;
		this.name=name;
		this.artist=artist;
	}
    
	public Bitmap getImage() 
	{
		return image;
	}
	
	public void setImage(Bitmap image) 
	{
		this.image = image;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getArtist() 
	{
		return artist;
	}
	
	public void setArtist(String artist) 
	{
		this.artist = artist;
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
