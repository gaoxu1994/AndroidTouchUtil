package com.example.bntouchutilandroid;

import org.w3c.dom.Text;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	static Handler hd;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hd=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				TextView tv=(TextView)findViewById(R.id.TextView01);
				switch(msg.what)
				{
				case 0:
					tv.setText("Ë«Ö¸Æ½ÒÆ");
					break;
				case 1:
					tv.setText("");
					break;
				}
			}
			
			
		};
	}
}
