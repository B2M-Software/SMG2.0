/**
 * 
 */
package org.fortiss.smg.client.openweathermap.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.AbstractConnector.IOnConnectListener;
import org.fortiss.smg.client.openweathermap.impl.api.OWMConfigResources;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

/**
 * @author Syed Ashfaq Hussain Shah
 *
 */
public class OWMClientActivator extends AbstractClient implements OWMConfigResources, BundleActivator {
	
	// Logger from sl4j
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(OWMClientActivator.class);

	private OWMClientImpl implClient;

	private List<OWMClientImpl> clients = new ArrayList<OWMClientImpl>();

	IActuatorMaster master4config = null;

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		// register here your services etc.
		// DO NOT start heavy operations here - use threads
		/*
		 * Try to connect to Master to get the wrapper's config file
		 */
		logger.info("Begins start().: 1");
		ArrayList<WrapperConfig> configList = new ArrayList<WrapperConfig>();
		logger.info("Begins start().: 2");
		DefaultProxy<IActuatorMaster> proxyMaster = new DefaultProxy<IActuatorMaster>(IActuatorMaster.class,
				ActuatorMasterQueueNames.getActuatorMasterInterfaceQueue(), 50000);
		logger.info("Begins start().: 3");
		try {
			
			logger.info("Connecting to master.: 4");
			master4config = proxyMaster.init();
			logger.info("After Connecting to master. Is connected: " + Boolean.toString(master4config != null));
			
		} catch (TimeoutException te) {
			logger.error("Unable to connect to master (Timeout).");
			te.printStackTrace();
		}

		/*
		 * If we have connection try to get the wrapper's config file
		 */
		if (master4config != null) {
			try {
				configList = master4config.getWrapperConfig(WRAPPER_KEY);
				logger.info("configList.size() :: " + configList.size());
				logger.info("configList.size() :: " + configList.toString());
			} catch (TimeoutException e) {
				logger.error("Unable to connect to master (Timeout to get configList).");
			} finally {
				try {
					proxyMaster.destroy();
				} catch (IOException e) {
					logger.info("Unable to close con. for queue:" + this.clientId);
				}
			}

			/*
			 * For each received wrapper config instance (possibly the same
			 * wrapper is used for multiple (physical) devices
			 */
			if (configList.size() > 0) {
				for (WrapperConfig config : configList) {

					final String clientKey = config.getKey();
					final String clientIDextension = config.getHost();

					implClient = new OWMClientImpl(config.getProtocol() + "://" + config.getHost(), config.getPort(),
							config.getWrapperID(), config.getPollingfrequency(), config.getUsername(),
							config.getPassword());
					// Register at Actuator Master (self, human readable name
					// for
					// device)
					registerAsClientAtServer(implClient, config.getWrapperName(), new IOnConnectListener() {

						@Override
						public void onSuccessFullConnection() {
							implClient.setMaster(master);
							implClient.setClientId(clientId);
							implClient.activate();
							logger.info("ActuatorClient[" + clientKey + "-" + clientIDextension + "] is alive");
							clients.add(implClient);

						}
					});

				}
				logger.info("OWMClient Wrapper started");
			} else {
				logger.info("No Configuration available");
			}
		} else {
			proxyMaster.destroy();
			logger.debug("OWMClient Wrapper could not read config from Master");
			this.stop(context);
		}

		// 2922582 Garching bei Munchen 48.250000 11.650000 DE
		// Should go under event calls
		// implClient.loadResults("2922582");
		// logger.info("OWMClientActivator:
		// Start:::::---------------------------------- getResponseStatusCode::
		// " + implClient.getResponseStatusCode());
		// logger.info("OWMClientActivator:
		// Start:::::---------------------------------- getResponseStatusCode::
		// " + implClient.getCurrentTeperature());

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
