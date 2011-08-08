package seamshop.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;

import seamshop.util.Log;

// FIXME: Bug: "version" set to NULL for every sub-entity. Maybe remove @SuppressWarnings("unused") from version setter or move version to every sub-entity.
// TODO: Low: Performance: Impl "toString" of every subclass using StringBuilder.
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractEntity<ID extends Serializable> implements Serializable
{
	protected static final String CONKEY_SEPARATOR = "-";

	@Transient
	protected final Log log = new Log(this.getClass());

	@Version
	private Integer version;

	@Column(nullable = false, updatable = false)
	private Date created;

	@Column(nullable = false, updatable = true)
	private Date updated;

	// Transient --------------------------------------------------------------

	@Transient
	private Integer hashCodeValue;

	public String[] getIndexedFields()
	{
		return new String[0];
	}

	public abstract ID getEntityId();

	public boolean hasId()
	{
		return null != getEntityId();
	}

	public Integer getVersion()
	{
		return version;
	}

	@SuppressWarnings("unused")
	private void setVersion(Integer version)
	{
		this.version = version;
	}

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(Date created)
	{
		this.created = created;
	}

	public Date getUpdated()
	{
		return updated;
	}

	public void setUpdated(Date updated)
	{
		this.updated = updated;
	}

	@PrePersist
	protected void onPrePersist()
	{
		Date now = new Date();
		created = now;
		updated = now;
	}

	/*
	 * FIXME: Do not use this method, because it reflects implicit changes to entity.
	 * E.g. if quantity of product has been changed while creating order then
	 * "updated" will be changed too - what is incorrect. But "updated" must be
	 * changed only when user updates its owned entity (shop, product) explicitly.
	 */
	@PreUpdate
	protected void onPreUpdate()
	{
		updated = new Date();
	}

	/**
	 * This equals method will check the equality of two
	 * <code>AbstractEntity</code>s by their IDs.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object entity)
	{
		// When compare to null, always return false.
		if (entity == null)
		{
			return false;
		}
		// Checking if other object is not sub-type of AbstractEntity then return
		// false.
		if (!(entity instanceof AbstractEntity))
		{
			return false;
		}
		// If object addresses equal, then this is the same object.
		if (this == entity)
		{
			return true;
		}

		AbstractEntity that = (AbstractEntity) entity;
		Serializable thatId = that.getEntityId();
		Serializable thisId = getEntityId();

		// If IDs of objects are null, then they are not equal.
		if ((thisId == null) || (thatId == null))
		{
			return false;
		}

		// Finally comparing objects IDs.
		return thisId.equals(thatId);
	}

	@Override
	public int hashCode()
	{
		if (hashCodeValue == null)
		{
			Serializable thisId = getEntityId();
			if (thisId == null)
			{
				hashCodeValue = super.hashCode();
			}
			else
			{
				hashCodeValue = thisId.hashCode();
			}
		}

		return hashCodeValue.intValue();
	}

	/**
	 * Default implementation for entity beans. Prints values of fields of this
	 * entity bean. Useful for logs.
	 */
	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("id=").append(getEntityId()).append("; ")
				.append("version=").append(version)
			.append("}")
			.toString();
	}
}
