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
	// �ֻ���
	private String cellphone;
	// �ؼ�
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

	// ʵ�����ؼ�
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

	// ���ü�����
	private void setListener() 
	{
		//Ϊ���Ͷ��Ű�ť����¼�
		cellphone_submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				random_data=RandomNumber();
				String cellphone=cellphone_number.getText().toString();
				String content="���ã�����ֻ���֤��Ϊ"+random_data+",�����������Ĳ�����";
				if (validate(cellphone, content))
				{
					sendSMS(cellphone, content);
				}
			}
		});
		
		//Ϊȷ�ϰ�ť����¼�
		verification.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(verifu_number.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "��֤�벻��Ϊ��", 1000).show();
				}
				else
				{
					String benchmark=verifu_number.getText().toString();
					String benchmark2=String.valueOf(random_data);
					if(benchmark.equals(benchmark2))
					{
						Toast.makeText(getApplicationContext(), "����������", 1000).show();
						password.setEnabled(true);
						password_again.setEnabled(true);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "��֤�벻��ȷ", 1000).show();
					}
				}
			}
		});
		
		//����ȷ�������������������֤����
		password_again.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) 
			{
				//��ȡ�״����������
				String psd1=password.getText().toString();
				//��ȡ�ٴ����������
				String psd2=password_again.getText().toString();
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
		
		//Ϊ�ύ��ť����¼�
		submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				verifu_number.setSelection(0);
				if(ref)
				{
					//�޸��������
					String password=password_again.getText().toString();
					boolean flag1=userinfoservice.updatepassword(password, username);
					if(flag1)
					{
						Toast.makeText(getApplicationContext(), "�޸�����ɹ�", 1000).show();
						//������ͼ������ת
						Intent intent_transfering2=new Intent(UpdateByCellphoneActivity.this,UserMainActivity.class);
						startActivity(intent_transfering2);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "�޸�����ʧ��", 1000).show();
					}
				}
			}
		});
		
		// Ϊ�˳���ť��Ӽ�����
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
		// ��ȡ��ͼ
		Intent get_intent = getIntent();
		username = get_intent.getStringExtra("username");
		// ���ݿ��������ȡ�������
		List<HashMap<String, Object>> data = this.userinfoservice
				.query_cellphone_operating(username);
		cellphone = data.get(0).get("user_cellphone").toString();
		cellphone_number.setText(cellphone);
		super.onStart();
	}
	
	//�������������
	private int RandomNumber()
	{
		int data;
		Random new_random=new Random();
		data=new_random.nextInt(9999-1000+1)+1000;
		return data;
	}
	
	//���Ͷ��Ų���
    private void sendSMS(String phoneNumber, String message) 
    {
    	
    	//��ӷ�����ͼ�Ĳ���
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,0);
		//��ӽ�����ͼ�Ĳ���
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
    	Toast.makeText(UpdateByCellphoneActivity.this, "���ͳɹ�!", Toast.LENGTH_LONG).show();
    	
    	//ע��㲥
    	registerReceiver(new BroadcastReceiver()
    	{
    		@Override
    		public void onReceive(Context _context,Intent _intent)
    		{
    			switch(getResultCode())
    			{
    				case Activity.RESULT_OK:
    					Toast.makeText(getBaseContext(), "���ŷ��ͳɹ�!",Toast.LENGTH_SHORT).show();
    					break;
    				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    					Toast.makeText(getBaseContext(), "���ŷ���ʧ��!",Toast.LENGTH_SHORT).show();
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
	
	//��֤�ֻ��źͶ������ݲ���
	private boolean validate(String telNo, String content)
	{
		if((null==telNo)||("".equals(telNo.trim())))
		{
    		Toast.makeText(this, "������绰����!",Toast.LENGTH_LONG).show();
    		return false;
    	}
    	if(!checkTelNo(telNo))
    	{
    		Toast.makeText(this, "��������ȷ�ĵ绰�����ʽ!",Toast.LENGTH_LONG).show();
    		return false;
    	}
		return true;
	}

	//��֤�绰�����ʽ����
	private boolean checkTelNo(String telNo) 
	{
		String reg ="^0{0,1}(13[0-9]|15[0-9])[0-9]{8}$"; 
    	return telNo.matches(reg);
	}
}
