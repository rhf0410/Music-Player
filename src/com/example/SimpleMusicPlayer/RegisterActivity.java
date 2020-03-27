package com.example.SimpleMusicPlayer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.circleimageview.CircleImageView;
import com.example.database.UserInfoService;

public class RegisterActivity extends Activity 
{
	//控件
	private CircleImageView user_image;
	private Bitmap s_user_image;
	private EditText user_name;
	private String s_user_name;
	private EditText user_password;
	private EditText user_password_once_more;
	private String s_user_password;
	private EditText user_cellphone;
	private String s_user_cellphone;
	private RadioGroup user_gender;
	private RadioButton male;
	private RadioButton female;
	private String s_gender;
	private String birthday;
	private EditText user_information;
	private String s_user_information;
	private String s_answer;
	private Button register;
	private String s_question;
	
    //生日操作控件
	private Spinner sYear;
	private Spinner sMonth;
	private Spinner sDay;
	private Spinner questions;
	//属性值
	private ArrayList<String>DataYear;
	private ArrayList<String>DataMonth;
	private ArrayList<String>DataDay;
	//适配器
	private ArrayAdapter<String>adapterYear;
	private ArrayAdapter<String>adapterMonth;
	private ArrayAdapter<String>adapterDay;
	//记录下弹框中选择的操作
	private int choice;
	//记录下图片的路径
	private String pic_path;
	
	//输入框内的控件
	private TextView icontent;
	
	//数据库服务类
	private UserInfoService userInfoService;
	//全局判断标志
	private boolean ref=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//实例化控件
		findView();
		//添加监听器
		setListener();
	}

	//实例化控件
	private void findView() 
	{
		user_image=(CircleImageView) findViewById(R.id.user_image);
		user_name=(EditText) findViewById(R.id.register_username_value);
		user_password=(EditText) findViewById(R.id.register_password_value);
		user_password_once_more=(EditText) findViewById(R.id.register_verify_password_value);
		user_cellphone=(EditText) findViewById(R.id.register_cellphone_value);
		user_information=(EditText) findViewById(R.id.register_personal_information_value);
		user_gender=(RadioGroup) findViewById(R.id.register_gender_group);
		male=(RadioButton) findViewById(R.id.register_gender_male);
		female=(RadioButton) findViewById(R.id.register_gender_female);
		sYear=(Spinner) findViewById(R.id.register_birthday_year);
		sMonth=(Spinner) findViewById(R.id.register_birthday_month);
		sDay=(Spinner) findViewById(R.id.register_birthday_day);
		questions=(Spinner) findViewById(R.id.register_question_value);
		questions.setSelection(0, false);
		DataYear=new ArrayList<String>();
		DataMonth=new ArrayList<String>();
		DataDay=new ArrayList<String>();
		//为控件添加数据
		setData();
		register=(Button) findViewById(R.id.register_login);
		icontent=(TextView) findViewById(R.id.register_question_input);
		userInfoService=new UserInfoService(getApplicationContext());
		user_name.setFocusable(true);
	}

	//添加监听器
	private void setListener() 
	{
		//为头像添加点击事件
		user_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//弹出提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("请选择图片来源");
				final String[] sources = {"从手机相册中选择", "拍照"};
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
							//定义意图用于跳转到照片集
							Intent intent_ablums=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							//启动意图
							startActivityForResult(intent_ablums, ActivityRequestCode.SHOW_MAP_DEPOT);
						}
                    	else if(choice==1)
                    	{
                    		//定义意图用于跳转到相机操作上
                    		Intent intent_camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    		//启动意图
                    		startActivityForResult(intent_camera, ActivityRequestCode.REQUEST_CODE_CAMERA);
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
		});
		
		//为RadioGroup添加监听事件
		user_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				//获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				//根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				s_gender=rb.getText().toString();
			}
		});
		
		//为月下拉器添加监听器
		sMonth.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				DataDay.clear();
				Calendar cal=Calendar.getInstance();
				cal.set(Calendar.YEAR, Integer.valueOf(sYear.getSelectedItem().toString()));
				cal.set(Calendar.MONTH, arg2);
				int day=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				for(int i=1;i<=day;i++)
				{
					DataDay.add(""+(i<10?"0"+i:i));
				}
				adapterDay.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		//为问题下拉控件添加点击事件
		questions.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			//点击完成后的事件
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				final EditText ianswer=new EditText(getApplicationContext());
				ianswer.setBackgroundColor(Color.WHITE);
				ianswer.setTextColor(Color.BLACK);
				//弹出提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("请输入答案");
				builder.setMessage(questions.getSelectedItem().toString());
				s_question=questions.getSelectedItem().toString();
				builder.setView(ianswer);
				builder.setPositiveButton("确定" , new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//获取答案
						s_answer=ianswer.getText().toString();
					}
					
				});  
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.cancel();
					}
				});
				builder.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		//为注册按钮添加监听事件
		register.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//获取数据
				s_user_name=user_name.getText().toString();
				s_user_password=user_password_once_more.getText().toString();
				s_user_cellphone=user_cellphone.getText().toString();
				s_user_information=user_information.getText().toString();
				birthday=sYear.getSelectedItem().toString()+"-"
						+sMonth.getSelectedItem().toString()+"-"
						+sDay.getSelectedItem().toString();
				//定义输出流用于保存图片
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				s_user_image.compress(Bitmap.CompressFormat.PNG, 100, os);
				if(ref)
				{
					//插入数据库操作
					boolean flag=userInfoService.doReg(s_user_name, s_user_password, s_user_cellphone, s_gender, birthday, s_user_information, s_question, s_answer, os.toByteArray());
					if(flag)
					{
						Toast.makeText(getApplicationContext(), "注册成功", 1000).show();
						//设置意图进行跳转操作
						Intent intent_transfering3=new Intent(RegisterActivity.this,UserMainActivity.class);
						startActivity(intent_transfering3);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "注册失败", 1000).show();
					}
				}
			}
		});
		
		//设置用户名监听器，用于验证操作
		user_name.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				//弹出警告框
				if(user_name.getText().toString().equals(""))
				{
					ref=false;
					//弹出提示框
					Toast.makeText(getApplicationContext(), "用户名不能为空!", 1000).show();
				}
				else
				{
					ref=true;
				}
			}
		});
		
		//设置用户名监听器，用于验证操作
		user_cellphone.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//正则表达式
				String telRegex = "[1][358]\\d{9}";
				//验证手机号
				String cellphone_number=user_cellphone.getText().toString();
				if(cellphone_number==null)
				{
					//弹出提示框
					Toast.makeText(getApplicationContext(), "手机号不能为空!", 1000).show();
				}
				else
				{
					if(!cellphone_number.matches(telRegex))
					{
						ref=false;
						//弹出提示框
						Toast.makeText(getApplicationContext(), "手机号格式错误!", 1000).show();
					}
					else
					{
						ref=true;
					}
				}
			}
		});
		
		//设置确认密码监听器，用于验证操作
		user_password_once_more.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//获取首次输入的密码
				String psd1=user_password.getText().toString();
				//获取再次输入的密码
				String psd2=user_password_once_more.getText().toString();
				if(psd1.equals(psd2))
				{
					ref=true;
				}
				else
				{
					ref=false;
					//弹出提示框
					Toast.makeText(getApplicationContext(), "两次输入的密码不同!", 1000).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	//为控件添加数据
	private void setData() 
	{
		//设定年份为当前年份的前后20年
		Calendar cal=Calendar.getInstance();
		for(int i=0;i<=40;i++)
		{
			DataYear.add(""+(cal.get(Calendar.YEAR )-20+i));
		}
		adapterYear=new ArrayAdapter<String>(this, R.layout.item,DataYear);
		adapterYear.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sYear.setAdapter(adapterYear);
		sYear.setSelection(20);
		
		//12个月
		for(int i=1;i<=12;i++)
		{
			DataMonth.add(""+(i<10?"0"+i:i));
		}
		adapterMonth=new ArrayAdapter<String>(this, R.layout.item,DataMonth);
		adapterMonth.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sMonth.setAdapter(adapterMonth);
		
		adapterDay=new ArrayAdapter<String>(this,R.layout.item,DataDay);
		adapterDay.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sDay.setAdapter(adapterDay);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data!=null)
		{
			if(requestCode==ActivityRequestCode.SHOW_MAP_DEPOT&&resultCode==Activity.RESULT_OK)
			{
				showYourPic(data);
			}
			else if(requestCode==ActivityRequestCode.REQUEST_CODE_CAMERA&&resultCode==Activity.RESULT_OK)
			{
				showYourCameraPic(data);
			}
		}
	}

	//使用Android自带图库显示选中的图片
	private void showYourPic(Intent data) 
	{
		//选中的图片
		Uri selectedImage=data.getData();
		String []filepathcolumn={MediaStore.Images.Media.DATA};
		//游标
		Cursor cursor=getContentResolver().query(selectedImage, filepathcolumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex=cursor.getColumnIndex(filepathcolumn[0]);
		String picture_path=cursor.getString(columnIndex);
		cursor.close();
		if(picture_path.equals(""))
			return;
		//缩放图片, width, height 按相同比例缩放图片
		BitmapFactory.Options options=new BitmapFactory.Options();
		//options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
		options.inJustDecodeBounds=true;
		Bitmap bitmap=BitmapFactory.decodeFile(picture_path,options);
		int scale = (int)(options.outWidth/(float) 300); 
		if (scale <= 0) 
			scale=1;
		options.inSampleSize=scale;
		options.inJustDecodeBounds=false;
		bitmap=BitmapFactory.decodeFile(picture_path,options);
		pic_path=picture_path;
		
		//显示图片
		user_image.setImageBitmap(bitmap);
		user_image.setVisibility(ImageView.VISIBLE);
		//保存获取的图片
		s_user_image=bitmap;
	}
	
	//用于显示相机拍摄的照片
	private void showYourCameraPic(Intent data)
	{
		 Bundle bundle = data.getExtras();
		 //获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
		 Bitmap bitmap = (Bitmap) bundle.get("data");
		 
		 //显示图片
	     user_image.setImageBitmap(bitmap);
		 user_image.setVisibility(ImageView.VISIBLE);
		 //保存获取的图片
		 s_user_image=bitmap;
	}
}
