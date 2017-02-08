package org.fortiss.smg.ems.impl;

import org.fortiss.smg.ems.api.EmsInterface;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.slf4j.LoggerFactory;

public class EmsImpl implements EmsInterface {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(EmsImpl.class);

		public String doSomething(String s){
			return "Hello smg";
		}

		@Override
		public boolean isComponentAlive() {
			// TODO Auto-generated method stub
			return false;
		}
}
