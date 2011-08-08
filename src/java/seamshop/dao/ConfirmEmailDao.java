package seamshop.dao;

import org.springframework.stereotype.Component;

import seamshop.model.ConfirmEmail;
import seamshop.util.StringUtils;

/**
 * @author Alex Siman 2009-08-31
 */
@Component
public class ConfirmEmailDao extends GenericDao<ConfirmEmail>
{
	public ConfirmEmail getByEmailAndUserId(String email, Long userId)
	{
		if (StringUtils.isNullOrEmpty(email))
		{
			log.warn("Input parameter 'email' is null or empty.");
			return null;
		}
		else if (userId == null)
		{
			log.warn("Input parameter 'userId' is null.");
			return null;
		}

		String hql =
			"select ce " +
			"from " + ConfirmEmail.class.getName() + " ce " +
			"where (ce.email = :email) and (ce.user.id = :userId)";

		return (ConfirmEmail) createQuery(hql)
			.setString("email", email)
			.setLong("userId", userId)
			.uniqueResult();
	}
}
