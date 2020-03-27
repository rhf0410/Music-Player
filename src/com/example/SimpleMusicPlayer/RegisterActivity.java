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
	//�ؼ�
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
	
    //���ղ����ؼ�
	private Spinner sYear;
	private Spinner sMonth;
	private Spinner sDay;
	private Spinner questions;
	//����ֵ
	private ArrayList<String>DataYear;
	private ArrayList<String>DataMonth;
	private ArrayList<String>DataDay;
	//������
	private ArrayAdapter<String>adapterYear;
	private ArrayAdapter<String>adapterMonth;
	private ArrayAdapter<String>adapterDay;
	//��¼�µ�����ѡ��Ĳ���
	private int choice;
	//��¼��ͼƬ��·��
	private String pic_path;
	
	//������ڵĿؼ�
	private TextView icontent;
	
	//���ݿ������
	private UserInfoService userInfoService;
	//ȫ���жϱ�־
	private boolean ref=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//ʵ�����ؼ�
		findView();
		//��Ӽ�����
		setListener();
	}

	//ʵ�����ؼ�
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
		//Ϊ�ؼ��������
		setData();
		register=(Button) findViewById(R.id.register_login);
		icontent=(TextView) findViewById(R.id.register_question_input);
		userInfoService=new UserInfoService(getApplicationContext());
		user_name.setFocusable(true);
	}

	//��Ӽ�����
	private void setListener() 
	{
		//Ϊͷ����ӵ���¼�
		user_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������ʾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("��ѡ��ͼƬ��Դ");
				final String[] sources = {"���ֻ������ѡ��", "����"};
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
							//������ͼ������ת����Ƭ��
							Intent intent_ablums=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							//������ͼ
							startActivityForResult(intent_ablums, ActivityRequestCode.SHOW_MAP_DEPOT);
						}
                    	else if(choice==1)
                    	{
                    		//������ͼ������ת�����������
                    		Intent intent_camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    		//������ͼ
                    		startActivityForResult(intent_camera, ActivityRequestCode.REQUEST_CODE_CAMERA);
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
		});
		
		//ΪRadioGroup��Ӽ����¼�
		user_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				//��ȡ������ѡ�����ID
				int radioButtonId = group.getCheckedRadioButtonId();
				//����ID��ȡRadioButton��ʵ��
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				s_gender=rb.getText().toString();
			}
		});
		
		//Ϊ����������Ӽ�����
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
		
		//Ϊ���������ؼ���ӵ���¼�
		questions.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			//�����ɺ���¼�
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				final EditText ianswer=new EditText(getApplicationContext());
				ianswer.setBackgroundColor(Color.WHITE);
				ianswer.setTextColor(Color.BLACK);
				//������ʾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("�������");
				builder.setMessage(questions.getSelectedItem().toString());
				s_question=questions.getSelectedItem().toString();
				builder.setView(ianswer);
				builder.setPositiveButton("ȷ��" , new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						//��ȡ��
						s_answer=ianswer.getText().toString();
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

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		//Ϊע�ᰴť��Ӽ����¼�
		register.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//��ȡ����
				s_user_name=user_name.getText().toString();
				s_user_password=user_password_once_more.getText().toString();
				s_user_cellphone=user_cellphone.getText().toString();
				s_user_information=user_information.getText().toString();
				birthday=sYear.getSelectedItem().toString()+"-"
						+sMonth.getSelectedItem().toString()+"-"
						+sDay.getSelectedItem().toString();
				//������������ڱ���ͼƬ
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				s_user_image.compress(Bitmap.CompressFormat.PNG, 100, os);
				if(ref)
				{
					//�������ݿ����
					boolean flag=userInfoService.doReg(s_user_name, s_user_password, s_user_cellphone, s_gender, birthday, s_user_information, s_question, s_answer, os.toByteArray());
					if(flag)
					{
						Toast.makeText(getApplicationContext(), "ע��ɹ�", 1000).show();
						//������ͼ������ת����
						Intent intent_transfering3=new Intent(RegisterActivity.this,UserMainActivity.class);
						startActivity(intent_transfering3);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "ע��ʧ��", 1000).show();
					}
				}
			}
		});
		
		//�����û�����������������֤����
		user_name.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				//���������
				if(user_name.getText().toString().equals(""))
				{
					ref=false;
					//������ʾ��
					Toast.makeText(getApplicationContext(), "�û�������Ϊ��!", 1000).show();
				}
				else
				{
					ref=true;
				}
			}
		});
		
		//�����û�����������������֤����
		user_cellphone.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//������ʽ
				String telRegex = "[1][358]\\d{9}";
				//��֤�ֻ���
				String cellphone_number=user_cellphone.getText().toString();
				if(cellphone_number==null)
				{
					//������ʾ��
					Toast.makeText(getApplicationContext(), "�ֻ��Ų���Ϊ��!", 1000).show();
				}
				else
				{
					if(!cellphone_number.matches(telRegex))
					{
						ref=false;
						//������ʾ��
						Toast.makeText(getApplicationContext(), "�ֻ��Ÿ�ʽ����!", 1000).show();
					}
					else
					{
						ref=true;
					}
				}
			}
		});
		
		//����ȷ�������������������֤����
		user_password_once_more.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//��ȡ�״����������
				String psd1=user_password.getText().toString();
				//��ȡ�ٴ����������
				String psd2=user_password_once_more.getText().toString();
				if(psd1.equals(psd2))
				{
					ref=true;
				}
				else
				{
					ref=false;
					//������ʾ��
					Toast.makeText(getApplicationContext(), "������������벻ͬ!", 1000).show();
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
	
	//Ϊ�ؼ��������
	private void setData() 
	{
		//�趨���Ϊ��ǰ��ݵ�ǰ��20��
		Calendar cal=Calendar.getInstance();
		for(int i=0;i<=40;i++)
		{
			DataYear.add(""+(cal.get(Calendar.YEAR )-20+i));
		}
		adapterYear=new ArrayAdapter<String>(this, R.layout.item,DataYear);
		adapterYear.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sYear.setAdapter(adapterYear);
		sYear.setSelection(20);
		
		//12����
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

	//ʹ��Android�Դ�ͼ����ʾѡ�е�ͼƬ
	private void showYourPic(Intent data) 
	{
		//ѡ�е�ͼƬ
		Uri selectedImage=data.getData();
		String []filepathcolumn={MediaStore.Images.Media.DATA};
		//�α�
		Cursor cursor=getContentResolver().query(selectedImage, filepathcolumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex=cursor.getColumnIndex(filepathcolumn[0]);
		String picture_path=cursor.getString(columnIndex);
		cursor.close();
		if(picture_path.equals(""))
			return;
		//����ͼƬ, width, height ����ͬ��������ͼƬ
		BitmapFactory.Options options=new BitmapFactory.Options();
		//options ��Ϊtrueʱ���������bitmapû��ͼƬ��ֻ��һЩ�����������Ϣ�����ȽϿ죬��Ϊfalseʱ������ͼƬ
		options.inJustDecodeBounds=true;
		Bitmap bitmap=BitmapFactory.decodeFile(picture_path,options);
		int scale = (int)(options.outWidth/(float) 300); 
		if (scale <= 0) 
			scale=1;
		options.inSampleSize=scale;
		options.inJustDecodeBounds=false;
		bitmap=BitmapFactory.decodeFile(picture_path,options);
		pic_path=picture_path;
		
		//��ʾͼƬ
		user_image.setImageBitmap(bitmap);
		user_image.setVisibility(ImageView.VISIBLE);
		//�����ȡ��ͼƬ
		s_user_image=bitmap;
	}
	
	//������ʾ����������Ƭ
	private void showYourCameraPic(Intent data)
	{
		 Bundle bundle = data.getExtras();
		 //��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ����������ͼ
		 Bitmap bitmap = (Bitmap) bundle.get("data");
		 
		 //��ʾͼƬ
	     user_image.setImageBitmap(bitmap);
		 user_image.setVisibility(ImageView.VISIBLE);
		 //�����ȡ��ͼƬ
		 s_user_image=bitmap;
	}
}
