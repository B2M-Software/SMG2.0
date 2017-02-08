/**
 * 
 */
package org.fortiss.smg.client.openweathermap.impl;

import java.util.ArrayList;
import java.util.List;

import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

/**
 * @author Syed Ashfaq Hussain Shah
 *
 */
public class OWMClientActivator  implements BundleActivator {

	// Logger from sl4j
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(OWMClientActivator.class);

	private OWMClientImpl implClient;

	private List<OWMClientImpl> clients = new ArrayList<OWMClientImpl>();

	IActuatorMaster master4config = null;

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		implClient = new OWMClientImpl();
		
		// 2922582	Garching bei Munchen	48.250000	11.650000	DE
		// Should go under event calls
		implClient.loadResults("2922582");
		logger.info("OWMClientActivator: Start:::::---------------------------------- getResponseStatusCode:: " + implClient.getResponseStatusCode());
		logger.info("OWMClientActivator: Start:::::---------------------------------- getResponseStatusCode:: " + implClient.getCurrentTeperature());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
