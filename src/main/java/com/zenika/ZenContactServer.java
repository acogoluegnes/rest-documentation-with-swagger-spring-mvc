/**
 * 
 */
package com.zenika;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author acogoluegnes
 *
 */
public class ZenContactServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int port = 8080;
		String contextPath = "zencontact";
		Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(
                        ServletContextHandler.SESSIONS);
        context.setContextPath("/"+contextPath);
        server.setHandler(context);
        
        WebApplicationContext appContext = ConfigurationUtils.createXmlConfiguration(context.getServletContext());
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
        context.addServlet(new ServletHolder(dispatcherServlet), "/*");
        
        server.start();
        
        System.out.println("Server started");
        System.out.printf("REST API documentation: http://localhost:%s/%s/api-docs %n",port,contextPath);
        System.out.printf("REST API documentation UI: http://localhost:%s/%s/swagger-ui/index.html",port,contextPath);
        server.join();

	}

}
