package com.example.database;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 用户服务类
 * @author Administrator
 *
 */
public class UserInfoService 
{
    private UserInfoDaoImpl userinfodaoimpl;
    //构造函数
    public UserInfoService(Context context) 
    {
		userinfodaoimpl=new UserInfoDaoImpl(context);
	}
    
    //注册函数
    public boolean doReg(String user_name,String user_password,String user_cellphone,String gender,String birthday,String information,String question,String answer,byte[] os)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.save(user_name, user_password, user_cellphone, gender, birthday, information, question, answer, os);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    	return flag;
    }
    
    //插入音乐操作
    public boolean doInsertingMusic(String music_name,String music_artist)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.insert_music(music_name, music_artist);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return flag;
    }
    
    //检测登录函数
    public List<HashMap<String,Object>>doCheckin(String name,String password)
    {
    	return this.userinfodaoimpl.checkLogin(name, password);
    }
    
    //查询图片操作
    public List<HashMap<String,Bitmap>>picture_capturing(String name,String password)
    {
    	return this.userinfodaoimpl.query_picture(name, password);
    }
    
    //查询图片操作
    public List<HashMap<String,Bitmap>>picture_capturing_byuser(String name)
    {
    	return this.userinfodaoimpl.query_picture_byuser(name);
    }
    
    //判断操作
    public boolean user_existence(String name,String password)
    {
    	return this.userinfodaoimpl.exist_or_not(name, password);
    }
    
    //更改数据函数
    public boolean updateUserInformation(String user_name,String user_cellphone,String gender,String birthday,String information,byte[] os)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.update_userinformation(user_name, user_cellphone, gender, birthday, information, os);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    	return flag;
    }
    
    //查询问题与答案操作
    public List<HashMap<String,Object>>question_and_answer_finding(String name)
    {
    	return this.userinfodaoimpl.question_and_answer(name);
    }
    
    //修改密码操作
    public boolean updatepassword(String user_password,String user_name)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.update_password_by_question(user_password, user_name);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    	return flag;
    }
    
    //查询手机操作
    public List<HashMap<String,Object>>query_cellphone_operating(String name)
    {
    	return this.userinfodaoimpl.query_cellphone(name);
    }
    
    //插入评论内容
    public boolean doInsertingRemark(String user_name,String music_name,String music_artist,String remark_content,String content_time)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.insert_remark(user_name,music_name,music_artist,remark_content,content_time);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return flag;
    }
    
    //查询评论操作
    public List<HashMap<String,Object>>query_remark(String music_name,String music_artist)
    {
    	return this.userinfodaoimpl.query_remark(music_name, music_artist);
    }
    
    //删除评论操作
    public boolean doDeletingMusicRemark(String user_name,String time,String music_name,String music_artist)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.delete_remark(user_name, time, music_name, music_artist);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return flag;
    }
    
    //修改评论操作
    public boolean updateremark(String contentrevised,String timerevised,String user_name,String time,String music_name,String music_artist)
    {
    	//标记
    	boolean flag=false;
    	try 
    	{
			this.userinfodaoimpl.update_remark(contentrevised, timerevised, user_name, time, music_name, music_artist);
			flag=true;
		} 
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
    	return flag;
    }
}
