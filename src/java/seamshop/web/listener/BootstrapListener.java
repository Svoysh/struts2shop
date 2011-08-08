package seamshop.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstrap listener for whole webapp. Here must be started up some frameworks
 * such as Spring or init static variables etc.
 *
 * @author Alex Siman 2008-10-22
 * @author Alex Siman 2009-11-05
 */
public class BootstrapListener implements ServletContextListener
{
	/**
	 * Initialize the root web application context.
	 */
	public void contextInitialized(ServletContextEvent event)
	{
		// TODO: Singletons or Factories can be initialized here.
		//       See [seamshop.interceptor.security.ActionRoleResolverAccessor].

		// Nothing to do yet.
	}

	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event)
	{
		// Nothing to do yet.
	}
}
