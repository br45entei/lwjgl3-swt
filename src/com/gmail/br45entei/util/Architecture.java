package com.gmail.br45entei.util;

/** This class contains modified code (as of 06/06/2020 05:47 PM CST) copied
 * from {@link org.lwjgl.system.Platform.Architecture} because using any
 * class from LWJGL before the native libraries are loaded in causes an
 * {@link UnsatisfiedLinkError}, resulting in the libraries not being loaded
 * properly.
 * 
 * @author LWJGL3-3.2.3 (Ripped from Platform.java) */
public enum Architecture {
	X86,
	X64,
	ARM32,
	ARM64;
	
	private static final Architecture current;
	
	static {
		String osArch = System.getProperty("os.arch");
		boolean is64Bit = osArch.contains("64") || osArch.startsWith("armv8");
		
		current = osArch.startsWith("arm") || osArch.startsWith("aarch64")//
				? (is64Bit ? Architecture.ARM64 : Architecture.ARM32)//
				: (is64Bit ? Architecture.X64 : Architecture.X86);
	}
	
	public static final Architecture get() {
		return current;
	}
	
}
