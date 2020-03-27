package com.example.circleimageview;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class BottomMenu implements OnClickListener,OnTouchListener
{
	private PopupWindow popupWindow;
	private ImageButton_define latest_play;
	private ImageButton_define by_artist;
	private ImageButton_define by_song;
	private ImageButton_define treasure;
	private View mMenuView;
    private Activity mContext;
    private OnClickListener clickListener;
    
    //构造函数
    public BottomMenu(Activity context,OnClickListener clickListener)
    {
    	LayoutInflater inflater = LayoutInflater.from(context);
    	this.clickListener=clickListener;
    	mContext=context;
    	mMenuView = inflater.inflate(com.example.SimpleMusicPlayer.R.layout.displaymenu, null);
    	latest_play=(ImageButton_define) mMenuView.findViewById(com.example.SimpleMusicPlayer.R.id.display_latest_play);
    	by_artist=(ImageButton_define) mMenuView.findViewById(com.example.SimpleMusicPlayer.R.id.display_by_artist);
    	by_song=(ImageButton_define) mMenuView.findViewById(com.example.SimpleMusicPlayer.R.id.display_by_song);
    	treasure=(ImageButton_define) mMenuView.findViewById(com.example.SimpleMusicPlayer.R.id.display_treasure);
    	latest_play.setOnClickListener(this);
    	by_artist.setOnClickListener(this);
    	by_song.setOnClickListener(this);
    	treasure.setOnClickListener(this);
    	popupWindow=new PopupWindow(mMenuView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,true);
    	popupWindow.setAnimationStyle(com.example.SimpleMusicPlayer.R.style.popwin_anim_style);
    	ColorDrawable dw = new ColorDrawable(context.getResources().getColor(com.example.SimpleMusicPlayer.R.color.ccc));
    	popupWindow.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(this);
    }
    
    /**
     * 显示菜单
     */
    public void show()
    {
    	//得到当前activity的rootView
    	View rootView=((ViewGroup)mContext.findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		int height = mMenuView.findViewById(com.example.SimpleMusicPlayer.R.id.display1).getTop();
		int y=(int) event.getY();
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			if(y<height)
			{
            	popupWindow. dismiss();
            }
		}
		return true;
	}

	@Override
	public void onClick(View v) 
	{
		popupWindow.dismiss();
		clickListener.onClick(v);
	}
}
