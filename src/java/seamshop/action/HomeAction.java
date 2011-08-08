package seamshop.action;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;

@Component
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class HomeAction extends AbstractGuestAction
{
	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("call execute()");

		return SUCCESS;
	}
}
