/**
 * 
 */
package org.fortiss.smg.client.openweathermap.impl.api;

/**
 * @author Syed Ashfaq Hussain Shah
 *
 * Constants to make a REST call to OpenWeatherMap server. 
 */
public interface OWMConfigResources {
	// "api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=1111111111";
	
		String TARGET_URL =  "http://api.openweathermap.org";
		String TARGET_PATH_CITY =  "/data/2.5/forecast/city"; //? // Currently make a call against city code as suggested by official website.
		String QUERYPARAM_ID = "id";
		String QUERYPARAM_APPID = "APPID";
		String QUERYPARAM_APPID_VALUE = "902ef182e47257b3782f4d7163ce6fbd"; // Also referred to as API Key
		
}
