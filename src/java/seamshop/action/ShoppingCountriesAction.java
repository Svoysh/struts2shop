package seamshop.action;

import static seamshop.interceptor.transaction.TransactionType.WRITE;
import static seamshop.util.CollectionUtils.removeNulls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.User;
import seamshop.model.UserShoppingCountry;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Alex Siman 2009-12-24
 */
// TODO: Low: Move to "Guest Settings" action group.
// TODO: Low: UI: Rethink: Use list of checkboxes or listboxes to select countries? (listboxes)
@Component("shoppingCountriesAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"namespace", "/", "actionName", "shopping-countries"}
	)
})
@SuppressWarnings("serial")
public class ShoppingCountriesAction extends AbstractGuestAction implements Preparable
{
	private List<UserShoppingCountry> shoppingCountries = new ArrayList<UserShoppingCountry>();

	/** Used to determine which UserShoppingCountries are used no more. */
	private Set<Long> submittedShoppingCountriesIds = new HashSet<Long>();

	@Override
	public void prepare()
	{
		// Do add submitted IDs only on "submit" action.
		if (!isEmpty(shoppingCountries))
		{
			removeNulls(shoppingCountries);
			for (UserShoppingCountry shoppingCountry : shoppingCountries)
			{
				Long shoppingCountryId = shoppingCountry.getId();
				if (null != shoppingCountryId)
				{
					submittedShoppingCountriesIds.add(shoppingCountryId);
				}
			}
		}
		User currentUser = userDao.reloadCurrentUser();
		shoppingCountries = currentUser.getShoppingCountries();
	}

	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();
		log.debug("Cleaning up shoppingCountries from null elements");
		removeNulls(shoppingCountries);
	}

	@Override
	public void validate()
	{
		super.validate();
		// Nothing.
	}

	@SkipValidation
	@Override
	public String execute()
	{
		return INPUT;
	}

	@AllowedMethod
	@Transactional(WRITE)
	public String submit()
	{
		Set<Long> shoppingCountryIdsToDelete = new HashSet<Long>();

		// Remove no more used UserShoppingCountry-es.
		if (!isEmpty(shoppingCountries))
		{
			List<UserShoppingCountry> copyOfShoppingCountries =
				new ArrayList<UserShoppingCountry>(shoppingCountries);
			Set<String> usedCountryCodes = new HashSet<String>();
			for (UserShoppingCountry shoppingCountry : copyOfShoppingCountries)
			{
				Long shoppingCountryId = shoppingCountry.getId();
				boolean deleteShoppingCountry = false;
				// If shoppingCountry was not found in submitted form.
				// So it must be removed.
				if ((null != shoppingCountryId) &&
					!submittedShoppingCountriesIds.contains(shoppingCountryId))
				{
					deleteShoppingCountry = true;
				}
				else
				{
					shoppingCountry.setUser(getCurrentUser());
					String countryCode = shoppingCountry.getCountryCode();
					if (!usedCountryCodes.contains(countryCode) &&
						countryCollection.isValidCode(countryCode))
					{
						usedCountryCodes.add(countryCode);
					}
					else
					{
						// This country code was already used.
						// This shoppingCountry must be removed as duplicate.
						log.debug("Removing UserShoppingCountry with " +
							"duplicated or invalid country code: [" + countryCode + "]");
						shoppingCountries.remove(shoppingCountry);
						if (null != shoppingCountryId)
						{
							deleteShoppingCountry = true;
						}
					}
				}

				if (deleteShoppingCountry)
				{
					log.debug("Planed to delete entity: [" + shoppingCountry + "]");
					shoppingCountryIdsToDelete.add(shoppingCountryId);
				}
			}
		}

		// If there are entities to delete.
		if (!isEmpty(shoppingCountryIdsToDelete))
		{
			userShoppingCountryDao.deleteBatch(shoppingCountryIdsToDelete);
		}

		entityManager.flush();

		// TODO: i18n
		addActionMessage("Shopping countries updated successfully.");

		return SUCCESS;
	}

	public List<UserShoppingCountry> getShoppingCountries()
	{
		return shoppingCountries;
	}

	public void setShoppingCountries(List<UserShoppingCountry> shoppingCountries)
	{
		this.shoppingCountries = shoppingCountries;
	}

	public Collection<Country> getCountries()
	{
		return countryCollection.getCollection();
	}
}
