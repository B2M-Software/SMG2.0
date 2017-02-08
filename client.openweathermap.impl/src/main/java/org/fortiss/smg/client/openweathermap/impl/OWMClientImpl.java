/**
 * 
 */
package org.fortiss.smg.client.openweathermap.impl;


import java.util.concurrent.TimeoutException;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.client.openweathermap.impl.api.OWMConfigResources;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;


/**
 * @author Syed Ashfaq Hussain Shah
 *
 */
public class OWMClientImpl implements OWMConfigResources, IActuatorClient{

	// "api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=1111111111";
	
	private String cityID;
	
	private String targetURI;// Currently not used.
	private String resultObj = null;
	
	private JsonObject jsonOWMData = null;
	
//	public OWMClientImpl(String cityId){		
//		prepareURI(cityId);		
//	}
	
	private void prepareURI(String cityId){
		this.cityID = cityId;
		this.targetURI = TARGET_URL + TARGET_PATH_CITY + "?" + QUERYPARAM_ID + "="
		+ this.cityID + QUERYPARAM_APPID + "=" + QUERYPARAM_APPID_VALUE;
			
	}
	
	public void loadResults(String currentCityId){
		if (resultObj == null || !currentCityId.equals(this.cityID)){
			prepareURI(currentCityId);
			
			// need to automate the refresh of data from OWM server
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.property(JsonGenerator.PRETTY_PRINTING, true);
			
			Client client = ClientBuilder.newClient(clientConfig);
			
			WebTarget webTarget = client.target(TARGET_URL)
					.path(TARGET_PATH_CITY);
			
			Response response = webTarget.queryParam(QUERYPARAM_ID, this.cityID)
					.queryParam(QUERYPARAM_APPID, QUERYPARAM_APPID_VALUE)
					.request(MediaType.APPLICATION_JSON)
					.get();
						
			//System.out.println("webTargetURI: " + webTarget.getUri());
			
			//System.out.println(response.getStatus());
			
			jsonOWMData = response.readEntity(JsonObject.class);
			
			//System.out.println(jsonOWMData.get("cod"));
			//System.out.println(jsonOWMData.get("city"));
			//System.out.println(response.readEntity(String.class));
			
		}
	}
	
	public double getCurrentTeperature(){	
		String result = null;
		
		return Double.parseDouble(jsonOWMData.get("list").toString());	
	}
	
	/*
	 * HTTP Status code of the latest Response.
	 */
	public int getResponseStatusCode(){	
				
		return Integer.parseInt(jsonOWMData.get("cod").toString());		
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}

}