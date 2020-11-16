package com.stackoverflow;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

/** This class allows you to search for the {@link GraphicsDevice} and
 * {@link GraphicsConfiguration} that best matches where your window is located.
 * 
 * @author <a href="https://stackoverflow.com/a/33799118/2398263">Roman
 *         Horváth</a> */
public class DeviceConfig {
	
	/** This method finds the graphics device and configuration by the location
	 * of specified window.
	 *
	 * @param windowBounds The bounds of the window which will help find the
	 *            graphics device and configuration
	 * @return An instance of a custom class type (DeviceConfig) that stores the
	 *         indexes and instances of graphics device and configuration of the
	 *         current graphics environment */
	public static DeviceConfig findDeviceConfig(Rectangle windowBounds) {
		// Prepare default return value:
		DeviceConfig deviceConfig = null;
		
		int lastArea = 0;
		
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		
		// Search through all devices…
		for(int i = 0; i < graphicsDevices.length; ++i) {
			GraphicsDevice graphicsDevice = graphicsDevices[i];
			
			GraphicsConfiguration[] graphicsConfigurations = graphicsDevice.getConfigurations();
			
			// It is possible that your device will have only one configuration,
			// but you cannot rely on this(!)…
			for(int j = 0; j < graphicsConfigurations.length; ++j) {
				GraphicsConfiguration graphicsConfiguration = graphicsConfigurations[j];
				
				Rectangle graphicsBounds = graphicsConfiguration.getBounds();
				
				Rectangle intersection = windowBounds.intersection(graphicsBounds);
				
				int area = intersection.width * intersection.height;
				
				if(area != 0) {
					if(deviceConfig == null) {
						deviceConfig = new DeviceConfig(i, j, graphicsDevice, graphicsConfiguration);
					} else if(area > lastArea) {
						lastArea = area;
						deviceConfig.deviceIndex = i;
						deviceConfig.configIndex = j;
						deviceConfig.device = graphicsDevice;
						deviceConfig.config = graphicsConfiguration;
					}
				}
			}
		}
		
		return deviceConfig;
	}
	
	/** This method finds the graphics device and configuration by the location
	 * of specified window.
	 *
	 * @param windowBounds The bounds of the window which will help find the
	 *            graphics device and configuration
	 * @return An instance of a custom class type (DeviceConfig) that stores the
	 *         indexes and instances of graphics device and configuration of the
	 *         current graphics environment */
	public static DeviceConfig findDeviceConfig(org.eclipse.swt.graphics.Rectangle windowBounds) {
		return findDeviceConfig(new Rectangle(windowBounds.x, windowBounds.y, windowBounds.width, windowBounds.height));
	}
	
	private volatile int deviceIndex;
	private volatile int configIndex;
	private volatile GraphicsDevice device;
	private volatile GraphicsConfiguration config;
	
	/** The default constructor. */
	public DeviceConfig() {
		this.deviceIndex = 0;
		this.configIndex = 0;
		this.device = null;
		this.config = null;
	}
	
	/** The full constructor. */
	public DeviceConfig(int deviceIndex, int configurationIndex, GraphicsDevice graphicsDevice, GraphicsConfiguration graphicsConfiguration) {
		this.deviceIndex = deviceIndex;
		this.configIndex = configurationIndex;
		this.device = graphicsDevice;
		this.config = graphicsConfiguration;
	}
	
	public int getDeviceIndex() {
		return this.deviceIndex;
	}
	
	public int getConfigurationIndex() {
		return this.configIndex;
	}
	
	public GraphicsDevice getDevice() {
		return this.device;
	}
	
	public GraphicsConfiguration getConfiguration() {
		return this.config;
	}
	
}
