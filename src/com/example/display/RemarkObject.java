package com.example.display;

import android.graphics.Bitmap;

public class RemarkObject 
{
	private Bitmap remark_image;
	private String remark_name;
	private String remark_time;
	private String remark_content;
	
	public Bitmap getRemark_image() 
	{
		return remark_image;
	}
	
	public void setRemark_image(Bitmap remark_image) 
	{
		this.remark_image = remark_image;
	}
	
	public String getRemark_name() 
	{
		return remark_name;
	}
	
	public void setRemark_name(String remark_name)
	{
		this.remark_name = remark_name;
	}
	
	public String getRemark_time() 
	{
		return remark_time;
	}
	
	public void setRemark_time(String remark_time)
	{
		this.remark_time = remark_time;
	}
	
	public String getRemark_content() 
	{
		return remark_content;
	}
	
	public void setRemark_content(String remark_content) 
	{
		this.remark_content = remark_content;
	}
}
