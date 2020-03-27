package com.example.database;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
/**
 * �û�ִ�����ݿ���
 * @author Administrator
 *
 */
public class UserInfoDaoImpl extends BaseDao 
{

	public UserInfoDaoImpl(Context context) 
	{
		super(context);
	}

	//�������ݵķ���
	public void save(String user_name,String user_password,String user_cellphone,String gender,String birthday,String information,String question,String answer,byte[] os)
	{
		//���ò����sql���
		String sql="insert into user_info(user_name,user_password,user_cellphone,user_gender,user_birthday,user_information,user_question,user_answer,user_image) "
				  +"values(?,?,?,?,?,?,?,?,?)";
		//ִ��sql���
		super.executeUpdate(sql, user_name,user_password,user_cellphone,gender,birthday,information,question,answer,os);
	}
	
	//�������ֲ�������
	public void insert_music(String music_name,String music_artist)
	{
		//���ò����sql���
		String sql="insert into music(music_name,music_artist) "
				  +"values(?,?)";
		//ִ��sql���
		super.executeUpdate(sql, music_name,music_artist);
	}
	
	//��ѯ����
	public List<HashMap<String,Object>>checkLogin(String name,String pwd)
	{
		//��дSQL
		String sql="select user_name,user_image,user_cellphone,user_gender,user_birthday,user_information "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//���ز�ѯ���
		return super.executeQuery(sql,name,pwd);
	}
	
	//��ѯͼƬ����
	public List<HashMap<String,Bitmap>>query_picture(String name,String pwd)
	{
		//��дSQL
		String sql="select user_image "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//���ز�ѯ���
		return super.executeQueryPicture(sql,name,pwd);
	}
	
	//��ѯͼƬ����
	public List<HashMap<String,Bitmap>>query_picture_byuser(String name)
	{
		//��дSQL
		String sql="select user_image "
				  +"from user_info "
				  +"where user_name=?";
		//���ز�ѯ���
		return super.executeQueryPicture(sql,name);
	}
	
	//�жϲ���
	public boolean exist_or_not(String name,String pwd)
	{
		//��дSQL
		String sql="select * "
				  +"from user_info "
				  +"where user_name=? and user_password=?";
		//���ز�ѯ���
		return super.Picture_is_existent_or_not(sql,name,pwd);
	}
	
	//�������ݲ���
	public void update_userinformation(String user_name,String user_cellphone,String gender,String birthday,String information,byte[] os)
	{
		//���ò����sql���
		String sql="update user_info "
				  +"set user_name=?, "
				  +"user_cellphone=?, "
				  +"user_gender=?, "
				  +"user_birthday=?, "
				  +"user_information=?, "
				  +"user_image=?";
		//ִ��sql���
		super.executeUpdate(sql, user_name,user_cellphone,gender,birthday,information,os);
	}
	
	//��ѯ�������
	public List<HashMap<String,Object>>question_and_answer(String name)
	{
		//��дSQL
		String sql="select user_question,user_answer "
				  +"from user_info "
				  +"where user_name=?";
		//���ز�ѯ���
		return super.executeQuery(sql,name);
	}
	
	//�������ݲ���
    public void update_password_by_question(String user_password,String user_name)
	{
		//���ò����sql���
		String sql="update user_info "
				  +"set user_password=? "
			      +"where user_name=?";
		//ִ��sql���
		super.executeUpdate(sql,user_password,user_name);
	}
    
    //��ѯ�ֻ���
  	public List<HashMap<String,Object>>query_cellphone(String name)
  	{
  		//��дSQL
  		String sql="select user_cellphone "
  				  +"from user_info "
  				  +"where user_name=?";
  		//���ز�ѯ���
  		return super.executeQuery(sql,name);
  	}
  	
    //������������
  	public void insert_remark(String user_name,String music_name,String music_artist,String remark_content,String content_time)
  	{
  		//���ò����sql���
  		String sql="insert into music_remark(user_name,music_name,music_artist,remark_content,content_time) "
  				  +"values(?,?,?,?,?)";
  		//ִ��sql���
  		super.executeUpdate(sql, user_name,music_name,music_artist,remark_content,content_time);
  	}
  	
    //��ѯƷ����Ϣ
  	public List<HashMap<String,Object>>query_remark(String music_name,String music_artist)
  	{
  		//��дSQL
  		String sql="select user_name,content_time,remark_content "
  				  +"from music_remark "
  				  +"where music_name=? and music_artist=?";
  		//���ز�ѯ���
  		return super.executeQuery(sql,music_name,music_artist);
  	}
  	
  	//ɾ��������Ϣ
  	public void delete_remark(String user_name,String time,String music_name,String music_artist)
  	{
  	    //��дSQL
  		String sql="delete "
  				  +"from music_remark "
  				  +"where user_name=? and content_time=? and music_name=? and music_artist=?";
  		//ִ��ɾ������
  		super.executeUpdate(sql, user_name,time,music_name,music_artist);
  	}
  	
    //�޸�������Ϣ
  	public void update_remark(String contentrevised,String timerevised,String user_name,String time,String music_name,String music_artist)
  	{
  	    //��дSQL
  		String sql="update music_remark "
  				  +"set remark_content=?,content_time=? "
  				  +"where user_name=? and content_time=? and music_name=? and music_artist=?";
  		//ִ��ɾ������
  		super.executeUpdate(sql,contentrevised, timerevised,user_name,time,music_name,music_artist);
  	}
}
