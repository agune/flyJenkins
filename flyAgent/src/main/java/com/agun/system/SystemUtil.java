package com.agun.system;

public class SystemUtil {
	public static boolean isWindows(){
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}
}
