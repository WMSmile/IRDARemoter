package com.ott.irdaremote;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ott.irdaremote.entity.KlEntity;

public class Control extends Activity implements OnClickListener {
	
	Button btn_pw,btn_mute;
	
	Button btn_keyup,btn_keyleft,btn_keyok,btn_keyright,btn_keydown;

	Button btn_volup,btn_voldown;

	Button btn_chup,btn_chdown;

	Button btn_mouse,btn_menu,btn_back;
	
	Button but_home;
	
	Context ctx;
	
	String boradName="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv);
		
		boradName = (String)getIntent().getExtras().get("name");
		if(TextUtils.isEmpty(boradName)){
			return;
		}
		ctx =this;
		btn_pw = (Button) findViewById(R.id.but_001);
		btn_mute= (Button) findViewById(R.id.but_002);
		btn_mute.setText("静");
		
		 btn_keyup = (Button) findViewById(R.id.but_005);
		 btn_keyleft= (Button) findViewById(R.id.but_006);
		 btn_keyok= (Button) findViewById(R.id.but_007);
		 btn_keyright= (Button) findViewById(R.id.but_008);
		 btn_keydown= (Button) findViewById(R.id.but_009);

		 btn_volup= (Button) findViewById(R.id.but_010);
		 btn_voldown= (Button) findViewById(R.id.but_011);

		 btn_chup= (Button) findViewById(R.id.but_003);
		 btn_chdown= (Button) findViewById(R.id.but_004);

		 btn_mouse= (Button) findViewById(R.id.but_012);
		 btn_menu= (Button) findViewById(R.id.but_013);
		 btn_back= (Button) findViewById(R.id.but_014);
		 
		 but_home =(Button) findViewById(R.id.but_home);

		 btn_mouse.setText("鼠标");
		 btn_menu.setText("菜单");
		 btn_back.setText("返回");
		 but_home.setText("主页");

			btn_pw.setOnClickListener(this);
			btn_mute.setOnClickListener(this);
			
			 btn_keyup.setOnClickListener(this);
			 btn_keyleft.setOnClickListener(this);
			 btn_keyok.setOnClickListener(this);
			 btn_keyright.setOnClickListener(this);
			 btn_keydown.setOnClickListener(this);

			 btn_volup.setOnClickListener(this);
			 btn_voldown.setOnClickListener(this);

			 btn_chup.setOnClickListener(this);
			 btn_chdown.setOnClickListener(this);

			 btn_mouse.setOnClickListener(this);
			 btn_menu.setOnClickListener(this);
			 btn_back.setOnClickListener(this);
			 but_home.setOnClickListener(this);
			 
			 
		Log.d("IrdaRemoter", "boradname name:" + boradName);
	}
	
	class playJob implements Runnable {
		PlayParameters parameter;

		public playJob(PlayParameters parameter) {
			this.parameter = parameter;
		}

		@Override
		public void run() {
			// makeDataFile("05aa55011515153fad200000ff07f8");
			// makeDataFile("05aa55011515153fad2000404011ee");//公版右按键

			//((MyApplication) ctx.getApplicationContext()).getMainapp().getmakeDataFile("05aa55011515153fad200010ef01fe");// 海美迪右按键
			
			Message m = ((MyApplication) ctx.getApplicationContext()).getMainappHanddle().obtainMessage(MainActivity.MEDIA_PALY, parameter);
			m.sendToTarget();
			//startSendIrda("/mnt/sdcard/tsg_temp_save2" + ".wav");
			// 50aa 5510 5151 51f3 da02 00 00ff 708f :00ff07f8
		}

	}

	//DBHelper.IRADDR
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String choosenKey = "" ;
		if(btn_pw.getId() == arg0.getId()){
			choosenKey = KlEntity.PW;
		}else if(btn_mute.getId() == arg0.getId()){
			choosenKey = KlEntity.MUTE;
		}else if(btn_keyup.getId() == arg0.getId()){

			choosenKey = KlEntity.KEYUP;
		}else if(btn_keyleft.getId() == arg0.getId()){

			choosenKey = KlEntity.KEYLEFT;
		}else if(btn_keyok.getId() == arg0.getId()){

			choosenKey = KlEntity.KEYOK;
		}else if(btn_keyright.getId() == arg0.getId()){

			choosenKey = KlEntity.KEYRIGHT;
		}else if(btn_keydown.getId() == arg0.getId()){

			choosenKey = KlEntity.KEYDOWN;
		}else if(btn_volup.getId() == arg0.getId()){

			choosenKey = KlEntity.VOLUMEUP;
		}else if(btn_voldown.getId() == arg0.getId()){

			choosenKey = KlEntity.VOLUMEDOWN;
		}else if(btn_chup.getId() == arg0.getId()){

			choosenKey = KlEntity.CUP;
		}else if(btn_chdown.getId() == arg0.getId()){

			choosenKey = KlEntity.CDOWN;
		}else if(btn_mouse.getId() == arg0.getId()){

			choosenKey = KlEntity.MOUSE;
		}else if(btn_menu.getId() == arg0.getId()){

			choosenKey = KlEntity.MENU;
		}else if(btn_back.getId() == arg0.getId()){

			choosenKey = KlEntity.BACK;
		}else if(but_home.getId() == arg0.getId()){

			choosenKey = KlEntity.HOME;
		}
		
		if(TextUtils.isEmpty(choosenKey)){
			Toast toast=Toast.makeText(getApplicationContext(), "没有按键!", Toast.LENGTH_SHORT); 
			//显示toast信息 
			toast.show(); 
			return;
		}

		String value = ((MyApplication) ctx.getApplicationContext()).getMap().get(boradName).get(choosenKey);
		String ir =  ((MyApplication) ctx.getApplicationContext()).getMap().get(boradName).get(KlEntity.IRADDR);
		ir = ir.substring(2, 4) + ir.substring(0, 2);
		if(value == null ||value.equals("null") || TextUtils.isEmpty(value)){
			Toast toast=Toast.makeText(getApplicationContext(), "按键没有键值!", Toast.LENGTH_SHORT); 
			//显示toast信息 
			toast.show(); 
			return;
		}
		String value_hex = Integer.toHexString(Integer.valueOf(value));
		if(value_hex.length() ==1 ){
			value_hex = "0"+value_hex;
		}
		PlayParameters pp = new PlayParameters(boradName, value_hex,ir);

		new Thread(new playJob(pp)).start();
	}
}
