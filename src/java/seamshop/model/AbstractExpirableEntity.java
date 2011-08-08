package seamshop.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

/**
 * @author Alex Siman 2009-08-31
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractExpirableEntity extends AbstractUuidBasedEntity
{
	public abstract long getDurationInMillis();

	// TODO: High: Test unit.
	public boolean isExpired()
	{
		return System.currentTimeMillis() > getExpirationTimeInMillis();
	}

	public long getExpirationTimeInMillis()
	{
		return getExpirationDate().getTime();
	}

	// TODO: High: Test unit.
	public Date getExpirationDate()
	{
		// TODO: Low: Cache? (mb)
		return new Date(getCreated().getTime() + getDurationInMillis());
	}
}
