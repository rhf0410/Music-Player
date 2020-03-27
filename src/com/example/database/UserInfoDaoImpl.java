package com.example.database;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
/**
 * 用户执行数据库类
 * @author Administrator
 *
 */
public class UserInfoDaoImpl extends BaseDao 
{

	public UserInfoDaoImpl(Context context) 
	{
		super(context);
	}

	//保存数据的方法
	public void save(String user_name,String user_password,String user_cellphone,String gender,String birthday,String information,String question,String answer,byte[] os)
	{
		//设置插入的sql语句
		String sql="insert into user_info(user_name,user_password,user_cellphone,user_gender,user_birthday,user_information,user_question,user_answer,user_image) "
				  +"values(?,?,?,?,?,?,?,?,?)";
		//执行sql语句
		super.executeUpdate(sql, user_name,user_password,user_cellphone,gender,birthday,information,question,answer,os);
	}
	
	//插入音乐操作方法
	public void insert_music(String music_name,String music_artist)
	{
		//设置插入的sql语句
		String sql="insert into music(music_name,music_artist) "
				  +"values(?,?)";
		//执行sql语句
		super.executeUpdate(sql, music_name,music_artist);
	}
	
	//查询操作
	public List<HashMap<String,Object>>checkLogin(String name,String pwd)
	{
		//编写SQL
		String sql="select user_name,user_image,user_cellphone,user_gender,user_birthday,user_information "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//返回查询结果
		return super.executeQuery(sql,name,pwd);
	}
	
	//查询图片操作
	public List<HashMap<String,Bitmap>>query_picture(String name,String pwd)
	{
		//编写SQL
		String sql="select user_image "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//返回查询结果
		return super.executeQueryPicture(sql,name,pwd);
	}
	
	//查询图片操作
	public List<HashMap<String,Bitmap>>query_picture_byuser(String name)
	{
		//编写SQL
		String sql="select user_image "
				  +"from user_info "
				  +"where user_name=?";
		//返回查询结果
		return super.executeQueryPicture(sql,name);
	}
	
	//判断操作
	public boolean exist_or_not(String name,String pwd)
	{
		//编写SQL
		String sql="select * "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//返回查询结果
		return super.Picture_is_existent_or_not(sql,name,pwd);
	}
	
	//更改数据操作
	public void update_userinformation(String user_name,String user_cellphone,String gender,String birthday,String information,byte[] os)
	{
		//设置插入的sql语句
		String sql="update user_info "
				  +"set user_name=?, "
				  +"user_cellphone=?, "
				  +"user_gender=?, "
				  +"user_birthday=?, "
				  +"user_information=?, "
				  +"user_image=?";
		//执行sql语句
		super.executeUpdate(sql, user_name,user_cellphone,gender,birthday,information,os);
	}
	
	//查询问题与答案
	public List<HashMap<String,Object>>question_and_answer(String name)
	{
		//编写SQL
		String sql="select user_question,user_answer "
				  +"from user_info "
				  +"where user_name=?";
		//返回查询结果
		return super.executeQuery(sql,name);
	}
	
	//更改数据操作
    public void update_password_by_question(String user_password,String user_name)
	{
		//设置插入的sql语句
		String sql="update user_info "
				  +"set user_password=? "
			      +"where user_name=?";
		//执行sql语句
		super.executeUpdate(sql,user_password,user_name);
	}
    
    //查询手机号
  	public List<HashMap<String,Object>>query_cellphone(String name)
  	{
  		//编写SQL
  		String sql="select user_cellphone "
  				  +"from user_info "
  				  +"where user_name=?";
  		//返回查询结果
  		return super.executeQuery(sql,name);
  	}
  	
    //插入评论内容
  	public void insert_remark(String user_name,String music_name,String music_artist,String remark_content,String content_time)
  	{
  		//设置插入的sql语句
  		String sql="insert into music_remark(user_name,music_name,music_artist,remark_content,content_time) "
  				  +"values(?,?,?,?,?)";
  		//执行sql语句
  		super.executeUpdate(sql, user_name,music_name,music_artist,remark_content,content_time);
  	}
  	
    //查询品论信息
  	public List<HashMap<String,Object>>query_remark(String music_name,String music_artist)
  	{
  		//编写SQL
  		String sql="select user_name,content_time,remark_content "
  				  +"from music_remark "
  				  +"where music_name=? and music_artist=?";
  		//返回查询结果
  		return super.executeQuery(sql,music_name,music_artist);
  	}
  	
  	//删除评论信息
  	public void delete_remark(String user_name,String time,String music_name,String music_artist)
  	{
  	    //编写SQL
  		String sql="delete "
  				  +"from music_remark "
  				  +"where user_name=? and content_time=? and music_name=? and music_artist=?";
  		//执行删除操作
  		super.executeUpdate(sql, user_name,time,music_name,music_artist);
  	}
  	
    //修改评论信息
  	public void update_remark(String contentrevised,String timerevised,String user_name,String time,String music_name,String music_artist)
  	{
  	    //编写SQL
  		String sql="update music_remark "
  				  +"set remark_content=?,content_time=? "
  				  +"where user_name=? and content_time=? and music_name=? and music_artist=?";
  		//执行删除操作
  		super.executeUpdate(sql,contentrevised, timerevised,user_name,time,music_name,music_artist);
  	}
}
