package com.gmail.br45entei.util;

/** This class contains modified code (as of 06/06/2020 05:47 PM CST) copied
 * from {@link org.lwjgl.system.Platform} because using any class from LWJGL
 * before the native libraries are loaded in causes an
 * {@link UnsatisfiedLinkError}, resulting in the libraries not being loaded
 * properly.
 * 
 * @author LWJGL3-3.2.3 (Ripped from Platform.java) */
public enum Platform {
	WINDOWS,
	LINUX,
	MACOSX,
	UNKNOWN;
	
	private static final Platform current;
	
	static {
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Windows")) {
			current = WINDOWS;
		} else if(osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix")) {
			current = LINUX;
		} else if(osName.startsWith("Mac OS X") || osName.startsWith("Darwin")) {
			current = MACOSX;
		} else {
			current = UNKNOWN;
		}
	}
	
	public static final Platform get() {
		return current;
	}
	
}
