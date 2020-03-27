package com.example.Xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.io.SAXReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.os.Environment;

import com.example.display.Music;

public class JavaXml 
{
	//获取sd卡根目录
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //通用目录
    private String general=sdDir+File.separator+"CDMusic"+File.separator;
    private LinkedList<Music>list;
    public MusicModel[]musics;
	
    public JavaXml(LinkedList<Music>list) 
    {
		this.list=list;
		musics=new MusicModel[list.size()];
		for(int i=0;i<list.size();i++)
		{
			musics[i]=new MusicModel(list.get(i).getName(), list.get(i).getArtist());
		}
	}
    
    public void BuildXMLDoc() throws IOException, JDOMException
    {
    	String num=String.valueOf(list.size());
    	// 创建根节点 并设置它的属性 ;
    	Element root = new Element("musics").setAttribute("count", num); 
    	// 将根节点添加到文档中；
    	Document Doc = new Document(root); 
    	for (int i = 0; i < list.size(); i++)
    	{
    		// 创建节点 MusicModel;
        	Element elements = new Element("music");
        	// music 节点添加子节点并赋值；  
        	elements.addContent(new Element("name").setText(musics[i].getName()));
        	elements.addContent(new Element("artist").setText(musics[i].getArtist()));
        	root.addContent(elements);  
    	}
    	// 输出 musics.xml 文件；
    	// 使xml文件 缩进效果  
    	Format format = Format.getPrettyFormat(); 
    	XMLOutputter XMLOut = new XMLOutputter(format);
    	XMLOut.output(Doc, new FileOutputStream(general+"musics.xml"));  
    }
}
