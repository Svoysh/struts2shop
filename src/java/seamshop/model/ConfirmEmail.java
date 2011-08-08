package seamshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import seamshop.consts.Time;
import seamshop.util.StringUtils;

// TODO: Low: Rename to: "EmailConfirmation", "ConfirmEmailData"? (mb)
// TODO: Make cron-job to clean DB from expired (w/in 5 days?) entities.
@Entity
@SuppressWarnings("serial")
public class ConfirmEmail extends AbstractExpirableEntity
{
	/**
	 * Copy of <code>user.email</code> on the moment of creation of this entity.
	 * This email must be equal to current <code>user.email</code> to be valid
	 * confirmation.
	 */
	@Column(/*unique = true,*/ nullable = false, updatable = false)
	private String email;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_confirm_email_user_id")
	private User user;

	/**
	 * Concurrent key: <code>email + "-" + user.id</code>.
	 */
	// TODO: High: Set length = email.length + 1 + user.id.length.
	// TODO: High: Use conkey almost for all entities like: CartItem, OrderItem, Order, Shop, etc.
	// TODO: Low: Rename to: "unikey", "ukey"?
	@Column(unique = true, nullable = false, updatable = false)
	private String conkey;

	@Override
	public long getDurationInMillis()
	{
		return Time.CONFIRM_EMAIL_DURATION_IN_MILLIS;
	}

	@Override
	protected void onPrePersist()
	{
		super.onPrePersist();

		String currentEmail = user.getEmail();
		if (StringUtils.isNullOrEmpty(currentEmail))
		{
			throw new NullPointerException("User email cannot be null.");
		}
		email = currentEmail;
		conkey = email + CONKEY_SEPARATOR + user.getId();
	}

	public String getEmail()
	{
		return email;
	}

	@SuppressWarnings("unused")
	private void setEmail(String email)
	{
		this.email = email;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public String getConkey()
	{
		return conkey;
	}

	@SuppressWarnings("unused")
	private void setConkey(String conkey)
	{
		this.conkey = conkey;
	}
}
