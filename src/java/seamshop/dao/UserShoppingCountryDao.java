package seamshop.dao;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.model.UserShoppingCountry;

/**
 * @author Alex Siman 2009-12-29
 */
@Component
public class UserShoppingCountryDao extends GenericDao<UserShoppingCountry>
{
	@SuppressWarnings("unchecked")
	public List<UserShoppingCountry> getAllByCurrentUser()
	{
		List<UserShoppingCountry> result = new ArrayList<UserShoppingCountry>();
		if (hasCurrentUser())
		{
			result = createCriteria().add(eq("user.id", getCurrentUserId())).list();
		}
		return result;
	}
}
