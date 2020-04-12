package com.icbc;

import java.net.URL;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launched {

	public static void main(String[] args) {

		URL resource = Launched.class.getResource("/");
		System.out.println(resource);
		String webappRootPath = null;
		if (resource == null) {
			resource = Thread.currentThread().getContextClassLoader().getResource("/");
		}

		if (resource != null) {
			webappRootPath = resource.getPath();
		} else {
			System.out.println("找不到对应路径，程序退出！");
			System.exit(1);
		}

		String webappPath = webappRootPath + "WEB-INF/web.xml";
		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		connector.setIdleTimeout(30000);
		connector.setAcceptQueueSize(1024);

		server.setConnectors(new Connector[] { connector });

		WebAppContext webContext = new WebAppContext(webappRootPath, "/");
		webContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "true");
		// 设置web.xml位置
		webContext.setDescriptor(webappPath);
		// 设置webapp的位置
		webContext.setResourceBase(webappRootPath + ".." + "/LookDbs");
		webContext.setClassLoader(Thread.currentThread().getContextClassLoader());

		server.setHandler(webContext);
		// 通过Configuration来配置可以使用jetty相关xml文件以及servlet注解
		// 只有配置AnnotationConfiguration才能创建jsp容器
		Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
		classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration");
		/** 设置ContainerIncludeJarPattern属性，以便jetty检查这些容器路径的jar，web-fragments等
		如果您省略了包含jstl .tlds的jar，那么jsp引擎将会替代它们进行扫描。（可以不配置） */
		webContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$");

		try {
			server.start();
			server.join();
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
