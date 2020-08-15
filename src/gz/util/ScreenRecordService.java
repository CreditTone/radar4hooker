package gz.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

//首先呢实现的是Handle.Callback接口 主要是做时间及计时时间回调的,会重新写HandleMessage方法,其实我个觉得跟在括号{} 里面重写没啥区别就是简洁页面
public class ScreenRecordService {
	public ScreenRecordService() {}
	/**
	
	//这个类是管理类拿到服务后会通过下面的类申请录屏,点击允许，
	//其中会回调两个参数,code码 和 data,都在ActivityForResult中进行判断code
	private MediaProjectionManager mProjectionManager;
	//这个会拿到申请的结果
	private MediaProjection mMediaProjection;
	//这个类就是我们主要的录屏录音的类啦
	private MediaRecorder mMediaRecorder;
	//这个就是我们要获取录制屏幕的大小,像素,等等一些数据
	//关于这类更详细的介绍https://blog.csdn.net/qq_16628781/article/details/62038163
	private VirtualDisplay mVirtualDisplay;
	//是否正在录制 false 没有录制
	private boolean mIsRunning;
	//获取屏幕的宽高和像素密度 稍后贴类
	private int mRecordWidth = TalScreenParams.getScreenWidth();
	private int mRecordHeight = TalScreenParams.getScreenWidth();
	private int mScreenDpi = TalScreenParams.getScreenDpi();
	//code data 就是上面说的允许后回调
	private int mResultCode;
	private Intent mResultData;
	//录屏文件的保存地址
	private String mRecordFilePath;
	//已经录制多少秒了
	private int mRecordSeconds = 0;
	//handle发送消息时的what
	private static final int MSG_TYPE_COUNT_DOWN = 110;


	public ScreenRecordService() {
		// 没有录制
		mIsRunning = false;
		// 创建对象 在create方法里只执行一次
		mMediaRecorder = new MediaRecorder();
	}

	//数据不为空
	public boolean isReady() {
		return mMediaProjection != null && mResultData != null;
	}

	public void clearRecordElement() {
		clearAll();
		if (mMediaRecorder != null) {
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
		mResultData = null;
		// 不执行的时候false
		mIsRunning = false;
	}

	//这个就是给是否正在录屏提供一个get方法
	public boolean ismIsRunning() {
		return mIsRunning;
	}


	//开始录制了
	public boolean startRecord() {
		//代码执行顺序(false,目前没录屏)
		if (mIsRunning) {
			return false;
		}
		// 再次创建,防止异常
		if (mMediaProjection == null) {
			mMediaProjection = mProjectionManager.getMediaProjection(mResultCode, mResultData);
		}
		//关于文件路径还有录屏的一些参数问题
		setUpMediaRecorder();
		// 关于获取录制屏幕的大小,像素,等等一些数据
		createVirtualDisplay();
		// 开始录制
		mMediaRecorder.start();
		//稍后贴类 监听录制情况
		TalScreenUtils.startRecord();
		// 最多录制三分钟
		mHandler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, 1000);
		// 录制时为true
		mIsRunning = true;
		return true;
	}

	//停止的方法
	public boolean stopRecord(String tip) {
		// mIsRunning 默认值为false !mIsRunning 就是true。。
		if (!mIsRunning) {
			return false;
		}
		mIsRunning = false;
		try {
			// 停止抓取异常,该置空的为空
			mMediaRecorder.stop();
			mMediaRecorder.reset();
			mMediaRecorder = null;
			mVirtualDisplay.release();
			mMediaProjection.stop();
		} catch (Exception e) {
			e.printStackTrace();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
		mMediaProjection = null;
		//停止时移出这条消息what 
		mHandler.removeMessages(MSG_TYPE_COUNT_DOWN);
		// 停止的监听 tip 是处理一些突发情况 比如内存不足
		TalScreenUtils.stopRecord(tip);
		// 录制时间不到两秒就删除录制文件
		if (mRecordSeconds <= 2) {
			TalFileUtils.deleteSDFile(mRecordFilePath);
		} else {
			// 录制的视频库,将数据添加到媒体库
			// 这个算是应用程序之间共享数据,把自己应用的数据添加到手机的媒体库ContentResolver
			// 举个例子,代码添加手机联系人到自己的联系人列表,或者代码添加图片到自己的图库，还有不懂得，贴个链接
			// https://blog.csdn.net/bzlj2912009596/article/details/80248272
			TalFileUtils.fileScanVideo(this, mRecordFilePath, 1280, 720, mRecordSeconds);
		}
		//	mRecordFilePath = null;
		mRecordSeconds = 0;
		return true;
	}

	//下面是关于处理在一些activity or fragment中生命周期的做法
	public void pauseRecord() {
		if (mMediaRecorder != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				mMediaRecorder.pause();
			}
		}
	}

	public void resumeRecord() {
		if (mMediaRecorder != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				mMediaRecorder.resume();
			}
		}
	}

	//这个就是刚才讲过的 绘制窗口大小,dpi问题 VirtualDisplay
	private void createVirtualDisplay() {
		mVirtualDisplay = mMediaProjection.createVirtualDisplay("MainScreen", 1280, 720, mScreenDpi,
				DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
	}

	//这个主要是路径,还有设置一些录制视频参数问题separator 为字节,占位用
	private void setUpMediaRecorder() {
		mRecordFilePath = getSaveDirectory() + File.separator + System.currentTimeMillis() + ".mp4";
		if (mMediaRecorder == null) {
			mMediaRecorder = new MediaRecorder();
		}
		// 设置音频源
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);
		//设置视频源    
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
		//设置输出的编码格式 
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		if (mRecordFilePath != null) {
			mMediaRecorder.setOutputFile(mRecordFilePath);
		} else if (mRecordFilePath == null) {
			mMediaRecorder.setOutputFile(mRecordFilePath);
		}
		// 设置录屏时屏幕大小,这个可跟mVirtualDisplay 一起控制屏幕大小
		// mVirtualDisplay 是将屏幕设置成多大多小，setVideoSize是输出文件时屏幕多大多小
		mMediaRecorder.setVideoSize(1280, 720);
		// 图像编码 H264较好还有其他选择
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		//音频编码    
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		// 设置码率 高清的话的要数越大
		mMediaRecorder.setVideoEncodingBitRate((int) (1280 * 720 * 2.6));
		// 设置帧率，该帧率必须是硬件支持的，可以通过Camera.CameraParameter.getSupportedPreviewFpsRange()方法获取相机支持的帧率
		mMediaRecorder.setVideoFrameRate(20);
		try {
			// 准备
			mMediaRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearAll() {
		if (mMediaProjection != null) {
			mMediaProjection.stop();
			mMediaProjection = null;
		}
	}

	//路径
	public String getRecordFilePath() {
		return mRecordFilePath;
	}

	//sd下绝对路径,先判断sd卡是否挂载
	public String getSaveDirectory() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return null;
		}
	}

	//这是实现了Handle.CallBack中重写方法 handleMessage
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TYPE_COUNT_DOWN: {
			String str = null;
			// 这是内存
			boolean enough = TalFileUtils.getSDFreeMemory() / (1024 * 1024) < 4;
			if (enough) {
				// 空间不足，停止录屏
				str = "空间不足";
				// 停止录屏时 通过接口回调一个信息,是因为什么停止录屏的
				stopRecord(str);
				mRecordSeconds = 0;
				break;
			}
			mRecordSeconds++;
			int minute = 0, second = 0;
			if (mRecordSeconds >= 60) {
				minute = mRecordSeconds / 60;
				second = mRecordSeconds % 60;
			} else {
				second = mRecordSeconds;
			}
			TalScreenUtils.onRecording("0" + minute + ":" + (second < 10 ? "0" + second : second + ""));
			if (mRecordSeconds < 5 * 60) {
				mHandler.sendEmptyMessageDelayed(MSG_TYPE_COUNT_DOWN, 1000);
			} else if (mRecordSeconds == 5 * 60) {
				str = "录制已到限定时长";
				stopRecord(str);
				mRecordSeconds = 0;
			}
			break;
		}
		}
		return true;
	}
**/
}