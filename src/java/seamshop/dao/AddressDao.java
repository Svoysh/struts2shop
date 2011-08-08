package seamshop.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import seamshop.model.Address;

/**
 * @author Alex Siman 2009-11-10
 */
@Component
public class AddressDao extends GenericDao<Address>
{
	// TODO: Refactor: Extract this method to GenericDao? (y)
	public Address getMineById(Long addressId)
	{
		Address resultAddress = null;
		if (addressId == null)
		{
			log.warn("'addressId' is null.");
			return resultAddress;
		}
		else if (!hasCurrentUser())
		{
			log.warn("Current user is null.");
			return resultAddress;
		}

		String hql = new StringBuilder()
			.append("from ").append(Address.class.getName()).append(" a ")
			.append("where (a.id = :addressId) and (a.user.id = :userId) ")
			.append("order by a.created")
			.toString();

		return (Address) createQuery(hql)
			.setLong("addressId", addressId)
			.setLong("userId", getCurrentUserId())
			.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Address> getAllMine()
	{
		if (!hasCurrentUser())
		{
			// TODO: Refactor: Duplication: Generalize this warn message.
			log.warn("Current user is null.");
			return new ArrayList<Address>();
		}

		String hql = new StringBuilder()
			.append("from ").append(Address.class.getName()).append(" a ")
			.append("where a.user.id = :userId ")
			.append("order by a.created")
			.toString();

		return createQuery(hql)
			.setLong("userId", getCurrentUserId())
			.list();
	}
}
