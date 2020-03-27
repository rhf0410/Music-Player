package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * ���ݿ������
 * @author Administrator
 *
 */
public class DBManager extends SQLiteOpenHelper 
{
	//���ݿ�����
	private static String name="userinfo.db";
	//���ݿ�İ汾
	private static int version=6;
    //���캯��
	public DBManager(Context context) 
	{
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//���崴�����SQL���
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
		//ִ�д���������
		db.execSQL(sql);
		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//ɾ�����SQL���
		String sql="drop table if exists user_info";
		//ִ��ɾ����SQL���
		db.execSQL(sql);
		//ɾ�����SQL���
		String sql1="drop table if exists music";
		//ִ��ɾ����SQL���
		db.execSQL(sql1);
		//ɾ�����SQL���
		String sql2="drop table if exists music_treasured";
		//ִ��ɾ����SQL���
		db.execSQL(sql2);
		//���´�����
		onCreate(db);
	}
}
