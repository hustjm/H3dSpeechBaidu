package com.h3dspeech_plugin.baidu;

import android.text.format.Time;

/**
 * 得到系统时间
 */
public class GetSystemDateTime {
	
	/*
	 * 得到系统时间 
	 */
	public static String GetCurrentTime()
	  {
	    Time localTime = new Time();
	    localTime.setToNow();
	    return localTime.format("%Y%m%d%H%M%S");
	  }
}
