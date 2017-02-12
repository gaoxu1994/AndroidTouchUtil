package com.bn.tu;

import java.util.*;
import android.annotation.SuppressLint;
import android.util.FloatMath;
import android.view.*;

@SuppressLint("FloatMath")
public class BNTouchUtil 
{
	//注册的监听器列表
	private static Map<String,TouchListener> eventMap= new HashMap<String,TouchListener>();
	
	//辅助变量
	static boolean isMove=false;
	static float xStart;
	static float yStart;
	static float oldDist;		//两点之间触控旧的距离
	static float oldAngle;		//两点之间旧的角度
	//判断触控是否为一点
	static boolean isOne=true;
	//两点触控并且移动标志位
	//两指move辅点按下 记录Y的值
	static float oldPointerY;
	//两指平移住店按下记录y值
	static float oldMainY;
	//添加监听器的方法
	public static void addListener(String id,TouchListener tl)
	{
		eventMap.put(id, tl);
	}
	
	//删除监听器的方法
	public static void removeListener(String id)
	{
		eventMap.remove(id);
	}
	
	//某个类型事件发生时扫描注册监听器列表并回调
	private static void processSpecEvent(EventType et,float[] eventData)
	{
		Set<String> ks=eventMap.keySet();
		for(String id:ks)
		{
			TouchListener tl=eventMap.get(id);
			if(et==tl.et)
			{
				tl.eventOccurrence(eventData);
			}
		}
	}
	public static void processEvent(MotionEvent e)
	{		
		//获取触控坐标
		int x = (int) e.getX();
		int y = (int) e.getY();
		//获取触控的动作编号
		int action=e.getAction()&MotionEvent.ACTION_MASK;		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN: //主点down
				isMove=false;
				xStart=x;
				yStart=y;
				oldMainY=e.getY(0);
				isOne=true;
			break;
			case MotionEvent.ACTION_UP: //主点up
				//单击
				if(!isMove&&isOne)
				{
					//恰当的时机回调
					processSpecEvent(EventType.DANJI,new float[]{x,y});
				}
			break;
			case MotionEvent.ACTION_POINTER_DOWN:	//辅点按下
				isOne=false;
				oldDist=spacing(e);
				oldAngle=rotation(e);
				oldPointerY=e.getY(1);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				isOne=false;
				break;
			case MotionEvent.ACTION_MOVE: 			//主/辅点move 
				if(e.getPointerCount()==1) 
				{
					//位移值大于阈值认为是移动
					if(Math.abs(x-x)>20||Math.abs(y-yStart)>20)
					{
						isMove=true;
					}
					//单指移动处理方法
					if(isMove&&isOne) 
					{
						//传回按下的第一个点的坐标xstart,ystart
						//传回当前移动到的点的x，y坐标
						processSpecEvent(EventType.MOVE, new float[]{xStart,yStart,x,y});
						xStart=x;
						yStart=y;
					}
					
				}else if(e.getPointerCount()==2)
				{
					//计算两指之间的差值
					float newDist = spacing(e);
					float dist=newDist-oldDist;
					
					//计算两次旋转的角度差值
					float newAngle = rotation(e);
					float angle_difference=newAngle-oldAngle;
					
					
					//获得主触控点的Y值
					float newMainY=e.getY(0);
					float newPointerY=e.getY(1);
					//主触控点的y值得差值
					float Y_Main_cha=newMainY-oldMainY;
					float Y_Pointer_cha=newPointerY-oldPointerY;//辅触控点y值得差值
					//主副两点的y差值的差值
					float Main_Pointer_Ycha=Y_Main_cha-Y_Pointer_cha;
					//缩放事件
					//两指之间的间距
					if(Math.abs(dist)>120)
					{	//传回之前两点的距离和当前两点的距离
						processSpecEvent(EventType.SUOFANG, new float[]{oldDist,newDist});
						oldDist=newDist;
					}
					//双指旋转事件
					if(Math.abs(angle_difference)>20)
					{
						//传回上一回角度的值和当前角度
						processSpecEvent(EventType.XUANZHUAN, new float[]{oldAngle,newAngle});
						oldAngle=newAngle;	
					}
					
					//双指平移判断事件
					if(Math.abs(Main_Pointer_Ycha)<10&&(Math.abs(Y_Main_cha)>10&&(Math.abs(Y_Pointer_cha)>10)))
					{
						BNTouchUtil.processSpecEvent(EventType.SHUANGZHIMOVE, new float[]{oldMainY,newMainY,oldPointerY,newPointerY});
						oldMainY=newMainY;
						oldPointerY=newPointerY;
					}
				}
			break;
		}		
	}
	//计算两点的距离
	private static float spacing(MotionEvent event)
	{ 
        float x = event.getX(0) - event.getX(1); 
        float y = event.getY(0) - event.getY(1); 
        return FloatMath.sqrt(x * x + y * y); 
    }
	//计算旋转的角度
	@SuppressLint("FloatMath")
	private static float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}
	
}
