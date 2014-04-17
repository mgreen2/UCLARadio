package com.mgapps.uclaradio;

import java.io.IOException;

import android.app.Notification;
//import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class UCLARadioService extends Service implements MediaPlayer.OnPreparedListener {
    //private static String ACTION_PLAY = "com.mgapp.uclaradio.ACTION_PLAY";
    private MediaPlayer radio = null;
    
    /** Enable wifi lock
     */
    private WifiLock wifiLock; 
    
    
    public void onCreate(){
    	String url = "http://stream.uclaradio.com:8000/listen"; // UCLA Radio's URL
        radio = new MediaPlayer();
        radio.setAudioStreamType(AudioManager.STREAM_MUSIC);
        radio.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
        		.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        
        /*try/catch block for set data source */
        try {
			radio.setDataSource(url);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        radio.setOnPreparedListener(this);
        radio.prepareAsync(); // prepare async to not block main thread
        wifiLock.acquire();
        
        /**Provide notification that the service is running**/
        //String ns = Context.NOTIFICATION_SERVICE;
        //NotificationManager nm = (NotificationManager) getSystemService(ns);
        
        long time = System.currentTimeMillis();
        Notification notification = new Notification(
        		R.drawable.ic_notification, "UCLARadio", time);
        Context context = getApplicationContext();
        CharSequence contentTitle = "UCLA Radio";
        CharSequence contentText = "UCLA's student run online radio!";
        //Intent notificationIntent = new Intent(this, UCLARadioService.class);
        //PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
        
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), UCLARadio.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        
        final int NOTIFY_ID = 1;
        startForeground(NOTIFY_ID, notification);
        //nm.notify(NOTIFY_ID, notification);
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();     
    }
    
    /** Remove radio from memory and turn off wakeLock/wifi **/
    public void onDestroy() {
    	wifiLock.release();
    	stopForeground(true);
    	radio.stop();
    	radio.release();
    	radio = null;
    }

	/** I don't know what this does because I didn't read the documentation very thoroughly **/
    @Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}