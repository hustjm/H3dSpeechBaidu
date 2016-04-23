package com.h3dspeech_plugin.baidu;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.speech.VoiceRecognitionService;
//import com.baidu.speech.VoiceClientStatusC
//import com.h3dspeech.speech.R;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.InputStream;
//import java.io.InputStream.android.media.AmrInputStream;

import com.h3dspeech_plugin.baidu.H3dSpeechDefaultPara;

public class H3dSpeech_Baidu extends Activity {
	
	//view source
	
	private static Context runContext;
	private static Button button_start;
	private static Button button_stop;
	private static TextView showText;
	
	//para
	private static int appID;				//申请到的appid
	private static String appKey;			//申请到的API_KEY
	private static String appSecretKey;		//申请到的SECRET_KEY
	private static String language;			//语言，"cmn-Hans-CN"普通话，"sichuan-Hans-CN"四川话，"yue-Hans-CN"粤语
	private static int sampleRate;			//采样频率，建议16000
	private static String audioFileDir;		//音频文件啊存放文件夹
	private static int outTimeLimit;		//超时设置，暂定为10000ms
	private static String asrBaseFilePath;	//离线包地址
	private static String licenseFilePath;	//支持离线语音识别的授权文件
	private static int prop;				//垂直领域,建议设为20000
	
	private static String paraJson;
	private static String retJson;
	 			
	//results
	private static String nameOrgAudioFile;		//原始录音文件的文件名
	private static String pathOrgAudioFile;		//原始录音文件的存放路径
	private static int lengthOfAudio;			//录音文件的时长
	private static int costTime;				//识别过程所用耗时
	private static String textResult;			//识别出来的文字结果
	//private static String nameCompressedAudioFile;		//**** 预留，压缩后的音频文件文件名
	//private static String pathCompressedAudioFile;		//**** 预留，压缩后的音频文件路径
	
	//for input control
	
	private static int checkTimeUnit = 100;
	private static int runTimeCount;
	private static AudioManager mAudioManager;
	private static int currentVolume;
	private static Handler mHandlerStart=new Handler();
	private static Handler mHandlerStop =new Handler(){
		public void handleMessage(Message _msg){
			switch(_msg.what){
			case 1:
				Stop();
				break;
			
			}
		}
	};
	private static Runnable timeCountThread = new Runnable(){
		public void run(){
			showText.setText("input time count: "+runTimeCount+"ms");
			if(runTimeCount<outTimeLimit){
				//继续计时
				mHandlerStop.postDelayed(timeCountThread, checkTimeUnit);
				runTimeCount = runTimeCount+checkTimeUnit;
			}else{   //输入超时，停掉输入，获取输入的转化结果，停掉该线程
				Message msg=new Message();
				msg.what=1;
				mHandlerStop.sendMessage(msg);
			}
		}
	};
	private static Runnable timerCommonThread = new Runnable(){
		public void run(){
			mHandlerStop.postDelayed(timerCommonThread, checkTimeUnit);
			runTimeCount = runTimeCount+checkTimeUnit;
		}
	};
	
	//for Baidu Recognize service
	
	// online recognize
	
	
	
	private static SpeechRecognizer mSpeechRecognizer;
	private static Intent recogPara = new Intent();
	private static RecognitionListener mListener = new RecognitionListener(){

		@Override
		public void onReadyForSpeech(Bundle params) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBeginningOfSpeech() {
			// TODO Auto-generated method stub
			//showText.setText("voice input begin");
		}

		@Override
		public void onRmsChanged(float rmsdB) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
			//showText.setText("voice input end");
			
		}

		@Override
		public void onError(int error) {
			// TODO Auto-generated method stub
			mSpeechRecognizer.cancel();
			mSpeechRecognizer.destroy();
			mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(runContext,
					new ComponentName(runContext, VoiceRecognitionService.class));
			mSpeechRecognizer.setRecognitionListener(mListener);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			//停止timerCommonThread,输出识别所用的耗时
			mHandlerStop.removeCallbacks(timerCommonThread);
			costTime = runTimeCount;
			showText.setText("Error:"+error+"\n"+
							 "Cost Time: "+costTime);
						
		}

		@Override
		public void onResults(Bundle results) {
			// TODO Auto-generated method stub
			
			//mSpeechRecognizer.destroy();
			//mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(runContext,
			//		new ComponentName(runContext, VoiceRecognitionService.class));
			//mSpeechRecognizer.setRecognitionListener(mListener);
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
			//停止timerCommonThread,输出识别所用的耗时
			mHandlerStop.removeCallbacks(timerCommonThread);
			
			
			ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
	        //print("识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
	        //String json_res = results.getString("origin_result");
	        //try {
	            //print("origin_result=\n" + new JSONObject(json_res).toString(4));
	        //} catch (Exception e) {
	            //print("origin_result=[warning: bad json]\n" + json_res);
	        //}
	        //btn.setText("开始");
	        String end = "\n";
	        mHandlerStart.removeCallbacks(timeCountThread);
	        costTime = runTimeCount;
	        textResult = nbest.get(0);
	        showText.setText("result: "+textResult + end+
	        		         "Recording Time: "+lengthOfAudio+"ms"+end+
	        		         "CostTime:"+costTime+"ms"+end+
	        		         "Audio File Path: " + pathOrgAudioFile);
		}

		@Override
		public void onPartialResults(Bundle partialResults) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEvent(int eventType, Bundle params) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public static void PaseJsonPara(String jsonPara) {
		
		JSONObject tmpJson;
		try{
			tmpJson = new JSONObject(jsonPara);
			appID = tmpJson.optInt("appID", H3dSpeechDefaultPara.appID);
			appKey = tmpJson.optString("appKey", H3dSpeechDefaultPara.appKey);
			appSecretKey = tmpJson.optString("appSecretKey", H3dSpeechDefaultPara.appSecretKey);
			language = tmpJson.optString("language", H3dSpeechDefaultPara.language);
			sampleRate = tmpJson.optInt("sampleRate", H3dSpeechDefaultPara.sampleRate);
			audioFileDir = tmpJson.optString("audioFileDir", H3dSpeechDefaultPara.GetAudioFileDir());
			outTimeLimit = tmpJson.optInt("outTimeLimit", H3dSpeechDefaultPara.outTimeLimit);
			asrBaseFilePath = tmpJson.optString("asrBaseFilePath", H3dSpeechDefaultPara.asrBaseFilePath);
			prop = tmpJson.optInt("prop", H3dSpeechDefaultPara.prop);
			checkTimeUnit = tmpJson.optInt("checkTimeUnit", H3dSpeechDefaultPara.checkTimeUnit);
			
		}catch(JSONException ex){
			;
		}
		
	}
	
	public static void ConstrucJsonRet(){
		JSONObject tmpJson;
		try{
			tmpJson = new JSONObject();
			tmpJson.put("audioLength", lengthOfAudio);
			tmpJson.put("audioPath", pathOrgAudioFile);
			tmpJson.put("textResult", textResult);
			
			retJson = tmpJson.toString();
			
		}catch(JSONException ex){
			
		}
	}
	
	
	//
	public static void Init(   int    _appID,
							   String _appKey,
							   String _appSecretKey,
							   String _language,
							   int    _sampleRate,
							   String _audioFileDir,
							   int    _outTimeLimit,
							   String _asrBaseFilePath,
							   String _licenseFilePath,
							   int    _prop){
		
		
		appID		= _appID;
		appKey		= _appKey;
		appSecretKey= _appSecretKey;
		language	= _language;
		sampleRate	= _sampleRate;
		audioFileDir= _audioFileDir;
		outTimeLimit= _outTimeLimit;
		asrBaseFilePath = _asrBaseFilePath;
		licenseFilePath = _licenseFilePath;
		prop			= _prop;
		
		
		
		
		/*
		RunCodeInMainThread(new Runnable(){
			public void run(){
				mSpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(runContext,
						new ComponentName(runContext, VoiceRecognitionService.class));
				mSpeechRecognizer.setRecognitionListener(mListener);
			}		
		});
		*/
	}
	
	
	/*
	private static void RunCodeInMainThread(Runnable runnable) {
		// TODO Auto-generated method stub
		runView.post(runnable);
		
	}
	*/



	public static void Start(){
		runTimeCount=0; //计时器归零
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		setRecogPara();
		mSpeechRecognizer.startListening(recogPara);
		mHandlerStart.post(timeCountThread); //开始计时
	}
	
	private static void setRecogPara() {
		// TODO Auto-generated method stub
		recogPara.putExtra("appid", appID);
		recogPara.putExtra("key",appKey);
		recogPara.putExtra("secret", appSecretKey);
		recogPara.putExtra("language", language);
		recogPara.putExtra("sample", sampleRate);
		
		File dir=new File(audioFileDir);
		if(dir.exists()){			
		}else{
			dir.mkdirs();
		}
		nameOrgAudioFile="SpeechAudio_"+GetSystemDateTime.GetCurrentTime();
		pathOrgAudioFile=audioFileDir + "/" + nameOrgAudioFile;
		recogPara.putExtra("outfile", pathOrgAudioFile);
		
		recogPara.putExtra("asr-base-file-path", asrBaseFilePath);
		recogPara.putExtra("license-file-path", licenseFilePath);
		recogPara.putExtra("prop", prop);		
	}



	public static void Stop(){
		mSpeechRecognizer.stopListening();
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
		mHandlerStop.removeCallbacks(timeCountThread);
		lengthOfAudio = runTimeCount;
		runTimeCount=0;
		mHandlerStart.post(timerCommonThread);
		
		
		//just for test view display
		button_start.setEnabled(true);
		button_stop.setEnabled(false);
	}
	
	public static String GetAudioPath(){
		return pathOrgAudioFile;
		
	}
	
	public static int GetRecognizeCostTime(){
		return costTime;
	}
	
	public static int GetAudioLength(){
		return lengthOfAudio;
	}
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		runContext = this;
		
		mAudioManager =  (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		mSpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(runContext,
				new ComponentName(runContext, VoiceRecognitionService.class));
		mSpeechRecognizer.setRecognitionListener(mListener);
		initView();
		Init(H3dSpeechDefaultPara.appID,
			 H3dSpeechDefaultPara.appKey,
			 H3dSpeechDefaultPara.appSecretKey,
			 H3dSpeechDefaultPara.language,
			 H3dSpeechDefaultPara.sampleRate,
			 H3dSpeechDefaultPara.GetAudioFileDir(),
			 H3dSpeechDefaultPara.outTimeLimit,
			 H3dSpeechDefaultPara.asrBaseFilePath,
			 H3dSpeechDefaultPara.licenseFilePath,
			 H3dSpeechDefaultPara.prop);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.h3d_speech__baidu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void initView(){
		button_start = (Button)this.findViewById(R.id.button_start);
		button_stop  = (Button)this.findViewById(R.id.button_stop);
		showText 	 = (TextView)this.findViewById(R.id.text_output);
		
		button_start.setEnabled(true);
		button_stop.setEnabled(false);
		
		//button_start.setLongClickable(true);
		
		button_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	showText.setText("语音输入中...");
                Start();
                button_start.setEnabled(false);
                button_stop.setEnabled(true);
                                              
            }
        });		
		
		button_stop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Stop();
				
			}
		});
	}
}
