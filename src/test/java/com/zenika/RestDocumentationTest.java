/**
 * 
 */
package com.zenika;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

import static org.hamcrest.Matchers.*;
import static com.jayway.jsonassert.JsonAssert.*;

/**
 * @author acogoluegnes
 *
 */
public class RestDocumentationTest {
	
	private int port = 8085;
	
	private String contextPath = "zencontact";

	@Test public void useDocumentationToNavigateJavaConfiguration() throws Exception {
		doTestUseDocumentationToNagivate(ConfigurationUtils.ConfigurationType.JAVA);
	}
	
	@Test public void useDocumentationToNavigateXmlConfiguration() throws Exception {
		doTestUseDocumentationToNagivate(ConfigurationUtils.ConfigurationType.XML);
	}
	
	private void doTestUseDocumentationToNagivate(ConfigurationUtils.ConfigurationType configurationType) throws Exception {
		Server server = null;
		try {
			server = createServer(port, contextPath, configurationType);
			
			String documentationUrl = String.format("http://localhost:%s/%s/api-docs",port,contextPath);
			String resourceType = "contacts";
			RestTemplate tpl = new RestTemplate();
			
			String documentation = tpl.getForObject(documentationUrl, String.class);
			
			String basePath = JsonPath.read(documentation, "basePath");
			List<String> apiDocumentationPath = JsonPath.read(documentation,"apis[?].path",filter(where("description").is(resourceType)));
			Assert.assertEquals(1,apiDocumentationPath.size());
			documentation = tpl.getForObject(basePath+apiDocumentationPath.get(0), String.class);
			List<String> apis = JsonPath.read(
				documentation,
				"$.apis[?].path",
				new OperationNicknameFilter("select","GET")
			);
			Assert.assertEquals(1,apis.size());
			
			String resourcePath = apis.get(0);
			String contacts = tpl.getForObject(basePath+resourcePath, String.class);
			
			with(contacts)
				.assertThat("$[0].firstname", equalTo("Erich"))
				.assertThat("$[0].lastname", equalTo("Gamma"));
			
		} finally {
			if(server != null) {
				server.stop();
			}
		}
	}
	
	private static class OperationNicknameFilter extends Filter.FilterAdapter<Map<String, Object>> {
		
		private final String operationNickname,httpMethod;
		
		public OperationNicknameFilter(String operation,String httpMethod) {
			super();
			this.operationNickname = operation;
			this.httpMethod = httpMethod;
		}

		@Override
		public boolean accept(Map<String, Object> map) {
			JSONObject operation = (JSONObject) ((JSONArray) map.get("operations")).get(0);
            return operation.get("nickname").equals(operationNickname)
            		&& operation.get("httpMethod").equals(httpMethod);			
		}
		
	}
	
	private Server createServer(int port,String contextPath,ConfigurationUtils.ConfigurationType configurationType) throws Exception {
		Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/"+contextPath);
        server.setHandler(context);
        
        WebApplicationContext appContext;
        if(configurationType == ConfigurationUtils.ConfigurationType.JAVA) {
        	appContext = ConfigurationUtils.createJavaConfiguration(context.getServletContext());
        } else {
        	appContext = ConfigurationUtils.createXmlConfiguration(context.getServletContext());
        }
        
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
        context.addServlet(new ServletHolder(dispatcherServlet), "/*");
        
        server.start();
        
        return server;
	}
	
}
