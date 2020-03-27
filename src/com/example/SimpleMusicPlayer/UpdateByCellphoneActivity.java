package com.example.SimpleMusicPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.UserInfoService;

public class UpdateByCellphoneActivity extends Activity 
{
	// 手机号
	private String cellphone;
	// 控件
	private TextView cellphone_number;
	private EditText verifu_number;
	private Button cellphone_submit;
	private Button verification;
	private EditText password;
	private EditText password_again;
	private Button submit;
	private Button recede;
	private UserInfoService userinfoservice;
	private String username;
	private boolean flag = false;
	private boolean ref = false;
	private String SENT_SMS_ACTION="SENT_SMS_ACTION";  
	private String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
	private int random_data;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_by_cellphone);
		findView();
		setListener();
	}

	// 实例化控件
	private void findView() 
	{
		cellphone_number = (TextView) findViewById(R.id.cellphone_value);
		verifu_number = (EditText) findViewById(R.id.number_value);
		cellphone_submit = (Button) findViewById(R.id.cellphone_submit);
		password = (EditText) findViewById(R.id.cellphone_password_value);
		password_again = (EditText) findViewById(R.id.cellphone_password_value_again);
		submit = (Button) findViewById(R.id.cellphone_sure_to_update);
		recede = (Button) findViewById(R.id.cellphone_recede_after_updating);
		verification = (Button) findViewById(R.id.cellphone_verification);
		userinfoservice = new UserInfoService(UpdateByCellphoneActivity.this);
		password.setEnabled(false);
		password_again.setEnabled(false);
	}

	// 设置监听器
	private void setListener() 
	{
		//为发送短信按钮添加事件
		cellphone_submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				random_data=RandomNumber();
				String cellphone=cellphone_number.getText().toString();
				String content="您好，你的手机验证码为"+random_data+",请进行密码更改操作。";
				if (validate(cellphone, content))
				{
					sendSMS(cellphone, content);
				}
			}
		});
		
		//为确认按钮添加事件
		verification.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(verifu_number.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "验证码不能为空", 1000).show();
				}
				else
				{
					String benchmark=verifu_number.getText().toString();
					String benchmark2=String.valueOf(random_data);
					if(benchmark.equals(benchmark2))
					{
						Toast.makeText(getApplicationContext(), "请输入密码", 1000).show();
						password.setEnabled(true);
						password_again.setEnabled(true);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "验证码不正确", 1000).show();
					}
				}
			}
		});
		
		//设置确认密码监听器，用于验证操作
		password_again.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//获取首次输入的密码
				String psd1=password.getText().toString();
				//获取再次输入的密码
				String psd2=password_again.getText().toString();
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
		
		//为提交按钮添加事件
		submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				verifu_number.setSelection(0);
				if(ref)
				{
					//修改密码操作
					String password=password_again.getText().toString();
					boolean flag1=userinfoservice.updatepassword(password, username);
					if(flag1)
					{
						Toast.makeText(getApplicationContext(), "修改密码成功", 1000).show();
						//设置意图进行跳转
						Intent intent_transfering2=new Intent(UpdateByCellphoneActivity.this,UserMainActivity.class);
						startActivity(intent_transfering2);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "修改密码失败", 1000).show();
					}
				}
			}
		});
		
		// 为退出按钮添加监听器
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
		getMenuInflater().inflate(R.menu.update_by_cellphone, menu);
		return true;
	}

	@Override
	protected void onStart() 
	{
		// 获取意图
		Intent get_intent = getIntent();
		username = get_intent.getStringExtra("username");
		// 数据库操作，提取问题与答案
		List<HashMap<String, Object>> data = this.userinfoservice
				.query_cellphone_operating(username);
		cellphone = data.get(0).get("user_cellphone").toString();
		cellphone_number.setText(cellphone);
		super.onStart();
	}
	
	//生成随机数函数
	private int RandomNumber()
	{
		int data;
		Random new_random=new Random();
		data=new_random.nextInt(9999-1000+1)+1000;
		return data;
	}
	
	//发送短信操作
    private void sendSMS(String phoneNumber, String message) 
    {
    	
    	//添加发送意图的参数
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,0);
		//添加接收意图的参数
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,deliverIntent, 0);

    	SmsManager sms = SmsManager.getDefault();
    	if (message.length() > 70) 
    	{
    		List<String> msgs = sms.divideMessage(message);
    		for (String msg : msgs) 
    		{
    			sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
    		}
    	} 
    	else 
    	{
    		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
    	}
    	Toast.makeText(UpdateByCellphoneActivity.this, "发送成功!", Toast.LENGTH_LONG).show();
    	
    	//注册广播
    	registerReceiver(new BroadcastReceiver()
    	{
    		@Override
    		public void onReceive(Context _context,Intent _intent)
    		{
    			switch(getResultCode())
    			{
    				case Activity.RESULT_OK:
    					Toast.makeText(getBaseContext(), "短信发送成功!",Toast.LENGTH_SHORT).show();
    					break;
    				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    					Toast.makeText(getBaseContext(), "短信发送失败!",Toast.LENGTH_SHORT).show();
    					break;
    				case SmsManager.RESULT_ERROR_RADIO_OFF:
    					Toast.makeText(getBaseContext(),"SMS radio off failure actions",Toast.LENGTH_SHORT).show();
    					break;
    				case SmsManager.RESULT_ERROR_NULL_PDU:
    					Toast.makeText(getBaseContext(), "SMS null PDU failure actions",Toast.LENGTH_SHORT).show();
    					break;
    			}
    		}
    	},
    	new IntentFilter(SENT_SMS_ACTION));
    	registerReceiver(new BroadcastReceiver()
    	{
    		@Override
    		public void onReceive(Context _context,Intent _intent)
    		{
    			Toast.makeText(getBaseContext(), "SMS delivered actions",Toast.LENGTH_SHORT).show();				
    		}
    	},
    	new IntentFilter(DELIVERED_SMS_ACTION));
    }
	
	//验证手机号和短信内容操作
	private boolean validate(String telNo, String content)
	{
		if((null==telNo)||("".equals(telNo.trim())))
		{
    		Toast.makeText(this, "请输入电话号码!",Toast.LENGTH_LONG).show();
    		return false;
    	}
    	if(!checkTelNo(telNo))
    	{
    		Toast.makeText(this, "请输入正确的电话号码格式!",Toast.LENGTH_LONG).show();
    		return false;
    	}
		return true;
	}

	//验证电话号码格式操作
	private boolean checkTelNo(String telNo) 
	{
		String reg ="^0{0,1}(13[0-9]|15[0-9])[0-9]{8}$"; 
    	return telNo.matches(reg);
	}
}
