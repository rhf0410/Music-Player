package com.example.display;

import java.util.Comparator;

import com.example.file.Mp3Info;

public class ArtistComparator implements Comparator<Artist>
{

	@Override
	public int compare(Artist lhs, Artist rhs) 
	{
		if (lhs.getSortLetters().equals("@")|| rhs.getSortLetters().equals("#")) 
		{
			return -1;
		} 
		else if (lhs.getSortLetters().equals("#")|| rhs.getSortLetters().equals("@")) 
		{
			return 1;
		} 
		else 
		{
			return lhs.getSortLetters().compareTo(rhs.getSortLetters());
		}
	}
}
