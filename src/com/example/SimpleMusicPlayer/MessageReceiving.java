package com.example.SimpleMusicPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MessageReceiving extends BroadcastReceiver 
{
	private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(strRes.equals(intent.getAction()))
		{
			StringBuilder sb = new StringBuilder();
			Bundle bundle = intent.getExtras();
			if(bundle!=null)
			{
				Object[] pdus = (Object[])bundle.get("pdus");
				SmsMessage[] msg = new SmsMessage[pdus.length];
				for(int i = 0 ;i<pdus.length;i++)
				{
					msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				}
				
				for(SmsMessage curMsg:msg)
				{
					sb.append("You got the message From:¡¾");
					sb.append(curMsg.getDisplayOriginatingAddress());
					sb.append("¡¿Content£º");
					sb.append(curMsg.getDisplayMessageBody());
				}
    			Toast.makeText(context, "ÊÕµ½¶ÌÐÅ!" + sb.toString(),Toast.LENGTH_SHORT).show();		
			}
		}
	}
}
