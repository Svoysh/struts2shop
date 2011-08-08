package seamshop.util;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Alex Siman 2009-10-16
 */
public class SpringUtils
{
	private SpringUtils()
	{
		// This is helper class.
		// Do not create instances of this class.
	}

    public static ApplicationContext getApplicationContext()
    {
    	ServletContext servletContext = ServletActionContext.getServletContext();
    	if (servletContext == null)
		{
    		throw new NullPointerException(
    			"This method can be called only within servlet environment.");
		}

        return WebApplicationContextUtils
        	.getWebApplicationContext(servletContext);
    }

    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanId)
    {
    	return (T) getApplicationContext().getBean(beanId);
    }
}
