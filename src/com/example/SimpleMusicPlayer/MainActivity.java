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
    
	//������Ϣ
    private TextView song_info;
    //�����б�
    private TextView song_list;
    //���
    private TextView song_lyrics;
    //����ѡ���б�
    private ListView song_list_choice;
    private SideBar sideBar;
    private SideBar artist_sideBar;
    private SideBar song_by_artist_sideBar;
    //������ϸ��Ϣ
    private TextView song_information_choice;
    //�����ʾ
    private LyricView song_lyrics_choice;
    //���Ÿ���
    private TextView song_name;
    //����ģʽ
    private ImageView play_mode;
    //���ſ�ʼ
    private ImageView play_start;
    //����ǰ��
    private ImageView play_proceed;
    //���ź���
    private ImageView play_retrieve;
    //ɾ������
    private ImageView treasure_music;
    //���ֲ���ʱ��
    private TextView current_time;
    //�����ܵ�ʱ��
    private TextView total_time;
    //���Ž�����
    private SeekBar seekbar;
    //����������
    private MusicAdapter musicadapter;
    private LatestMusicAdapter lmusicadapter;
    private LatestMusicAdapter tmusicadapter;
    //�ж��Ƿ����ɾ������
	private boolean flag2=false;
    
    //����ʵ���б�
    private List<Mp3Info>mp3s;
    private List<Music>musics;
    
    //����״̬
    private static final int IDLE=0;
    private static final int PAUSE=1;
    private static final int START=2;
    private static final int CURR_TIME_VALUE=1;
    //��ǰ��������״̬
    private int currentstate=IDLE;
    
    //��ǰ���ֲ�������
    private int currentindex=0;
    //���ƽ������̵߳ı��
    private boolean flag=true;
    //���ֲ���ģʽ���
    private int status=1;
    //���ÿ�еļ��
    private int INTERVAL=45;
    
    //������ͼ��ת������
    private Intent intent_music;
    
    //�����ʼ���㲥
    private InitializeBroadcast iBroadcast;
    //����㲥
    private ChangeBroadcast cBroadcast;
    //����㲥
    private Status2_Broadcast s2Broadcast;
    //����㲥
    private ViewBroadcast vBroadcast;
    
    //����ȫ�ֱ���
    private MusicApplication mApplication;
    
    //��̨����
    private RemoteViews contentview;
    //֪ͨ
    private Notification notification;
    //֪ͨ������
    private NotificationManager nm;
    //���ֲ��Ų����ӿ�
    private MyService.MusicListener musiclistener;
    //���ӷ������
    private ServiceConnection connection;
    
    //��¼��ʾ��
    private RelativeLayout log_display;
    private CircleImageView cimage;
    private TextView cname;
    /**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<Music> SourceDateList;
	private List<Artist> SourceArtistList;
	/**
	 * ����ƴ��������ListView�����������
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

	//����UI����
	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch(msg.what)
			{
			    case CURR_TIME_VALUE:
			    	//���¸���б���ͼ
			    	song_lyrics_choice.invalidate();
			    	current_time.setText(msg.obj.toString());
			    	break;
			    default:
			        break;
			}
		};
	};
	
	//�ؼ�ʵ����
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
	    //ʵ������ͼ
		intent_music=new Intent();
		intent_music.setAction("com.example.SimpleMusicPlayer.PLAY_SERVICE");
		//ע��㲥
		registerReceiver();
		
		//ʵ����Application����
		mApplication=(MusicApplication) getApplication();
		mApplication.setCurrent_location(currentindex);
		mApplication.setStatus(status);
		//ʵ������ͼ
		contentview=new RemoteViews(getPackageName(),R.layout.notification_control);
		//��ʼ��֪ͨ����
		InitializeNotification();
		cimage=(CircleImageView) findViewById(R.id.circleImageView);
		cname=(TextView) findViewById(R.id.user_login);
		//ʵ��������תƴ����
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
		//���г�ʼ��
	    initialise();
	    initialise2();
	    fillData();
		// ����a-z��������Դ����
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
		//�󶨷���
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
		//������Ҫ����
		song_information_choice.setVisibility(View.INVISIBLE);
		song_list_choice.setVisibility(View.VISIBLE);
		song_lyrics_choice.setVisibility(View.INVISIBLE);
		artist_display.setVisibility(View.INVISIBLE);
		song_by_artist.setVisibility(View.INVISIBLE);
		treasuredmusic.setVisibility(View.INVISIBLE);
		latest.setVisibility(View.INVISIBLE);
	}

	//���ü�����
	private void setListener() 
	{
		//Ϊ������Ϣ��ť��Ӽ�����
		song_info.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//��ɫ����
				song_info.setTextColor(Color.BLUE);
				song_list.setTextColor(Color.BLACK);
				song_lyrics.setTextColor(Color.BLACK);
				//������Ҫ����
				song_information_choice.setVisibility(View.VISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
			}
		});
		
		//Ϊ������Ϣ��ť��Ӽ�����
		song_list.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//��ɫ����
				song_info.setTextColor(Color.BLACK);
				song_list.setTextColor(Color.BLUE);
				song_lyrics.setTextColor(Color.BLACK);
				//������Ҫ����
				song_information_choice.setVisibility(View.INVISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				//��ʾ�˵�
				menuWindow = new BottomMenu(MainActivity.this, clickListener);
				menuWindow.show();
			}
		});
				
		//Ϊ����Ϣ��ť��Ӽ�����
		song_lyrics.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//��ɫ����
				song_info.setTextColor(Color.BLACK);
				song_list.setTextColor(Color.BLACK);
				song_lyrics.setTextColor(Color.BLUE);
				//������Ҫ����
				song_information_choice.setVisibility(View.INVISIBLE);
				song_display.setVisibility(View.INVISIBLE);
				song_lyrics_choice.setVisibility(View.VISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				//���Ҹ��
				findLyrics();
			}
		});
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
					
			@Override
			public void onTouchingLetterChanged(String s) 
			{
				//����ĸ�״γ��ֵ�λ��
				int position = musicadapter.getPositionForSection(s.charAt(0));
				if(position != -1)
				{
					song_list_choice.setSelection(position);
				}	
			}
		});
		
		//�����Ҳഥ������
		artist_sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() 
		{
					
			@Override
			public void onTouchingLetterChanged(String s) 
			{
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1)
				{
					sortArtistView.setSelection(position);
				}		
			}
		});
		
		//�����б����¼�
		song_list_choice.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				//����������ͼ
				song_information_choice.setVisibility(View.INVISIBLE);
				song_list_choice.setVisibility(View.VISIBLE);
				song_lyrics_choice.setVisibility(View.INVISIBLE);
				latest.setVisibility(View.INVISIBLE);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.INVISIBLE);
				treasuredmusic.setVisibility(View.INVISIBLE);
				
				//��¼���ֲ��ŵ�λ��
				currentindex=arg2;
				mApplication.setCurrent_location(currentindex);
				ChangingTreasure();
				findLyrics();
				start();
			}
		});
		
		//������ֲ��ŵ���б�
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
		
		//ɾ������
		song_list_choice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() 
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
				alert.setTitle("��ʾ");
				alert.setMessage("�Ƿ�ɾ��ѡ�е�����?");
				alert.setCancelable(false);
				alert.setNegativeButton("ȡ��", null);
				
				alert.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
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
					//ɾ���ļ�����
					File file_music=new File(mp3path+File.separator+mp3s.get(arg2).getMusicName());
					file_music.delete();
					File file_lyrics=new File(lyrics+File.separator+mp3s.get(arg2).getLyrics());
					file_lyrics.delete();
					File file_picture=new File(picture+File.separator+mp3s.get(arg2).getPicture());
					file_picture.delete();
					File file_info=new File(info+File.separator+mp3s.get(arg2).getPicture().replace(".jpg", ".txt"));
					file_info.delete();
					
					//�ı��������
					musics.remove(arg2);
					mp3s.remove(arg2);
					musicadapter.notifyDataSetChanged();
				}
				return false;
			}
		});
		
		//Ϊ�����б���ӵ���¼�
		sortArtistView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				String name=SourceArtistList.get(arg2).getName();
				fillSongData(name);
				// ����a-z��������Դ����
				Collections.sort(song_by_artist_value, pinyinComparator);
				song_by_artist_adapter= new MusicAdapter(MainActivity.this,R.layout.song_list,song_by_artist_value);
				song_by_artist_listview.setAdapter(song_by_artist_adapter);
				artist_display.setVisibility(View.INVISIBLE);
				song_by_artist.setVisibility(View.VISIBLE);
			}
		});
		
		//ΪSeekBar��Ӽ�����
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				if(fromUser==true)
				{
					//���ݽ��Ȳ���
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
		
		//���Ű�ť�����¼�
		play_start.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				play();
			}
		});
		
		//��ǰ��ť�����¼�
		play_proceed.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				next();
			}
		});
		
		//���ť�����¼�
		play_retrieve.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				previous();
			}
		});
		
		//���ֲ���ģʽ��ť������
		play_mode.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(status==1)
				{
					//����ѭ��ģʽ
					status=2;
					play_mode.setImageResource(R.drawable.cycle);
					Toast.makeText(MainActivity.this, "ѭ��ģʽ", 1000).show();
				}
				else if(status==2)
				{
					//�����������ģʽ
					status=3;
					play_mode.setImageResource(R.drawable.random);
					Toast.makeText(MainActivity.this, "�������", 1000).show();
				}
				else if(status==3)
				{
					//����˳�򲥷�ģʽ
					status=4;
					play_mode.setImageResource(R.drawable.queue);
					Toast.makeText(MainActivity.this, "˳�򲥷�", 1000).show();
				}
				else
				{
					//���뵥��ѭ��ģʽ
					status=1;
					play_mode.setImageResource(R.drawable.singlecycle);
					Toast.makeText(MainActivity.this, "����ѭ��", 1000).show();
				}
				//�������ֲ���ģʽ
				mApplication.setStatus(status);
			}
		});
		
		//��¼��ʾ������¼�
		log_display.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(!mApplication.flag_login)
				{
					//������ת��ͼ
					Intent intent_login=new Intent(MainActivity.this,UserMainActivity.class);
					//��ת����
					startActivity(intent_login);
				}
				else
				{
					//������ת��ͼ
					Intent intent_login=new Intent(MainActivity.this,PersonInformationActivity.class);
					//��ת����
					startActivity(intent_login);
				}
			}
		});
		
		//Ϊ�ղذ�ť��Ӽ�����
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
		
		//Ϊ������ť��Ӽ�����
		search.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//������ͼ������ת
				Intent intent=new Intent(MainActivity.this,SearchActivity.class);
				startActivityForResult(intent,1);
			}
		});
		
		//Ϊ���۰�ť��Ӽ�����
		remark.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(mApplication.flag_login)
				{
					//������ת��ͼ
					Intent intent=new Intent(MainActivity.this,RemarkActivity.class);
					intent.putExtra("music_name", musics.get(currentindex).getName());
					intent.putExtra("music_artist", musics.get(currentindex).getArtist());
					//��ת����
					startActivity(intent);
				}
				else
				{
					Toast.makeText(MainActivity.this, "����ִ�е�¼����!", 1000).show();
				}
			}
		});
	}

	//��ʼ���˵�
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return true;
	}
	
	//��ȡ�����ļ���
	public String[] getFileNames(String path)
	{
		//�ļ����ڵ�Ŀ¼
		File path_song=new File(path);
		//�õ��ļ�
		File[] files=path_song.listFiles();
		//���ڴ洢�����ļ���
		String []songNames =new String[files.length];
		if(files!=null)
		{
			//���ڼ�¼�����е�����
			int i=0;
			for(File file:files)
			{
				
				songNames[i++]=file.getName();
			}
		}
		//���ظ����ļ���
		return songNames;
	}
	
	//��ʼ�����б�
	public void initialise()
	{
		//��ȡ�����ļ�
		String []mp3paths=getFileNames(mp3path);
		//��ȡ����ļ�
		String []lyricspaths=getFileNames(lyrics);
		//��ȡͼƬ�ļ�
		String []picturepaths=getFileNames(picture);
		//��ȡ��Ϣ�ļ�
		String []infopaths=getFileNames(info);
		
		//�����б�
		for(int i=0;i<mp3paths.length;i++)
		{
			Mp3Info mp3=new Mp3Info();
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(mp3paths[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]"))
			{
				mp3.setSortLetters(sortString.toUpperCase());
			}
			else
			{
				mp3.setSortLetters("#");
			}
			//��������ʵ��
			mp3.setMusicName(mp3paths[i]);
			mp3.setMusicInfo(getSongInformation(info+File.separator+infopaths[i]));
			mp3.setLyrics(lyricspaths[i]);
			mp3.setPicture(picturepaths[i]);
			//װ���б���
			mp3s.add(mp3);
		}
	}
	
	//��ʼ�����б�
	public void initialise2()
	{
		//��ȡ�����ļ�
		String []mp3paths=getFileNames(mp3path);
		//��ȡͼƬ�ļ�
		String []picturepaths=getFileNames(picture);
			
		//�����б�
		for(int i=0;i<mp3paths.length;i++)
		{
			Music music=new Music();
			String[]trans=mp3paths[i].split("-");
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(trans[0]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]"))
			{
				music.setSortLetters(sortString.toUpperCase());
			}
			else
			{
				music.setSortLetters("#");
			}
			//�������
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
			//�������ݿ����
			userinfoservice.doInsertingMusic(music.getName(), music.getArtist());
			//װ���б���
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
		
		//���Ų���
		private void play()
		{
			//�жϵ�ǰ״̬
			switch(currentstate)
			{
			    case IDLE:
			    	start();
			    	break;
			    case PAUSE:
			    	//���ݹ㲥
			    	Pass_State(currentstate);
			    	play_start.setImageResource(R.drawable.playstart);
			    	//����֪ͨ����ͼ
			    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playstart);
			    	nm.notify(0, notification);
			    	//��ȡ���Ž���
			    	song_lyrics_choice.SelectIndex((mApplication.play_proceed)*(song_lyrics_choice.getSIZEWORD()+INTERVAL-1));
			    	currentstate=START;
			    	break;
			    case START:
			    	//���ݹ㲥
			    	Pass_State(currentstate);
			    	play_start.setImageResource(R.drawable.playpause);
			    	//����֪ͨ����ͼ
			    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
			    	nm.notify(0, notification);
			    	currentstate=PAUSE;
			    	break;
			}
		}

		//��һ��
		private void previous()
		{
			//��ȡ��ǰ�Ĳ���λ��
			currentindex=mApplication.getCurrent_location();
			if((currentindex-1)>=0)
			{
				currentindex--;
				findLyrics();
				//����ȫ�ֱ���
				mApplication.setCurrent_location(currentindex);
				//���Ų���
				start();
			}
			else
			{
				Toast.makeText(MainActivity.this, "��ǰ�Ѿ��ǵ�һ�׸���", 1000).show();
			}
		}
		
		//��һ��
		private void next()
		{
			//��ȡ��ǰ�Ĳ���λ��
			currentindex=mApplication.getCurrent_location();
			if((currentindex+1)<mp3s.size())
			{
				currentindex++;
				findLyrics();
				//����ȫ�ֱ���
				mApplication.setCurrent_location(currentindex);
				//���Ų���
				start();
			}
			else
			{
				Toast.makeText(MainActivity.this, "��ǰ�Ѿ������һ�׸���", 1000).show();
			}
		}
		
		//���ֲ���
		private void start()
		{
			//������ͼ����
			UpdateNotification();
			//����������������б�
			Addmusic(musics.get(currentindex));
			//�жϵ�ǰ�����Ƿ����б���
			if(mp3s.size()>0 && currentindex<mp3s.size())
			{
				//��������
				startService(intent_music);
				//�ı䲥��ͼ��
				play_start.setImageResource(R.drawable.playpause);
				//����֪ͨ����ͼ
		    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
		    	nm.notify(0, notification);
				//�ı䵱ǰ״̬
				currentstate=PAUSE;
			}
			else
			{
				//��ʾ�������б�Ϊ�ա�
				Toast.makeText(MainActivity.this, "�����б�Ϊ��!", 1000).show();
			}
		}

        //����ʱ��
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
		
		//���Ҹ���ļ�
		private void SearchLrc()
		{
			String lrc=lyrics+File.separator+mp3s.get(currentindex).getLyrics();
			lrc=lrc.substring(0,lrc.length()-4).trim()+".lrc".trim();
			song_lyrics_choice.read(lrc);
			song_lyrics_choice.SetTextSize();
			song_lyrics_choice.setOffsetY(350);
		}
		
		//���Ҹ�ʹ���
		public void findLyrics()
		{
			SearchLrc();
			song_lyrics_choice.SetTextSize();
		}
		
		//�ͷ���Դ
		protected void onDestroy()
		{
			javaxml=new JavaXml(treasureList);
			try 
			{
				//����musics.xml�ļ�
				javaxml.BuildXMLDoc();
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//�����㲥��
			unregisterReceiver(iBroadcast);
			unregisterReceiver(cBroadcast);
			unregisterReceiver(s2Broadcast);
			super.onDestroy();
		}
		
		//�Զ���㲥
		public class InitializeBroadcast extends BroadcastReceiver
		{
			//�Զ��巽��
			public void onReceive(Context context, Intent broadcast_intent) 
			{
				//��ȡ���ֲ��ŵ���ʱ��
				String totaltime=broadcast_intent.getStringExtra("totaltime");
				//�������ֲ��ŵ���ʱ��
				total_time.setText(toTime(Integer.valueOf(totaltime).intValue()));
				//��ȡλ��
				int location=Integer.valueOf(broadcast_intent.getStringExtra("location")).intValue();
				//���ò������ֵ�����
				song_name.setText(mp3s.get(location).getMusicName().replace(".mp3", ""));
				//����������Ϣ
				song_information_choice.setText(mp3s.get(location).getMusicInfo());
				//���ò��Ž��������ŵ��ʱ��
				seekbar.setMax(Integer.valueOf(totaltime).intValue());
				//���ý������ĳ�ʼʱ��
				seekbar.setProgress(0);
			}
		}
		
		//�Զ���㲥
		public class ChangeBroadcast extends BroadcastReceiver
		{
			//�Զ��巽��
			public void onReceive(Context context, Intent intent) 
			{
				//��ȡ���ֵĵ�ǰ���Ž���
				int location=Integer.valueOf(intent.getStringExtra("location")).intValue();
				//��ȡ��ǰ�ı�־λ
				boolean flag=Boolean.valueOf(intent.getStringExtra("flag")).booleanValue();
				if(flag)
				{
					if(location<seekbar.getMax())
					{
						//���õ�ǰ��ʱ��
						song_lyrics_choice.setOffsetY(song_lyrics_choice.getOffsetY()-song_lyrics_choice.SpeedLrc());
						song_lyrics_choice.SelectIndex(location);
						seekbar.setProgress(location);
						Message msg=handler.obtainMessage(
								                             CURR_TIME_VALUE,
								                             toTime(location)
								                         );
						//������Ϣ
						handler.sendMessage(msg);
					}
				}
			}
		}
		
		//����㲥
		public class Status2_Broadcast extends BroadcastReceiver
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				//��ȡ���ֲ��ŵ�λ��
				currentindex=mApplication.getCurrent_location();
				//����֪ͨ����ͼ����
				UpdateNotification();
				//��ȡ���ֲ���ģʽ
				status=mApplication.getStatus();
				//�ж����ֲ����б��Ƿ�Ϊ��
				if(mp3s.size()>0)
				{
					//����ѭ��ģʽ
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
					Toast.makeText(MainActivity.this, "�����б�Ϊ��", 1000).show();
				}
			}
		}
		
		//����㲥
		public class ViewBroadcast extends BroadcastReceiver
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
			    //��ȡ��ǰ��״̬
				currentstate=Integer.valueOf(intent.getStringExtra("currentstate")).intValue();
				//�жϵ�ǰ״̬
				switch(currentstate)
				{
				    case PAUSE:
				    	//����֪ͨ����ͼ
				    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playstart);
				    	play_start.setImageResource(R.drawable.playstart);
			    	    currentstate=START;
			    	    break;
			        case START:
			        	//����֪ͨ����ͼ
				    	contentview.setImageViewResource(R.id.notification_song_play, R.drawable.playpause);
				    	play_start.setImageResource(R.drawable.playpause);
			    	    currentstate=PAUSE;
			    	    break;
				}
				nm.notify(0, notification);
			}
		}
		
		//ע��㲥
		private void registerReceiver()
		{
			//ע���ʼ���㲥
			iBroadcast=new InitializeBroadcast();
			IntentFilter filter1 = new IntentFilter();
			filter1.addAction("com.example.SimpleMusicPlayer.MUSIC_INITIALIZE");
			registerReceiver(iBroadcast, filter1);
			
			//ע��������ı�㲥
			cBroadcast=new ChangeBroadcast();
			IntentFilter filter2 = new IntentFilter();
			filter2.addAction("com.example.SimpleMusicPlayer.MUSIC_PROGRESS_CHANGING");
			registerReceiver(cBroadcast, filter2);
			
			//ע��״̬�ı�㲥
			s2Broadcast=new Status2_Broadcast();
			IntentFilter filter3 = new IntentFilter();
			filter3.addAction("com.example.SimpleMusicPlayer.STATUS2_CHANGING");
			registerReceiver(s2Broadcast, filter3);
			
			//ע����ͼ�ı�㲥
			vBroadcast=new ViewBroadcast();
			IntentFilter filter4 = new IntentFilter();
			filter4.addAction("com.example.SimpleMusicPlayer.VIEW_CHANGING");
			registerReceiver(vBroadcast, filter4);
		}
		
		//���ݽ��Ȳ���
		public void Pass_Progress(int progress)
		{
			//������ͼ
			Intent sendIntent = new Intent(); 
			sendIntent.setAction("com.example.SimpleMusicPlayer.PROGRESS_CHANGING");
			//��װ����
			String progress2=String.valueOf(progress);
			sendIntent.putExtra("progress", progress2);
			//���͹㲥������Activity����е�ProgressBroadcast���յ� 
			sendBroadcast(sendIntent);  
		}
		
		//����״̬����
		public void Pass_State(int state)
		{
			//������ͼ
			Intent sendIntent = new Intent(); 
			sendIntent.setAction("com.example.SimpleMusicPlayer.MARK_CHANGING");
			//��װ����
			String state2=String.valueOf(state);
			sendIntent.putExtra("currentstate", state2);
			//���͹㲥������Activity����е�MarkBroadcast���յ� 
			sendBroadcast(sendIntent);  
		}
		
		//��ʼ��֪ͨ���ؼ�����
		private void InitializeNotification()
		{
			//��ȡϵͳ֪ͨ�Ĺ�����
			nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//ʵ����֪ͨ�����
			notification=new Notification();
			//���֪ͨͼ��
			notification.icon=R.drawable.simplemusicplayer;
			//Ϊ֪ͨ����ӽ���
			notification.contentView=contentview;
			
			//����֪ͨ����򻬶�ʱ�������
			notification.flags=notification.FLAG_NO_CLEAR;
			
			//���á����š���ͼ
			Intent intent_play=new Intent("play");
			PendingIntent pending_play=PendingIntent.getBroadcast(this,0,intent_play,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_play, pending_play);
			
			//���á���ǰ���š���ͼ
			Intent intent_next=new Intent("next");
			PendingIntent pending_next=PendingIntent.getBroadcast(this,0,intent_next,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_proceed, pending_next);
			
			//���á���ʧ����ͼ
			Intent intent_cancel=new Intent("cancel");
			PendingIntent pending_cancel=PendingIntent.getBroadcast(this,0,intent_cancel,0);
			contentview.setOnClickPendingIntent(R.id.notification_song_close, pending_cancel);
			
			//����֪ͨ
			nm.notify(0, notification);
		}
		
		//����֪ͨ��
		private void UpdateNotification()
		{
			//����֪ͨ����ͼ
			contentview.setImageViewBitmap(R.id.notification_picture, musics.get(currentindex).getImage());
			contentview.setTextViewText(R.id.notification_song_name, musics.get(currentindex).getName());
			contentview.setTextViewText(R.id.notification_song_singer, musics.get(currentindex).getArtist());
			//����֪ͨ
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
			//��ʼ����¼����
			if(mApplication.flag_login)
			{
				cname.setText(mApplication.bundle.getString("user_name"));
				cimage.setImageBitmap(mApplication.userimage);
			}
			//�����µ��ļ�
			File file = new File(general+"musics.xml");
			//����xml�ļ�
			pullxml.ParseXml();
			List<MusicModel>list=pullxml.getList();
			for(MusicModel model:list)
			{
				ReInsertingMusic(model.getName(),model.getArtist());
			}
			//ɾ��ԭ�ȵ�xml�ļ�
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
	    
	    //�������
	    public void Addmusic(Music music)
	    {
	    	//ȥ���ظ����ֲ���
	    	for(int i=0;i<latest_played.size();i++)
	    	{
	    		Music rmusic=latest_played.get(i);
	    		if(rmusic.getName().equals(music.getName())&&rmusic.getArtist().equals(music.getArtist()))
	    		{
	    			latest_played.remove(i);
	    			break;
	    		}
	    	}
	    	//������ֲ���
	    	latest_played.addFirst(music);
	    	lmusicadapter.notifyDataSetChanged();
	    }
	    
	    //Ϊ���������������
	    private void fillData() 
	    {
			for(String artist_name:artists_and_songs.keySet())
			{
				Artist artist=new Artist();
				artist.setName(artist_name);
				//����ת����ƴ��
				String pinyin = characterParser.getSelling(artist_name);
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	    
	    //Ϊ���������������
	    private void fillSongData(String name) 
	    {
	    	song_by_artist_value.clear();
	    	ArrayList<Music>data=artists_and_songs.get(name);
			for(Music music:data)
			{
			    //����ת����ƴ��
				String pinyin = characterParser.getSelling(music.getName());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	    
	    //���ֲ�������
	    private void specifiedPlay(String name,String artist)
	    {
	    	for(int i=0;i<musics.size();i++)
			{
				if(name.equals(musics.get(i).getName())&&artist.equals(musics.get(i).getArtist()))
				{
					//��¼���ֲ��ŵ�λ��
					currentindex=i;
					break;
				}
			}
			mApplication.setCurrent_location(currentindex);
			ChangingTreasure();
			findLyrics();
			start();
	    }
	    
	    //�ı��ղر�־
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
	    
	    //����ض������ֶ���
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
	    
	    //��װ���ղص�����
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
	    	//���������������
	    	if(resultCode==RESULT_OK)
	    	{
	    		String name=data.getStringExtra("name");
	    		String artist=data.getStringExtra("artist");
	    		specifiedPlay(name,artist);
	    	}
	    	super.onActivityResult(requestCode, resultCode, data);
	    }
}
