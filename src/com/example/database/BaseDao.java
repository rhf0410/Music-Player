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
	//数据库管理类
    private DBManager dbManager;
    private SQLiteDatabase db;
    
    //构造函数
    public BaseDao(Context context) 
    {
		dbManager=new DBManager(context);
	}
    
    //执行修改操作的方法
    protected final void executeUpdate(String sql,Object...args)
    {
    	db=dbManager.getWritableDatabase();
    	db.execSQL(sql,args);
    	db.close();
    }
    
    //执行查询操作方法
    protected final List<HashMap<String,Object>>executeQuery(String sql,String...args)
    {
    	List<HashMap<String,Object>>data=new ArrayList<HashMap<String,Object>>();  
    	db=dbManager.getReadableDatabase();
    	//返回值为游标其实就是java中的resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	//获取从查询的所有列名  
    	String []names=cur.getColumnNames();
    	//获取查询的列数  
    	int n=cur.getColumnCount();  
    	//获取查询的记录总条数
    	int m=cur.getCount();
    	//moveToNext移动到下一条记录，如果有返回为true，否则为false 
    	//用于保存每一行中的数据，存储方式已列名为key，数据为value
    	HashMap<String,Object>item=null;
    	while(cur.moveToNext())
    	{
    		//获取数据  
    		item=new HashMap<String, Object>();
    		for(String name:names)
    		{
    			if(name.equals("user_image"))
    			{
    				continue;
    			}
    			else
    			{
    				//根据列名获取该列所在的下标在根据下标获取对应的数据 
        			String value=cur.getString(cur.getColumnIndex(name)); 
        			item.put(name, value); 
    			}
    		}
    		//保存该行数据 
    		data.add(item);
    	}
    	cur.close();
    	return data;
    }
    
    //执行图片查询操作方法
    protected final List<HashMap<String,Bitmap>>executeQueryPicture(String sql,String...args)
    {
    	List<HashMap<String,Bitmap>>data=new ArrayList<HashMap<String,Bitmap>>();  
    	db=dbManager.getReadableDatabase();
    	//返回值为游标其实就是java中的resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	//获取从查询的所有列名  
    	String []names=cur.getColumnNames();
    	//获取查询的列数  
    	int n=cur.getColumnCount();  
    	//获取查询的记录总条数
    	int m=cur.getCount();
    	//moveToNext移动到下一条记录，如果有返回为true，否则为false 
    	//用于保存每一行中的数据，存储方式已列名为key，数据为value
    	HashMap<String,Bitmap>item=null;
    	while(cur.moveToNext())
    	{
    		//获取数据  
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
    		//保存该行数据 
    		data.add(item);
    	}
    	cur.close();
    	return data;
    }
    
    //判断记录是否存在
    protected final boolean Picture_is_existent_or_not(String sql,String...args)
    {
    	boolean flag=false;
    	db=dbManager.getReadableDatabase();
    	//返回值为游标其实就是java中的resultSet  
    	Cursor cur=db.rawQuery(sql, args);
    	if(cur.getCount()!=0)
    		flag=true;
    	return flag;
    }
}
