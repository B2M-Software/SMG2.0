package org.fortiss.smg.ems.test;


import org.fortiss.smg.ems.api.EmsInterface;
import org.fortiss.smg.ems.impl.EmsImpl;

import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestEmsSimple {

 	private EmsImpl impl;
 	private EmsInterface intface;
 	
	@Before
	public void setUp() {
		impl = new EmsImpl();
        }

	@After
	public void tearDown(){
            // TODO do some cleanup
        }

	@Test(timeout=5000)
	public void testYourMethod() throws TimeoutException{
		System.out.print("Hello smg!");
	}
}
