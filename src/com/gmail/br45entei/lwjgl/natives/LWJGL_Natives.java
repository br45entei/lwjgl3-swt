/*******************************************************************************
 * 
 * Copyright (C) 2020 Brian_Entei (br45entei@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package com.gmail.br45entei.lwjgl.natives;

import com.gmail.br45entei.util.Architecture;
import com.gmail.br45entei.util.CodeUtil;
import com.gmail.br45entei.util.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Class used to load system libraries for LWJGL upon startup.
 * 
 * @author Brian_Entei */
public class LWJGL_Natives {
	
	/** Returns the folder where native library files are unpacked.<br>
	 * It is a sub-folder named &quot;natives&quot; in the program's current
	 * working directory.
	 * 
	 * @return The folder where native library files are unpacked */
	public static final File getNativesFolder() {
		final File rootDir = new File(CodeUtil.getProperty("user.dir"));//new File(AccessController.doPrivileged(new GetPropertyAction("user.dir")));
		File folder = new File(rootDir, "natives");
		
		switch(Platform.get()) {
		case WINDOWS:
			folder = new File(folder, "windows");
			break;
		case LINUX:
			folder = new File(folder, "linux");
			break;
		case MACOSX:
			folder = new File(folder, "macos");
			break;
		case UNKNOWN:
		default:
			throw new AssertionError("No native libraries are available for this platform!");
		}
		
		switch(Architecture.get()) {
		case X86:
			folder = new File(folder, "x86");
			break;
		case X64:
			folder = new File(folder, "x64");
			break;
		case ARM32:
			folder = new File(folder, "arm32");
			break;
		case ARM64:
			folder = new File(folder, "arm64");
			break;
		default:
			throw new AssertionError("No native libraries are available for this platform!");
		}
		
		folder.mkdirs();
		return folder;
	}
	
	static {
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			@Override
			public Void run() {
				File folder = LWJGL_Natives.getNativesFolder();
				//System.setProperty("org.lwjgl.util.Debug", "true");
				//System.setProperty("org.lwjgl.util.DebugLoader", "true");
				System.setProperty("java.library.path", folder.getAbsolutePath());
				System.setProperty("org.lwjgl.librarypath", folder.getAbsolutePath());
				return null;
			}
		});
	}
	
	/** Loads the native libraries required by LWJGL3-3.2.3 along with any other
	 * native libraries passed to this function via
	 * <code>extraNativeResourcePathsToLoad</code>.
	 * 
	 * @param extraNativeResourcePathsToLoad A collection of resource path
	 *            strings pointing to the extra native libraries to be loaded
	 * @return A list of the files that were created and then loaded as a
	 *         result */
	public static final List<File> loadNatives(Collection<String> extraNativeResourcePathsToLoad) {
		final File folder = LWJGL_Natives.getNativesFolder();
		
		List<File> unpackedNatives = new ArrayList<>(),
				loadedNatives = new ArrayList<>();
		
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			@Override
			public Void run() {
				String pathRoot = "/natives/";
				List<String> paths = new ArrayList<>();
				switch(Platform.get()) {
				case WINDOWS:
					pathRoot = pathRoot.concat("windows/");
					
					switch(Architecture.get()) {
					case X64:
						pathRoot = pathRoot.concat("x64/org/lwjgl/");
						break;
					case X86:
						pathRoot = pathRoot.concat("x86/org/lwjgl/");
						break;
					//$CASES-OMITTED$
					default:
						throw new AssertionError("No native libraries are available for this platform!");
					}
					
					paths.add(pathRoot.concat("lwjgl.dll"));
					paths.add(pathRoot.concat("jemalloc/jemalloc.dll"));
					paths.add(pathRoot.concat("stb/lwjgl_stb.dll"));
					paths.add(pathRoot.concat("opengl/lwjgl_opengl.dll"));
					paths.add(pathRoot.concat("opengles/lwjgl_opengles.dll"));
					paths.add(pathRoot.concat("openal/OpenAL.dll"));
					paths.add(pathRoot.concat("glfw/glfw.dll"));
					break;
				case LINUX:
					pathRoot = pathRoot.concat("linux/");
					switch(Architecture.get()) {
					case X64:
						pathRoot = pathRoot.concat("x64/org/lwjgl/");
						break;
					case ARM32:
						pathRoot = pathRoot.concat("arm32/org/lwjgl/");
						break;
					case ARM64:
						pathRoot = pathRoot.concat("arm32/org/lwjgl/");
						break;
					//$CASES-OMITTED$
					default:
						throw new AssertionError("No native libraries are available for this platform!");
					}
					
					paths.add(pathRoot.concat("liblwjgl.so"));
					paths.add(pathRoot.concat("jemalloc/libjemalloc.so"));
					paths.add(pathRoot.concat("stb/liblwjgl_stb.so"));
					paths.add(pathRoot.concat("opengl/liblwjgl_opengl.so"));
					paths.add(pathRoot.concat("opengles/liblwjgl_opengles.so"));
					paths.add(pathRoot.concat("openal/libopenal.so"));
					paths.add(pathRoot.concat("glfw/libglfw.so"));
					paths.add(pathRoot.concat("glfw/libglfw_wayland.so"));
					break;
				case MACOSX:
					pathRoot = pathRoot.concat("macos/");
					switch(Architecture.get()) {
					case X64:
						pathRoot = pathRoot.concat("x64/org/lwjgl/");
						break;
					//$CASES-OMITTED$
					default:
						throw new AssertionError("No native libraries are available for this platform!");
					}
					
					paths.add(pathRoot.concat("liblwjgl.dylib"));
					paths.add(pathRoot.concat("jemalloc/libjemalloc.dylib"));
					paths.add(pathRoot.concat("stb/liblwjgl_stb.dylib"));
					paths.add(pathRoot.concat("opengl/liblwjgl_opengl.dylib"));
					paths.add(pathRoot.concat("opengles/liblwjgl_opengles.dylib"));
					paths.add(pathRoot.concat("openal/libopenal.dylib"));
					paths.add(pathRoot.concat("glfw/libglfw.dylib"));
					paths.add(pathRoot.concat("vulkan/libMoltenVK.dylib"));
					break;
				case UNKNOWN:
				default:
					throw new AssertionError("No native libraries are available for this platform!");
				}
				
				/*for(String resourcePath : paths) {
					System.out.println(resourcePath + "; Present: " + (LWJGL_Natives.class.getResourceAsStream(resourcePath) == null ? "No" : "Yes"));
				}*/
				
				if(extraNativeResourcePathsToLoad != null) {
					for(String resourcePath : extraNativeResourcePathsToLoad) {
						System.out.print(String.format("Extra native library path: \"%s\"; Is present: ", resourcePath));
						boolean inputStreamPresent;
						try(InputStream check = LWJGL_Natives.class.getResourceAsStream(resourcePath)) {
							inputStreamPresent = check != null;
						} catch(IOException | NullPointerException ex) {
							inputStreamPresent = false;
						}
						if(inputStreamPresent && resourcePath.contains("/")) {
							paths.add(resourcePath);
							System.out.println("Yes");
						} else {
							System.out.println("No: " + (inputStreamPresent ? "Resource path does not contain any forward-slashes! ('/')" : "Resource is missing or bad resource path!"));
						}
						System.out.flush();
					}
				}
				
				for(String resourcePath : paths) {
					try(InputStream in = LWJGL_Natives.class.getResourceAsStream(resourcePath)) {
						String name = resourcePath.substring(resourcePath.lastIndexOf('/') + 1);
						File file = new File(folder, name);
						if(file.exists() && file.isFile() && file.length() > 0) {
							System.out.println(String.format("Skipping unpack of pre-existing native \"%s\"...", file.getAbsolutePath()));
							unpackedNatives.add(file);
							continue;
						}
						if(!file.exists() && !file.createNewFile()) {
							throw new IOException(String.format("Unable to create file \"%s\"!", file.getAbsolutePath()));
						}
						System.out.println(String.format("Unpacking native \"%s\" to \"%s\"...", resourcePath, file.getAbsolutePath()));
						try(FileOutputStream out = new FileOutputStream(file)) {
							byte[] b = new byte[4096];
							int len;
							while((len = in.read(b)) != -1) {
								out.write(b, 0, len);
							}
							out.flush();
						}
						unpackedNatives.add(file);
					} catch(IOException | NullPointerException ex) {
						throw new RuntimeException("Failed to unpack natives!", ex);
					}
				}
				for(File nativ3 : unpackedNatives) {
					System.out.println(String.format("Loading native \"%s\"...", nativ3.getAbsolutePath()));
					System.load(nativ3.getAbsolutePath());
					loadedNatives.add(nativ3);
				}
				/*try {
					Runtime.getRuntime().exec(String.format("explorer.exe /select,\"%s\"", (loadedNatives.size() >= 1 ? loadedNatives.get(0) : folder).getAbsolutePath()));
				} catch(IOException ex) {
					throw new RuntimeException(ex);
				}*/
				return null;
			}
		});
		return loadedNatives;
	}
	
	/** Loads the native libraries required by LWJGL3-3.2.3 along with any other
	 * native libraries passed to this function via
	 * <code>extraNativeResourcePathsToLoad</code>.
	 * 
	 * @param extraNativeResourcePathsToLoad One or more resource path strings
	 *            pointing to the extra native libraries to be loaded
	 * @return A list of the files that were created and then loaded as a
	 *         result */
	public static final List<File> loadNatives(String... extraNativeResourcePathsToLoad) {
		return loadNatives(Arrays.asList(extraNativeResourcePathsToLoad));
	}
	
	/** Runs a simple LWJGL3/SWT demo application.
	 * 
	 * @param args Program command line arguments
	 * @see com.gmail.br45entei.lwjgl.demo.LWJGL_SWT_Demo LWJGL_SWT_Demo */
	public static final void main(String[] args) {
		loadNatives();
		com.gmail.br45entei.lwjgl.demo.LWJGL_SWT_Demo.runDemo(args);
	}
	
}
