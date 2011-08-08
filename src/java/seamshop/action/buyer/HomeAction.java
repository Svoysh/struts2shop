package seamshop.action.buyer;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;

@Component("buyerHomeAction")
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class HomeAction extends AbstractBuyerAction
{
	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("--------------- execute()");

		return SUCCESS;
	}
}
