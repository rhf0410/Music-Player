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
	//��ȡsd����Ŀ¼
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //ͨ��Ŀ¼
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
    	// �������ڵ� �������������� ;
    	Element root = new Element("musics").setAttribute("count", num); 
    	// �����ڵ���ӵ��ĵ��У�
    	Document Doc = new Document(root); 
    	for (int i = 0; i < list.size(); i++)
    	{
    		// �����ڵ� MusicModel;
        	Element elements = new Element("music");
        	// music �ڵ�����ӽڵ㲢��ֵ��  
        	elements.addContent(new Element("name").setText(musics[i].getName()));
        	elements.addContent(new Element("artist").setText(musics[i].getArtist()));
        	root.addContent(elements);  
    	}
    	// ��� musics.xml �ļ���
    	// ʹxml�ļ� ����Ч��  
    	Format format = Format.getPrettyFormat(); 
    	XMLOutputter XMLOut = new XMLOutputter(format);
    	XMLOut.output(Doc, new FileOutputStream(general+"musics.xml"));  
    }
}
