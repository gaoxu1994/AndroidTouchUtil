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
	//初始位置
	public float currentX = 200;
    public float currentY = 300;
    //初始缩放比
    public float currScale=0.8f;
    //初始旋转角度
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
        //创建画笔
        Paint p = new Paint();
        //加载图片
        Bitmap bitmapTmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.left_one);
        //平移矩阵
      	Matrix m1=new Matrix();
		m1.setTranslate(currentX-bitmapTmp.getWidth()/2,currentY-bitmapTmp.getHeight()/2);
		//旋转矩阵
		Matrix m2=new Matrix();
		m2.setRotate(currRotate,bitmapTmp.getWidth()/2,bitmapTmp.getHeight()/2);
		//旋转平移矩阵
		Matrix mz=new Matrix();
		mz.setConcat(m1,m2);
		Matrix m3=new Matrix();
		//判断缩放比
		if(currScale<0.5f)
		{
			currScale=0.5f;
		}
		m3.setScale(currScale,currScale,bitmapTmp.getWidth()/2,bitmapTmp.getHeight()/2);
		Matrix mzz=new Matrix();
		mzz.setConcat(mz,m3);
		canvas.drawBitmap(bitmapTmp, mzz, p);
//===================================注册监听器==========================================
		//添加触控单击监听器
		TouchListener tl=new TouchListener(EventType.DANJI)
		{
			public void eventOccurrence(float[] eventData)
			{
				currentX=eventData[0];
				currentY=eventData[1];
				//Log.d("单击", currentX+":"+currentY);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("dj", tl);
		//添加移动监听器
		TouchListener t2=new TouchListener(EventType.MOVE)
		{
			public void eventOccurrence(float[] eventData)
			{
				currentX=currentX+(eventData[2]-eventData[0]);
				currentY=currentY+(eventData[3]-eventData[1]);
				//Log.d("移动", eventData[0]+":"+eventData[1]);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("mv", t2);
		//添加缩放监听器
		TouchListener t3=new TouchListener(EventType.SUOFANG)
		{
			public void eventOccurrence(float[] eventData)
			{
				float chazhi=eventData[0]-eventData[1];
				currScale=currScale-(chazhi/200);
				//Log.d("缩放", "oldDist:"+eventData[0]+"newDist:"+eventData[1]);
				MainActivity.hd.sendEmptyMessage(1);
			}
		};
		BNTouchUtil.addListener("sf", t3);
		
		//添加旋转监听器
		TouchListener t4=new TouchListener(EventType.XUANZHUAN)
		{
			public void eventOccurrence(float[] eventData)
			{
				float angle_chazhi=eventData[0]-eventData[1];
				currRotate=currRotate-angle_chazhi;
				MainActivity.hd.sendEmptyMessage(1);
			//	Log.d("旋转", "oldAngle:"+eventData[0]+"newAngle:"+eventData[1]);
			}
		};
		BNTouchUtil.addListener("xz", t4);	
		//双指平移监听器
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
    	//通知改组件重绘
    	this.invalidate();
    	return true;
    }

}
