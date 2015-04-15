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

public class Control extends Activity implements OnClickListener {
	
	Button btn_pw,btn_mute;
	
	Button btn_keyup,btn_keyleft,btn_keyok,btn_keyright,btn_keydown;

	Button btn_volup,btn_voldown;

	Button btn_chup,btn_chdown;

	Button btn_mouse,btn_menu,btn_back;
	
	Button but_home;
	
	Context ctx;
	
	String boradname="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv);
		
		boradname = (String)getIntent().getExtras().get("name");
		if(TextUtils.isEmpty(boradname)){
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

		 btn_mouse.setText("鼠");;
		 btn_menu.setText("三");;
		 btn_back.setText("回");;
		 but_home.setText("主");

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
			 
			 
		Log.d("IrdaRemoter", "boradname name:" + boradname);
	}
	
	class cleanjob implements Runnable {
		PlayParameters boardname;

		public cleanjob(PlayParameters boardname) {
			this.boardname = boardname;
		}

		@Override
		public void run() {
			// makeDataFile("05aa55011515153fad200000ff07f8");
			// makeDataFile("05aa55011515153fad2000404011ee");//公版右按键

			//((MyApplication) ctx.getApplicationContext()).getMainapp().getmakeDataFile("05aa55011515153fad200010ef01fe");// 海美迪右按键
			
			Message m = ((MyApplication) ctx.getApplicationContext()).getMainappHanddle().obtainMessage(MainActivity.PALY, boardname);
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
			choosenKey = DBHelper.PW;
		}else if(btn_mute.getId() == arg0.getId()){
			choosenKey = DBHelper.MUTE;
		}else if(btn_keyup.getId() == arg0.getId()){

			choosenKey = DBHelper.KEYUP;
		}else if(btn_keyleft.getId() == arg0.getId()){

			choosenKey = DBHelper.KEYLEFT;
		}else if(btn_keyok.getId() == arg0.getId()){

			choosenKey = DBHelper.KEYOK;
		}else if(btn_keyright.getId() == arg0.getId()){

			choosenKey = DBHelper.KEYRIGHT;
		}else if(btn_keydown.getId() == arg0.getId()){

			choosenKey = DBHelper.KEYDOWN;
		}else if(btn_volup.getId() == arg0.getId()){

			choosenKey = DBHelper.VOLUMEUP;
		}else if(btn_voldown.getId() == arg0.getId()){

			choosenKey = DBHelper.VOLUMEDOWN;
		}else if(btn_chup.getId() == arg0.getId()){

			choosenKey = DBHelper.CUP;
		}else if(btn_chdown.getId() == arg0.getId()){

			choosenKey = DBHelper.CDOWN;
		}else if(btn_mouse.getId() == arg0.getId()){

			choosenKey = DBHelper.MOUSE;
		}else if(btn_menu.getId() == arg0.getId()){

			choosenKey = DBHelper.MENU;
		}else if(btn_back.getId() == arg0.getId()){

			choosenKey = DBHelper.BACK;
		}else if(but_home.getId() == arg0.getId()){

			choosenKey = DBHelper.HOME;
		}
		
		if(TextUtils.isEmpty(choosenKey)){
			Toast toast=Toast.makeText(getApplicationContext(), "没有按键!", Toast.LENGTH_SHORT); 
			//显示toast信息 
			toast.show(); 
			return;
		}

		String value = ((MyApplication) ctx.getApplicationContext()).getMap().get(boradname).get(choosenKey);
		String ir =  ((MyApplication) ctx.getApplicationContext()).getMap().get(boradname).get(DBHelper.IRADDR);
		if(value == null ||value.equals("null") || TextUtils.isEmpty(value)){
			Toast toast=Toast.makeText(getApplicationContext(), "按键没有键值!", Toast.LENGTH_SHORT); 
			//显示toast信息 
			toast.show(); 
			return;
		}
		PlayParameters pp = new PlayParameters(boradname, value,ir);

		new Thread(new cleanjob(pp)).start();
	}
}
