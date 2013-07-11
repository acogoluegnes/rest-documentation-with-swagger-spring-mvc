/**
 * 
 */
package com.zenika;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author acogoluegnes
 *
 */
public class ConfigurationUtils {
	
	public enum ConfigurationType {
		JAVA,XML
	}

	public static WebApplicationContext createJavaConfiguration(ServletContext context) {
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setServletContext(context);
        appContext.register(ZenContactConfiguration.class);
        appContext.refresh();
        return appContext;
	}
	
	public static WebApplicationContext createXmlConfiguration(ServletContext context) {
		XmlWebApplicationContext appContext = new XmlWebApplicationContext();
		appContext.setServletContext(context);
		appContext.setConfigLocation("classpath:/applicationContext.xml");
		appContext.refresh();
		return appContext;
	}
	
}
