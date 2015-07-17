package com.ott.irdaremote.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	static public boolean isNumericOrLetter(String str) {
		Pattern pattern = Pattern.compile("[0-9a-z]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
