package seamshop.action.checkout;

import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import seamshop.dto.Country;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.model.Address;

@SuppressWarnings("serial")
public abstract class AbstractAddressAction
	extends AbstractCheckoutInProcessAction implements BeforeValidationAware
{
	public static final String RESULT_REDIRECT_NEW_ADDRESS = "redirect-new-address";

	public static final String ACTION_BILLING_ADDRESS = "billing-address";
	public static final String ACTION_SHIPPING_ADDRESS = "shipping-address";

	protected Long addressId;

	protected List<Address> addresses;

	/**
	 * @return
	 * <ul>
	 *     <li><code>true</code> - if it is action for billing address;</li>
	 *     <li><code>false</code> - if it is action for shipping address;</li>
	 * </ul>
	 */
	// TODO: Remove as unneeded? (mb)
	public abstract boolean isBillingAddress();

	public abstract String getReturnTo();

	public abstract String newAddress();

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		addresses = addressDao.getAllMine();
		if (isEmpty(addresses))
		{
			return newAddress();
		}

		return INPUT;
	}

	public Long getAddressId()
	{
		return addressId;
	}

	public void setAddressId(Long addressId)
	{
		this.addressId = addressId;
	}

	public List<Address> getAddresses()
	{
		return addresses;
	}

	public Collection<Country> getCountries()
	{
		return countryCollection.getCollection();
	}
}