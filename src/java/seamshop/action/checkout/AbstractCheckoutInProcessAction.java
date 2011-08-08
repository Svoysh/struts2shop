package seamshop.action.checkout;

import seamshop.context.util.Checkout;

@SuppressWarnings("serial")
public abstract class AbstractCheckoutInProcessAction extends AbstractCheckoutAction
{
	protected String checkoutId;

	public String getCheckoutId()
	{
		return checkoutId;
	}

	public void setCheckoutId(String checkoutId)
	{
		this.checkoutId = checkoutId;
	}

	protected Long getShopId()
	{
		return getCheckout().getShopId();
	}

	protected Checkout getCheckout()
	{
		return sessionContext.getCheckoutList().get(checkoutId);
	}

	/**
	 * End current checkout.
	 */
	protected void endCheckout()
	{
		sessionContext.getCheckoutList().remove(checkoutId);
	}
}
