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
//音乐播放服务类
public class MyService extends Service
{
	//获取sd卡根目录
    private String sdDir=Environment.getExternalStorageDirectory().getPath();
    //通用目录
    private String general=sdDir+File.separator+"CDMusic"+File.separator;
    //Mp3文件
    private String mp3path=general+"Music";
    //Lyrics文件
    private String lyrics=general+"Lyrics";
    //歌曲信息文件
    private String info=general+"Info";
    //图片文件
    private String picture=general+"Picture";
    //音乐播放路径
    private String mp3path2;
	
	//音乐播放路径
	private String music_path;
	//音乐播放类
	private MediaPlayer mediaplayer;
	//音乐播放标识
	private boolean flag=false;
	//定义线程池，保证只有一个线程在运行
    ExecutorService es=Executors.newSingleThreadExecutor();
    //定义状态
    private static final int IDLE=0;
    private static final int PAUSE=1;
    private static final int START=2;
    //当前播放器的状态
    private int currentstate=IDLE;
    
    //广播
    private Receive_Broadcast rBroadcast;
    private Mark_Broadcast mBroadcast;
    
    //创建全局对象
    private MusicApplication mApplication;
    //音乐实体列表
    private List<Mp3Info>mp3s;
    private List<Music>musics;
    //音乐播放位置
    private int currentlocation;
    //音乐播放模式
    private int status=1;

	//创建服务
	public void onCreate() 
	{
		//初始化媒体播放类
		mediaplayer=new MediaPlayer();
		registerBroadcast();
		//获取全局对象
		mApplication=(MusicApplication) this.getApplication();
		//获取相应的对象
		mp3s=mApplication.getMp3s();
		musics=mApplication.getMusics();
		//音乐播放结束处理
		mediaplayer.setOnCompletionListener(new OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				//获取音乐的播放模式
				status=mApplication.getStatus();
				//获取当前播放音乐的位置
				currentlocation=mApplication.getCurrent_location();
				//判断音乐播放列表是否为空
				if(mp3s.size()>0)
				{
					//单曲循环模式
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
	
	//开始时启动服务
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		//获取音乐播放的位置
		currentlocation=mApplication.getCurrent_location();
		//改变播放状态
		currentstate=IDLE;
		//进行播放操作
		play();
		return super.onStartCommand(intent, flags, startId);
	}
	
	//摧毁服务
	public void onDestroy() 
	{
		//撤销广播
		unregisterReceiver(mBroadcast);
		unregisterReceiver(rBroadcast);
		super.onDestroy();
	}

	//绑定类
	@Override
	public IBinder onBind(Intent intent) 
	{
		// TODO Auto-generated method stub
		return new MusicListener();
	}
	
	//解除绑定
	@Override
	public void unbindService(ServiceConnection conn) 
	{
		//撤出音乐播放类操作
		if(mediaplayer!=null)
		{
			mediaplayer.stop();
			flag=false;
			mediaplayer.release();
		}
		super.unbindService(conn);
	}
	
	//音乐播放
	private void start()
	{
		Log.d("location",String.valueOf(currentlocation));
		//获取当前播放音乐的路径
		mp3path2=mp3path
				+File.separator
				+mp3s.get(currentlocation).getMusicName();
		try 
		{
			//重置媒体播放器
			mediaplayer.reset();
			//设置资源
			mediaplayer.setDataSource(mp3path2);
			//准备阶段
			mediaplayer.prepare();
			//音乐开始播放
			mediaplayer.start();
			//传递到Activity 
			Pass_Time(mediaplayer.getDuration(),currentlocation);
			//改变当前状态
			currentstate=PAUSE;
			
			//更改进度条操作
			es.execute(new Runnable() 
			{
				@Override
				public void run() 
				{
					flag=true;
					//音乐播放位置
					int location;
					while(flag)
					{
						if(mediaplayer.getCurrentPosition()<mediaplayer.getDuration())
						{
							//获取音乐当前播放的位置
							location=mediaplayer.getCurrentPosition();
							//记录播放进度
							mApplication.play_proceed=location;
						}
						else
						{
							//音乐播放结束处理
							flag=false;
							location=0;
						}
						//传递到Activity中
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
	
	//传递时间操作
	public void Pass_Time(int totaltime,int location)
	{
		//定义意图
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.MUSIC_INITIALIZE");
		//填装数据
		String totaltime2=String.valueOf(totaltime);
		sendIntent.putExtra("totaltime", totaltime2);
		String location2=String.valueOf(location);
		sendIntent.putExtra("location", location2);
		//发送广播，将被Activity组件中的InitializeBroadcast接收到 
		sendBroadcast(sendIntent);  
	}
	
	//传递进度值操作
	public void Pass_Progress(int location, boolean flag)
	{
		//定义意图
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.MUSIC_PROGRESS_CHANGING");
		//填装数据
		sendIntent.putExtra("location", String.valueOf(location));
		sendIntent.putExtra("flag", String.valueOf(flag));
		//发送广播，将被Activity组件中的ChangeBroadcast接收到 
		sendBroadcast(sendIntent);  
	}
	
	//传递状态操作
    public void Pass_Status2()
	{
		//定义意图
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.STATUS2_CHANGING");
		//发送广播，将被Activity组件中的Pass_Status2Broadcast接收到 
		sendBroadcast(sendIntent);  
	}
    
    //传递状态操作
    public void Pass_View(int state)
	{
		//定义意图
		Intent sendIntent = new Intent(); 
		sendIntent.setAction("com.example.SimpleMusicPlayer.VIEW_CHANGING");
		//填装数据
		sendIntent.putExtra("currentstate", String.valueOf(state));
		//发送广播，将被Activity组件中的Pass_Status2Broadcast接收到 
		sendBroadcast(sendIntent);  
	}
	
	//定义进度广播
	public class Receive_Broadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			//获取进度
			int progress=Integer.valueOf(intent.getStringExtra("progress")).intValue();
			//根据进度条改变音乐播放的进度
			mediaplayer.seekTo(progress);
		}
	}
	
	//定义播放状态广播
	public class Mark_Broadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			//获取当前的播放状态
			currentstate=Integer.valueOf(intent.getStringExtra("currentstate")).intValue();
			//获取音乐播放的位置
			currentlocation=mApplication.getCurrent_location();
			//改变播放状态
			play();
		}
	}
	
	//注册广播操作
	public void registerBroadcast()
	{
		//注册初始化广播
		rBroadcast=new Receive_Broadcast();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("com.example.SimpleMusicPlayer.PROGRESS_CHANGING");
		registerReceiver(rBroadcast, filter1);
		
		//注册初始化
		mBroadcast=new Mark_Broadcast();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("com.example.SimpleMusicPlayer.MARK_CHANGING");
		registerReceiver(mBroadcast, filter2);
	}
	
	//音乐播放操作
	private void play()
	{
		//判断当前状态
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
	
	//音乐播放监听器
	public class MusicListener extends Binder implements IMusic
	{
		//重写前进方法
		public void moveon() 
		{
			currentlocation++;
			mApplication.setCurrent_location(currentlocation);
			Pass_Status2();
			start();
		}

		//重写播放方法
		public void playSong() 
		{
			//判断当前状态
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
