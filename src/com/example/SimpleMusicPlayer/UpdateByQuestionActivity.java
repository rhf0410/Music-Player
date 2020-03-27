package com.example.SimpleMusicPlayer;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.UserInfoService;

public class UpdateByQuestionActivity extends Activity 
{
	private String ds_question;
	private String ds_answer;
    //�ؼ�
	private TextView d_question;
	private EditText v_answer;
	private Button submit;
	private EditText password;
	private EditText password_again;
	private Button y_submit;
	private Button recede;
	private UserInfoService userinfoservice;
	private String username;
	private boolean flag=false;
	private boolean ref=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_by_question);
		findView();
		setListener();
	}

	//�ؼ�ʵ����
	private void findView() 
	{
		d_question=(TextView) findViewById(R.id.question_value);
		v_answer=(EditText) findViewById(R.id.answer_value);
		submit=(Button) findViewById(R.id.submit);
		password=(EditText) findViewById(R.id.password_value);
		password_again=(EditText) findViewById(R.id.password_value_again);
		y_submit=(Button) findViewById(R.id.sure_to_update);
		recede=(Button) findViewById(R.id.recede_after_updating);
		userinfoservice=new UserInfoService(UpdateByQuestionActivity.this);
		password.setEnabled(false);
		password_again.setEnabled(false);
	}

	//���ü�����
	private void setListener() 
	{
		//�ύ��ť����
		submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(v_answer.getText().toString().equals(""))
				{
					Toast.makeText(getApplicationContext(), "�𰸲���Ϊ��", 1000).show();
				}
				else
				{
					String benchmark=v_answer.getText().toString();
					if(benchmark.equals(ds_answer))
					{
						Toast.makeText(getApplicationContext(), "����������", 1000).show();
						password.setEnabled(true);
						password_again.setEnabled(true);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "����Ĵ𰸲���ȷ", 1000).show();
					}
				}
			}
		});
		
		/*//����ȷ�������������������֤����
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
		});*/
		
		//����ȷ�������������������֤����
		password_again.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				//��ȡ�״����������
				String psd1=password.getText().toString();
				//��ȡ�ٴ����������
				String psd2=password_again.getText().toString();
				if(psd1.equals(""))
				{
					//������ʾ��
					Toast.makeText(getApplicationContext(), "�������Ϊ��!", 1000).show();
				}
				else
				{
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
			}
		});
		
		//�����ύ��ť����
		y_submit.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				v_answer.setSelection(0);
				if(ref)
				{
					//�޸��������
					String password=password_again.getText().toString();
					boolean flag1=userinfoservice.updatepassword(password, username);
					if(flag1)
					{
						Toast.makeText(getApplicationContext(), "�޸�����ɹ�", 1000).show();
						//������ͼ������ת
						Intent intent_transfering=new Intent(UpdateByQuestionActivity.this,UserMainActivity.class);
						startActivity(intent_transfering);
						finish();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "�޸�����ʧ��", 1000).show();
					}
				}
			}
		});
		
		//���˰�ť
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
		getMenuInflater().inflate(R.menu.update_by_question, menu);
		return true;
	}

	@Override
	protected void onStart() 
	{
		//��ȡ��ͼ
		Intent get_intent=getIntent();
		username=get_intent.getStringExtra("username");
		//���ݿ��������ȡ�������
		List<HashMap<String,Object>>data=this.userinfoservice.question_and_answer_finding(username);
		ds_question=data.get(0).get("user_question").toString();
		ds_answer=data.get(0).get("user_answer").toString();
		d_question.setText(ds_question);
		super.onStart();
	}
}
