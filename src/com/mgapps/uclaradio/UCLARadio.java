package com.mgapps.uclaradio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UCLARadio extends Activity {
	TextView quitkey;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /** Start the service for playing the radio station
        **/
        
        this.bindService(new Intent(UCLARadio.this, UCLARadioService.class),
                null, Context.BIND_AUTO_CREATE);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        /** Sets the 'quit' button **/
        quitkey = (TextView)findViewById(R.id.quitButton);
        quitkey.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
    }
    
}