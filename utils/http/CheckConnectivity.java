package com.ip.barcodescanner.utils.http;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckConnectivity {

	public static boolean check(final Context context) {
		ConnectivityManager conxMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobileNwInfo = conxMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNwInfo = conxMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (((mobileNwInfo == null ? false : mobileNwInfo.isAvailable()) || (wifiNwInfo == null ? false
				: wifiNwInfo.isAvailable()))) {
			if (((mobileNwInfo == null ? false : mobileNwInfo.isConnected()) || (wifiNwInfo == null ? false
					: wifiNwInfo.isConnected()))) {
				return true;
			} else {
				errorMsg(context);
				return false;
			}
		} else {
			errorMsg(context);
			return false;
		}
	}

	private static void errorMsg(final Context context) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, "Connect to Internet",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	public static boolean hasActiveInternetConnection(Context context) {
		if (check(context)) {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL(
						"http://www.google.com").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1500);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				errorMsg(context);
			}
		} else {
			errorMsg(context);
		}
		return false;
	}
}
