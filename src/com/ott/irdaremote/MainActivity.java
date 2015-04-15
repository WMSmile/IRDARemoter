package com.ott.irdaremote;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int THREAD = 0;
	public static final int PALY = 1;
	Button bt, button_static;
	TextView tx;
	Spinner sp;
	Context ctx;
	ArrayAdapter<String> adapter;

	AudioManager am;
	MediaPlayer mp;
	String starttag, endtag, parsetag;
	String[] O = new String[24];
	int Q = 0;
	byte[] P;
	byte retArr[];

	String[] vm_property = { "java.vm.name", "java.vm.specification.vendor",
			"java.vm.vendor", "java.vm.specification.name",

			"java.specification.name", "java.specification.vendor",

			"java.vendor", "ro.yunos.version",

			"lemur.vm.version" };

	String[] property_name = { DBHelper.NAME, DBHelper.IRADDR, DBHelper.PW,
			DBHelper.KEYUP, DBHelper.KEYDOWN, DBHelper.KEYLEFT,
			DBHelper.KEYRIGHT, DBHelper.KEYOK, DBHelper.VOLUMEUP,
			DBHelper.VOLUMEDOWN, DBHelper.BACK, DBHelper.MENU, DBHelper.CUP,
			DBHelper.CDOWN, DBHelper.MOUSE ,DBHelper.MUTE,DBHelper.HOME};


	DBHelper dbHelper ;//= new DBHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bt = (Button) findViewById(R.id.button_start);
		tx = (TextView) findViewById(R.id.textView_judge);
		button_static = (Button) findViewById(R.id.button_static);
		sp = (Spinner) findViewById(R.id.spinner1);

		bt.setOnClickListener(this);
		button_static.setOnClickListener(this);
		ctx = this;
		d();
		((MyApplication)getApplication()).setMainappHanddle(mhadler);

		am = ((AudioManager) getSystemService("audio"));
		am.getStreamMaxVolume(3);
		am.getStreamVolume(3);
		mp = new MediaPlayer();
		mp.setOnCompletionListener(new ca(this));

		dbHelper = new DBHelper(this);
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.RECOMMENDS_TABLE, null, null, null,
				null, null, null);
		if (cursor != null) {

			Log.d("IrdaRemoter", "有数据");
			
			while(cursor.moveToNext()){

				String name = cursor.getString(cursor
						.getColumnIndex(DBHelper.NAME));
				
				HashMap<String, String> key_value = new HashMap<String, String>();
				InputStream in;
				for (String n : property_name) {
						key_value.put(n, cursor.getString(cursor
								.getColumnIndex(n)));

				((MyApplication) ctx.getApplicationContext()).getMap().put(
						name, key_value);
				}
			}
			cursor.close();
		} else {

			Log.d("IrdaRemoter", "无数据");
		}
		// db.execSQL(sqlString);
		List<String> namelist =new ArrayList<String>();
		namelist.addAll(((MyApplication) ctx.getApplicationContext()).getMap().keySet());
		Log.d("IrdaRemoter", "namelist数据:"+namelist.size());
		
		adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_checked ,namelist);    
        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        //第四步：将适配器添加到下拉列表上    
        sp.setAdapter(adapter);    
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中    
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
                /* 将所选mySpinner 的值带入myTextView 中*/    
                //myTextView.setText("您选择的是："+ adapter.getItem(arg2));    
                /* 将mySpinner 显示*/    
                arg0.setVisibility(View.VISIBLE);    
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                arg0.setVisibility(View.VISIBLE);    
            }    
        });

	}

	public String getFormAssert(String paramString) {
		try {
			InputStreamReader localInputStreamReader = new InputStreamReader(
					getResources().getAssets().open(paramString));
			BufferedReader localBufferedReader = new BufferedReader(
					localInputStreamReader);
			String localObject = "";
			String str1 = localBufferedReader.readLine();
			if (str1 == null) {
				localInputStreamReader.close();
				return localObject;
			}
			String str2 = localObject + str1;
			localObject = str2;
			return localObject;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return (String) "";
	}

	public void d() {
		int i1 = 0;
		int i2 = 0;
		InputStreamReader localInputStreamReader = null;
		BufferedReader localBufferedReader = null;
		try {
			localInputStreamReader = new InputStreamReader(getResources()
					.getAssets().open("tsg_str.tsg"));
			localBufferedReader = new BufferedReader(localInputStreamReader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		while (true) {
			try {
				i1 = 0;
				String str;

				do {
					if (!localBufferedReader.ready()) {
						localBufferedReader.close();
						localInputStreamReader.close();
						return;
					}
					i1++;
					StringBuilder localStringBuilder = new StringBuilder("");
					localStringBuilder.append(localBufferedReader.readLine());
					str = EncodingUtils.getString(localStringBuilder.toString()
							.getBytes(), "UTF-8");
				} while (str.length() <= 4);

				if (i2 == 0)
					this.O[i2] = str.substring(1);
				else
					this.O[i2] = str;
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
				return;
			}
			++i2;
			/*
			 * if(i1 >=24){ break; }
			 */
		}
	}

	public String d(String paramString) {
		String str1 = paramString.toUpperCase();
		StringBuilder localStringBuilder = new StringBuilder("");
		while (true) {
			if (str1.length() <= 0)
				return localStringBuilder.toString();
			if (str1.length() <= 1)
				continue;
			String str2 = str1.substring(0, 2);
			int i1 = Integer.parseInt(str2.substring(1), 16);
			localStringBuilder.append(this.O[i1]);
			Log.d("IrdaRemoter", "this.O1:" + this.O[i1]);
			int i2 = Integer.parseInt(str2.substring(0, 1), 16);
			localStringBuilder.append(this.O[i2]);
			Log.d("IrdaRemoter", "this.O2:" + this.O[i2]);
			if (str1.length() == 1)
				str1 = "";
			str1 = str1.substring(2);
		}
	}

	public String a(long paramLong) {
		String str1 = "";
		if (paramLong <= 0L) {
			return "0" + str1;
		} else {
			String str2 = "";
			while (true) {
				int i1 = (int) paramLong % 16;
				str1 = str2 + str1;
				paramLong /= 16L;
				if (paramLong == 0 && i1 == 0) {
					return str1;
				}
				switch (i1) {
				case 10:
					str2 = "A";
					continue;
				case 11:
					str2 = "B";
					continue;
				case 12:
					str2 = "C";
					continue;
				case 13:
					str2 = "D";
					continue;
				case 14:
					str2 = "E";
					continue;
				case 15:
					str2 = "F";
					continue;
				default:
					str2 = String.valueOf(i1);
					continue;
				}

			}
		}

	}

	public String b(long paramLong) {
		String str1 = "";
		String str2 = a(paramLong);
		if (str2.length() % 2 != 0)
			str2 = "0" + str2;

		while (true) {
			if (str1.length() >= 8) {
				return str1;
			}

			if (str2.length() <= 1) {
				str1 = str1 + "0";
			} else {
				str1 = str1 + str2.substring(-2 + str2.length());
				str2 = str2.substring(0, -2 + str2.length());
			}

		}
	}

	public int b(String paramString) {
		return Integer.parseInt(paramString, 16);
	}

	class cleanjob implements Runnable {

		PlayParameters pp;
		public cleanjob(PlayParameters pp) {
			this.pp = pp;
		}
		
		public   String bytesToHexString(byte[] bArray) {   
		    StringBuffer sb = new StringBuffer(bArray.length);   
		    String sTemp;   
		    for (int i = 0; i < bArray.length; i++) {   
		     sTemp = Integer.toHexString(0xFF & bArray[i]);   
		     if (sTemp.length() < 2)   
		      sb.append(0);   
		     sb.append(sTemp.toUpperCase());   
		    }   
		    return sb.toString();   
		} 

		@Override
		public void run() {
			// makeDataFile("05aa55011515153fad200000ff07f8");
			// makeDataFile("05aa55011515153fad2000404011ee");//公版右按键
			//makeDataFile("05aa55011515153fad200010ef01fe");// 海美迪右按键

			Log.d("IrdaRemoter", "to play data:" + pp.ir_address);
			Log.d("IrdaRemoter", "to play data:" + pp.key);
			
			String fanma = String.format("%1$#9x", (byte)b(pp.key));
			Log.d("IrdaRemoter", "to play data~:" +fanma );
			
			
			fanma =String.format("%1$#9x", ~(byte)b(pp.key)|0xffffff00); 
			Log.d("IrdaRemoter", "to play byte~:" + fanma);
			
			fanma = fanma.replace("0xffffff", "");
			//fanma = fanma.substring(6);
			String str = "05aa55011515153fad2000"+pp.ir_address+pp.key+fanma;
			Log.d("IrdaRemoter", "to play data:" + str);
			
			makeDataFile(str);// 海美迪右按键

			startSendIrda(Environment.getExternalStorageDirectory()+"/tsg_temp_save2" + ".wav");
			// 50aa 5510 5151 51f3 da02 00 00ff 708f :00ff07f8
		}

	}

	public List<String> GetFiles(String Path, String Extension,
			boolean IsIterative) // 搜索目录，扩展名(判断的文件类型的后缀名)，是否进入子文件夹
	{

		List<String> apklist = new ArrayList<String>(); // 配置结果 List
		File[] files = new File(Path).listFiles();
		if(files == null){
			return apklist;
		}
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			Log.d("IrdaRemoter", "properties files path:" + f.getPath());
			if (f.isFile() && f.getPath().indexOf("/.") == -1) {

				Log.d("IrdaRemoter",
						"properties files path_compare:" + f.getPath());
				if (f.getPath()
						.substring(f.getPath().length() - Extension.length())
						.equals(Extension)) // 判断扩展名
					apklist.add(f.getPath());
				/*
				 * if (!IsIterative) break; //如果不进入子集目录则跳出
				 */
			}
			/*
			 * else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) //
			 * 忽略点文件（隐藏文件/文件夹） GetFiles(f.getPath(), Extension, IsIterative);
			 * //这里就开始递归了
			 */
		}

		Log.d("IrdaRemoter", "properties files size:" + apklist.size());
		return apklist;
	}

	class insertDataJob implements Runnable {

		@Override
		public void run() {
			List<String> apklist = ((MyApplication) ctx.getApplicationContext())
					.getDatalist();
			apklist.clear();
			String str = Environment.getExternalStorageDirectory().getPath();
			Log.d("IrdaRemoter", "properties START find file in:" + str);

			apklist.addAll(GetFiles(str + "/ir", "properties", false));

			for (String str_ : apklist) {

				Log.d("IrdaRemoter", "properties file:" + str_);

				String name = str_.substring(str_.indexOf("ir/") + 3);
				Log.d("IrdaRemoter", "properties file name :" + name);

				if (name == null || TextUtils.isEmpty(name)) {
					continue;
				}
				name = name .replace(".properties", "");

				HashMap<String, String> key_value = new HashMap<String, String>();
				InputStream in;
				try {
					in = new BufferedInputStream(new FileInputStream(str_));

					Properties p = new Properties();
					p.load(in);
					for (String n : property_name) {
						key_value.put(n, p.getProperty(n));
					}
					key_value.put("name", name);//

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((MyApplication) ctx.getApplicationContext()).getMap().put(
						name, key_value);

			}
			
			//update db

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			HashMap<String, HashMap<String, String>> tomap = ((MyApplication) ctx.getApplicationContext()).getMap();
			
			for(String name : tomap.keySet()){

				String name_x =name.trim();
				Cursor cursor = db.query(DBHelper.RECOMMENDS_TABLE, null, "name=?", new String[]{name_x}, null, null, null);
				

				ContentValues cv = new ContentValues();
				//cv.put("name", name_x);
				Log.d("IrdaRemoter", "insert board name :" + name_x);
				for(String str_x : tomap.get(name_x).keySet()){
					cv.put(str_x, tomap.get(name_x).get(str_x));
				}
				
				if(cursor!=null||cursor.getCount() !=0){//新增
					db.insert(DBHelper.RECOMMENDS_TABLE, null, cv);
				}else{//更新
					db.update(DBHelper.RECOMMENDS_TABLE, cv, "name=?", new String[]{name_x});
				}

				cursor.close();
			}

			
			//notify to update spinner ui
			Message m = mhadler.obtainMessage(THREAD, "");
			m.sendToTarget();
		}
	}
	

	class checkjob implements Runnable {

		@Override
		public void run() {
			// makeDataFile("FF0007F8");
			// parsetag = getFormAssert();

			/*
			 * InputStreamReader localInputStreamReader = new InputStreamReader(
			 * getResources
			 * ().getAssets().open("tsg_temp_save5_00_FF_07_F8.wav"));
			 * ByteArrayInputStream localBufferedReader = new BufferedReader(
			 * localInputStreamReader);
			 */
			BufferedInputStream bis;
			ByteArrayOutputStream baos = null;
			try {
				// tsg_temp_save_01_FE_1F_E0.png
				// tsg_temp_save5_00_FF_07_F8.wav
				bis = new BufferedInputStream(getResources().getAssets().open(
						"tsg_temp_save_01_FE_1F_E0.png"));
				baos = new ByteArrayOutputStream();

				int c = bis.read();// 读取bis流中的下一个字节

				while (c != -1) {

					baos.write(c);

					c = bis.read();

				}

				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			retArr = baos.toByteArray();
			Log.d("IrdaRemoter", "parse job length:" + retArr.length);

			Log.d("IrdaRemoter", "first index is::" + findoffsetStrindex(1782));

			int j = 1782;
			do {
				result r = findoffsetStrindex(j);
				Log.d("IrdaRemoter", "===index is::" + r);
				if (r.index == -1) {
					break;
				}
				j += r.offset;
			} while (true);

			/*
			 * int length = (parsetag.length() / 2);
			 * 
			 * Log.d("IrdaRemoter", "parse job length:" + length); byte[]
			 * parsebytes = new byte[length]; for (int i1 = 0; i1 < length;
			 * ++i1) { parsebytes[i1] = (byte)b(parsetag.substring(i1 * 2, 2 +
			 * i1 * 2)); }
			 */

		}

		class result {
			int index;
			int offset;

			@Override
			public String toString() {
				return "result [index=" + index + ", offset=" + offset + "]";
			}

		}

		private result findoffsetStrindex(int offset) {
			result r = new result();
			int index = -1;
			ArrayList<Integer> omitchoose = new ArrayList<Integer>();
			int i = 0;
			for (i = 0;; i++) {
				byte temp = retArr[offset + i];

				if (omitchoose.size() == 16) {
					break;
				}

				int las = -1;

				for (int j = 0; j < 16; j++) {
					if (!omitchoose.contains(Integer.valueOf(j))) {
						las = j;

						if (omitchoose.size() == 15 && O[j].length() / 2 == i) {
							index = las;
							break;
						}

						if ((byte) b(O[j].substring(i * 2, 2 + i * 2)) != temp) {

							// Log.d("IrdaRemoter", "ommit index:" + j);

							omitchoose.add(Integer.valueOf(j));
						}

					}
					if (omitchoose.size() == 16) {
						break;
					}
				}

				if (omitchoose.size() == 15 && O[las].length() / 2 == i) {
					break;
				}

			}

			r.index = index;
			r.offset = i;

			// Log.d("IrdaRemoter", "r value:" + r);
			return r;

		}

	}

	public boolean makeDataFile(String paramString) {

		starttag = getFormAssert("start.tsg");
		endtag = getFormAssert("end.tsg");
		Q = (endtag.length() / 2);
		P = new byte[this.Q];
		for (int i1 = 0; i1 < this.Q; ++i1) {
			this.P[i1] = (byte) b(this.endtag.substring(i1 * 2, 2 + i1 * 2));
		}

		if ((paramString == null) || (paramString.equals("")))
			return false;
		Log.d("IrdaRemoter", "Creat_and_play_file:" + paramString);
		Log.d("IrdaRemoter", "data_start");
		StringBuilder localStringBuilder = new StringBuilder("");
		localStringBuilder.append(starttag);
		localStringBuilder.append(d(paramString));

		String str1 = localStringBuilder.toString();
		Log.d("IrdaRemoter", "get_code");
		int i1 = str1.length() / 2 + this.Q;
		Log.d("IrdaRemoter", "str1 byte_lenth:" + str1.length() / 2);
		Log.d("IrdaRemoter", "q byte_lenth:" + this.Q);
		Log.d("IrdaRemoter", "i1 byte_lenth:" + i1);
		String str2 = b(i1 + 38);
		String str3 = b(i1);
		Log.d("IrdaRemoter", "str2:" + str2 + ",str3:" + str3);
		String str4 = "52494646" + str2 + "57415645666D7420"
				+ "120000000100020044AC000010B10200" + "04001000000064617461"
				+ str3 + str1;
		int i2 = str4.length() / 2;
		Log.d("IrdaRemoter", "byte_lenth:" + String.valueOf(i2 + this.Q));
		byte[] arrayOfByte = new byte[i2];

		File localFile;
		localFile = new File(Environment.getExternalStorageDirectory()+"/tsg_temp_save2" + ".wav");
		localFile.deleteOnExit();

		int i3 = 0;
		do {
			arrayOfByte[i3] = (byte) b(str4.substring(i3 * 2, 2 + i3 * 2));
			++i3;
			/*
			 * this.C = false; if (!localFile.exists()) break label560;
			 * this.aS.reset(); this.aS.setLooping(false); FileInputStream
			 * localFileInputStream = new FileInputStream(localFile);
			 * this.aS.setDataSource(localFileInputStream.getFD());
			 * this.aS.prepare(); this.aS.start(); Log.d("IrdaRemoter",
			 * "play start OK");
			 */
		} while (i3 < str4.length() / 2);

		FileOutputStream localFileOutputStream;
		try {
			localFileOutputStream = new FileOutputStream(localFile);
			localFileOutputStream.write(arrayOfByte);
			localFileOutputStream.write(this.P);
			localFileOutputStream.flush();
			localFileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d("IrdaRemoter", "save_data OK");
		return true;
	}

	public void startSendIrda(String localFile) {
		/*
		 * FileOutputStream localFileOutputStream = new
		 * FileOutputStream(localFile);
		 * localFileOutputStream.write(arrayOfByte);
		 * localFileOutputStream.write(this.P); localFileOutputStream.flush();
		 * localFileOutputStream.close();
		 */

		mp.reset();
		mp.setLooping(false);

		FileInputStream localFileInputStream;
		try {
			localFileInputStream = new FileInputStream(localFile);
			mp.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
			mp.setDataSource(localFileInputStream.getFD());
			mp.prepare();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		mp.start();
		((Vibrator) getSystemService("vibrator")).vibrate(new long[] { 5L, 10L,
				5L, 10L }, -1);

	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.button_start) {
			// startIrda();
			// Intent intent = new Intent(Intent.ACTION_VIEW);
			// intent.setData(Uri.parse("appstore://start?module=category&catCode=46"));
			// startActivity(intent);
			// startJudge();
			
			//new Thread(new cleanjob()).start();
			Intent i = new Intent(this,Control.class);
			Bundle bl = new Bundle();
			bl.putCharSequence("name", (String)sp.getSelectedItem());
			i.putExtras(bl);
			startActivity(i);
		} else if (arg0.getId() == R.id.button_static) {
			// startStaticIP();

			new Thread(new insertDataJob()).start();
			// new Thread(new checkjob()).start();
		}

	}

	private void startStaticIP() {

		int netStatus = -1;
		try {
			ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()
						&& activeNetworkInfo.isConnected()) {
					netStatus = 1;
				} else if (activeNetworkInfo.isConnected()) {
					netStatus = 2;
				} else {
					netStatus = 3;
				}
				tx.setText(String.valueOf(netStatus));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * ConnectivityManager mService = (ConnectivityManager)
		 * getSystemService(Context.CONNECTIVITY_SERVICE); EthernetManager
		 * mEthManager = (EthernetManager)
		 * getSystemService(Context.ETHERNET_SERVICE); if
		 * (mEthManager.getState() == EthernetManager.ETHERNET_STATE_ENABLED) {
		 * EthernetDevInfo mInterfaceInfo = mEthManager.getSavedConfig(); String
		 * mIp; String mMask; String mGw; String mDns; mIp = "192.168.0.118";
		 * mMask = "255.255.255.0"; mGw = "192.168.0.1"; mDns = "192.168.0.1";
		 * mInterfaceInfo
		 * .setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL);
		 * mInterfaceInfo.setIpAddress(mIp); mInterfaceInfo.setNetMask(mMask);
		 * mInterfaceInfo.setDnsAddr(mDns); mInterfaceInfo.setGateWay(mGw); try
		 * { mEthManager.updateDevInfo(mInterfaceInfo); Thread.sleep(500); }
		 * catch (Exception e) { e.printStackTrace(); } } else {
		 * Toast.makeText(this, "Ethernet state disabled!", 5000).show(); }
		 */
	}

	Handler mhadler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case THREAD:
				//tx.setText((String) msg.obj);
				//update insert data
				List<String> namelist =new ArrayList<String>();
				namelist.addAll(((MyApplication) ctx.getApplicationContext()).getMap().keySet());
				
				adapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_checked, namelist);    
		        //第三步：为适配器设置下拉列表下拉时的菜单样式。    
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		        //第四步：将适配器添加到下拉列表上    
		        sp.setAdapter(adapter);    
				break;
			case PALY:
				new Thread(new cleanjob((PlayParameters)msg.obj)).start();
			}
			super.handleMessage(msg);
		}
	};

	private void startIrda() {
		try {
			Object localObject = getSystemService("irda");
			if (localObject == null) {
				System.err.println("irda no such services");
				return;
			}
			localObject.getClass();
			Method[] ms = localObject.getClass().getMethods();
			for (Method m : ms) {
				System.err.println(m.toString());
			}
			// localObject.getClass().getMethod("write_irsend", new Class[] {
			// String.class }).invoke(localObject, new Object[] {ircode });
			// return; }
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	private void startJudge() {
		// TODO Auto-generated method stub
		// Message m = mhadler.obtainMessage(THREAD, "test");
		// m.sendToTarget();

		try {
			// execCommand("getprop");
			execCommand("");
			// execCommand("ls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Properties p = System.getProperties();

		StringBuilder strb = new StringBuilder(tx.getText());
		for (String str : vm_property) {
			System.err.println(p.get(str));

			strb.append(str + ":" + p.get(str) + "\n");
		}

		tx.setText(strb.toString());

	}

	public void execCommand(String command) throws IOException {
		Runtime runtime = Runtime.getRuntime();

		Process proc = runtime.exec(new String[] { "sh", "-c",
				"getprop|grep uuid" });

		try {

			if (proc.waitFor() != 0) {

				System.err.println("exit value = " + proc.exitValue());

			} else {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(proc.getInputStream()));
				int read;
				char[] buffer = new char[4096];
				StringBuffer output = new StringBuffer();
				while ((read = reader.read(buffer)) > 0) {
					output.append(buffer, 0, read);
				}
				reader.close();

				// Waits for the command to finish.
				// process.waitFor();

				System.out.println(output.toString());
				Message m = mhadler.obtainMessage(THREAD, output.toString());
				m.sendToTarget();
			}

		} catch (InterruptedException e) {

			System.err.println(e);

		}

	}

}
