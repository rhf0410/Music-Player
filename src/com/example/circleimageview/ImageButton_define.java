package com.example.circleimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageButton_define extends LinearLayout 
{
    public ImageButton imagebutton;
    
   /* public TextView getTextview() 
    {
		return textview;
	}

	public void setTextview(TextView textview) 
	{
		this.textview = textview;
	}*/

	private TextView textview;
	public ImageButton_define(Context context,AttributeSet attrs) 
	{
		super(context,attrs);
		// TODO Auto-generated constructor stub
		imagebutton=new ImageButton(context,attrs);
		imagebutton.setPadding(0, 0, 0, 0);
		
		textview=new TextView(context, attrs);
		textview.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		textview.setPadding(0, 0, 0, 0);
		
		setClickable(true);
		setFocusable(true);
		setBackgroundColor("#ffffff");
		setOrientation(LinearLayout.VERTICAL);
		
		addView(imagebutton);
		addView(textview);
	}
	
	private void setBackgroundColor(String string) 
	{
		// TODO Auto-generated method stub
		
	}
	
	//用于获取图片的含义
	public String getTextview() 
    {
		return textview.getText().toString();
    }

	public void setTextview(TextView textview) 
	{
		this.textview = textview;
	}
}
