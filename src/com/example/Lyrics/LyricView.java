package com.example.Lyrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LyricView extends View
{
    private static TreeMap<Integer, LyricObject>lrc_map;
    //��ĻX����е�,��ֵ�̶�,���ָ����X�м���ʾ
    private float mX;
    //�����Y���ϵ�ƫ����,��ֵ����ݸ�ʵĹ�����С
    private float offsetY;
    private static boolean blLrc=false;
    //���������Viewʱ�����浱ǰ�����Y����
    private float touchY;
    private float touchX;
    private boolean blScrollView=false;
    //������TreeMap���±�
    private int lrcIndex=0;
    //��ʾ��ʴ�С��ֵ
    private int SIZEORD=0;
    //���ÿ�еļ��
    private int INTERVAL=45;
    //���ʣ����ڻ����Ǹ������
    Paint paint=new Paint();
    //���ʣ����ڻ�������ʣ�����ǰ�����ĸ��
    Paint paintHL=new Paint();
    
	public LyricView(Context context) 
	{
		super(context);
		init();
	}
	
	public LyricView(Context context,AttributeSet attrs) 
	{
		super(context, attrs);
		init();
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		if(blLrc)
		{
			paintHL.setTextSize(SIZEORD);
			paint.setTextSize(SIZEORD);
			LyricObject temp=lrc_map.get(lrcIndex);
			canvas.drawText(temp.lrc, mX, offsetY+(SIZEORD+INTERVAL)*lrcIndex, paintHL);
			//�����֮ǰ�ĸ��
			for(int i=lrcIndex-1;i>=0;i--)
			{
				temp=lrc_map.get(i);
				if(offsetY+(SIZEORD+INTERVAL)*i<0)
				{
					break;
				}
				canvas.drawText(temp.lrc, mX, offsetY+(SIZEORD+INTERVAL)*i, paint);
			}
			//�����֮��ĸ��
			for(int i=lrcIndex+1;i<lrc_map.size();i++)
			{
				temp=lrc_map.get(i);
				if(offsetY+(SIZEORD+INTERVAL)*i>600)
				{
					break;
				}
				canvas.drawText(temp.lrc, mX, offsetY+(SIZEORD+INTERVAL)*i, paint);
			}
		}
		else
		{
			paint.setTextSize(25);
			canvas.drawText("��Ǹ���޷��ҵ���ʡ�", mX, 310, paint);
		}
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		System.out.println("bllll===="+blScrollView);
		float tt=event.getY();
		if(!blLrc)
		{
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) 
		{
		    case MotionEvent.ACTION_DOWN:
			    touchX=event.getX();
			    break;
		    case MotionEvent.ACTION_MOVE:
		    	touchY=tt-touchY;
		    	offsetY+=touchY;
		    	break;
		    case MotionEvent.ACTION_UP:
		    	blScrollView=false;
			    break;
		}
		touchY=tt;
		return true;
	}
	
	//��ʼ������
	private void init() 
	{
		lrc_map=new TreeMap<Integer, LyricObject>();
		offsetY=320;
		paint=new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setAlpha(180);
		
		paintHL=new Paint();
		paintHL.setTextAlign(Paint.Align.CENTER);
		paintHL.setColor(Color.BLUE);
		paintHL.setAntiAlias(true);
		paintHL.setAlpha(255);
	}

	public void SetTextSize()
	{
		if(!blLrc)
		{
			return;
		}
		int max=lrc_map.get(0).lrc.length();
		for(int i=1;i<lrc_map.size();i++)
		{
			LyricObject lrcStrLength=lrc_map.get(i);
			if(max<lrcStrLength.lrc.length())
			{
				max=lrcStrLength.lrc.length();
			}
		}
		SIZEORD=600/max;
	}
	
	protected void onSizeChanged(int w,int h,int oldw,int oldh)
	{
		mX=w*0.5f;
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	//��ʹ����ٶ�
	public float SpeedLrc()
	{
		float speed=0;
		if(offsetY+(SIZEORD+INTERVAL)*lrcIndex>220)
		{
			speed=((offsetY+(SIZEORD+INTERVAL)*lrcIndex-220)/20);
		}
		else if(offsetY+(SIZEORD+INTERVAL)*lrcIndex<120)
		{
			Log.i("speed", "speed is too fast!");
			speed=0;
		}
		if(speed<0.2)
		{
			speed=0.2f;
		}
		return speed;
	}
	
	//����ǰ�ĸ�������ʱ�䣬�Ӹ����������һ��
	public int SelectIndex(int time)
	{
		if(!blLrc)
		{
			return 0;
		}
		int index=0;
		for(int i=0;i<lrc_map.size();i++)
		{
			LyricObject temp=lrc_map.get(i);
			if(temp.begintime<time)
			{
				++index;
			}
		}
		lrcIndex=index-1;
		if(lrcIndex<0)
		{
			lrcIndex=0;
		}
		return lrcIndex;
	}
	
	//��ȡ����ļ�
	public static void read(String file)
	{
		TreeMap<Integer, LyricObject>lrc_read=new TreeMap<Integer, LyricObject>();
		String data="";
		try 
		{
			File saveFile=new File(file);
			if(!saveFile.isFile())
			{
				blLrc=false;
				return;
			}
			blLrc=true;
			FileInputStream stream=new FileInputStream(saveFile);
			BufferedReader br=new BufferedReader(new InputStreamReader(stream,"GB2312"));
			int i=0;
			Pattern pattern=Pattern.compile("\\d{2}");
			while((data=br.readLine())!=null)
			{
				data=data.replace("[", "");
				data=data.replace("]", "@");
				String splitdata[]=data.split("@");
				if(data.endsWith("@"))
				{
					for(int k=0;k<splitdata.length;k++)
					{
						String str=splitdata[k];
						str=str.replace(":", ".");
						str=str.replace(".", "@");
						String timedata[]=str.split("@");
						Matcher matcher=pattern.matcher(timedata[0]);
						if(timedata.length==3 && matcher.matches())
						{
							//��
							int m=Integer.parseInt(timedata[0]);
							//��
							int s=Integer.parseInt(timedata[1]);
							//����
							int ms=Integer.parseInt(timedata[2]);
							int currTime=(m*60+s)*1000+ms*10;
							LyricObject item1=new LyricObject();
							item1.begintime=currTime;
							item1.lrc="";
							lrc_read.put(currTime, item1);
						}
					}
				}
				else
				{
					String lrcContent=splitdata[splitdata.length-1];
					for(int j=0;j<splitdata.length-1;j++)
					{
						String tmpstr=splitdata[j];
						
						tmpstr=tmpstr.replace(":", ".");
						tmpstr=tmpstr.replace(".","@");
						String timedata[]=tmpstr.split("@");
						Matcher matcher=pattern.matcher(timedata[0]);
						if(timedata.length==3 && matcher.matches())
						{
							//��
							int m=Integer.parseInt(timedata[0]);
							//��
							int s=Integer.parseInt(timedata[1]);
							//����
							int ms=Integer.parseInt(timedata[2]);
							int currTime=(m*60+s)*1000+ms*10;
							LyricObject item1=new LyricObject();
							item1.begintime=currTime;
							item1.lrc=lrcContent;
							lrc_read.put(currTime, item1);
							i++;
						}
					}
				}
			}
			stream.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//����HashMap����ÿ��������Ҫ��ʱ��
		lrc_map.clear();
		data="";
		Iterator<Integer>iterator=lrc_read.keySet().iterator();
		LyricObject oldval=null;
		int i=0;
		while(iterator.hasNext())
		{
			Object ob=iterator.next();
			
			LyricObject val=(LyricObject)lrc_read.get(ob);
			if(oldval==null)
			{
				oldval=val;
			}
			else
			{
				LyricObject item1=new LyricObject();
				item1=oldval;
				item1.timeline=val.begintime-oldval.begintime;
				lrc_map.put(new Integer(i), item1);
				i++;
				oldval=val;
			}
			if(!iterator.hasNext())
			{
				lrc_map.put(new Integer(i), val);
			}
		}
	}
	
	//���ø�ʱ���ͼƬ
	public void setBackGroundImage(String file)
	{
		
	}
	
	public static boolean isBlLrc()
	{
		return blLrc;
	}
	
	public float getOffsetY()
	{
		return offsetY;
	}
	
	public void setOffsetY(float offsetY)
	{
		this.offsetY=offsetY;
	}
	
	//���ظ�����ֵĴ�С
	public int getSIZEWORD()
	{
		return SIZEORD;
	}
	
	//���ø�����ֵĴ�С
	public void setSIZEWORD(int SIZEWORD)
	{
		this.SIZEORD=SIZEWORD;
	}
}
