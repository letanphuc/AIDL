package com.coalminesoftware.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.coalminesoftware.aidl.service.IAudioPlayerService;
import com.coalminesoftware.aidl.service.ServiceIntentBuilder;

public class MainActivity extends Activity {
	private static final String LOGGING_TAG = MainActivity.class.getSimpleName();

	private IAudioPlayerService audioPlayerService;
	private TextView label;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		label = findViewById(R.id.label);

		connectToService();
	}

	private void connectToService() {
		ServiceConnection connection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				audioPlayerService = IAudioPlayerService.Stub.asInterface(service);

				// Do work that was waiting for the service to connect
				try {
					String text = audioPlayerService.getTrackTitle(1);
					label.setText(text);
				} catch (RemoteException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				audioPlayerService = null;
			}
		};

		// The value returned by bindSearch() only indicates whether binding was successfully
		// initiated, based on preliminary checks like whether the service exists and whether the
		// caller has the necessary permissions to bind. Binding is not complete until
		// ServiceConnection#onServiceConnected() is called.
		//
		// Binding is asynchronous. However, the process that calls onServiceConnected() gets queued
		// and is likely to (eventually) be run by the same thread that called bindService().
		// Because of that, it is not possible to block the thread that calls bindService() until
		// onServiceConnected() is called.
		boolean bindingRequestedSuccessfully = bindService(
				ServiceIntentBuilder.buildAudioPlayerBindIntent(),
				connection,
				Context.BIND_AUTO_CREATE);

		Log.i(LOGGING_TAG, "Binding requested successfully: " + bindingRequestedSuccessfully);
	}
}
