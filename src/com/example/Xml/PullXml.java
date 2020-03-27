package com.example.Xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Environment;

public class PullXml 
{
	//获取sd卡根目录
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //通用目录
    private String general=sdDir+File.separator+"CDMusic"+File.separator;
	private List<MusicModel>list;
	//构造函数
	public PullXml() 
	{
		list=new ArrayList<MusicModel>();
	}
    //解析xml文件
	public void ParseXml()
	{
		try 
		{
			File path = new File(general+"musics.xml");
			FileInputStream fis = new FileInputStream(path);
			// 获得pull解析器对象  
			XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();  
			// 指定解析的文件和编码格式  
			parser.setInput(fis, "utf-8");
			// 获得事件类型  
			int eventType = parser.getEventType(); 
			String name = "";  
			String artist="";
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				// 获得当前节点的名称
				String tagName = parser.getName(); 
				switch (eventType)
				{
				    case XmlPullParser.START_TAG:
					    if ("musics".equals(tagName)) 
					    {
						  
					    }
					    else if("music".equals(tagName))
					    {
						 
					    }
					    else if ("name".equals(tagName))
					    {
						    name=parser.nextText();
					    }
					    else if ("artist".equals(tagName))
					    {
						    artist=parser.nextText();
					    }
					    break;
				    case XmlPullParser.END_TAG:
					    if ("music".equals(tagName))
					    {
					    	MusicModel model=new MusicModel();
					    	model.setName(name);
					    	model.setArtist(artist);
					    	list.add(model);
					    }
					    break;
				     default:
					     break;
				}
				// 获得下一个事件类型  
				eventType = parser.next(); 
			}
		} 
		catch (Exception e) 
		{
	       e.printStackTrace();
		}
	 }
	
	public List<MusicModel> getList() 
	{
		return list;
	}
	public void setList(List<MusicModel> list) 
	{
		this.list = list;
	}
}
