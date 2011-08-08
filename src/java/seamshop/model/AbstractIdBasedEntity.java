package seamshop.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

// TODO: Rename class: "Abstract(Generated|Native)IdBasedEntity".
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractIdBasedEntity extends AbstractEntity<Long>
{
	@Id
	@GeneratedValue
	private Long id;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Transient
	@Override
	public Long getEntityId()
	{
		return getId();
	}
}
