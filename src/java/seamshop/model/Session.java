package seamshop.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import seamshop.consts.Time;

// TODO: Rename: "UserSession", "RememberMe", "SessionToken", "LoginToken"? (mb, n)
// TODO: Make cron job to clean DB from expired session.
@Entity
@SuppressWarnings("serial")
public class Session extends AbstractExpirableEntity
{
	/*@Column(nullable = false, updatable = false)
	private String subdomain;*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_session_user_id")
	private User user;

	@Override
	public long getDurationInMillis()
	{
		return Time.SESSION_DURATION_IN_MILLIS;
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
