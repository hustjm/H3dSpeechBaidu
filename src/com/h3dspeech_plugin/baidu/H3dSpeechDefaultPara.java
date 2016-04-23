package com.h3dspeech_plugin.baidu;

import android.os.Environment;

public class H3dSpeechDefaultPara {
	
	//default paras list
	public static final int appID=8006086;							//���뵽��appid
	public static final String appKey="4alevdho2FYah7Xj31BUUq66";			//���뵽��API_KEY
	public static final String appSecretKey="7ce247df35e645546a5f01cff9ca73af";	//���뵽��SECRET_KEY
	public static final String language="cmn-Hans-CN";	//���ԣ�"cmn-Hans-CN"��ͨ����"sichuan-Hans-CN"�Ĵ�����"yue-Hans-CN"����
	public static final int sampleRate=16000;			//����Ƶ�ʣ�����16000
	public static final String audioFileDir=GetAudioFileDir();//��Ƶ�ļ�������ļ���
	public static final int outTimeLimit=10000;		//��ʱ���ã��ݶ�Ϊ10000ms
	public static final String asrBaseFilePath="/offline/s_2_InputMethod";	//���߰���ַ
	public static final String licenseFilePath="/offline/temp_license_2016-04-16";	//֧����������ʶ�����Ȩ�ļ�
	public static final int prop=20000;				//��ֱ����,������Ϊ20000
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
			sdDir = Environment.getExternalStorageDirectory().toString();//��ø�Ŀ¼
		}
		return sdDir;
	}
	
	public static boolean isHaveSDcard(){
		//�ж�SD���Ƿ���� ���ڷ���true �����ڷ���false
		return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	

}
