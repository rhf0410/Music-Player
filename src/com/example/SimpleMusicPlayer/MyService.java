package com.example.SimpleMusicPlayer;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.example.display.Music;
import com.example.file.Mp3Info;
//���ֲ��ŷ�����
public class MyService extends Service
{
	//��ȡsd����Ŀ¼
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //ͨ��Ŀ¼
    private String general=sdDir+File.separator+"CDMusic"+File.separator;
    //Mp3�ļ�
    private String mp3path=general+"Music";
    //Lyrics�ļ�
    private String lyrics=general+"Lyrics";
    //������Ϣ�ļ�
    private String info=general+"Info";
    //ͼƬ�ļ�
    private String picture=general+"Picture";
    //���ֲ���·��
    private String mp3path2;
	
	//���ֲ���·��
	private String music_path;
	//���ֲ�����
	private MediaPlayer mediaplayer;
	//���ֲ��ű�ʶ
	private boolean flag=false;
	//�����̳߳أ���ֻ֤��һ���߳�������
    ExecutorService es=Executors.newSingleThreadExecutor();
    //����״̬
    private static final int IDLE=0;
    private static final int PAUSE=1;
    private static final int START=2;
    //��ǰ��������״̬
    private int currentstate=IDLE;
    
    //�㲥
    private Receive_Broadcast rBroadcast;
    private Mark_Broadcast mBroadcast;
    
    //����ȫ�ֶ���
    private MusicApplication mApplication;
    //����ʵ���б�
    private List<Mp3Info>mp3s;
    private List<Music>musics;
    //���ֲ���λ��
    private int currentlocation;
    //���ֲ���ģʽ
    private int status=1;

	//��������
	public void onCreate() 
	{
		//��ʼ��ý�岥����
		mediaplayer=new MediaPlayer();
		registerBroadcast();
		//��ȡȫ�ֶ���
		mApplication=(MusicApplication) this.getApplication();
		//��ȡ��Ӧ�Ķ���
		mp3s=mApplication.getMp3s();
		musics=mApplication.getMusics();
		//���ֲ��Ž�������
		mediaplayer.setOnCompletionListener(new OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				//��ȡ���ֵĲ���ģʽ
				status=mApplication.getStatus();
				//��ȡ��ǰ�������ֵ�λ��
				currentlocation=mApplication.getCurrent_location();
				//�ж����ֲ����б��Ƿ�Ϊ��
				if(mp3s.size()>0)
				{
					//����ѭ��ģʽ
					if(status==1)
					{
						Pass_Status2();
						start();
					}
					else if(status==2)
					{
						if(currentlocation==mp3s.size()-1)
						{
							currentlocation=0;
							mApplication.setCurrent_location(currentlocation);
							Pass_Status2();
							start();
						}
						else
						{
							currentlocation++;
							mApplication.setCurrent_location(currentlocation);
							Pass_Status2();
							start();
						}
					}
					else if(status==3)
					{
						Random random=new Random();
						currentlocation=random.nextInt(mp3s.size());
						mApplication.setCurrent_location(currentlocation);
						Pass_Status2();
						start();
					}
					else if(status==4)
					{
						currentlocation++;
						mApplication.setCurrent_location(currentlocation);
						Pass_Status2();
						start();
					}
				}
			}
		});
		super.onCreate();
	}
	
	//��ʼʱ��������
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		//��ȡ���ֲ��ŵ�λ��
		currentlocation=mApplication.getCurrent_location();
		//�ı䲥��״̬
		currentstate=IDLE;
		//���в��Ų���
		play();
		return super.onStartCommand(intent, flags, startId);
	}
	
	//�ݻٷ���
	public void onDestroy() 
	{
		//�����㲥
		unregisterReceiver(mBroadcast);
		unregisterReceiver(rBroadcast);
		super.onDestroy();
	}

	//����
	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return new MusicListener();
	}
	
	//�����
	@Override
	public void unbindService(ServiceConnection conn) 
	{
		//�������ֲ��������
		if(mediaplayer!=null)
		{
			mediaplayer.stop();
			flag=false;
			mediaplayer.release();
		}
		super.unbindService(conn);
	}
	
	//���ֲ���
	private void start()
	{
		Log.d("location",String.valueOf(currentlocation));
		//��ȡ��ǰ�������ֵ�·��
		mp3path2=mp3path
				+File.separator
				+mp3s.get(currentlocation).getMusicName();
		try 
		{
			//����ý�岥����
			mediaplayer.reset();
			//������Դ
			mediaplayer.setDataSource(mp3path2);
			//׼���׶�
			mediaplayer.prepare();
			//���ֿ�ʼ����
			mediaplayer.start();
			//���ݵ�Activity 
			Pass_Time(mediaplayer.getDuration(),currentlocation);
			//�ı䵱ǰ״̬
			currentstate=PAUSE;
			
			//���Ľ���������
			es.execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					flag=true;
					//���ֲ���λ��
					int location;
					while(flag)
					{
						if(mediaplayer.getCurrentPosition()<mediaplayer.getDuration())
						{
							//��ȡ���ֵ�ǰ���ŵ�λ��
							location=mediaplayer.getCurrentPosition();
							//��¼���Ž���
							mApplication.play_proceed=location;
						}
						else
						{
							//���ֲ��Ž�������
							flag=false;
							location=0;
						}
						//���ݵ�Activity��
						Pass_Progress(location,flag);
					}
				}
			});
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//����ʱ�����
	public void Pass_Time(int totaltime,int location)
	{
		//������ͼ
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.MUSIC_INITIALIZE");
		//��װ����
		String totaltime2=String.valueOf(totaltime);
		sendIntent.putExtra("totaltime", totaltime2);
		String location2=String.valueOf(location);
		sendIntent.putExtra("location", location2);
		//���͹㲥������Activity����е�InitializeBroadcast���յ� 
		sendBroadcast(sendIntent);  
	}
	
	//���ݽ���ֵ����
	public void Pass_Progress(int location, boolean flag)
	{
		//������ͼ
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.MUSIC_PROGRESS_CHANGING");
		//��װ����
		sendIntent.putExtra("location", String.valueOf(location));
		sendIntent.putExtra("flag", String.valueOf(flag));
		//���͹㲥������Activity����е�ChangeBroadcast���յ� 
		sendBroadcast(sendIntent);  
	}
	
	//����״̬����
    public void Pass_Status2()
	{
		//������ͼ
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.STATUS2_CHANGING");
		//���͹㲥������Activity����е�Pass_Status2Broadcast���յ� 
		sendBroadcast(sendIntent);  
	}
    
    //����״̬����
    public void Pass_View(int state)
	{
		//������ͼ
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.VIEW_CHANGING");
		//��װ����
		sendIntent.putExtra("currentstate", String.valueOf(state));
		//���͹㲥������Activity����е�Pass_Status2Broadcast���յ� 
		sendBroadcast(sendIntent);  
	}
	
	//������ȹ㲥
	public class Receive_Broadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			//��ȡ����
			int progress=Integer.valueOf(intent.getStringExtra("progress")).intValue();
			//���ݽ������ı����ֲ��ŵĽ���
			mediaplayer.seekTo(progress);
		}
	}
	
	//���岥��״̬�㲥
	public class Mark_Broadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			//��ȡ��ǰ�Ĳ���״̬
			currentstate=Integer.valueOf(intent.getStringExtra("currentstate")).intValue();
			//��ȡ���ֲ��ŵ�λ��
			currentlocation=mApplication.getCurrent_location();
			//�ı䲥��״̬
			play();
		}
	}
	
	//ע��㲥����
	public void registerBroadcast()
	{
		//ע���ʼ���㲥
		rBroadcast=new Receive_Broadcast();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("com.example.SimpleMusicPlayer.PROGRESS_CHANGING");
		registerReceiver(rBroadcast, filter1);
		
		//ע���ʼ��
		mBroadcast=new Mark_Broadcast();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("com.example.SimpleMusicPlayer.MARK_CHANGING");
		registerReceiver(mBroadcast, filter2);
	}
	
	//���ֲ��Ų���
	private void play()
	{
		//�жϵ�ǰ״̬
		switch(currentstate)
		{
		    case IDLE:
			    start();
			    break;
		    case PAUSE:
	    	    mediaplayer.pause();
	    	    break;
	        case START:
	    	    mediaplayer.start();
	    	    break;
		}
	}
	
	//���ֲ��ż�����
	public class MusicListener extends Binder implements IMusic
	{
		//��дǰ������
		public void moveon() 
		{
			currentlocation++;
			mApplication.setCurrent_location(currentlocation);
			Pass_Status2();
			start();
		}

		//��д���ŷ���
		public void playSong() 
		{
			//�жϵ�ǰ״̬
			switch(currentstate)
			{
			    case PAUSE:
		    	    mediaplayer.pause();
		    	    Pass_View(currentstate);
		    	    currentstate=START;
		    	    break;
		        case START:
		    	    mediaplayer.start();
		    	    Pass_View(currentstate);
		    	    currentstate=PAUSE;
		    	    break;
			}
		}
	}
}
