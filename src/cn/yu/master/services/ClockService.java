package cn.yu.master.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import cn.yu.master.utils.AlarmAlertWakeLock;

public class ClockService extends Service {

	private boolean mPlaying = false;
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;
	private int mInitialCallState;

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
		android.util.Log.e("----------------", "TimerRingService  start...-----");
		startForeground(0, null);
		play();
		mInitialCallState = mTelephonyManager.getCallState();

		return START_STICKY;
	}

	private static final float IN_CALL_VOLUME = 0.125f;

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

		try {
			if (mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
				mMediaPlayer.setVolume(IN_CALL_VOLUME, IN_CALL_VOLUME);
				// setDataSourceFromResource(getResources(), mMediaPlayer,
				// R.raw.in_call_alarm);
			} else {
				AssetFileDescriptor afd = getAssets().openFd(
						"sounds/Timer_Expire.ogg");
				mMediaPlayer.setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
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
			player.setLooping(true);
			player.prepare();
			player.start();
		}
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
