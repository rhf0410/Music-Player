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
    //ע�ᰴť
	private Button register;
	private EditText username;
	private EditText userpassword;
	private Button login;
	private CircleImageView imageview;
	private Button update_password;
	//ע����ͼ
	private Intent intent_register;
	private Intent intent_login;
	//���ݿ������
	private UserInfoService userInfoService;
	private MusicApplication mApplication;
	private int choice;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_main);
		//ʵ�����ؼ�����
		findView();
		//Ϊ�ؼ���Ӽ���������
		setListener();
	}

	//ʵ�����ؼ�����
	private void findView() 
	{
		register=(Button) findViewById(R.id.user_register_operating);
		username=(EditText) findViewById(R.id.user_name_value);
		userpassword=(EditText) findViewById(R.id.user_password_value);
		login=(Button) findViewById(R.id.user_log_in_operating);
		//ʵ������ͼ
		intent_register=new Intent();
		intent_login=new Intent();
		userInfoService=new UserInfoService(getApplicationContext());
		imageview=(CircleImageView) findViewById(R.id.user_image);
		mApplication=(MusicApplication) getApplication();
		update_password=(Button) findViewById(R.id.user_password_forgetting_operating);
	}

	//Ϊ�ؼ���Ӽ���������
	private void setListener() 
	{
		//Ϊ��¼��ť��Ӽ�����
		login.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				//���е�¼����
				String uname=username.getText().toString();
				String upassword=userpassword.getText().toString();
				//�ж��û��Ƿ����
				if(userInfoService.user_existence(uname, upassword))
				{
					List<HashMap<String,Object>>results=userInfoService.doCheckin(uname, upassword);
					//Ϊ��ͼ��Ӷ���
					intent_login.setClass(UserMainActivity.this, MainActivity.class);
					List<HashMap<String,Bitmap>>result2=userInfoService.picture_capturing(uname, upassword);
			        //��װ����
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
					//������ʾ
					Toast.makeText(getApplicationContext(), "���˻�������!", 1000).show();
				}
			}
		});
		
		//Ϊ�������Ӵ����¼�
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
				//��ʾ��ͼ����
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
					Toast.makeText(getApplicationContext(), "�û�������Ϊ��!", 1000).show();
				}
			}
		});
		
		//�޸��������
		update_password.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//�ж��û�������
				if(username.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "�������û���!", 1000).show();
				}
				else
				{
					//������ʾ��
					AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this);
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("��ѡ���޸�����ķ�ʽ");
					final String[] sources = {"ͨ���ֻ���֤���һ�", "ͨ�������һ�"};
	                //����һ������ѡ��������
					builder.setSingleChoiceItems(sources, 0, new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							choice=which;
						}
					});
					//ȷ����ť����
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	if(choice==0)
							{
	                    		//������ͼ������ת
	                    		Intent intent_cellphone=new Intent(UserMainActivity.this,UpdateByCellphoneActivity.class);
	                    		intent_cellphone.putExtra("username", username.getText().toString());
	                    		startActivity(intent_cellphone);
							}
	                    	else if(choice==1)
	                    	{
	                    		//������ͼ������ת
	                    		Intent intent_question=new Intent(UserMainActivity.this,UpdateByQuestionActivity.class);
	                    		intent_question.putExtra("username", username.getText().toString());
	                    		startActivity(intent_question);
	                    	}
	                    }
	                });
					//ȡ����ť����
					builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	//�رնԻ���
	                        dialog.cancel();
	                    }
	                });
					//��ʾ��ʾ��
					builder.show();
				}
			}
		});
		
		//Ϊע�ᰴť��Ӽ�����
		register.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//Ϊ��ͼ��Ӷ���
				intent_register.setClass(UserMainActivity.this, RegisterActivity.class);
				//������ͼ
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
