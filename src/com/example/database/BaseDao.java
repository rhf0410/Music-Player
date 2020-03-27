package com.example.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BaseDao 
{
	//���ݿ������
    private DBManager dbManager;
    private SQLiteDatabase db;
    
    //���캯��
    public BaseDao(Context context) 
    {
		dbManager=new DBManager(context);
	}
    
    //ִ���޸Ĳ����ķ���
    protected final void executeUpdate(String sql,Object...args)
    {
    	db=dbManager.getWritableDatabase();
    	db.execSQL(sql,args);
    	db.close();
    }
    
    //ִ�в�ѯ��������
    protected final List<HashMap<String,Object>>executeQuery(String sql,String...args)
    {
    	List<HashMap<String,Object>>data=new ArrayList<HashMap<String,Object>>();  
    	db=dbManager.getReadableDatabase();
    	//����ֵΪ�α���ʵ����java�е�resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	//��ȡ�Ӳ�ѯ����������  
    	String []names=cur.getColumnNames();
    	//��ȡ��ѯ������  
    	int n=cur.getColumnCount();  
    	//��ȡ��ѯ�ļ�¼������
    	int m=cur.getCount();
    	//moveToNext�ƶ�����һ����¼������з���Ϊtrue������Ϊfalse 
    	//���ڱ���ÿһ���е����ݣ��洢��ʽ������Ϊkey������Ϊvalue
    	HashMap<String,Object>item=null;
    	while(cur.moveToNext())
    	{
    		//��ȡ����  
    		item=new HashMap<String, Object>();
    		for(String name:names)
    		{
    			if(name.equals("user_image"))
    			{
    				continue;
    			}
    			else
    			{
    				//����������ȡ�������ڵ��±��ڸ����±��ȡ��Ӧ������ 
        			String value=cur.getString(cur.getColumnIndex(name)); 
        			item.put(name, value); 
    			}
    		}
    		//����������� 
    		data.add(item);
    	}
    	cur.close();
    	return data;
    }
    
    //ִ��ͼƬ��ѯ��������
    protected final List<HashMap<String,Bitmap>>executeQueryPicture(String sql,String...args)
    {
    	List<HashMap<String,Bitmap>>data=new ArrayList<HashMap<String,Bitmap>>();  
    	db=dbManager.getReadableDatabase();
    	//����ֵΪ�α���ʵ����java�е�resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	//��ȡ�Ӳ�ѯ����������  
    	String []names=cur.getColumnNames();
    	//��ȡ��ѯ������  
    	int n=cur.getColumnCount();  
    	//��ȡ��ѯ�ļ�¼������
    	int m=cur.getCount();
    	//moveToNext�ƶ�����һ����¼������з���Ϊtrue������Ϊfalse 
    	//���ڱ���ÿһ���е����ݣ��洢��ʽ������Ϊkey������Ϊvalue
    	HashMap<String,Bitmap>item=null;
    	while(cur.moveToNext())
    	{
    		//��ȡ����  
    		item=new HashMap<String, Bitmap>();
    		for(String name:names)
    		{
    			if(name.equals("user_image"))
    			{
    				byte[] in = cur.getBlob(cur.getColumnIndex(name));
    				Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
    				item.put(name, bmpout);
    			}
    			else
    			{
    				continue;
    			}
    		}
    		//����������� 
    		data.add(item);
    	}
    	cur.close();
    	return data;
    }
    
    //�жϼ�¼�Ƿ����
    protected final boolean Picture_is_existent_or_not(String sql,String...args)
    {
    	boolean flag=false;
    	db=dbManager.getReadableDatabase();
    	//����ֵΪ�α���ʵ����java�е�resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	if(cur.getCount()!=0)
    		flag=true;
    	return flag;
    }
}
