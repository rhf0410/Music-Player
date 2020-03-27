package com.example.SimpleMusicPlayer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Lyrics.LyricView;
import com.example.SimpleMusicPlayer.MyService.MusicListener;
import com.example.Xml.JavaXml;
import com.example.Xml.MusicModel;
import com.example.Xml.PullXml;
import com.example.circleimageview.BottomMenu;
import com.example.circleimageview.CircleImageView;
import com.example.database.UserInfoService;
import com.example.display.AlphaComparator;
import com.example.display.Artist;
import com.example.display.ArtistComparator;
import com.example.display.CharacterParser;
import com.example.display.LatestMusicAdapter;
import com.example.display.Music;
import com.example.display.MusicAdapter;
import com.example.display.PinyinComparator;
import com.example.display.SideBar;
import com.example.display.SideBar.OnTouchingLetterChangedListener;
import com.example.display.SortAdapter;
import com.example.file.Mp3Info;

public class MainActivity extends Activity 
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
    
	//歌曲信息
    private TextView song_info;
    //歌曲列表
    private TextView song_list;
    //歌词
    private TextView song_lyrics;
    //歌曲选择列表
    private ListView song_list_choice;
    private SideBar sideBar;
    private SideBar artist_sideBar;
    private SideBar song_by_artist_sideBar;
    //歌曲详细信息
    private TextView song_information_choice;
    //歌词显示
    private LyricView song_lyrics_choice;
    //播放歌曲
    private TextView song_name;
    //播放模式
    private ImageView play_mode;
    //播放开始
    private ImageView play_start;
    //播放前进
    private ImageView play_proceed;
    //播放后退
    private ImageView play_retrieve;
    //删除音乐
    private ImageView treasure_music;
    //音乐播放时间
    private TextView current_time;
    //音乐总的时间
    private TextView total_time;
    //播放进度条
    private SeekBar seekbar;
    //音乐适配器
    private MusicAdapter musicadapter;
    private LatestMusicAdapter lmusicadapter;
    private LatestMusicAdapter tmusicadapter;
    //判断是否进行删除操作
	private boolean flag2=false;
    
    //音乐实体列表
    private List<Mp3Info>mp3s;
    private List<Music>musics;
    
    //定义状态
    private static final int IDLE=0;
    private static final int PAUSE=1;
    private static final int START=2;
    private static final int CURR_TIME_VALUE=1;
    //当前播放器的状态
    private int currentstate=IDLE;
    
    //当前音乐播放索引
    private int currentindex=0;
    //控制进度条线程的标记
    private boolean flag=true;
    //音乐播放模式标记
    private int status=1;
    //歌词每行的间隔
    private int INTERVAL=45;
    
    //定义意图跳转到服务
    private Intent intent_music;
    
    //定义初始化广播
    private InitializeBroadcast iBroadcast;
    //定义广播
    private ChangeBroadcast cBroadcast;
    //定义广播
    private Status2_Broadcast s2Broadcast;
    //定义广播
    private ViewBroadcast vBroadcast;
    
    //定义全局变量
    private MusicApplication mApplication;
    
    //后台操作
    private RemoteViews contentview;
    //通知
    private Notification notification;
    //通知管理者
    private NotificationManager nm;
    //音乐播放操作接口
    private MyService.MusicListener musiclistener;
    //连接服务操作
    private ServiceConnection connection;
    
    //登录显示栏
    private RelativeLayout log_display;
    private CircleImageView cimage;
    private TextView cname;
    /**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<Music> SourceDateList;
	private List<Artist> SourceArtistList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private AlphaComparator alphaComparator;
	private ArtistComparator artistComparator;
	private FrameLayout song_display;
	private TextView dialog;
	private BottomMenu menuWindow;
	private ListView latest;
	private LinkedList<Music>latest_played;
	private FrameLayout artist_display;
	private TextView artist_dialog;
	private SortAdapter adapter;
	private ListView sortArtistView;
	private HashMap<String, ArrayList<Music>>artists_and_songs;
	private FrameLayout song_by_artist;
	private TextView song_by_artist_dialog;
	private MusicAdapter song_by_artist_adapter;
	private ListView song_by_artist_listview;
	private List<Music>song_by_artist_value;
	private ImageView treasure;
	private UserInfoService userinfoservice;
	private LinkedList<Music> treasureList;
	private ListView treasuredmusic;
	private JavaXml javaxml;
	private PullXml pullxml;
	private TextView search;
	private ImageView remark;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		setListener();
	}

	//控制UI界面
	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch(msg.what)
			{
			    case CURR_TIME_VALUE:
			    	//更新歌词列表视图
			    	song_lyrics_choice.invalidate();
			    	current_time.setText(msg.obj.toString());
			    	break;
			    default:
			        break;
			}
		};
	};
	
	//控件实例化
	private void findView() 
	{
		song_info=(TextView) findViewById(R.id.Song_information);
		song_list=(TextView) findViewById(R.id.Song_list);
		song_lyrics=(TextView) findViewById(R.id.Song_lyric);
		song_name=(TextView) findViewById(R.id.song_display);
		song_information_choice=(TextView)findViewById(R.id.song_information_choice);
	    song_lyrics_choice=(LyricView) findViewById(R.id.song_lyrics_choice);
		mp3s=new ArrayList<Mp3Info>();
	    musics=new ArrayList<Music>();
	    song_list_choice=(ListView) findViewById(R.id.song_list_choice);
	    play_mode=(ImageView) findViewById(R.id.play_mode);
	    play_start=(ImageView) findViewById(R.id.play_start);
	    play_proceed=(ImageView) findViewById(R.id.play_proceed);
	    play_retrieve=(ImageView) findViewById(R.id.play_retrive);
	    current_time=(TextView) findViewById(R.id.textView_curr_time);
	    total_time=(TextView) findViewById(R.id.textView_total_time);
	    seekbar=(SeekBar) findViewById(R.id.Progress_bar);
	    log_display=(RelativeLayout) findViewById(R.id.Simple_Search_Frame);
	    treasure=(ImageView) findViewById(R.id.play_song_treasured);
	    treasuredmusic=(ListView) findViewById(R.id.song_treasured);
	    userinfoservice=new UserInfoService(MainActivity.this);
	    //实例化意图
		intent_music=new Intent();
		intent_music.setAction("com.example.SimpleMusicPlayer.PLAY_SERVICE");
		//注册广播
		registerReceiver();
		
		//实例化Application对象
		mApplication=(MusicApplication) getApplication();
		mApplication.setCurrent_location(currentindex);
		mApplication.setStatus(status);
		//实例化视图
		contentview=new RemoteViews(getPackageName(),R.layout.notification_control);
		//初始化通知界面
		InitializeNotification();
		cimage=(CircleImageView) findViewById(R.id.circleImageView);
		cname=(TextView) findViewById(R.id.user_login);
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		alphaComparator=new AlphaComparator();
		artistComparator=new ArtistComparator();
		song_display=(FrameLayout) findViewById(R.id.song_list_display);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		artist_display=(FrameLayout) findViewById(R.id.artist_list_display);
		artist_dialog=(TextView) findViewById(R.id.artist_dialog);
		sortArtistView=(ListView) findViewById(R.id.artist_list_choice);
		artist_sideBar=(SideBar) findViewById(R.id.artist_sidrbar);
		artist_sideBar.setTextView(artist_dialog);
		song_by_artist=(FrameLayout) findViewById(R.id.song_by_artist_list_display);
		song_by_artist_dialog=(TextView) findViewById(R.id.by_artist_dialog);
		song_by_artist_listview=(ListView) findViewById(R.id.song_by_artist_list_choice);
		song_by_artist_sideBar=(SideBar) findViewById(R.id.by_artist_sidrbar);
		artists_and_songs=new HashMap<String, ArrayList<Music>>();
		SourceArtistList=new LinkedList<Artist>();
		song_by_artist_value=new ArrayList<Music>();
		//进行初始化
	    initialise();
	    initialise2();
	    fillData();
		// 根据a-z进行排序源数据
		Collections.sort(musics, pinyinComparator);
		Collections.sort(mp3s, alphaComparator);
		Collections.sort(SourceArtistList, artistComparator);
		mApplication.setMusics(musics);
		mApplication.setMp3s(mp3s);
		musicadapter=new MusicAdapter(MainActivity.this, R.layout.song_list, musics);
		song_list_choice.setAdapter(musicadapter);
	    musicadapter.notifyDataSetChanged();
	    adapter=new SortAdapter(MainActivity.this, SourceArtistList);
	    sortArtistView.setAdapter(adapter);
	    connection=new ServiceConnection() 
	    {
			@Override
			public void onServiceDisconnected(ComponentName name) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				musiclistener=(MusicListener) service;
				mApplication.music=musiclistener;
			}
		};
		//绑定服务
		bindService(intent_music, connection, BIND_AUTO_CREATE);
		latest=(ListView) findViewById(R.id.song_played_recently);
		latest_played=new LinkedList<Music>();
		lmusicadapter=new LatestMusicAdapter(MainActivity.this, R.layout.latestmusic, latest_played);
		latest.setAdapter(lmusicadapter);
		lmusicadapter.notifyDataSetChanged();
		treasureList=new LinkedList<Music>();
		tmusicadapter=new LatestMusicAdapter(MainActivity.this,R.layout.latestmusic,treasureList);
		treasuredmusic.setAdapter(tmusicadapter);
		tmusicadapter.notifyDataSetChanged();
		pullxml=new PullXml();
		search=(TextView) findViewById(R.id.Search_Song);
		remark=(ImageView) findViewById(R.id.play_song_remark);
		//设置主要界面
		song_information_choice.setVisibility(View.INVISIBLE);
		song_list_choice.setVisibility(View.VISIBLE);
		song_lyrics_choice.setVisibility(View.INVISIBLE);
		artist_display.setVisibility(View.INVISIBLE);
		song_by_artist.setVisibility(View.INVISIBLE);
		treasuredmusic.setVisibility(View.INVISIBLE);
		latest.setVisibility(View.INVISIBLE);
	}

	//设置监听器
	private void setListener() 
	{
		//为歌曲信息按钮添加监听器
		song_info.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//颜色设置
				song_info.setTextColor(Color.BLUE);
				song_list.setTextColor(Color.BLACK);
				song_lyrics.setTextColor(Color.BLACK);
				//设置主要界面
				song_information_choice.setVisibility(View.VISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
			}
		});
		
		//为歌曲信息按钮添加监听器
		song_list.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//颜色设置
				song_info.setTextColor(Color.BLACK);
				song_list.setTextColor(Color.BLUE);
				song_lyrics.setTextColor(Color.BLACK);
				//设置主要界面
				song_information_choice.setVisibility(View.INVISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				//显示菜单
				menuWindow = new BottomMenu(MainActivity.this, clickListener);
				menuWindow.show();
			}
		});
				
		//为歌信息按钮添加监听器
		song_lyrics.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//颜色设置
				song_info.setTextColor(Color.BLACK);
				song_list.setTextColor(Color.BLACK);
				song_lyrics.setTextColor(Color.BLUE);
				//设置主要界面
				song_information_choice.setVisibility(View.INVISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.VISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				//查找歌词
				findLyrics();
			}
		});
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
					
			@Override
			public void onTouchingLetterChanged(String s) 
			{
				//该字母首次出现的位置
				int position = musicadapter.getPositionForSection(s.charAt(0));
				if(position != -1)
				{
					song_list_choice.setSelection(position);
				}	
			}
		});
		
		//设置右侧触摸监听
		artist_sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
					
			@Override
			public void onTouchingLetterChanged(String s) 
			{
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1)
				{
					sortArtistView.setSelection(position);
				}		
			}
		});
		
		//歌曲列表点击事件
		song_list_choice.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				//屏蔽其它视图
				song_information_choice.setVisibility(View.INVISIBLE);
				song_list_choice.setVisibility(View.VISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				
				//记录音乐播放的位置
				currentindex=arg2;
				mApplication.setCurrent_location(currentindex);
				ChangingTreasure();
				findLyrics();
				start();
			}
		});
		
		//最近音乐播放点击列表
		latest.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				String name=latest_played.get(arg2).getName();
				String artist=latest_played.get(arg2).getArtist();
				specifiedPlay(name,artist);
			}
		});
		
		song_by_artist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				String name=song_by_artist_value.get(arg2).getName();
				String artist=song_by_artist_value.get(arg2).getArtist();
				specifiedPlay(name,artist);
			}
		});
		
		//删除操作
		song_list_choice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
				alert.setTitle("提示");
				alert.setMessage("是否删除选中的音乐?");
				alert.setCancelable(false);
				alert.setNegativeButton("取消", null);
				
				alert.setPositiveButton("确定", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						flag2=true;
					}
				});
				alert.show();
				
				if(flag2)
				{
					//删除文件操作
					File file_music=new File(mp3path+File.separator+mp3s.get(arg2).getMusicName());
					file_music.delete();
					File file_lyrics=new File(lyrics+File.separator+mp3s.get(arg2).getLyrics());
					file_lyrics.delete();
					File file_picture=new File(picture+File.separator+mp3s.get(arg2).getPicture());
					file_picture.delete();
					File file_info=new File(info+File.separator+mp3s.get(arg2).getPicture().replace(".jpg", ".txt"));
					file_info.delete();
					
					//改变操作界面
					musics.remove(arg2);
					mp3s.remove(arg2);
					musicadapter.notifyDataSetChanged();
				}
				return false;
			}
		});
		
		//为歌手列表添加点击事件
		sortArtistView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				String name=SourceArtistList.get(arg2).getName();
				fillSongData(name);
				// 根据a-z进行排序源数据
				Collections.sort(song_by_artist_value, pinyinComparator);
				song_by_artist_adapter= new MusicAdapter(MainActivity.this,R.layout.song_list,song_by_artist_value);
				song_by_artist_listview.setAdapter(song_by_artist_adapter);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.VISIBLE);
			}
		});
		
		//为SeekBar添加监听器
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				if(fromUser==true)
				{
					//传递进度操作
					Pass_Progress(progress);
					song_lyrics_choice.setOffsetY(
							220-song_lyrics_choice.SelectIndex(progress)*(song_lyrics_choice.getSIZEWORD()+INTERVAL-1));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) 
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		//播放按钮监听事件
		play_start.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				play();
			}
		});
		
		//向前按钮监听事件
		play_proceed.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				next();
			}
		});
		
		//向后按钮监听事件
		play_retrieve.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				previous();
			}
		});
		
		//音乐播放模式按钮监听器
		play_mode.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(status==1)
				{
					//进入循环模式
					status=2;
					play_mode.setImageResource(R.drawable.cycle);
					Toast.makeText(MainActivity.this, "循环模式", 1000).show();
				}
				else if(status==2)
				{
					//进入随机播放模式
					status=3;
					play_mode.setImageResource(R.drawable.random);
					Toast.makeText(MainActivity.this, "随机播放", 1000).show();
				}
				else if(status==3)
				{
					//进入顺序播放模式
					status=4;
					play_mode.setImageResource(R.drawable.queue);
					Toast.makeText(MainActivity.this, "顺序播放", 1000).show();
				}
				else
				{
					//进入单曲循环模式
					status=1;
					play_mode.setImageResource(R.drawable.singlecycle);
					Toast.makeText(MainActivity.this, "单曲循环", 1000).show();
				}
				//设置音乐播放模式
				mApplication.setStatus(status);
			}
		});
		
		//登录显示栏点击事件
		log_display.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(!mApplication.flag_login)
				{
					//设置跳转意图
					Intent intent_login=new Intent(MainActivity.this,UserMainActivity.class);
					//跳转操作
					startActivity(intent_login);
				}
				else
				{
					//设置跳转意图
					Intent intent_login=new Intent(MainActivity.this,PersonInformationActivity.class);
					//跳转操作
					startActivity(intent_login);
				}
			}
		});
		
		//为收藏按钮添加监听器
		treasure.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(musics.get(currentindex).flag)
				{
					clearMusic();
					tmusicadapter.notifyDataSetChanged();
					musics.get(currentindex).flag=false;
					treasure.setImageResource(R.drawable.nokeep);
				}
				else
				{
					treasureList.add(musics.get(currentindex));
					tmusicadapter.notifyDataSetChanged();
					musics.get(currentindex).flag=true;
				    treasure.setImageResource(R.drawable.keep);
				}
			}
		});
		
		//为搜索按钮添加监听器
		search.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//设置意图用于跳转
				Intent intent=new Intent(MainActivity.this,SearchActivity.class);
				startActivityForResult(intent,1);
			}
		});
		
		//为评论按钮添加监听器
		remark.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(mApplication.flag_login)
				{
					//设置跳转意图
					Intent intent=new Intent(MainActivity.this,RemarkActivity.class);
					intent.putExtra("music_name", musics.get(currentindex).getName());
					intent.putExtra("music_artist", musics.get(currentindex).getArtist());
					//跳转操作
					startActivity(intent);
				}
				else
				{
					Toast.makeText(MainActivity.this, "请先执行登录操作!", 1000).show();
				}
			}
		});
	}

	//初始化菜单
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return true;
	}
	
	//获取歌曲文件名
	public String[] getFileNames(String path)
	{
		//文件所在的目录
		File path_song=new File(path);
		//得到文件
		File[] files=path_song.listFiles();
		//用于存储歌曲文件名
		String []songNames =new String[files.length];
		if(files!=null)
		{
			//用于记录数组中的数据
			int i=0;
			for(File file:files)
			{
				
				songNames[i++]=file.getName();
			}
		}
		//返回歌曲文件名
		return songNames;
	}
	
	//初始音乐列表
	public void initialise()
	{
		//读取音乐文件
		String []mp3paths=getFileNames(mp3path);
		//读取歌词文件
		String []lyricspaths=getFileNames(lyrics);
		//读取图片文件
		String []picturepaths=getFileNames(picture);
		//读取信息文件
		String []infopaths=getFileNames(info);
		
		//存入列表
		for(int i=0;i<mp3paths.length;i++)
		{
			Mp3Info mp3=new Mp3Info();
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(mp3paths[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]"))
			{
				mp3.setSortLetters(sortString.toUpperCase());
			}
			else
			{
				mp3.setSortLetters("#");
			}
			//创建音乐实体
			mp3.setMusicName(mp3paths[i]);
			mp3.setMusicInfo(getSongInformation(info+File.separator+infopaths[i]));
			mp3.setLyrics(lyricspaths[i]);
			mp3.setPicture(picturepaths[i]);
			//装入列表中
			mp3s.add(mp3);
		}
	}
	
	//初始音乐列表
	public void initialise2()
	{
		//读取音乐文件
		String []mp3paths=getFileNames(mp3path);
		//读取图片文件
		String []picturepaths=getFileNames(picture);
			
		//存入列表
		for(int i=0;i<mp3paths.length;i++)
		{
			Music music=new Music();
			String[]trans=mp3paths[i].split("-");
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(trans[0]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]"))
			{
				music.setSortLetters(sortString.toUpperCase());
			}
			else
			{
				music.setSortLetters("#");
			}
			//填充数据
			music.setImage(transferpicture(picturepaths[i]));
			music.setName(trans[0]);
			music.setArtist(trans[1].replace(".mp3", ""));
			ArrayList<Music> gets=null;
			if(artists_and_songs.containsKey(music.getArtist()))
			{
				gets=artists_and_songs.get(music.getArtist());
			}
			else
			{
				gets=new ArrayList<Music>();
			}
			gets.add(music);
			artists_and_songs.put(music.getArtist(), gets);
			//插入数据库操作
			userinfoservice.doInsertingMusic(music.getName(), music.getArtist());
			//装入列表中
			musics.add(music);
		}
	}
	
	public Bitmap transferpicture(String name)
	{
		String imagepath=picture+File.separator+name;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=2;
		Bitmap bm=BitmapFactory.decodeFile(imagepath,options);
		return bm;
	}
		
	public String getSongInformation(String name)
	{
		String message="";
		BufferedReader reader;
		try 
		{
			FileInputStream fis=new FileInputStream(new File(name));
			BufferedInputStream in=new BufferedInputStream(fis);
			in.mark(4);
			byte[] first3byte3=new byte[3];
			in.read(first3byte3);
			in.reset();
			if(first3byte3[0]==(byte)0xEF && first3byte3[1]==(byte)0xBB && first3byte3[1]==(byte)0xBF)
			{
				reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
			}
			else if(first3byte3[0]==(byte)0xFE && first3byte3[1]==(byte)0xFF )
			{
				reader=new BufferedReader(new InputStreamReader(in,"utf-16le"));
			}
			else
			{
				reader=new BufferedReader(new InputStreamReader(in,"GBK"));
			}
			String str=reader.readLine();
			while(str!=null)
			{
				message+=str+"\r\n";
				str=reader.readLine();
			}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
		}
		
		//播放操作
		private void play()
		{
			//判断当前状态
			switch(currentstate)
			{
			    case IDLE:
			    	start();
			    	break;
			    case PAUSE:
			    	//传递广播
			    	Pass_State(currentstate);
			    	play_start.setImageResource(R.drawable.playstart);
			    	//更新通知栏视图
			    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playstart);
			    	nm.notify(0, notification);
			    	//获取播放进度
			    	song_lyrics_choice.SelectIndex((mApplication.play_proceed)*(song_lyrics_choice.getSIZEWORD()+INTERVAL-1));
			    	currentstate=START;
			    	break;
			    case START:
			    	//传递广播
			    	Pass_State(currentstate);
			    	play_start.setImageResource(R.drawable.playpause);
			    	//更新通知栏视图
			    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
			    	nm.notify(0, notification);
			    	currentstate=PAUSE;
			    	break;
			}
		}

		//上一首
		private void previous()
		{
			//获取当前的播放位置
			currentindex=mApplication.getCurrent_location();
			if((currentindex-1)>=0)
			{
				currentindex--;
				findLyrics();
				//设置全局变量
				mApplication.setCurrent_location(currentindex);
				//播放操作
				start();
			}
			else
			{
				Toast.makeText(MainActivity.this, "当前已经是第一首歌了", 1000).show();
			}
		}
		
		//下一首
		private void next()
		{
			//获取当前的播放位置
			currentindex=mApplication.getCurrent_location();
			if((currentindex+1)<mp3s.size())
			{
				currentindex++;
				findLyrics();
				//设置全局变量
				mApplication.setCurrent_location(currentindex);
				//播放操作
				start();
			}
			else
			{
				Toast.makeText(MainActivity.this, "当前已经是最后一首歌了", 1000).show();
			}
		}
		
		//音乐播放
		private void start()
		{
			//更新视图操作
			UpdateNotification();
			//更新最近播放音乐列表
			Addmusic(musics.get(currentindex));
			//判断当前索引是否在列表内
			if(mp3s.size()>0 && currentindex<mp3s.size())
			{
				//启动服务
				startService(intent_music);
				//改变播放图标
				play_start.setImageResource(R.drawable.playpause);
				//更新通知栏视图
		    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
		    	nm.notify(0, notification);
				//改变当前状态
				currentstate=PAUSE;
			}
			else
			{
				//提示“播放列表为空”
				Toast.makeText(MainActivity.this, "播放列表为空!", 1000).show();
			}
		}

        //计算时间
		private String toTime(int time) 
		{
			int minute=time/1000/60;
			int second=time/1000%60;
			String mm=null;
			String ss=null;
			if(minute<10)
			{
				mm="0"+minute;
			}
			else
			{
				mm=minute+"";
			}
			if(second<10)
			{
				ss="0"+second;
			}
			else
			{
				ss=""+second;
			}
			return mm+":"+ss;
		}
		
		//查找歌词文件
		private void SearchLrc()
		{
			String lrc=lyrics+File.separator+mp3s.get(currentindex).getLyrics();
			lrc=lrc.substring(0,lrc.length()-4).trim()+".lrc".trim();
			song_lyrics_choice.read(lrc);
			song_lyrics_choice.SetTextSize();
			song_lyrics_choice.setOffsetY(350);
		}
		
		//查找歌词工作
		public void findLyrics()
		{
			SearchLrc();
			song_lyrics_choice.SetTextSize();
		}
		
		//释放资源
		protected void onDestroy()
		{
			javaxml=new JavaXml(treasureList);
			try 
			{
				//生成musics.xml文件
				javaxml.BuildXMLDoc();
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//撤销广播类
			unregisterReceiver(iBroadcast);
			unregisterReceiver(cBroadcast);
			unregisterReceiver(s2Broadcast);
			super.onDestroy();
		}
		
		//自定义广播
		public class InitializeBroadcast extends BroadcastReceiver
		{
			//自定义方法
			public void onReceive(Context context, Intent broadcast_intent) 
			{
				//获取音乐播放的总时间
				String totaltime=broadcast_intent.getStringExtra("totaltime");
				//定义音乐播放的总时间
				total_time.setText(toTime(Integer.valueOf(totaltime).intValue()));
				//获取位置
				int location=Integer.valueOf(broadcast_intent.getStringExtra("location")).intValue();
				//设置播放音乐的名称
				song_name.setText(mp3s.get(location).getMusicName().replace(".mp3", ""));
				//更新音乐信息
				song_information_choice.setText(mp3s.get(location).getMusicInfo());
				//设置播放进度条播放的最长时间
				seekbar.setMax(Integer.valueOf(totaltime).intValue());
				//设置进度条的初始时间
				seekbar.setProgress(0);
			}
		}
		
		//自定义广播
		public class ChangeBroadcast extends BroadcastReceiver
		{
			//自定义方法
			public void onReceive(Context context, Intent intent) 
			{
				//获取音乐的当前播放进度
				int location=Integer.valueOf(intent.getStringExtra("location")).intValue();
				//获取当前的标志位
				boolean flag=Boolean.valueOf(intent.getStringExtra("flag")).booleanValue();
				if(flag)
				{
					if(location<seekbar.getMax())
					{
						//设置当前的时间
						song_lyrics_choice.setOffsetY(song_lyrics_choice.getOffsetY()-song_lyrics_choice.SpeedLrc());
						song_lyrics_choice.SelectIndex(location);
						seekbar.setProgress(location);
						Message msg=handler.obtainMessage(
								                             CURR_TIME_VALUE,
								                             toTime(location)
								                         );
						//发送消息
						handler.sendMessage(msg);
					}
				}
			}
		}
		
		//定义广播
		public class Status2_Broadcast extends BroadcastReceiver
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				//获取音乐播放的位置
				currentindex=mApplication.getCurrent_location();
				//更新通知栏视图操作
				UpdateNotification();
				//获取音乐播放模式
				status=mApplication.getStatus();
				//判断音乐播放列表是否为空
				if(mp3s.size()>0)
				{
					//单曲循环模式
					if(status==1)
					{
						song_lyrics_choice.SetTextSize();
						song_lyrics_choice.setOffsetY(200);
					}
					else
					{
						findLyrics();
					}
				}
				else
				{
					Toast.makeText(MainActivity.this, "播放列表为空", 1000).show();
				}
			}
		}
		
		//定义广播
		public class ViewBroadcast extends BroadcastReceiver
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
			    //获取当前的状态
				currentstate=Integer.valueOf(intent.getStringExtra("currentstate")).intValue();
				//判断当前状态
				switch(currentstate)
				{
				    case PAUSE:
				    	//更新通知栏视图
				    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playstart);
				    	play_start.setImageResource(R.drawable.playstart);
			    	    currentstate=START;
			    	    break;
			        case START:
			        	//更新通知栏视图
				    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
				    	play_start.setImageResource(R.drawable.playpause);
			    	    currentstate=PAUSE;
			    	    break;
				}
				nm.notify(0, notification);
			}
		}
		
		//注册广播
		private void registerReceiver()
		{
			//注册初始化广播
			iBroadcast=new InitializeBroadcast();
			IntentFilter filter1 = new IntentFilter();
			filter1.addAction("com.example.SimpleMusicPlayer.MUSIC_INITIALIZE");
			registerReceiver(iBroadcast, filter1);
			
			//注册进度条改变广播
			cBroadcast=new ChangeBroadcast();
			IntentFilter filter2 = new IntentFilter();
			filter2.addAction("com.example.SimpleMusicPlayer.MUSIC_PROGRESS_CHANGING");
			registerReceiver(cBroadcast, filter2);
			
			//注册状态改变广播
			s2Broadcast=new Status2_Broadcast();
			IntentFilter filter3 = new IntentFilter();
			filter3.addAction("com.example.SimpleMusicPlayer.STATUS2_CHANGING");
			registerReceiver(s2Broadcast, filter3);
			
			//注册视图改变广播
			vBroadcast=new ViewBroadcast();
			IntentFilter filter4 = new IntentFilter();
			filter4.addAction("com.example.SimpleMusicPlayer.VIEW_CHANGING");
			registerReceiver(vBroadcast, filter4);
		}
		
		//传递进度操作
		public void Pass_Progress(int progress)
		{
			//定义意图
			Intent sendIntent = new Intent(); 
			sendIntent.setAction("com.example.SimpleMusicPlayer.PROGRESS_CHANGING");
			//填装数据
			String progress2=String.valueOf(progress);
			sendIntent.putExtra("progress", progress2);
			//发送广播，将被Activity组件中的ProgressBroadcast接收到 
			sendBroadcast(sendIntent);  
		}
		
		//传递状态操作
		public void Pass_State(int state)
		{
			//定义意图
			Intent sendIntent = new Intent(); 
			sendIntent.setAction("com.example.SimpleMusicPlayer.MARK_CHANGING");
			//填装数据
			String state2=String.valueOf(state);
			sendIntent.putExtra("currentstate", state2);
			//发送广播，将被Activity组件中的MarkBroadcast接收到 
			sendBroadcast(sendIntent);  
		}
		
		//初始化通知栏控件操作
		private void InitializeNotification()
		{
			//获取系统通知的管理者
			nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//实例化通知类对象
			notification=new Notification();
			//添加通知图标
			notification.icon=R.drawable.simplemusicplayer;
			//为通知栏添加界面
			notification.contentView=contentview;
			
			//设置通知点击或滑动时不被清除
			notification.flags=notification.FLAG_NO_CLEAR;
			
			//设置“播放”意图
			Intent intent_play=new Intent("play");
			PendingIntent pending_play=PendingIntent.getBroadcast(this,0,intent_play,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_play, pending_play);
			
			//设置“向前播放”意图
			Intent intent_next=new Intent("next");
			PendingIntent pending_next=PendingIntent.getBroadcast(this,0,intent_next,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_proceed, pending_next);
			
			//设置“消失”意图
			Intent intent_cancel=new Intent("cancel");
			PendingIntent pending_cancel=PendingIntent.getBroadcast(this,0,intent_cancel,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_close, pending_cancel);
			
			//启动通知
			nm.notify(0, notification);
		}
		
		//更新通知栏
		private void UpdateNotification()
		{
			//更新通知栏视图
			contentview.setImageViewBitmap(R.id.notification_picture, musics.get(currentindex).getImage());
			contentview.setTextViewText(R.id.notification_song_name, musics.get(currentindex).getName());
			contentview.setTextViewText(R.id.notification_song_singer, musics.get(currentindex).getArtist());
			//启动通知
			nm.notify(0, notification);
		}
		
		@Override
		protected void onResume() 
		{
			super.onResume();
		}
		
		@Override
		protected void onRestart() 
		{
	         super.onRestart();
		}
		
		@Override
		protected void onStart() 
		{
			//初始化登录界面
			if(mApplication.flag_login)
			{
				cname.setText(mApplication.bundle.getString("user_name"));
				cimage.setImageBitmap(mApplication.userimage);
			}
			//创建新的文件
			File file = new File(general+"musics.xml");
			//解析xml文件
			pullxml.ParseXml();
			List<MusicModel>list=pullxml.getList();
			for(MusicModel model:list)
			{
				ReInsertingMusic(model.getName(),model.getArtist());
			}
			//删除原先的xml文件
			File xml_file=new File(general+"musics.xml");
			xml_file.delete();
			super.onStart();
		}
		
		@Override
		protected void onPause() 
		{
			super.onPause();
		}
		
		
		private OnClickListener  clickListener = new OnClickListener()
		{
	        public void onClick(View v) 
	        {
	        	switch (v.getId())
	        	{
	        	    case R.id.display_latest_play:
	        	    	latest.setVisibility(View.VISIBLE);
	        		    break;
	        	    case R.id.display_by_artist:
	        	    	artist_display.setVisibility(View.VISIBLE);
	        	    	break;
	        	    case R.id.display_by_song:
	        	    	song_display.setVisibility(View.VISIBLE);
	        	        break;
	        	    case R.id.display_treasure:
	        	    	treasuredmusic.setVisibility(View.VISIBLE);
	        	    	break;
	        	}
	        }
	    };
	    
	    //添加音乐
	    public void Addmusic(Music music)
	    {
	    	//去除重复音乐操作
	    	for(int i=0;i<latest_played.size();i++)
	    	{
	    		Music rmusic=latest_played.get(i);
	    		if(rmusic.getName().equals(music.getName())&&rmusic.getArtist().equals(music.getArtist()))
	    		{
	    			latest_played.remove(i);
	    			break;
	    		}
	    	}
	    	//添加音乐操作
	    	latest_played.addFirst(music);
	    	lmusicadapter.notifyDataSetChanged();
	    }
	    
	    //为歌曲作者填充数据
	    private void fillData() 
	    {
			for(String artist_name:artists_and_songs.keySet())
			{
				Artist artist=new Artist();
				artist.setName(artist_name);
				//汉字转换成拼音
				String pinyin = characterParser.getSelling(artist_name);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]"))
				{
					artist.setSortLetters(sortString.toUpperCase());
				}
				else
				{
					artist.setSortLetters("#");
				}
				SourceArtistList.add(artist);
			}
		}
	    
	    //为歌曲作者填充数据
	    private void fillSongData(String name) 
	    {
	    	song_by_artist_value.clear();
	    	ArrayList<Music>data=artists_and_songs.get(name);
			for(Music music:data)
			{
			    //汉字转换成拼音
				String pinyin = characterParser.getSelling(music.getName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]"))
				{
					music.setSortLetters(sortString.toUpperCase());
				}
				else
				{
					music.setSortLetters("#");
				}
				song_by_artist_value.add(music);
			}
		}
	    
	    //音乐播放设置
	    private void specifiedPlay(String name,String artist)
	    {
	    	for(int i=0;i<musics.size();i++)
			{
				if(name.equals(musics.get(i).getName())&&artist.equals(musics.get(i).getArtist()))
				{
					//记录音乐播放的位置
					currentindex=i;
					break;
				}
			}
			mApplication.setCurrent_location(currentindex);
			ChangingTreasure();
			findLyrics();
			start();
	    }
	    
	    //改变收藏标志
	    private void ChangingTreasure()
	    {
	    	if(musics.get(currentindex).flag)
	    	{
	    		treasure.setImageResource(R.drawable.keep);
	    	}
	    	else
	    	{
	    		treasure.setImageResource(R.drawable.nokeep);
	    	}
	    }
	    
	    //清除特定的音乐对象
	    private void clearMusic()
	    {
	    	for(int i=0;i<treasureList.size();i++)
	    	{
	    		if(treasureList.get(i).getArtist().equals(musics.get(currentindex).getArtist())&&treasureList.get(i).getName().equals(musics.get(currentindex).getName()))
	    		{
	    			treasureList.remove(i);
	    		}
	    	}
	    }
	    
	    //填装已收藏的音乐
	    private void ReInsertingMusic(String name,String artist) 
	    {
			for(Music music:musics)
			{
				if(name.equals(music.getName())&&artist.equals(music.getArtist()))
				{
					music.flag=true;
					treasureList.add(music);
				}
			}
		}
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	    {
	    	//播放搜索后的音乐
	    	if(resultCode==RESULT_OK)
	    	{
	    		String name=data.getStringExtra("name");
	    		String artist=data.getStringExtra("artist");
	    		specifiedPlay(name,artist);
	    	}
	    	super.onActivityResult(requestCode, resultCode, data);
	    }
}
