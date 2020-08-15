package gz.util;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.view.Display;
import android.widget.Toast;
import gz.radar.Android;

public class XScreenRecord {
	
	public static XScreenRecord xScreenRecord;
	
	public static void startRecord() throws Exception {
		if (xScreenRecord == null) {
			xScreenRecord = new XScreenRecord();
		}
		xScreenRecord.start();
	}
	
	public static void stopRecord() throws Exception {
		xScreenRecord.stop();
	}

	private Application application;
	private MediaProjectionManager projectionManager;
	private MediaProjection mediaProjection;
	private VirtualDisplay virtualDisplay;
	private Activity topActivity;
	private MediaRecorder mediaRecorder;
	private boolean running;

	public XScreenRecord() throws Exception {
		application = Android.getApplication();
		Android.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
		Android.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		projectionManager = (MediaProjectionManager) application.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
	}

	public void start() throws Exception {
		topActivity = Android.getTopActivity();
		Intent captureIntent = projectionManager.createScreenCaptureIntent();
		topActivity.startActivityForResult(captureIntent, 3121);
		
	}

	public void onActivityResult(int resultCode, Intent data) {
		mediaProjection = projectionManager.getMediaProjection(resultCode, data);
		if (mediaProjection == null || running) {
			return;
		}
		createRecorder();
		createVirtualDisplay();
		mediaRecorder.start();
		running = true;
		topActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(application, "开始录屏", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void createVirtualDisplay() {
		Display display = topActivity.getWindowManager().getDefaultDisplay();
		virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", display.getWidth(), display.getHeight(),
				120, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
	}

	private void createRecorder() {
		Display display = topActivity.getWindowManager().getDefaultDisplay();
		mediaRecorder = new MediaRecorder();
		File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".mp4");
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.REMOTE_SUBMIX);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setOutputFile(file.getAbsolutePath());
		mediaRecorder.setVideoSize(display.getWidth(), display.getHeight());
		mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
		mediaRecorder.setVideoFrameRate(30);
		try {
			mediaRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean stop() {
		if (!running) {
			return false;
		}
		running = false;
		mediaRecorder.stop();
		mediaRecorder.reset();
		virtualDisplay.release();
		mediaProjection.stop();
		return true;
	}
}
