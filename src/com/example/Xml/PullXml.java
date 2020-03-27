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
	//��ȡsd����Ŀ¼
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //ͨ��Ŀ¼
    private String general=sdDir+File.separator+"CDMusic"+File.separator;
	private List<MusicModel>list;
	//���캯��
	public PullXml() 
	{
		list=new ArrayList<MusicModel>();
	}
    //����xml�ļ�
	public void ParseXml()
	{
		try 
		{
			File path = new File(general+"musics.xml");
			FileInputStream fis = new FileInputStream(path);
			// ���pull����������  
			XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();  
			// ָ���������ļ��ͱ����ʽ  
			parser.setInput(fis, "utf-8");
			// ����¼�����  
			int eventType = parser.getEventType(); 
			String name = "";  
			String artist="";
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				// ��õ�ǰ�ڵ������
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
				// �����һ���¼�����  
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
