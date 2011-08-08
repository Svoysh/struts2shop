package seamshop.action.checkout;

import static seamshop.action.checkout.AbstractCheckoutAction.PARAM_CHECKOUT_ID;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.ShippingMethod;

import com.opensymphony.xwork2.Action;

/**
 * @author Alex Siman 2009-11-09
 */

@Component("checkoutShippingMethodAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		location = "shipping-method.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "payment-method", PARAM_CHECKOUT_ID, "${checkoutId}"}
	),
})
@SuppressWarnings("serial")
// TODO: Refactor: Duplication: See PaymentMethodAction.
public class ShippingMethodAction extends AbstractCheckoutInProcessAction
	implements BeforeValidationAware
{
	private List<ShippingMethod> shippingMethods;
	private Long shippingMethodId;

	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();

		Long shopId = getShopId();
		shippingMethods = shippingMethodDao.getAllByShopId(shopId);
	}

	@Override
	public void validate()
	{
		super.validate();

		if ((shippingMethodId == null) && !isEmpty(shippingMethods))
		{
			// TODO: i18n
			addFieldError("shippingMethod", "You should choose shipping method.");
		}
	}

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		// Redirect to payment method page, if shop does not have any shipping method.
		if (isEmpty(shippingMethods))
		{
			return SUCCESS;
		}
		// Let user select one of shop's shipping methods.
		else
		{
			return INPUT;
		}
	}

	@AllowedMethod
	public String submit()
	{
		log.debug("call submit()");

		getCheckout().setShippingMethodId(shippingMethodId);

		return SUCCESS;
	}

	public Long getShippingMethodId()
	{
		return shippingMethodId;
	}

	public void setShippingMethodId(Long shippingMethodId)
	{
		this.shippingMethodId = shippingMethodId;
	}

	public List<ShippingMethod> getShippingMethods()
	{
		return shippingMethods;
	}
}
