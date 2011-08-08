package seamshop.action;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;

/**
 * Helps to be able to place current user object to value stack.
 *
 * @author Alex Siman 2009-05-16
 */
@Component
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class DefaultAction extends AbstractGuestAction
{
	@SkipValidation
	@Override
	public String execute()
	{
		return SUCCESS;
	}
}
