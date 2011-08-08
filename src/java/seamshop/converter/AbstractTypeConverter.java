package seamshop.converter;

import org.apache.struts2.util.StrutsTypeConverter;

import seamshop.util.Log;

/**
 * @author Alex Siman 2009-05-30
 */
public abstract class AbstractTypeConverter extends StrutsTypeConverter
{
	protected final Log log = new Log(this.getClass());
}
