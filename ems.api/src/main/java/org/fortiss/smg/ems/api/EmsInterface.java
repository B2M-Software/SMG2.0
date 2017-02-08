package org.fortiss.smg.ems.api;

import org.fortiss.smg.ambulance.api.HealthCheck;
import java.util.concurrent.TimeoutException;

public interface EmsInterface extends HealthCheck {
    String doSomething(String arg) throws TimeoutException;
}
