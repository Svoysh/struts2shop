package seamshop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ForeignKey;

@Entity
@SuppressWarnings("serial")
public class Cart extends AbstractIdBasedEntity
{
	@OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<CartItem> cartItems = new HashSet<CartItem>();

	// TODO: Rename to "user", "owner"? (xz, y)
	// TODO: Set customer, if current user is registered.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_cart_user_id")
	private User customer;

	public Set<CartItem> getCartItems()
	{
		return cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems)
	{
		this.cartItems = cartItems;
	}

	public User getCustomer()
	{
		return customer;
	}

	public void setCustomer(User customer)
	{
		this.customer = customer;
	}
}
