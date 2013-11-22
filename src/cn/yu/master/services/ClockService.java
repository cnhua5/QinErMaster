package cn.yu.master.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import cn.yu.master.utils.AlarmAlertWakeLock;

public class ClockService extends Service {

	private static final String tag = "ClockService";

	private static final float IN_CALL_VOLUME = 0.125f;

	private boolean mPlaying = false;
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;
	private int mInitialCallState;
	private int loopCount = 1;

	private String alarmAudioPath;

	private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String ignored) {
			if (state != TelephonyManager.CALL_STATE_IDLE
					&& state != mInitialCallState) {
				stopSelf();
			}
		}
	};

	@Override
	public void onCreate() {
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		AlarmAlertWakeLock.acquireScreenCpuWakeLock(this);
	}

	@Override
	public void onDestroy() {
		Log.e(tag, "Clock Service destory.....");
		stop();
		mTelephonyManager.listen(mPhoneStateListener, 0);
		AlarmAlertWakeLock.releaseCpuLock();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}
		android.util.Log.e("----------------", "TimerRingService  start..");
		alarmAudioPath = intent.getStringExtra("AlarmAudioPath");
		play();
		mInitialCallState = mTelephonyManager.getCallState();
		return START_STICKY;
	}

	private void play() {

		if (mPlaying) {
			return;
		}
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mp.stop();
				mp.release();
				mMediaPlayer = null;
				return true;
			}
		});
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.e(tag, "play complete.....loopCount = " + loopCount);
				if (loopCount > 0) {
					mp.start();
					loopCount--;
				}
			}
		});

		try {
			if (mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
				mMediaPlayer.setVolume(IN_CALL_VOLUME, IN_CALL_VOLUME);
				// setDataSourceFromResource(getResources(), mMediaPlayer,
				// R.raw.in_call_alarm);
			} else {
				if (alarmAudioPath != null) {
					mMediaPlayer.setDataSource(alarmAudioPath);
				} else {
					AssetFileDescriptor afd = getAssets().openFd(
							"sounds/song.mp3");
					mMediaPlayer.setDataSource(afd.getFileDescriptor(),
							afd.getStartOffset(), afd.getLength());
				}
			}
			startAlarm(mMediaPlayer);
		} catch (Exception ex) {
			try {
				// Must reset the media player to clear the error state.
				mMediaPlayer.reset();
				// setDataSourceFromResource(getResources(), mMediaPlayer,
				// R.raw.fallbackring);
				startAlarm(mMediaPlayer);
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
		mPlaying = true;
	}

	private void startAlarm(MediaPlayer player) throws java.io.IOException,
			IllegalArgumentException, IllegalStateException {
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.setVolume((float) 0.1, (float) 0.1);
			player.prepare();
			loopCount = setLoopPlayCount(player.getDuration());
			player.start();
		}
	}

	private int setLoopPlayCount(long duration) {
		int loopCount = Math.round((60 * 1000) / duration);
		Log.e(tag, "loop count is " + loopCount + "  duration = " + duration);
		return loopCount;
	}

	private void setDataSourceFromResource(Resources resources,
			MediaPlayer player, int res) throws java.io.IOException {
		AssetFileDescriptor afd = resources.openRawResourceFd(res);
		if (afd != null) {
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			afd.close();
		}
	}

	public void stop() {
		if (mPlaying) {
			mPlaying = false;

			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		}
	}

}
