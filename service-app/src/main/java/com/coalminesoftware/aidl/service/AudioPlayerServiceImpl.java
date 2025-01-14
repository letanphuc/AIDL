package com.coalminesoftware.aidl.service;

import android.os.IBinder;
import android.os.RemoteException;

public class AudioPlayerServiceImpl extends IAudioPlayerService.Stub {
	@Override
	public String getTrackTitle(int trackNumber) throws RemoteException {
		return "Your Favorite Song " + trackNumber;
	}

	@Override
	public IBinder asBinder() {
		return this;
	}
}
