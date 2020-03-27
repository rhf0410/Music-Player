package com.example.Xml;

public class MusicModel 
{
    private String name;
    private String artist;
    
    public MusicModel() 
    {
		
	}
    
    public MusicModel(String name,String artist) 
    {
		this.name=name;
		this.artist=artist;
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
}
