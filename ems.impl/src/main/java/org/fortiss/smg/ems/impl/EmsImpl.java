package org.fortiss.smg.ems.impl;

import org.fortiss.smg.ems.api.EmsInterface;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.LoggerFactory;

public class EmsImpl implements IActuatorClient {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(EmsImpl.class);
	private IActuatorMaster master;
	private String clientId;
	private ScheduledExecutorService executor;
	private int pollFrequency;
	private String host;

	private String username;
	private String password;
	private int deviceCounter;
	// private JSONReaderFroeschl reader;

	private String wrapperTag;

	private ArrayList<DeviceContainer> devices = new ArrayList<DeviceContainer>();

	public EmsImpl(String host, String port, String wrapperTag, int pollFreq, String username, String password) {

		this.host = host;
		// this.port = port;
		this.wrapperTag = wrapperTag;
		this.username = username;
		this.password = password;
		this.deviceCounter = 0;
		// reader = new JSONReaderFroeschl();
		// loadStaticDevs(this.wrapperTag, reader.readJsonFroeschl(host));
		// sendNewDeviceEvents();
		pollFrequency = pollFreq;
	}

	public synchronized void activate() {

		sendNewDeviceEvents();

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new EmsLooper(this), 0, getPollFrequency(), TimeUnit.SECONDS);

		logger.info("Scheduled Executor started");
	}

	private void sendNewDeviceEvents() {
		for (DeviceContainer dev : devices) {
			try {
				logger.debug("Dev:" + dev.getDeviceId().getDevid() + " -> " + clientId);
				System.out.println(dev.getHrName());
				master.sendDeviceEvent(new DeviceEvent(dev), this.clientId);
			} catch (TimeoutException e) {
				logger.debug("Failed to send " + dev.getDeviceId() + " to master");
			}
		}
	}

	public void setMaster(IActuatorMaster master) {
		this.master = master;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setPollFrequency(int pollFrequency) {
		this.pollFrequency = pollFrequency;
	}

	public int getPollFrequency() {
		return pollFrequency;
	}

	public void setWrapperTag(String wrapperTag) {
		this.wrapperTag = wrapperTag;
	}

	@Override
	public boolean isComponentAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDoubleCommand(DoubleCommand command, DeviceId dev) {
		// TODO Auto-generated method stub
		logger.debug("Received Doublecommand " + command.getValue() + " for " + dev.getDevid());
		//logger.debug("EMS does not recieve commands");

	}

	public String doSomething(String s) {
		return "Hello smg";
	}
}