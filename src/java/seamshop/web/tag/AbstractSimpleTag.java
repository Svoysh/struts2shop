package seamshop.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import seamshop.action.AbstractAction;
import seamshop.util.Log;

import com.opensymphony.xwork2.ActionContext;

/**
 * Base class for any custom tag. Has helper methods, logger.
 *
 * @author Alex Siman 2009-10-20
 */
public abstract class AbstractSimpleTag extends SimpleTagSupport
{
	protected final Log log = new Log(this.getClass());

	public AbstractSimpleTag()
	{
		super();
	}

	@Override
	public void doTag() throws JspException, IOException
	{
		try
		{
			print();
		}
		catch (Exception ex)
		{
			log.error("Tag encountered error: ", ex);
		}
	}

	public abstract void print() throws Exception;

	protected void printJspBodyIfExists() throws JspException, IOException
	{
		JspFragment jspBody = getJspBody();
		if (jspBody != null)
		{
			jspBody.invoke(null);
		}
		else
		{
			log.debug("jspBody is null.");
		}
	}

	protected JspWriter getJspWriter()
	{
		return getJspContext().getOut();
	}

	protected void printText(String text) throws IOException
	{
		getJspWriter().write(text);
	}

	/**
	 * @return Currently executing Struts 2 action.
	 */
	@SuppressWarnings("unchecked")
	protected <A extends AbstractAction> A getAction()
	{
		return (A) ActionContext.getContext().getActionInvocation().getAction();
	}

	protected static TagAttribute attribute(String name, String value)
	{
		return new TagAttribute(name, value);
	}

	protected static TagAttribute attribute(String name, String value, boolean escape)
	{
		return new TagAttribute(name, value, escape);
	}

	protected static TagAttribute idAttribute(String value)
	{
		return new TagAttribute("id", value);
	}

	protected static TagAttribute classAttribute(String value)
	{
		return new TagAttribute("class", value);
	}
}
