package seamshop.action.checkout;

import org.apache.struts2.convention.annotation.Results;

import seamshop.action.AbstractGuestAction;

@Results({
	// TODO: Test if this redirect works.
	// TODO: Fix: Cycling to cart!items w/ INPUT result, such as on fail any
	//       subclass of AbstractCheckoutAction add error message.
	/*@Result(
		name = Action.ERROR,
		type = ResultType.REDIRECT_ACTION,
		params = {"namespace", "/", "actionName", "cart!subcarts"}
	)*/
})
@SuppressWarnings("serial")
public abstract class AbstractCheckoutAction extends AbstractGuestAction
{
	protected static final String PARAM_CHECKOUT_ID = "checkoutId";
}
