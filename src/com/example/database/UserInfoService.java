package com.example.database;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * �û�������
 * @author Administrator
 *
 */
public class UserInfoService 
{
    private UserInfoDaoImpl userinfodaoimpl;
    //���캯��
    public UserInfoService(Context context) 
    {
		userinfodaoimpl=new UserInfoDaoImpl(context);
	}
    
    //ע�ắ��
    public boolean doReg(String user_name,String user_password,String user_cellphone,String gender,String birthday,String information,String question,String answer,byte[] os)
    {
    	//���
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
    
    //�������ֲ���
    public boolean doInsertingMusic(String music_name,String music_artist)
    {
    	//���
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
    
    //����¼����
    public List<HashMap<String,Object>>doCheckin(String name,String password)
    {
    	return this.userinfodaoimpl.checkLogin(name, password);
    }
    
    //��ѯͼƬ����
    public List<HashMap<String,Bitmap>>picture_capturing(String name,String password)
    {
    	return this.userinfodaoimpl.query_picture(name, password);
    }
    
    //��ѯͼƬ����
    public List<HashMap<String,Bitmap>>picture_capturing_byuser(String name)
    {
    	return this.userinfodaoimpl.query_picture_byuser(name);
    }
    
    //�жϲ���
    public boolean user_existence(String name,String password)
    {
    	return this.userinfodaoimpl.exist_or_not(name, password);
    }
    
    //�������ݺ���
    public boolean updateUserInformation(String user_name,String user_cellphone,String gender,String birthday,String information,byte[] os)
    {
    	//���
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
    
    //��ѯ������𰸲���
    public List<HashMap<String,Object>>question_and_answer_finding(String name)
    {
    	return this.userinfodaoimpl.question_and_answer(name);
    }
    
    //�޸��������
    public boolean updatepassword(String user_password,String user_name)
    {
    	//���
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
    
    //��ѯ�ֻ�����
    public List<HashMap<String,Object>>query_cellphone_operating(String name)
    {
    	return this.userinfodaoimpl.query_cellphone(name);
    }
    
    //������������
    public boolean doInsertingRemark(String user_name,String music_name,String music_artist,String remark_content,String content_time)
    {
    	//���
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
    
    //��ѯ���۲���
    public List<HashMap<String,Object>>query_remark(String music_name,String music_artist)
    {
    	return this.userinfodaoimpl.query_remark(music_name, music_artist);
    }
    
    //ɾ�����۲���
    public boolean doDeletingMusicRemark(String user_name,String time,String music_name,String music_artist)
    {
    	//���
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
    
    //�޸����۲���
    public boolean updateremark(String contentrevised,String timerevised,String user_name,String time,String music_name,String music_artist)
    {
    	//���
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
