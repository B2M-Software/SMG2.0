/**
 * 
 */
package org.fortiss.smg.client.openweathermap.impl;


import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
import org.slf4j.LoggerFactory;
import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.client.openweathermap.impl.api.OWMConfigResources;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;


/**
 * @author Syed Ashfaq Hussain Shah
 *
 */
public class OWMClientImpl implements OWMConfigResources, IActuatorClient{

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(OWMClientImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private ScheduledExecutorService executor;
	private int pollFrequency;
	private String host;
	
	// TODO:
	// Need to change to wrapperID -----?????????????????????
	private String wrapperTag;
	
	private ContainerManagerInterface broker;
	
	ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();
	
	
	OWMClientImpl(String protocolNHost, String port, String wrapperID, int pollFreq, String userNname, String passWord){
		this.host = protocolNHost;
		this.wrapperTag = wrapperID;		
		this.pollFrequency = pollFreq;
		
		//loadStaticDevs(wrapperTag);
	}
	
	/*
	Getters and Setters
	*/
	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public void setMaster(IActuatorMaster master) {
		this.master = master;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setWrapperTag(String wrapperTag) {
		this.wrapperTag = wrapperTag;
	}
	
	public IActuatorMaster getMaster() {
		return this.master;
	}

	public int getPollFrequency() {
		return this.pollFrequency;
	}

	public String getWrapperTag() {
		return this.wrapperTag;
	}
	
	public String getHost() {
		return this.host;
	}

	/*
	* To activate and deactivate this OWMclient	module.
	*/
public synchronized void activate() {
		sendNewDeviceEvents();
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new OWMClientLooper(this), 0,
				getPollFrequency(), TimeUnit.SECONDS);
	}

	public synchronized void deactivate() {
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	* To send device Events
	*/
	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				//master.sendDeviceEvent(dev, this.clientId);
				logger.info("Try to send " + dev.getDeviceId()
						+ " to master");
				master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
				logger.info("Sent " + dev.getDeviceId()
						+ " to master");
				
				
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId()
						+ " to master");
			}
		}
	}
	
	
	/*
	* Inherited methods to override
	*/
	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	//////////////////////--------------------------------------------------------
	
	// "api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=1111111111";
	
	private String cityID;
	
	private String targetURI;// Currently not used.
	private String resultObj = null;
	
	private JsonObject jsonOWMData = null;
	
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



}