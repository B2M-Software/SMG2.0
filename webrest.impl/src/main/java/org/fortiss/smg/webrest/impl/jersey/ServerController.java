package org.fortiss.smg.webrest.impl.jersey;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

//import org.eclipse.jetty.servlet.ServletContextHandler;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.fortiss.smg.webrest.impl.LoginFilter;
//import org.mortbay.jetty.Server;
//import org.mortbay.jetty.servlet.Context;
//import org.mortbay.jetty.servlet.FilterHolder;
//import org.mortbay.jetty.servlet.ServletHolder;




import com.sun.syndication.io.FeedException;

public class ServerController {
	
	private Server server;
	private int port = 8091; // By Shah, Need to exclude the value assignment.
/*
* @Author(name = "Syed Ashfaq Hussain Shah", date = "September 6, 2016")------ BEGINS
* 
*/
public ServerController(){
		
	}

	public ServerController(String host, int port) {
		
		this.port = port;
		try{
			server = new Server(new InetSocketAddress(InetAddress.getByName(host), port));

			//server = new Server(port);
				
		}catch (Exception uhe){//(UnknownHostException uhe){
			System.out.println("ServerController :: UnknownHostException");
		}	
	}
	
	public void setServletContext(ServletHolder servHolder){
		// does not work :-(
		servHolder.setInitParameter(
				"com.sun.jersey.config.property.resourceConfigClass",
				"org.fortiss.smg.webrest.impl.jersey.UriExtensionsConfig");

		// add here all packages you would like to be public
		servHolder.setInitParameter("com.sun.jersey.config.property.packages",
				"org.fortiss.smg.webrest.impl.front");
		// BY SHAH ---STARTS
		//servHolder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "org.fortiss.smg.webrest.impl.front");
		// BY SHAH ---ENDS
		
		// Integrate JSON with Jersey - IMPORTANT!
		servHolder.setInitParameter(
				"com.sun.jersey.api.json.POJOMappingFeature", "true");

		FilterHolder filHolder = new FilterHolder(new LoginFilter());

		filHolder.setInitParameter("filter-name", "Auth-Filter");
		filHolder.setInitParameter("urlPatterns", "*");
		filHolder.setInitParameter("servletNames", "");
    			
		FilterHolder filterHolder = new FilterHolder(new CrossOriginFilter());

		filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		
		

		// Global context
		//Context context = new Context(server, "/api", Context.SESSIONS);
		// context for servlet = everything
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api");
        server.setHandler(context);
	
		context.addServlet(servHolder, "/*");
		context.addFilter(filHolder, "/*", EnumSet.allOf(DispatcherType.class));
		context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));
	
		//context.addFilter(filHolder, "/*", ServletContextHandler.NO_SECURITY);
	}
	
	/*
	* @Author(name = "Syed Ashfaq Hussain Shah", date = "September 6, 2016")
	* 
	*/	
	public void getServerStatus(){
		// Need to improve
		if(server.isStarted())
			System.out.println("ServerController.serverStatus() :: "+server.getState());
		else
			System.out.println("ServerController.serverStatus() :: "+server.getState());
	}
	
	/*
	* @Author(name = "Syed Ashfaq Hussain Shah", date = "September 6, 2016")
	*  Need to improve
	*/
	public void start() {
		try {
			server.start();
			//server.join();
			
		} catch (javax.xml.bind.JAXBException j) {
			j.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	* Author(name = "Syed Ashfaq Hussain Shah", date = "September 6, 2016") ------ ENDS
	*/
	
	
	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}
	
	
	
	
	
	
	
	
// BY Shah, Need to review Calls and replace with above written methods ---------------------> STARTS	
	public void start(int port, ServletHolder servHolder) {
		this.port = port;
		// does not work :-(
		servHolder.setInitParameter(
				"com.sun.jersey.config.property.resourceConfigClass",
				"org.fortiss.smg.webrest.impl.jersey.UriExtensionsConfig");

		// add here all packages you would like to be public
		servHolder.setInitParameter("com.sun.jersey.config.property.packages",
				"org.fortiss.smg.webrest.impl.front");

		
		// set alias
		// jerseyServletParams.put("alias", "/api");

		// servHolder.setInitParameter(
		// "com.sun.jersey.spi.container.ContainerRequestFilters",
		// "org.fortiss.smartmicrogrid.api.rest.test.server.MyApppFilter");

		// Integrate JSON with Jersey - IMPORTANT!
		servHolder.setInitParameter(
				"com.sun.jersey.api.json.POJOMappingFeature", "true");

		FilterHolder filHolder = new FilterHolder(new LoginFilter());

		filHolder.setInitParameter("filter-name", "Auth-Filter");
		filHolder.setInitParameter("urlPatterns", "*");
		filHolder.setInitParameter("servletNames", "");
    	
		
		FilterHolder filterHolder = new FilterHolder(new CrossOriginFilter());
		String origin = "http://localhost";
		filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");


		server = new Server(port);

		// Global context
		//Context context = new Context(server, "/api", Context.SESSIONS);
		// context for servlet = everything
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api");
        server.setHandler(context);
	
		context.addServlet(servHolder, "/*");
		context.addFilter(filHolder, "/*", EnumSet.allOf(DispatcherType.class));
		context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));
	
		//context.addFilter(filHolder, "/*", ServletContextHandler.NO_SECURITY);

		try {
			server.start();
		} catch (javax.xml.bind.JAXBException j) {
			j.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// BY Shah, Need to review and replace with above written methods ---------------------------------------------------> ENDS


}
