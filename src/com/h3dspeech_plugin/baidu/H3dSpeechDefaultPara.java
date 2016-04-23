package com.h3dspeech_plugin.baidu;

import android.os.Environment;

public class H3dSpeechDefaultPara {
	
	//default paras list
	public static final int appID=8006086;							//申请到的appid
	public static final String appKey="4alevdho2FYah7Xj31BUUq66";			//申请到的API_KEY
	public static final String appSecretKey="7ce247df35e645546a5f01cff9ca73af";	//申请到的SECRET_KEY
	public static final String language="cmn-Hans-CN";	//语言，"cmn-Hans-CN"普通话，"sichuan-Hans-CN"四川话，"yue-Hans-CN"粤语
	public static final int sampleRate=16000;			//采样频率，建议16000
	public static final String audioFileDir=GetAudioFileDir();//音频文件啊存放文件夹
	public static final int outTimeLimit=10000;		//超时设置，暂定为10000ms
	public static final String asrBaseFilePath="/offline/s_2_InputMethod";	//离线包地址
	public static final String licenseFilePath="/offline/temp_license_2016-04-16";	//支持离线语音识别的授权文件
	public static final int prop=20000;				//垂直领域,建议设为20000
	public static final int checkTimeUnit = 100;
	
	private static final String audio_file_dir  = "/H3d_BaiduAudio";
	
	
	
	public static String GetAsrBaseFilePath(){
		return GetAudioFileDir()+asrBaseFilePath;
	}
	
	public static String GetLicenceFilePath(){
		return GetAudioFileDir()+licenseFilePath;
	}
	
	public static String GetAudioFileDir(){
		return Environment.getExternalStorageDirectory().toString()+audio_file_dir;
	}
	
	public static String GetSdCardPath(){
		String sdDir=null;
		if(isHaveSDcard()){
			sdDir = Environment.getExternalStorageDirectory().toString();//获得根目录
		}
		return sdDir;
	}
	
	public static boolean isHaveSDcard(){
		//判断SD卡是否存在 存在返回true 不存在返回false
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	

}
