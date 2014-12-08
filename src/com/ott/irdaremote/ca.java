package com.ott.irdaremote;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

class ca implements MediaPlayer.OnCompletionListener {
	ca(Context ctx) {
	}

	public void onCompletion(MediaPlayer paramMediaPlayer) {
		//this.a.y = false;
		Log.d("IrdaRemoter", "is_doing=false");
	}
}