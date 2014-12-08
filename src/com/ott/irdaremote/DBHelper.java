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
 * @author Joshua �÷��� DBHelper dbHelper = new DBHelper(this);
 *         dbHelper.createDataBase(); SQLiteDatabase db =
 *         dbHelper.getWritableDatabase(); Cursor cursor = db.query()
 *         db.execSQL(sqlString); ע�⣺execSQL��֧�ִ�;�Ķ���SQL��䣬ֻ��һ��һ����ִ�У����˺ܾò�����
 *         ��execSQL��Դ��ע�� (Multiple statements separated by ;s are not
 *         supported.) ����assets�µ����ݿ��ļ�ֱ�Ӹ��Ƶ�DB_PATH�������ݿ��ļ���С������1M����
 *         ����г���1M�Ĵ��ļ�������Ҫ�ȷָ�ΪN��С�ļ���Ȼ��ʹ��copyBigDatabase()�滻copyDatabase()
 */
public class DBHelper extends SQLiteOpenHelper {
	// �û����ݿ��ļ��İ汾
	private static final int DB_VERSION = 7;
	// ���ݿ��ļ�Ŀ����·��ΪϵͳĬ��λ�ã�cn.arthur.examples ����İ���
	private static String DB_PATH = "/databases/";
	/*
	 * //�����������ݿ��ļ������SD���Ļ� private static String DB_PATH =
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
	 * ������ݿ��ļ��ϴ�ʹ��FileSplit�ָ�ΪС��1M��С�ļ� �����зָ�Ϊ hello.db.101 hello.db.102
	 * hello.db.103
	 */
	// ��һ���ļ�����׺
	private static final int ASSETS_SUFFIX_BEGIN = 101;
	// ���һ���ļ�����׺
	private static final int ASSETS_SUFFIX_END = 103;
	public static final String RECOMMENDS_TABLE = TABLE_NAME;

	/**
	 * ��SQLiteOpenHelper�����൱�У������иù��캯��
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            һ�㶼��null
	 * @param version
	 *            ��ǰ���ݿ�İ汾��ֵ���������������ǵ�����״̬
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��

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
			// ���ݿ��Ѵ��ڣ�do nothing.
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
			// ����asseets�е�db�ļ���DB_PATH��
		}
	}

	// ������ݿ��Ƿ���Ч
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
		//InputStream myInput = new FileInputStream(MainActivity.DB_PATH+MainActivity.DB_NAME); //����ԭ�ļ�
		InputStream myInput = new FileInputStream(MainActivity.DB_PATH_EX+MainActivity.DB_NAME); //����ԭ�ļ�
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

	// ����assets�µĴ����ݿ��ļ�ʱ�����
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
	 * �ú������ڵ�һ�δ�����ʱ��ִ�У� ʵ�����ǵ�һ�εõ�SQLiteDatabase�����ʱ��Ż�����������
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d("irdaremote","onCreate" );
	}

	/**
	 * ���ݿ��ṹ�б仯ʱ����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
