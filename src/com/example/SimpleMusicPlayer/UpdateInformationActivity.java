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
	//�ؼ�
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
		
	//���ղ����ؼ�
	private Spinner sYear;
	private Spinner sMonth;
	private Spinner sDay;
	
	//����ֵ
	private ArrayList<String>DataYear;
	private ArrayList<String>DataMonth;
	private ArrayList<String>DataDay;
	
	//������
	private ArrayAdapter<String>adapterYear;
	private ArrayAdapter<String>adapterMonth;
	private ArrayAdapter<String>adapterDay;
	
	//���ݿ������
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

	//�ؼ�ʵ����
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
		//Ϊ�ؼ��������
		setData();
		//��ʼ������
		InitializingData();
	}
	
	//���ü�����
	private void setListener() 
	{
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
		
		//Ϊͷ����ӵ���¼�
		update_image.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������ʾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInformationActivity.this);
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
		update_gender.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) 
			{
				//��ȡ������ѡ�����ID
				int radioButtonId = group.getCheckedRadioButtonId();
				//����ID��ȡRadioButton��ʵ��
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				s_gender=rb.getText().toString();
				mApplication.bundle.putString("user_gender", s_gender);
			}
		});
		
		//���°�ť����
		update.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//��ȡ����
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
				//������������ڱ���ͼƬ
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				s_user_image.compress(Bitmap.CompressFormat.PNG, 100, os);
				if(male.isChecked())
					s_gender="��";
				if(female.isChecked())
					s_gender="Ů";
				mApplication.bundle.putString("user_gender", s_gender);
				//�������ݿ����ݵĲ���
				boolean flag=false;
				flag=userInfoService.updateUserInformation(s_user_name, s_user_cellphone, s_gender, birthday, s_user_information, os.toByteArray());
				if(flag)
				{
					Toast.makeText(getApplicationContext(), "�������ݲ����ɹ�!", 1000).show();
					//������ͼ��������ת����
					Intent intent_transfering4=new Intent(UpdateInformationActivity.this,PersonInformationActivity.class);
					startActivity(intent_transfering4);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "�������ݲ���ʧ��!", 1000).show();
				}
			}
		});
		
		//Ϊ�˳���ť���ʱ��
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
	
	//��ʼ������
	private void InitializingData() 
	{
		update_name.setText(mApplication.bundle.getString("user_name"));
		update_cellphone.setText(mApplication.bundle.getString("user_cellphone"));
		update_image.setImageBitmap(mApplication.userimage);
		update_information.setText(mApplication.bundle.getString("user_information"));
		if(mApplication.bundle.getString("user_gender").equals("��"))
		{
			male.setChecked(true);
		}
		else
		{
			female.setChecked(true);
		}
		String []data_section=mApplication.bundle.getString("user_birthday").split("-");
		//�趨���Ϊ��ǰ��ݵ�ǰ��20��
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
			
		//��ʾͼƬ
		update_image.setImageBitmap(bitmap);
		update_image.setVisibility(ImageView.VISIBLE);
		mApplication.userimage=bitmap;
	}
		
	//������ʾ����������Ƭ
	private void showYourCameraPic(Intent data)
	{
		Bundle bundle = data.getExtras();
		//��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ����������ͼ
		Bitmap bitmap = (Bitmap) bundle.get("data");
			 
		//��ʾͼƬ
		update_image.setImageBitmap(bitmap);
		update_image.setVisibility(ImageView.VISIBLE);
		mApplication.userimage=bitmap;
	}
}
