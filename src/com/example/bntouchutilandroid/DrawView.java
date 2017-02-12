package com.example.bntouchutilandroid;

import com.bn.tu.BNTouchUtil;
import com.bn.tu.EventType;
import com.bn.tu.TouchListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	//��ʼλ��
	public float currentX = 200;
    public float currentY = 300;
    //��ʼ���ű�
    public float currScale=0.8f;
    //��ʼ��ת�Ƕ�
    public float currRotate=0f;
    public Context context;
    public DrawView(Context context , AttributeSet set)
    {
        super(context , set);
        this.context=context;
    }
    @SuppressLint("ShowToast")
	@Override
    public void onDraw (Canvas canvas)
    {
        super.onDraw(canvas);
        //��������
        Paint p = new Paint();
        //����ͼƬ
        Bitmap bitmapTmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.left_one);
        //ƽ�ƾ���
      	Matrix m1=new Matrix();
		m1.setTranslate(currentX-bitmapTmp.getWidth()/2,currentY-bitmapTmp.getHeight()/2);
		//��ת����
		Matrix m2=new Matrix();
		m2.setRotate(currRotate,bitmapTmp.getWidth()/2,bitmapTmp.getHeight()/2);
		//��תƽ�ƾ���
		Matrix mz=new Matrix();
		mz.setConcat(m1,m2);
		Matrix m3=new Matrix();
		//�ж����ű�
		if(currScale<0.5f)
		{
			currScale=0.5f;
		}
		m3.setScale(currScale,currScale,bitmapTmp.getWidth()/2,bitmapTmp.getHeight()/2);
		Matrix mzz=new Matrix();
		mzz.setConcat(mz,m3);
		canvas.drawBitmap(bitmapTmp, mzz, p);
//===================================ע�������==========================================
		//��Ӵ��ص���������
		TouchListener tl=new TouchListener(EventType.DANJI)
		{
			public void eventOccurrence(float[] eventData)
			{
				currentX=eventData[0];
				currentY=eventData[1];
				//Log.d("����", currentX+":"+currentY);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("dj", tl);
		//����ƶ�������
		TouchListener t2=new TouchListener(EventType.MOVE)
		{
			public void eventOccurrence(float[] eventData)
			{
				currentX=currentX+(eventData[2]-eventData[0]);
				currentY=currentY+(eventData[3]-eventData[1]);
				//Log.d("�ƶ�", eventData[0]+":"+eventData[1]);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("mv", t2);
		//������ż�����
		TouchListener t3=new TouchListener(EventType.SUOFANG)
		{
			public void eventOccurrence(float[] eventData)
			{
				float chazhi=eventData[0]-eventData[1];
				currScale=currScale-(chazhi/200);
				//Log.d("����", "oldDist:"+eventData[0]+"newDist:"+eventData[1]);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("sf", t3);
		
		//�����ת������
		TouchListener t4=new TouchListener(EventType.XUANZHUAN)
		{
			public void eventOccurrence(float[] eventData)
			{
				float angle_chazhi=eventData[0]-eventData[1];
				currRotate=currRotate-angle_chazhi;
				MainActivity.hd.sendEmptyMessage(1);
			//	Log.d("��ת", "oldAngle:"+eventData[0]+"newAngle:"+eventData[1]);
			}
		};
		BNTouchUtil.addListener("xz", t4);	
		//˫ָƽ�Ƽ�����
		TouchListener t5=new TouchListener(EventType.SHUANGZHIMOVE)
		{
			public void eventOccurrence(float[] eventData)
			{
				MainActivity.hd.sendEmptyMessage(0);
			}
		};
		BNTouchUtil.addListener("sm", t5);
		//MainActivity.hd.sendEmptyMessage(1);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	BNTouchUtil.processEvent(event);
    	//֪ͨ������ػ�
    	this.invalidate();
    	return true;
    }

}
