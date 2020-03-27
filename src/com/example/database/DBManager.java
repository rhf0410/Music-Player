package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库管理类
 * @author Administrator
 *
 */
public class DBManager extends SQLiteOpenHelper 
{
	//数据库名称
	private static String name="userinfo.db";
	//数据库的版本
	private static int version=6;
    //构造函数
	public DBManager(Context context) 
	{
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//定义创建表的SQL语句
		String sql="create table user_info "
				  +"("
				  +"user_name text, "
				  +"user_password text, "
				  +"user_cellphone text, "
				  +"user_gender text, "
				  +"user_birthday text, "
				  +"user_information text, "
				  +"user_question text, "
				  +"user_answer text, "
				  +"user_image blob, "
				  +"primary key(user_name)"
		          +")";
		
		String sql1="create table music "
				   +"("
				   +"music_name text, "
				   +"music_artist text, "
				   +"primary key(music_name,music_artist)"
				   +")";
		
		String sql2="create table music_remark "
				   +"("
				   +"user_name text, "
				   +"music_name text, "
				   +"music_artist text, "
				   +"remark_content text, "
				   +"content_time text"
				   +")";
		//执行创建语句操作
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//删除表的SQL语句
		String sql="drop table if exists user_info";
		//执行删除的SQL语句
		db.execSQL(sql);
		//删除表的SQL语句
		String sql1="drop table if exists music";
		//执行删除的SQL语句
		db.execSQL(sql1);
		//删除表的SQL语句
		String sql2="drop table if exists music_treasured";
		//执行删除的SQL语句
		db.execSQL(sql2);
		//重新创建表
		onCreate(db);
	}
}
