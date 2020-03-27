package com.example.SimpleMusicPlayer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.circleimageview.CircleImageView;
import com.example.database.UserInfoService;

public class UpdateInformationActivity extends Activity 
{
	//控件
    private CircleImageView update_image;
    private Bitmap s_user_image;
	private EditText update_name;
	private String s_user_name;
	private EditText update_cellphone;
	private String s_user_cellphone;
	private RadioGroup update_gender;
	private RadioButton male;
	private RadioButton female;
	private String s_gender;
	private EditText update_information;
	private String s_user_information;
	private String birthday;
	private Button update;
    private Button recede;
		
	//生日操作控件
	private Spinner sYear;
	private Spinner sMonth;
	private Spinner sDay;
	
	//属性值
	private ArrayList<String>DataYear;
	private ArrayList<String>DataMonth;
	private ArrayList<String>DataDay;
	
	//适配器
	private ArrayAdapter<String>adapterYear;
	private ArrayAdapter<String>adapterMonth;
	private ArrayAdapter<String>adapterDay;
	
	//数据库服务类
	private UserInfoService userInfoService;
	private MusicApplication mApplication;
	private int choice;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_information);
		findView();
		setListener();
	}

	//控件实例化
	private void findView() 
	{
		update_image=(CircleImageView) findViewById(R.id.update_user_image);
		update_name=(EditText) findViewById(R.id.update_register_username_value);
		update_cellphone=(EditText) findViewById(R.id.update_register_cellphone_value);
		update_gender=(RadioGroup) findViewById(R.id.update_gender_group);
		male=(RadioButton) findViewById(R.id.update_gender_male);
		female=(RadioButton) findViewById(R.id.update_gender_female);
		update_information=(EditText) findViewById(R.id.update_personal_information_value);
		update=(Button) findViewById(R.id.update_login);
		recede=(Button) findViewById(R.id.update_recede);
		sYear=(Spinner) findViewById(R.id.update_birthday_year);
		sMonth=(Spinner) findViewById(R.id.update_birthday_month);
		sDay=(Spinner) findViewById(R.id.update_birthday_day);
		DataYear=new ArrayList<String>();
		DataMonth=new ArrayList<String>();
		DataDay=new ArrayList<String>();
		mApplication=(MusicApplication) getApplication();
		userInfoService=new UserInfoService(getApplicationContext());
		//为控件添加数据
		setData();
		//初始化数据
		InitializingData();
	}
	
	//设置监听器
	private void setListener() 
	{
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
		
		//为头像添加点击事件
		update_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//弹出提示框
				AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInformationActivity.this);
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
		update_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				//获取变更后的选中项的ID
				int radioButtonId = group.getCheckedRadioButtonId();
				//根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				s_gender=rb.getText().toString();
				mApplication.bundle.putString("user_gender", s_gender);
			}
		});
		
		//更新按钮操作
		update.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//获取数据
				s_user_image=mApplication.userimage;
				s_user_name=update_name.getText().toString();
				mApplication.bundle.putString("user_name", s_user_name);
				s_user_cellphone=update_cellphone.getText().toString();
				mApplication.bundle.putString("user_cellphone", s_user_cellphone);
				s_user_information=update_information.getText().toString();
				mApplication.bundle.putString("user_information", s_user_information);
				birthday=sYear.getSelectedItem().toString()+"-"
						+sMonth.getSelectedItem().toString()+"-"
						+sDay.getSelectedItem().toString();
				mApplication.bundle.putString("user_birthday", birthday);
				//定义输出流用于保存图片
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				s_user_image.compress(Bitmap.CompressFormat.PNG, 100, os);
				if(male.isChecked())
					s_gender="男";
				if(female.isChecked())
					s_gender="女";
				mApplication.bundle.putString("user_gender", s_gender);
				//更改数据库数据的操作
				boolean flag=false;
				flag=userInfoService.updateUserInformation(s_user_name, s_user_cellphone, s_gender, birthday, s_user_information, os.toByteArray());
				if(flag)
				{
					Toast.makeText(getApplicationContext(), "更改数据操作成功!", 1000).show();
					//设置意图，进行跳转操作
					Intent intent_transfering4=new Intent(UpdateInformationActivity.this,PersonInformationActivity.class);
					startActivity(intent_transfering4);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "更改数据操作失败!", 1000).show();
				}
			}
		});
		
		//为退出按钮添加时间
		recede.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_information, menu);
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
	
	//初始化数据
	private void InitializingData() 
	{
		update_name.setText(mApplication.bundle.getString("user_name"));
		update_cellphone.setText(mApplication.bundle.getString("user_cellphone"));
		update_image.setImageBitmap(mApplication.userimage);
		update_information.setText(mApplication.bundle.getString("user_information"));
		if(mApplication.bundle.getString("user_gender").equals("男"))
		{
			male.setChecked(true);
		}
		else
		{
			female.setChecked(true);
		}
		String []data_section=mApplication.bundle.getString("user_birthday").split("-");
		//设定年份为当前年份的前后20年
		Calendar cal=Calendar.getInstance();
		sYear.setSelection(Integer.valueOf(data_section[0])-cal.get(Calendar.YEAR )+20);
		sMonth.setSelection(Integer.valueOf(data_section[1])-1);
		sDay.setSelection(Integer.valueOf(data_section[2])-1);
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
			
		//显示图片
		update_image.setImageBitmap(bitmap);
		update_image.setVisibility(ImageView.VISIBLE);
		mApplication.userimage=bitmap;
	}
		
	//用于显示相机拍摄的照片
	private void showYourCameraPic(Intent data)
	{
		Bundle bundle = data.getExtras();
		//获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
		Bitmap bitmap = (Bitmap) bundle.get("data");
			 
		//显示图片
		update_image.setImageBitmap(bitmap);
		update_image.setVisibility(ImageView.VISIBLE);
		mApplication.userimage=bitmap;
	}
}
