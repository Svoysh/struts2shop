package seamshop.dao;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.model.User;

// TODO: Refactor "get*" DAO methods to "find*"? (yes)
@Component
public class UserDao extends GenericDao<User>
{
	// FIXME: Security: Do not count admins!
	@Override
	public long count()
	{
		return super.count();
	}

	// FIXME: Security: Do not print admins!
	// TODO: Init shop count per user. See ShopDao.initProductCountOfShop();.
	@Override
	public List<User> getPage()
	{
		return super.getPage();
	}

	/**
	 * Load fresh instance of user from DB by current user ID and update
	 * current user in session context.
	 *
	 * @return reloaded instance of current user.
	 */
	public User reloadCurrentUser()
	{
		log.debug("Reloading current user...");

		User reloadedUser = null;
		Long currentUserId = getCurrentUserId();
		if (getCurrentUserId() != null)
		{
			reloadedUser = get(currentUserId);
			sessionContext.setUser(reloadedUser);
		}

		return reloadedUser;
	}

	// TODO: Low: Duplication: extract getByField(fieldNam, fieldvValue)
	//       or use Criteria API.
	// TODO: Refactor: Rename to: "findByEmail"? (y)
	public User getUserByEmail(String email)
	{
		if (isEmpty(email))
		{
			log.warn("Input parameter 'email' is null or empty.");
			return null;
		}

		String hql =
			"select u " +
			"from " + User.class.getName() + " u " +
			"where u.email = :email";

		return (User) createQuery(hql)
			.setString("email", email)
			.uniqueResult();
	}

	public boolean isPresentByEmail(String email)
	{
		return !isUniqueCriteria(createCriteria().add(eq("email", email)));
	}
}
