package com.ott.irdaremote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application {

	private List<String> datalist = new ArrayList<String>(); // ÅäÖÃ½á¹û List

	private HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

	private Handler mainappHanddle;
	

	public Handler getMainappHanddle() {
		return mainappHanddle;
	}

	public void setMainappHanddle(Handler mainappHanddle) {
		this.mainappHanddle = mainappHanddle;
	}

	public List<String> getDatalist() {
		return datalist;
	}

	public void setDatalist(List<String> datalist) {
		this.datalist = datalist;
	}

	public HashMap<String, HashMap<String, String>> getMap() {
		return map;
	}

	public void setMap(HashMap<String, HashMap<String, String>> map) {
		this.map = map;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

}
