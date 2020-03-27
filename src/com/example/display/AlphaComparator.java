package com.example.display;

import java.util.Comparator;

import com.example.file.Mp3Info;

public class AlphaComparator implements Comparator<Mp3Info>
{

	@Override
	public int compare(Mp3Info lhs, Mp3Info rhs) 
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
