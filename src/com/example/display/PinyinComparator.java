package com.example.display;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<Music>
{

	public int compare(Music o1, Music o2) 
	{
		if (o1.getSortLetters().equals("@")|| o2.getSortLetters().equals("#")) 
		{
			return -1;
		} 
		else if (o1.getSortLetters().equals("#")|| o2.getSortLetters().equals("@")) 
		{
			return 1;
		} 
		else 
		{
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
