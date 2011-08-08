package seamshop.model;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.mindrot.jbcrypt.BCrypt;

import seamshop.model.enums.UserRole;
import seamshop.util.Log;

@Entity
@SuppressWarnings("serial")
public class User extends AbstractIdBasedEntity
{
	@Transient
	protected static final Log staticLog = new Log(User.class);

	/**
	 * Used instead of a "username" notion to log into the webapp. Also used
	 * directly as email: to restore forgotten password, to notify about
	 * shopping order status, etc. Cannot be <code>null</code>.
	 * It must be confirmed/validated through unique web link.
	 */
	// TODO: Make it case-sensitive? (n)
	// TODO: Low: Set "updatable = false" for security issues? (n)
	@Column(length = 255, unique = true, nullable = false, updatable = true)
	private String email;

	@Column(nullable = false, updatable = true)
	private String passwordHash;

	/*
	 * TODO: Utilize "User.enabled" field somehow.
	 * Maybe do not allow to login if user did not validate its email.
	 * Or allow to login only for 1 day while email contains not validated, etc.
	 */
	// TODO: Rename: "emailConfirmed", "confirmed"? (mb)
	private boolean enabled = false;

	// TODO: Impl: Auth:
//	private boolean registered = false;

	@Column(nullable = false, updatable = true)
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.DEFAULT;

	@Column(length = 255, nullable = true, updatable = true)
	private String firstName;

	@Column(length = 255, nullable = true, updatable = true)
	private String lastName;

	// TODO: Impl: timeZone. Use for date printing, formatting, etc.
	// TODO: Impl: localeCode. Save selected UI locale to User entity.

	// TODO: Implement.
//	private String fullName; // firstName + lastName
//	private Image avatar;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Shop> shops = new HashSet<Shop>();

	// TODO: Rethink usecase: "Guest + Cart" - create new user entity? Not for SE.
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "customer", cascade = {CascadeType.PERSIST})
	private Cart cart;

	// TODO: Refactor Set to List with "number"? (mb) See Shop.
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Address> addresses = new HashSet<Address>();

	/** Countries where user wants to shop. */
	// TODO: High: Autoset country by user IP and Google Geolocation.
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<UserShoppingCountry> shoppingCountries = new ArrayList<UserShoppingCountry>();

	/**
	 * Hash user's password and return hashed password.
	 *
	 * @param user user which password will be hashed
	 * @return hashed password
	 */
	public static String hashPassword(String password)
	{
		// Gensalt's log_rounds parameter determines the complexity
		// the work factor is 2**log_rounds, and the default is 10.
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Check that an unencrypted password matches one that has previously been hashed.
	 *
	 * @param candidatePassword candidate password
	 * @param passwordHash password hash
	 * @return <code>true</code> if <code>candidatePassword</code> is valid
	 */
	public static boolean isValidPassword(String candidatePassword, String passwordHash)
	{
		try
		{
			if (BCrypt.checkpw(candidatePassword, passwordHash))
			{
				return true;
			}
		}
		catch (Exception ex)
		{
			staticLog.error("Password validation failed", ex);
		}

		return false;
	}

	// Initializer. Called at the beginning of every constructor of this class.
	{
		cart = new Cart();
		cart.setCustomer(this);
	}

	public User()
	{}

	public User(String email, String password)
	{
		setEmail(email);
		setPassword(password);
	}

	// TODO: Fix: Refactor: Return value based on "fullNameType" or custom
	//       "fullName" property.
	public String getFullName()
	{
		// TODO: Low: i18n
		String result = "[UNDEFINED]";

		// TODO: Low: Optimize this "if".
		if (!isBlank(firstName)
			&& !isBlank(lastName))
		{
			result = firstName + " " + lastName;
		}
		else if (!isBlank(firstName)
			&& isBlank(lastName))
		{
			result = firstName;
		}
		else if (isBlank(firstName)
			&& !isBlank(lastName))
		{
			result = lastName;
		}

		return result;
	}

	/**
	 * Hash provided password and set password hash to it.
	 */
	public void setPassword(String password)
	{
		log.debug("Creating hash of new password");
		passwordHash = hashPassword(password);
	}

	/**
	 * Check if an unencrypted password matches to this user's password hash.
	 *
	 * @return <code>true</code> if <code>candidatePassword</code> is valid
	 */
	public boolean isValidPassword(String candidatePassword)
	{
		return isValidPassword(candidatePassword, passwordHash);
	}

	// TODO: Low: Performance: Cache result? (mb)
	public List<String> getShoppingCountryCodes()
	{
		List<String> countryCodes = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(shoppingCountries))
		{
			for (UserShoppingCountry userShoppingCountry : shoppingCountries)
			{
				countryCodes.add(userShoppingCountry.getCountryCode());
			}
		}
		return countryCodes;
	}

	// TODO: Cover by strong JUnit test!
	@Override
	public boolean equals(Object entity)
	{
		if (super.equals(entity) == false)
		{
			return false;
		}

		User user = (User) entity;

		// Simply comparing all most principal fields.
		// TODO: Compare roles? (y)
		if (email.equals(user.getEmail()) &&
			passwordHash.equals(user.getPasswordHash()) &&
			(enabled == user.isEnabled()))
		{
			return true;
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName()).append("{")
				.append("id=").append(getId()).append("; ")
				.append("email=").append(email).append("; ")
				.append("role=").append(role).append("; ")
				.append("enabled=").append(enabled)
			.append("}")
			.toString();
	}

	private String getPasswordHash()
	{
		return passwordHash;
	}

	@SuppressWarnings("unused")
	private void setPasswordHash(String passwordHash)
	{
		this.passwordHash = passwordHash;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public UserRole getRole()
	{
		return role;
	}

	public void setRole(UserRole role)
	{
		this.role = role;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Set<Shop> getShops()
	{
		return shops;
	}

	public void setShops(Set<Shop> shops)
	{
		this.shops = shops;
	}

	public Cart getCart()
	{
		return cart;
	}

	public void setCart(Cart cart)
	{
		this.cart = cart;
	}

	public Set<Address> getAddresses()
	{
		return addresses;
	}

	public void setAddresses(Set<Address> addresses)
	{
		this.addresses = addresses;
	}

	public List<UserShoppingCountry> getShoppingCountries()
	{
		return shoppingCountries;
	}

	public void setShoppingCountries(List<UserShoppingCountry> shoppingCountries)
	{
		this.shoppingCountries = shoppingCountries;
	}
}
