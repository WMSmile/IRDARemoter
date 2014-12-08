package com.ott.irdaremote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Joshua 用法： DBHelper dbHelper = new DBHelper(this);
 *         dbHelper.createDataBase(); SQLiteDatabase db =
 *         dbHelper.getWritableDatabase(); Cursor cursor = db.query()
 *         db.execSQL(sqlString); 注意：execSQL不支持带;的多条SQL语句，只能一条一条的执行，晕了很久才明白
 *         见execSQL的源码注释 (Multiple statements separated by ;s are not
 *         supported.) 将把assets下的数据库文件直接复制到DB_PATH，但数据库文件大小限制在1M以下
 *         如果有超过1M的大文件，则需要先分割为N个小文件，然后使用copyBigDatabase()替换copyDatabase()
 */
public class DBHelper extends SQLiteOpenHelper {
	// 用户数据库文件的版本
	private static final int DB_VERSION = 7;
	// 数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名
	private static String DB_PATH = "/databases/";
	/*
	 * //如果你想把数据库文件存放在SD卡的话 private static String DB_PATH =
	 * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
	 * "/arthurcn/drivertest/packfiles/";
	 */
	private static String DB_NAME = "ir.db";
	private static String TABLE_NAME = "irdata";
	private static String ASSETS_NAME = "recommendMap.db";
			
	public static String _ID = "id" ;
	public static String NAME = "name";
	public static String IRADDR ="ircode_address";
	public static String PW = "power";
	public static String MUTE = "mute";
	public static String KEYUP="key_up";
	public static String KEYDOWN="key_down";
	public static String KEYLEFT="key_left";
	public static String KEYRIGHT="key_right";
	public static String KEYOK="key_ok";
	public static String VOLUMEUP="volume_up";
	public static String VOLUMEDOWN="volume_down";
	public static String BACK="backup";
	public static String MENU="menu";
	public static String CUP="channel_up";
	public static String CDOWN="channel_down";
	public static String MOUSE="mousemode";
	public static String HOME="home";

	public static String REVERSE1 = "reversed1";
	public static String REVERSE2 = "reversed2";
	public static String REVERSE3 = "reversed3";
	public static String REVERSE4 = "reversed4";
	public static String REVERSE5= "reversed5";
	public static String REVERSE6= "reversed6";
	

	private SQLiteDatabase myDataBase = null;
	private final Context myContext;

	/**
	 * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件 此例中分割为 hello.db.101 hello.db.102
	 * hello.db.103
	 */
	// 第一个文件名后缀
	private static final int ASSETS_SUFFIX_BEGIN = 101;
	// 最后一个文件名后缀
	private static final int ASSETS_SUFFIX_END = 103;
	public static final String RECOMMENDS_TABLE = TABLE_NAME;

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函数
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 *            一般都是null
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状态
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// 必须通过super调用父类当中的构造函数

		super(context, name, null, version);
		this.myContext = context;
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DBHelper(Context context, String name) {
		this(context, name, DB_VERSION);
	}

	public DBHelper(Context context) {

		this(context, context.getFilesDir().getPath().replace("/files", "")+DB_PATH + DB_NAME);
	}

	public void createDataBase() throws IOException {
		//boolean dbExist = checkDataBase();

		String pathdb = myContext.getFilesDir().getPath().replace("/files", "") + DB_PATH;
		Log.d("irdaremote","db path:"+pathdb );
		
		boolean dbExist =  new File(pathdb +DB_NAME).exists();
				
		if (dbExist) {
			// 数据库已存在，do nothing.
			/*File dbf = new File(DB_PATH + DB_NAME);
			if (dbf.exists()) {
				dbf.delete();
			}*/
		} else {
			
			File dir = new File(pathdb);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			/*File dbf = new File(DB_PATH + DB_NAME);
			if (dbf.exists()) {
				dbf.delete();
			}*/
			
			SQLiteDatabase myDatabase =SQLiteDatabase.openOrCreateDatabase(pathdb +DB_NAME, null);
			
			String DATABASE_CREATE = "create table " + TABLE_NAME +
					"( _id integer primary key autoincrement,"+
					NAME + "  text,"+
					IRADDR + "  text,"+
					PW + "  text,"+
					MUTE+  "  text,"+
					KEYUP + "  text,"+
					KEYDOWN + "  text,"+
					KEYLEFT + "  text,"+
					KEYRIGHT + "  text,"+
					KEYOK + "  text,"+
					VOLUMEUP + "  text,"+
					VOLUMEDOWN + "  text,"+
					BACK + "  text,"+
					MENU + "  text,"+
					CUP + "  text,"+
					CDOWN + "  text,"+
					MOUSE + "  text,"+
					HOME + "  text,"+
					REVERSE1 + "  text,"+
					REVERSE2 + "  text,"+
					REVERSE3 + "  text,"+
					REVERSE4 + "  text,"+
					REVERSE5 + "  text,"+
					REVERSE6 + "  text"+
					");";
					
			 myDatabase.execSQL(DATABASE_CREATE);
			// 复制asseets中的db文件到DB_PATH下
		}
	}

	// 检查数据库是否有效
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
			e.printStackTrace();
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		//InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
		/*
		//InputStream myInput = new FileInputStream(MainActivity.DB_PATH+MainActivity.DB_NAME); //读入原文件
		InputStream myInput = new FileInputStream(MainActivity.DB_PATH_EX+MainActivity.DB_NAME); //读入原文件
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();*/
	}

	// 复制assets下的大数据库文件时用这个
	private void copyBigDataBase() throws IOException {
		InputStream myInput;
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END + 1; i++) {
			myInput = myContext.getAssets().open(ASSETS_NAME + "." + i);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
		}
		myOutput.close();
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	/**
	 * 该函数是在第一次创建的时候执行， 实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d("irdaremote","onCreate" );
	}

	/**
	 * 数据库表结构有变化时采用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
