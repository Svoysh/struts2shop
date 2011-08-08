package seamshop.action;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;

/**
 * @author Alex Siman 2009-11-07
 */
@Component
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class ErrorAction extends AbstractGuestAction
{
	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("call execute()");

		return SUCCESS;
	}
}
