package com.example.SimpleMusicPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.circleimageview.CircleImageView;

public class PersonInformationActivity extends Activity 
{
    //�ؼ�
	private CircleImageView user_image;
	private TextView user_name;
	private TextView user_cellphone;
	private TextView user_gender;
	private TextView user_birthday;
	private TextView user_information;
	private Button update_information;
	private Button secede_login;
	private MusicApplication musicApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_information);
		findView();
		setListener();
	}

	//�ؼ�ʵ����
	private void findView() 
	{
		user_image=(CircleImageView) findViewById(R.id.display_user_image);
		user_name=(TextView) findViewById(R.id.display_register_username_value);
		user_cellphone=(TextView) findViewById(R.id.display_register_cellphone_value);
	    user_gender=(TextView) findViewById(R.id.display_register_gender_value);
	    user_birthday=(TextView) findViewById(R.id.display_register_birthday_value);
	    user_information=(TextView) findViewById(R.id.display_register_personal_information_value);
	    update_information=(Button) findViewById(R.id.display_login);
	    secede_login=(Button) findViewById(R.id.display_secede_login);
	    musicApplication=(MusicApplication) getApplication();
	}

	//���ü�����
	private void setListener() 
	{
		//�˳���¼����
		secede_login.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				//������ʾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(PersonInformationActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("��ʾ");
				builder.setMessage("�Ƿ�Ҫ�˳���¼!");
				builder.setPositiveButton("ȷ��" , new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//����û�����
						musicApplication.flag_login=false;
						musicApplication.bundle.clear();
						//������ͼ������ת
						Intent intent_main=new Intent();
						intent_main.setClass(getApplicationContext(), MainActivity.class);
						startActivity(intent_main);
					}
					
				});  
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				});
				builder.show();
			}
		});
		
		//�޸���Ϣ��ť
		update_information.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				//������ͼ������ת
				Intent intent_main=new Intent();
				intent_main.setClass(getApplicationContext(), UpdateInformationActivity.class);
				startActivity(intent_main);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_information, menu);
		return true;
	}

	@Override
	protected void onStart() 
	{
		//��ʾ��Ϣ
		user_image.setImageBitmap(musicApplication.userimage);
		user_name.setText(musicApplication.bundle.getString("user_name"));
		user_cellphone.setText(musicApplication.bundle.getString("user_cellphone"));
		user_gender.setText(musicApplication.bundle.getString("user_gender"));
		user_birthday.setText(musicApplication.bundle.getString("user_birthday"));
		user_information.setText(musicApplication.bundle.getString("user_information"));
		super.onStart();
	}
}
