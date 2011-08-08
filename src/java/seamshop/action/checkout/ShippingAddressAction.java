package seamshop.action.checkout;

import static seamshop.action.checkout.AbstractAddressAction.ACTION_SHIPPING_ADDRESS;
import static seamshop.action.checkout.AbstractAddressAction.RESULT_REDIRECT_NEW_ADDRESS;
import static seamshop.action.checkout.AbstractCheckoutAction.PARAM_CHECKOUT_ID;
import static seamshop.consts.ResultType.REDIRECT_ACTION;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;

import com.opensymphony.xwork2.Action;

@Component("checkoutShippingAddressAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		location = "shipping-address.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shipping-method", PARAM_CHECKOUT_ID, "${checkoutId}"}
	),
	@Result(
		name = RESULT_REDIRECT_NEW_ADDRESS,
		type = REDIRECT_ACTION,
		params = {"actionName", "address", PARAM_CHECKOUT_ID, "${checkoutId}",
			"returnTo", ACTION_SHIPPING_ADDRESS}
	),
})
// TODO: Refactor: Duplication: Extract superclass for Shipping/BillingAddressAction-s.
@SuppressWarnings("serial")
public class ShippingAddressAction extends AbstractAddressAction
{
	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();

		// TODO: Preload user's addresses if user is a member.
	}

	@AllowedMethod
	public String submit()
	{
		log.debug("call submit()");
		if (null != addressId)
		{
			getCheckout().setShippingAddressId(addressId);
			return SUCCESS;
		}
		else
		{
			// TODO: i18n
			// TODO: Rethink: Write descriptive sentence of error.
			addActionError("Please return to a previous page and try again.");
			return ERROR;
		}
	}

	@AllowedMethod
	@Override
	public String newAddress()
	{
		log.debug("call newAddress()");

		// Nothing.

		return RESULT_REDIRECT_NEW_ADDRESS;
	}

	@Override
	public boolean isBillingAddress()
	{
		return false;
	}

	@Override
	public String getReturnTo()
	{
		return ACTION_SHIPPING_ADDRESS;
	}
}
