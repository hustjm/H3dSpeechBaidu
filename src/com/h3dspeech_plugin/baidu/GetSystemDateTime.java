package com.h3dspeech_plugin.baidu;

import android.text.format.Time;

/**
 * �õ�ϵͳʱ��
 */
public class GetSystemDateTime {
	
	/*
	 * �õ�ϵͳʱ�� 
	 */
	public static String GetCurrentTime()
	  {
	    Time localTime = new Time();
	    localTime.setToNow();
	    return localTime.format("%Y%m%d%H%M%S");
	  }
}
