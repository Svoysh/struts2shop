package seamshop.action.checkout;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;

/**
 * @author Alex Siman 2009-11-09
 */
@Component("checkoutEndAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class EndAction extends AbstractCheckoutInProcessAction
{
	private String orderNumber;

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		// TODO: Remove successfully placed order from session context.

		return SUCCESS;
	}

	public String getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber)
	{
		this.orderNumber = orderNumber;
	}
}
