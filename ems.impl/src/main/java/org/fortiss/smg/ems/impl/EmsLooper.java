/**
 * 
 */
package org.fortiss.smg.ems.impl;

import java.util.Date;

import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.slf4j.LoggerFactory;

/**
 * @author dagmar
 *
 */
public class EmsLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(EmsLooper.class);

	private EmsImpl impl;
	private DoubleEvent ev;
 	//private JSONReaderFroeschl jsonReader;
	EmsLooper(EmsImpl impl) {
		this.impl = impl;
		//jsonReader= new JSONReaderFroeschl();	
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		logger.info("Read values at: " + (new Date()));
		readDevices();

	}


	private void readDevices() {
		// TODO Auto-generated method stub
		
	}

}
