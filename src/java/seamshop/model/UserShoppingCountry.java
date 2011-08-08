package seamshop.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Used to filter shops by country for user's shopping.
 *
 * @author Alex Siman 2009-12-24
 */
@Entity
@SuppressWarnings("serial")
public class UserShoppingCountry extends AbstractEntityWithCountry
{
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_user_shopping_country_user_id")
	private User user;

	public UserShoppingCountry()
	{}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
