package com.ott.irdaremote.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

import android.util.Log;

import com.ott.irdaremote.MyApplication;

public class FileParser {
	/**
     * һ��һ�ж�ȡ�ļ��������ȡ�����ַ�ʱ��������
     * 
     * ���Ĺر�˳���ȴ򿪵ĺ�أ���򿪵��ȹأ�
     *       �����п��ܳ���java.io.IOException: Stream closed�쳣
     * 
     * @throws IOException 
     */
    static public void readFileBySplit(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filepath), "UTF-8"));
        String line="";
        String[] arrs=null;
        while ((line=br.readLine())!=null) {
            arrs=line.split(" ");
            Log.d(MyApplication.TAG,arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
        }
        br.close();
    }

    //Vendor_0001_Product_0001_Version_0100.kl is common kl files
    static public  void readFileByScan(String filepath,HashMap<String,String> key_value_map) throws IOException {
    	
    	//HashMap<String,String> keyMap = new HashMap<String,String>();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filepath), "UTF-8"));
        String line="";
        String[] arrs=null;
        while ((line=br.readLine())!=null) {
            Log.d(MyApplication.TAG, "line string is : " + line);
            
            Scanner scan=new Scanner(line);
            if(!line.startsWith("key")){
            	continue;
            }
            /*
             *��һ�����Կո��зֿ����ĸ����ݷֱ����
             */
                String ommit="";
                String index="";
                String keyname="";
                //while (scan.hasNext()) {
                	ommit = scan.next();
                    index= scan.next();   
                    keyname = scan.next();   
                    Log.d(MyApplication.TAG,"index="+index+",keyname="+keyname);
                    key_value_map.put(keyname,index);
                 //}
                
        }
        br.close();
        

    }
    
}
