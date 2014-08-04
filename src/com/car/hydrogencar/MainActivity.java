package com.car.hydrogencar;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

//import us.costan.chrome.ChromeView;

import com.car.hydrogencar.JoystickMovedListener;
import com.car.hydrogencar.JoystickView;
import com.car.hydrogencar.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.util.DisplayMetrics;


import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import android.webkit.WebView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.View.OnTouchListener;


public class MainActivity extends Activity implements OnTouchListener {
	 	private float downXValue;
	 	private ImageView im1;
	 	private ImageView im2;
		private TextView tv;
		private TextView tv2;
		private TextView tv3;
		private TextView speedometer1;
		private Button button;
		public int speed;
		private Button ok;
		private Button g1;
		private Button g2;
		private Button g3;
		private Button g4;
	    public JoystickView joystick1;
	    public JoystickView joystick2;
	    private Toast mToast;
		private DatagramSocket mSocket;
		public byte[] mBytes = new byte[5];
		public EditText ardui = null;
		public EditText cam = null;
		public String arduino = "192.168.43.207";
		public String camera = "192.168.0.103";
		public byte[] buf = new byte[4];
		public String text = "00";
		private DatagramSocket sock;
		private int counter = 0;
		public int counter1 = 0; 
		public int counterg1 = 0; 
		public int counterg2 = 0; 
		public int counterg3 = 0;
		public int counterg4 = 0; 
		public byte[] message = new byte[13];
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	//StrictMode.setThreadPolicy(policy); 
        super.onCreate(savedInstanceState);
        // Set main.XML as the layout for this Activity
		main();      
    }

    
	@Override
	 public boolean onTouch(View arg0, MotionEvent arg1) {

        // Get the action that was done on this touch event
        switch (arg1.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();            

                // going backwards: pushing stuff to the right
                if (downXValue - currentX < -500)
                {
                    // Get a reference to the ViewFlipper
                     ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
                     // Set the animation
                      vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
                      // Flip!
                      vf.showPrevious();
                      Toast.makeText(this, arduino +", "+ camera , Toast.LENGTH_SHORT).show();
                }
                // going forwards: pushing stuff to the left
                if (downXValue - currentX > 500 )
                {
                    // Get a reference to the ViewFlipper
                    ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
                     // Set the animation
                     vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
                      // Flip!
                     vf.showNext();
                     Toast.makeText(this, arduino +", "+ camera , Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
    }
	
	
	
	
	@Override
    public void onBackPressed() {
		Toast.makeText(this, "press back button one more time to exit" , Toast.LENGTH_SHORT).show();
		counter=counter+1;
		if(counter ==2){
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
    }
	
	
	private void connectSocket(String ip){
        InetSocketAddress mAddr =  new InetSocketAddress(ip ,8080);
    	try {mSocket = new DatagramSocket();}
    	catch(SocketException E){
    		mToast.setText("new socket: " + E.toString());
    		mToast.show();
    	};
    	
    	try {mSocket.connect(mAddr);}
    	catch(SocketException connectE){
    		mToast.setText("connect: " + connectE.toString());
    		mToast.show();
    		}
    	}
	private void sendData(byte[] byteData){
		if(mSocket == null) return;
		DatagramPacket dPacket = new DatagramPacket(byteData, byteData.length);
		try{
			mSocket.send(dPacket);
			
		}catch(IOException e){
			mToast.setText("send: " + e.toString());
			mToast.show();
		}
	}
	
	private void main() {
		
        setContentView(R.layout.activity_main);
        LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
        layMain.setOnTouchListener((OnTouchListener) this);
        
        //main screen
        Toast.makeText(this, arduino +", "+ camera , Toast.LENGTH_SHORT).show();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final WebView webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://gnfos.com/jp/src/1394443664254.gif");
        webView.setVisibility(View.INVISIBLE);
         

        im1 = (ImageView) findViewById(R.id.im1);
        im2 = (ImageView) findViewById(R.id.im2);
        speedometer1 = (TextView) findViewById(R.id.speedometer1);
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView)findViewById(R.id.tv3);
		ok = (Button) findViewById(R.id.ok);
		g1 = (Button) findViewById(R.id.g1);
		g1.setAlpha((float) 0.7);
		g2 = (Button) findViewById(R.id.g2);
		g2.setAlpha((float) 0.7);
		g3 = (Button) findViewById(R.id.g3);
		g3.setAlpha((float) 0.7);
		g4 = (Button) findViewById(R.id.g4);
		g4.setAlpha((float) 0.7);
		speedometer1.setText(text);
		
        g1.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				counterg1=counterg1+1;
				// TODO Auto-generated method stub
				if(counterg1 == 1){	
				g1.setAlpha((float) 0.2);
				g2.setAlpha((float) 0.7);
				g3.setAlpha((float) 0.7);
				g4.setAlpha((float) 0.7);
				speed = 32;
				counterg2=0;
				counterg3=0;
				counterg4=0;
				;}
				if(counterg1 == 2){
					g1.setAlpha((float) 0.7);
					counterg1 = 0;
					speed = 0;
					;}
			}
        });
        g2.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				counterg2=counterg2+1;
				// TODO Auto-generated method stub
				if(counterg2 == 1){	
				g2.setAlpha((float) 0.2);
				g1.setAlpha((float) 0.7);
				g3.setAlpha((float) 0.7);
				g4.setAlpha((float) 0.7);
				speed = 96;
				counterg1=0;
				counterg3=0;
				counterg4=0;
				;}
				if(counterg2 == 2){
					g2.setAlpha((float) 0.7);
					counterg2 = 0;
					speed = 0;
					;}
			}
        });
        

        
        
        g3.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				counterg3=counterg3+1;
				// TODO Auto-generated method stub
				if(counterg3 == 1){	
				g3.setAlpha((float) 0.2);
				g1.setAlpha((float) 0.7);
				g2.setAlpha((float) 0.7);
				g4.setAlpha((float) 0.7);
				speed = 96;
				counterg1=0;
				counterg2=0;
				counterg4=0;
				;}
				if(counterg3 == 2){
					g3.setAlpha((float) 0.7);
					counterg3 = 0;
					speed = 0;
					;}
			}
        });
        g4.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				counterg4=counterg4+1;
				// TODO Auto-generated method stub
				if(counterg4 == 1){	
				g4.setAlpha((float) 0.2);
				g1.setAlpha((float)0.7);
				g2.setAlpha((float) 0.7);
				g3.setAlpha((float) 0.7);
				speed = 127;
				counterg1=0;
				counterg2=0;
				counterg3=0;
				;}
				if(counterg4 == 2){
					g4.setAlpha((float) 0.7);
					counterg4 = 0;
					speed = 0;
					;}
			}
        });
		
		
		
		
		joystick2 = (JoystickView) findViewById(R.id.joystick2);
        joystick1 = (JoystickView) findViewById(R.id.joystick1);
        button = (Button) findViewById(R.id.button1);
        final Animation mAnimation = new AlphaAnimation(1, (float) 0.2);
        mAnimation.setDuration(2000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.REVERSE); 
        button.startAnimation(mAnimation);
        
        button.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				counter1=counter1+1;
				// TODO Auto-generated method stub
				if(counter1 == 1){	
				webView.setVisibility(View.VISIBLE);
				button.clearAnimation();
				button.setAlpha((float) 0.2);
				joystick1.setAlpha((float) 0.8);
				joystick2.setAlpha((float) 0.8);
				im1.setVisibility(View.INVISIBLE);
				im2.setVisibility(View.INVISIBLE);
				;}
				if(counter1 == 2){
					webView.setVisibility(View.INVISIBLE);
					button.startAnimation(mAnimation);
					button.setAlpha((float) 1);
					joystick1.setAlpha((float) 1);
					joystick2.setAlpha((float) 1);
					im1.setVisibility(View.VISIBLE);
					im2.setVisibility(View.VISIBLE);
					counter1=0;
					;}
			}
        });
		
		final Thread connect;
		connect = new Thread(){
		public void run(){
		connectSocket(arduino);
		}};
		connect.setPriority(Thread.MAX_PRIORITY);
		connect.start();
		
		new Thread(new Runnable() {
		     public void run() {

		       while(true){
		    	try {
					sock = new DatagramSocket(8081);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        DatagramPacket p = new DatagramPacket(message, message.length);
		        try {
					sock.receive(p);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        text = new String(message, 0, p.getLength());
		        text = text.substring(1, 3);
		        runOnUiThread(new Runnable() {

		        @Override
		        public void run() {
		          speedometer1.setText(text);
		        }
		    });
		                }
		}}).start();
        
		joystick1.setOnJostickMovedListener(new JoystickMovedListener() {
	        @Override
				public void OnReturnedToCenter() {
	        	Thread tc;
	        	tc = new Thread(){	
	        		public void run(){
	        		try{
	        		
	        		mBytes[0] = (byte) (127);
	 				mBytes[1] = (byte) (127);	
	 				mBytes[4] = (byte) (1);
	 				sendData(mBytes);
	 				
	        		}
	        		finally{}
	        	}};
	        	tc.setPriority(Thread.MAX_PRIORITY);
	        	tc.start();
				}
				
				@Override
				public void OnReleased() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnMoved(final int pan, final int tilt){
					tv.setText(Integer.toString(pan * 30 +127)+", "+Integer.toString((tilt * 30 * speed)/127 + 127));
					joystick1.setAutoReturnToCenter(true);
					Thread t1;
					t1 = new Thread(){
						
						public void run(){
					try{
						
						mBytes[0] = (byte) (pan * 30 + 127);
						mBytes[1] = (byte) ((tilt * 30 * speed)/127 + 127);
						mBytes[4] = (byte) (1);	
						//connectSocket(arduino);
						sendData(mBytes);
					}
					finally{}
				}};
	        	t1.setPriority(Thread.MAX_PRIORITY);
	        	t1.start();
	    		
			}
				});

		joystick2.setOnJostickMovedListener(new JoystickMovedListener() {
            @Override
    			public void OnReturnedToCenter() {
            	Thread t2c;
            	t2c = new Thread(){	
            		public void run(){
            		try{
            		
            		mBytes[2] = (byte) (127);
     				mBytes[3] = (byte) (127);	
     				mBytes[4] = (byte) (1);
     				//connectSocket(arduino);
     				sendData(mBytes);
     				
            		}
            		finally{}
            	}};
            	t2c.setPriority(Thread.MAX_PRIORITY);
            	t2c.start();
    			}
    			
    			@Override
    			public void OnReleased() {
    				// TODO Auto-generated method stub
    				
    			}
    			
    			@Override
    			public void OnMoved(final int pan, final int tilt){
    				tv2.setText(Integer.toString(pan*30+127)+", "+Integer.toString((tilt * 30  + 127)));
    				joystick2.setAutoReturnToCenter(true);
    				Thread t2;
    				t2 = new Thread(){
    					
    					public void run(){
    				try{
    					
    					mBytes[2] = (byte) (pan * 30 + 127);
    					mBytes[3] = (byte) ((tilt * 30 * + 127));
    					mBytes[4] = (byte) (1);	
    					//connectSocket(arduino);
    					sendData(mBytes);
    					
    				}
    				finally{}
    			}};
            	t2.setPriority(Thread.MAX_PRIORITY);
            	t2.start();
    		}
    			});
		//second screen alr
		
		ok.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				main();
			}
        });

		ardui = (EditText)findViewById(R.id.arduino);
		cam = (EditText)findViewById(R.id.camera);
		ardui.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				arduino = s.toString();
			}
			
		});
		
		
		cam.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				camera = s.toString();
			}
			
		});
		
	}

	
}