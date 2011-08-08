package seamshop.action.checkout;

import static com.opensymphony.xwork2.Action.SUCCESS;
import static seamshop.action.checkout.AbstractCheckoutAction.PARAM_CHECKOUT_ID;
import static seamshop.consts.ResultType.REDIRECT_ACTION;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.PaymentMethod;

import com.opensymphony.xwork2.Action;

/**
 * @author Alex Siman 2009-11-09
 */

@Component("checkoutPaymentMethodAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		location = "payment-method.jsp"
	),
	@Result(
		name = SUCCESS,
		type = REDIRECT_ACTION,
		params = {"actionName", "review-order", PARAM_CHECKOUT_ID, "${checkoutId}"}
	),
})
@SuppressWarnings("serial")
// TODO: Refactor: Duplication: See ShippingMethodAction.
public class PaymentMethodAction extends AbstractCheckoutInProcessAction
	implements BeforeValidationAware
{
	private List<PaymentMethod> paymentMethods;
	private Long paymentMethodId;

	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();

		Long shopId = getShopId();
		paymentMethods = paymentMethodDao.getAllByShopId(shopId);
	}

	@Override
	public void validate()
	{
		super.validate();

		if ((paymentMethodId == null) && !isEmpty(paymentMethods))
		{
			// TODO: i18n
			addFieldError("paymentMethod", "You should choose payment method.");
		}
	}

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		// Redirect to order review page, if shop does not have any payment method.
		if (isEmpty(paymentMethods))
		{
			return SUCCESS;
		}
		// Let user select one of shop's payment methods.
		else
		{
			return INPUT;
		}
	}

	@AllowedMethod
	public String submit()
	{
		log.debug("call submit()");

		getCheckout().setPaymentMethodId(paymentMethodId);

		return SUCCESS;
	}

	public Long getPaymentMethodId()
	{
		return paymentMethodId;
	}

	public void setPaymentMethodId(Long paymentMethodId)
	{
		this.paymentMethodId = paymentMethodId;
	}

	public List<PaymentMethod> getPaymentMethods()
	{
		return paymentMethods;
	}
}
