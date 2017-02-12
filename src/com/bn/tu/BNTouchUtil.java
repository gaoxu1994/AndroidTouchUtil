package com.bn.tu;

import java.util.*;
import android.annotation.SuppressLint;
import android.util.FloatMath;
import android.view.*;

@SuppressLint("FloatMath")
public class BNTouchUtil 
{
	//ע��ļ������б�
	private static Map<String,TouchListener> eventMap= new HashMap<String,TouchListener>();
	
	//��������
	static boolean isMove=false;
	static float xStart;
	static float yStart;
	static float oldDist;		//����֮�䴥�ؾɵľ���
	static float oldAngle;		//����֮��ɵĽǶ�
	//�жϴ����Ƿ�Ϊһ��
	static boolean isOne=true;
	//���㴥�ز����ƶ���־λ
	//��ָmove���㰴�� ��¼Y��ֵ
	static float oldPointerY;
	//��ָƽ��ס�갴�¼�¼yֵ
	static float oldMainY;
	//��Ӽ������ķ���
	public static void addListener(String id,TouchListener tl)
	{
		eventMap.put(id, tl);
	}
	
	//ɾ���������ķ���
	public static void removeListener(String id)
	{
		eventMap.remove(id);
	}
	
	//ĳ�������¼�����ʱɨ��ע��������б��ص�
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
		//��ȡ��������
		int x = (int) e.getX();
		int y = (int) e.getY();
		//��ȡ���صĶ������
		int action=e.getAction()&MotionEvent.ACTION_MASK;		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN: //����down
				isMove=false;
				xStart=x;
				yStart=y;
				oldMainY=e.getY(0);
				isOne=true;
			break;
			case MotionEvent.ACTION_UP: //����up
				//����
				if(!isMove&&isOne)
				{
					//ǡ����ʱ���ص�
					processSpecEvent(EventType.DANJI,new float[]{x,y});
				}
			break;
			case MotionEvent.ACTION_POINTER_DOWN:	//���㰴��
				isOne=false;
				oldDist=spacing(e);
				oldAngle=rotation(e);
				oldPointerY=e.getY(1);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				isOne=false;
				break;
			case MotionEvent.ACTION_MOVE: 			//��/����move 
				if(e.getPointerCount()==1) 
				{
					//λ��ֵ������ֵ��Ϊ���ƶ�
					if(Math.abs(x-x)>20||Math.abs(y-yStart)>20)
					{
						isMove=true;
					}
					//��ָ�ƶ�������
					if(isMove&&isOne) 
					{
						//���ذ��µĵ�һ���������xstart,ystart
						//���ص�ǰ�ƶ����ĵ��x��y����
						processSpecEvent(EventType.MOVE, new float[]{xStart,yStart,x,y});
						xStart=x;
						yStart=y;
					}
					
				}else if(e.getPointerCount()==2)
				{
					//������ָ֮��Ĳ�ֵ
					float newDist = spacing(e);
					float dist=newDist-oldDist;
					
					//����������ת�ĽǶȲ�ֵ
					float newAngle = rotation(e);
					float angle_difference=newAngle-oldAngle;
					
					
					//��������ص��Yֵ
					float newMainY=e.getY(0);
					float newPointerY=e.getY(1);
					//�����ص��yֵ�ò�ֵ
					float Y_Main_cha=newMainY-oldMainY;
					float Y_Pointer_cha=newPointerY-oldPointerY;//�����ص�yֵ�ò�ֵ
					//���������y��ֵ�Ĳ�ֵ
					float Main_Pointer_Ycha=Y_Main_cha-Y_Pointer_cha;
					//�����¼�
					//��ָ֮��ļ��
					if(Math.abs(dist)>120)
					{	//����֮ǰ����ľ���͵�ǰ����ľ���
						processSpecEvent(EventType.SUOFANG, new float[]{oldDist,newDist});
						oldDist=newDist;
					}
					//˫ָ��ת�¼�
					if(Math.abs(angle_difference)>20)
					{
						//������һ�ؽǶȵ�ֵ�͵�ǰ�Ƕ�
						processSpecEvent(EventType.XUANZHUAN, new float[]{oldAngle,newAngle});
						oldAngle=newAngle;	
					}
					
					//˫ָƽ���ж��¼�
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
	//��������ľ���
	private static float spacing(MotionEvent event)
	{ 
        float x = event.getX(0) - event.getX(1); 
        float y = event.getY(0) - event.getY(1); 
        return FloatMath.sqrt(x * x + y * y); 
    }
	//������ת�ĽǶ�
	@SuppressLint("FloatMath")
	private static float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}
	
}
