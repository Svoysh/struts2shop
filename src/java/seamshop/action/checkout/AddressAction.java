package seamshop.action.checkout;

import static seamshop.action.checkout.AbstractCheckoutAction.PARAM_CHECKOUT_ID;
import static seamshop.interceptor.transaction.TransactionType.WRITE;

import java.util.Collection;

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
import seamshop.dto.Country;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Address;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

@Component("checkoutAddressAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		location = "address.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "${returnTo}", PARAM_CHECKOUT_ID, "${checkoutId}"}
	),
})
// TODO: Move this action to /member group? (y)
// TODO: Refactor: Duplication: Extract superclass for Shipping/BillingAddressAction-s.
@SuppressWarnings("serial")
public class AddressAction extends AbstractCheckoutInProcessAction
	implements BeforeValidationAware, Preparable
{
	private String returnTo;
	private Long addressId;
	private Address address = new Address();

	@Override
	public void prepare()
	{
		log.debug("call prepare(); addressId=" + addressId);
		if (!isNewAddress())
		{
			address = addressDao.getMineById(addressId);
		}
	}

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		// Nothing.

		return INPUT;
	}

	@AllowedMethod
	@Transactional(WRITE)
	public String save()
	{
		log.debug("call save()");
		try
		{
			if (isNewAddress())
			{
				// Add this address to current user's addresses.
				address.setUser(getCurrentUser());
				entityManager.persist(address);
				entityManager.flush();
				// TODO: i18n
				addActionMessage("New address <strong>" + escape(address.getSummary()) +
					"</strong> saved successfully.");
			}
			else
			{
				// TODO: What to do/write here?
			}
			return SUCCESS;
		}
		catch (Exception ex)
		{
			log.error("Failed to save new address.", ex);
			// TODO: i18n
			addActionError("Failed to save this address. Please, try again later.");
			return ERROR;
		}
	}

	@AllowedMethod
	@Transactional(WRITE)
	public String update()
	{
		log.debug("call update()");
		try
		{
			if (isNewAddress())
			{
				entityManager.flush();
				// TODO: i18n
				addActionMessage("Address <strong>" + escape(address.getSummary()) +
					"</strong> updated successfully.");
			}
			else
			{
				// TODO: What to do/write here?
			}
			return SUCCESS;
		}
		catch (Exception ex)
		{
			log.error("Failed to update address.", ex);
			// TODO: i18n:
			addActionError("Failed to update this address. Please, try again later.");
			return ERROR;
		}
	}

	/*@AllowedMethod
	@Transactional(WRITE)
	public String delete()
	{
		// TODO: Impl: Delete user address.
	}*/

	public boolean isNewAddress()
	{
		return null == addressId;
	}

	public String getReturnTo()
	{
		return returnTo;
	}

	/**
	 * Safely set "returnTo" through escaping parameter.
	 */
	public void setReturnTo(String returnTo)
	{
		// TODO: Think: Maybe there is no need in escaping? (mb)
		this.returnTo = escape(returnTo);
	}

	public Long getAddressId()
	{
		return addressId;
	}

	public void setAddressId(Long addressId)
	{
		this.addressId = addressId;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public Collection<Country> getCountries()
	{
		return countryCollection.getCollection();
	}
}
