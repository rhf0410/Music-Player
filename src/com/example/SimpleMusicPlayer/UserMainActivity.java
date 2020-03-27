package com.example.SimpleMusicPlayer;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.circleimageview.CircleImageView;
import com.example.database.UserInfoService;

public class UserMainActivity extends Activity 
{
    //注册按钮
	private Button register;
	private EditText username;
	private EditText userpassword;
	private Button login;
	private CircleImageView imageview;
	private Button update_password;
	//注册意图
	private Intent intent_register;
	private Intent intent_login;
	//数据库服务类
	private UserInfoService userInfoService;
	private MusicApplication mApplication;
	private int choice;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_main);
		//实例化控件操作
		findView();
		//为控件添加监听器操作
		setListener();
	}

	//实例化控件操作
	private void findView() 
	{
		register=(Button) findViewById(R.id.user_register_operating);
		username=(EditText) findViewById(R.id.user_name_value);
		userpassword=(EditText) findViewById(R.id.user_password_value);
		login=(Button) findViewById(R.id.user_log_in_operating);
		//实例化意图
		intent_register=new Intent();
		intent_login=new Intent();
		userInfoService=new UserInfoService(getApplicationContext());
		imageview=(CircleImageView) findViewById(R.id.user_image);
		mApplication=(MusicApplication) getApplication();
		update_password=(Button) findViewById(R.id.user_password_forgetting_operating);
	}

	//为控件添加监听器操作
	private void setListener() 
	{
		//为登录按钮添加监听器
		login.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				//进行登录操作
				String uname=username.getText().toString();
				String upassword=userpassword.getText().toString();
				//判断用户是否存在
				if(userInfoService.user_existence(uname, upassword))
				{
					List<HashMap<String,Object>>results=userInfoService.doCheckin(uname, upassword);
					//为意图添加动作
					intent_login.setClass(UserMainActivity.this, MainActivity.class);
					List<HashMap<String,Bitmap>>result2=userInfoService.picture_capturing(uname, upassword);
			        //填装数据
					mApplication.flag_login=true;
					mApplication.userimage=result2.get(0).get("user_image");
					mApplication.bundle.putString("user_name", results.get(0).get("user_name").toString());
					mApplication.bundle.putString("user_cellphone", results.get(0).get("user_cellphone").toString());
					mApplication.bundle.putString("user_gender", results.get(0).get("user_gender").toString());
					mApplication.bundle.putString("user_birthday", results.get(0).get("user_birthday").toString());
					mApplication.bundle.putString("user_information", results.get(0).get("user_information").toString());
					startActivity(intent_login);
					finish();
				}
				else
				{
					//弹出提示
					Toast.makeText(getApplicationContext(), "该账户不存在!", 1000).show();
				}
			}
		});
		
		//为密码框添加触发事件
		userpassword.addTextChangedListener(new TextWatcher() 
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				//显示视图操作
				if(!username.getText().toString().equals(""))
				{
					String name=username.getText().toString();
					if(userInfoService.user_existence(name, s.toString()))
					{
						List<HashMap<String,Bitmap>>result2=userInfoService.picture_capturing(name, s.toString());
						imageview.setImageBitmap(result2.get(0).get("user_image"));
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "用户名不能为空!", 1000).show();
				}
			}
		});
		
		//修改密码操作
		update_password.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//判断用户名操作
				if(username.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "请输入用户名!", 1000).show();
				}
				else
				{
					//弹出提示框
					AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("请选择修改密码的方式");
					final String[] sources = {"通过手机验证码找回", "通过问题找回"};
	                //设置一个单项选择下拉框
					builder.setSingleChoiceItems(sources, 0, new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							choice=which;
						}
					});
					//确定按钮操作
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	if(choice==0)
							{
	                    		//设置意图用于跳转
	                    		Intent intent_cellphone=new Intent(UserMainActivity.this,UpdateByCellphoneActivity.class);
	                    		intent_cellphone.putExtra("username", username.getText().toString());
	                    		startActivity(intent_cellphone);
							}
	                    	else if(choice==1)
	                    	{
	                    		//设置意图用于跳转
	                    		Intent intent_question=new Intent(UserMainActivity.this,UpdateByQuestionActivity.class);
	                    		intent_question.putExtra("username", username.getText().toString());
	                    		startActivity(intent_question);
	                    	}
	                    }
	                });
					//取消按钮操作
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	//关闭对话框
	                        dialog.cancel();
	                    }
	                });
					//显示提示框
					builder.show();
				}
			}
		});
		
		//为注册按钮添加监听器
		register.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//为意图添加动作
				intent_register.setClass(UserMainActivity.this, RegisterActivity.class);
				//启动意图
				startActivity(intent_register);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_main, menu);
		return true;
	}
	
	@Override
	protected void onStart() 
	{
		super.onStart();
	}
	
    @Override
    protected void onPause() 
    {
    	super.onPause();
    }
}
