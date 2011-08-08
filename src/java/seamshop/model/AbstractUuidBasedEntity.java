package seamshop.model;

import static seamshop.util.UuidGenerator.UUID_LENGTH;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import seamshop.util.UuidGenerator;

/**
 * @author Alex Siman 2009-08-27
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractUuidBasedEntity extends AbstractEntity<String>
{
	/**
	 * ID is universally unique and unmodifiable.
	 */
	@Id
	@Column(unique = true, nullable = false, updatable = false, length = UUID_LENGTH)
	private String id = UuidGenerator.generateRandomUuid();

	public String getId()
	{
		return id;
	}

	/**
	 * Used only by Hibernate. Must be private because it is generated
	 * unmodifiable unique value.
	 *
	 * @see UuidGenerator.
	 */
	@SuppressWarnings("unused")
	private void setId(String uuid)
	{
		id = uuid;
	}

	@Transient
	@Override
	public String getEntityId()
	{
		return getId();
	}
}
