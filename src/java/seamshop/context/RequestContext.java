package seamshop.context;

import java.io.Serializable;

import seamshop.actionutil.Pager;
import seamshop.util.Log;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ValidationAwareSupport;

/**
 * Helper class to store request scoped objects. It is Spring bean.
 * <p/>
 * Usage: update values of objects of this context in Struts interceptors,
 * place it after defaultStack interceptor.
 *
 * @author Alex Siman 2009-05-16
 * @author Alex Siman 2009-07-30 (Added ValidationAware.)
 */
// This Spring bean configured in "applicationContext.xml".
// TODO: Remove Spring related annotations? (y)
//@Component
//@Scope(WebApplicationContext.SCOPE_REQUEST)

@SuppressWarnings("serial")
public class RequestContext implements Serializable
{
	private final Log log = new Log(this.getClass());

	private Action action = null;

	private ValidationAwareSupport validationAware = null;

	private Pager pager = new Pager();

	// TODO: Low: Need here? (y)
//	private Map<String, Cookie> cookies = null;

	public Action getAction()
	{
		return action;
	}

	public void setAction(Action action)
	{
		this.action = action;
	}

	public ValidationAwareSupport getValidationAware()
	{
		return validationAware;
	}

	public void setValidationAware(ValidationAwareSupport validationAware)
	{
		if ((this.validationAware != null) && (this.validationAware != validationAware))
		{
			log.warn("Setting new validation aware that is set already.");
		}
		this.validationAware = validationAware;
	}

	public Pager getPager()
	{
		return pager;
	}

	public void setPager(Pager pager)
	{
		this.pager = pager;
	}
}
