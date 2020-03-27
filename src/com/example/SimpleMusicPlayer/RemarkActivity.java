package com.example.SimpleMusicPlayer;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.database.UserInfoService;
import com.example.display.RemarkMusicAdapter;
import com.example.display.RemarkObject;

public class RemarkActivity extends Activity
{
    private EditText editcontent;
    private Button send;
    private ListView music_content;
    private MusicApplication mApplication;
    private SimpleDateFormat formatter;
    private List<RemarkObject>list;
    private RemarkMusicAdapter adapter;
    private UserInfoService userinfoservice;
    private String music_name;
	private String artist;
	private String username;
	private String name;
	private int choice;
	private String contentrevised;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remark);
		findView();
		setListener();
	}

	@Override
	protected void onStart() 
	{
		list.clear();
		Intent intent=getIntent();
		music_name=intent.getStringExtra("music_name");
		artist=intent.getStringExtra("music_artist");
		List<HashMap<String,Object>>results=userinfoservice.query_remark(music_name, artist);
		for(int i=0;i<results.size();i++)
		{
			HashMap<String,Object>hash=results.get(i);
			username=hash.get("user_name").toString();
			Bitmap user_image=userinfoservice.picture_capturing_byuser(username).get(0).get("user_image");
			String play_time=hash.get("content_time").toString();
			String remark_content=hash.get("remark_content").toString();
			RemarkObject ro=new RemarkObject();
			ro.setRemark_name(username);
			ro.setRemark_image(user_image);
			ro.setRemark_time(play_time);
			ro.setRemark_content(remark_content);
			list.add(ro);
		}
		adapter.notifyDataSetChanged();
		super.onStart();
	}
	
	//实例化控件
	private void findView() 
	{
		editcontent=(EditText) findViewById(R.id.remark_sending);
		send=(Button) findViewById(R.id.send);
		music_content=(ListView) findViewById(R.id.concrete_remark_content);
	    mApplication=(MusicApplication)this.getApplication();
	    formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm");   
	    userinfoservice=new UserInfoService(this);
	    list=new ArrayList<RemarkObject>();
	    adapter=new RemarkMusicAdapter(RemarkActivity.this, R.layout.remark_item, list);
	    music_content.setAdapter(adapter);
	    adapter.notifyDataSetChanged();
	}

	//设置监听器
	private void setListener() 
	{
		//为发表按钮添加监听事件
		send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				//创建对象并填充数据
				RemarkObject ro=new RemarkObject();
				Bitmap image=mApplication.userimage;
				name=mApplication.bundle.getString("user_name");
				Date curDate=new Date(System.currentTimeMillis()); 
				String time=formatter.format(curDate);
				String content=editcontent.getText().toString();
				
				ro.setRemark_image(image);
				ro.setRemark_name(name);
				ro.setRemark_time(time);
				ro.setRemark_content(content);
				//插入数据库操作
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.PNG, 100, os);
				if(userinfoservice.doInsertingRemark(name,music_name,artist,content,time))
				{
					Toast.makeText(RemarkActivity.this, "成功发表!", 1000).show();
					list.add(ro);
					adapter.notifyDataSetChanged();
					editcontent.setText("");
				}
				else
				{
					Toast.makeText(RemarkActivity.this, "发表失败!", 1000).show();
				}
			}
		});
		
		//为列表添加点击事件
		music_content.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
            String name1=mApplication.bundle.getString("user_name");
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				final int location=arg2;
				final String ref_name=list.get(arg2).getRemark_name();
        		final String time=list.get(arg2).getRemark_time();
				if(ref_name.equals(name1))
				{
					//弹出提示框
					AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this);
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("请选择操作方式");
					final String[] sources = {"删除评论", "编辑评论"};
	                //设置一个单项选择下拉框
					builder.setSingleChoiceItems(sources, 0, new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							choice=which;
						}
					});
					//确定按钮操作
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	if(choice==0)
							{

	                    		if(userinfoservice.doDeletingMusicRemark(ref_name, time, music_name, artist))
	                    		{
	                    			Toast.makeText(RemarkActivity.this, "成功删除!", 1000).show();
	                    			list.remove(location);
	                    			adapter.notifyDataSetChanged();
	                    		}
	                    		else
	                    		{
	                    			Toast.makeText(RemarkActivity.this, "删除失败!", 1000).show();
	                    		}
							}
	                    	else if(choice==1)
	                    	{
	                    		final EditText ianswer=new EditText(getApplicationContext());
	            				ianswer.setBackgroundColor(Color.WHITE);
	            				ianswer.setTextColor(Color.BLACK);
	                    		//弹出提示框
	            				AlertDialog.Builder builder = new AlertDialog.Builder(RemarkActivity.this);
	            				builder.setIcon(R.drawable.ic_launcher);
	            				builder.setTitle("请输入修改后的内容");
	            				builder.setView(ianswer);
	            				ianswer.setText(list.get(location).getRemark_content());
	            				builder.setPositiveButton("确定" , new DialogInterface.OnClickListener()
	            				{
	            					@Override
	            					public void onClick(DialogInterface dialog, int which) 
	            					{
	            						//获取答案
	            						contentrevised=ianswer.getText().toString();
	            						Date curDate=new Date(System.currentTimeMillis()); 
	            						String time=formatter.format(curDate);
	            						if(userinfoservice.updateremark(contentrevised, time, list.get(location).getRemark_name(), list.get(location).getRemark_time(), music_name, artist))
	            						{
	            							Toast.makeText(RemarkActivity.this, "编辑成功!", 1000).show();
	            							list.get(location).setRemark_content(contentrevised);
	            							list.get(location).setRemark_time(time);
	            							adapter.notifyDataSetChanged();
	            							dialog.cancel();
	            						}
	            						else
	            						{
	            							Toast.makeText(RemarkActivity.this, "编辑失败!", 1000).show();
	            						}
	            					}
	            					
	            				});  
	            				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
	            				{
	            					public void onClick(DialogInterface dialog, int which) 
	            					{
	            						dialog.cancel();
	            					}
	            				});
	            				builder.show();
	                    	}
	                    }
	                });
					//取消按钮操作
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                    	//关闭对话框
	                        dialog.cancel();
	                    }
	                });
					//显示提示框
					builder.show();
				}
				else
				{
					Toast.makeText(RemarkActivity.this, "您无法对此条评论予以操作", 1000).show();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remark, menu);
		return true;
	}

}
