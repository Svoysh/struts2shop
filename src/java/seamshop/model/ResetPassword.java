package seamshop.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import seamshop.consts.Time;

// TODO: Low: Rename to: "PasswordReset", "ResetPasswordData"? (mb)
// TODO: Make cron-job to clean DB from expired (w/in 5 days?) entities.
@Entity
@SuppressWarnings("serial")
public class ResetPassword extends AbstractExpirableEntity
{
	/**
	 * Copy of <code>user.passwordHash</code> on the moment of creation of this entity.
	 * This passwordHash must be equal to current <code>user.passwordHash</code> to be valid
	 * confirmation.
	 */
	// TODO: Needed? (n)
	/*@Column(nullable = false, updatable = false)
	private String passwordHash;*/

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_reset_password_user_id")
	private User user;

	@Override
	public long getDurationInMillis()
	{
		return Time.RESET_PASSWORD_DURATION_IN_MILLIS;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
