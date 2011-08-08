package seamshop.action.member;

import java.io.Serializable;

import seamshop.model.AbstractEntity;

/**
 * @param <E> class of entity (e.g. User, Shop, Product, etc.)
 *
 * @author Alex Siman 2009-05
 * @author Alex Siman 2009-11-06
 */
// TODO: Do something w/ this abstract action.
@SuppressWarnings("serial")
public abstract class AbstractCrudMemberAction
	<E extends AbstractEntity<ID>, ID extends Serializable>
	extends AbstractMemberAction
{
	public static final String RESULT_LIST = "list";
	public static final String RESULT_DELETE = "delete";
	public static final String RESULT_REDIRECT_LIST = "redirect-list";
	public static final String RESULT_REDIRECT_VIEW = "redirect-view";
	public static final String RESULT_REDIRECT_ADD = "redirect-add";

	protected ID id;

	public boolean isNewEntity()
	{
		String method = getMethod();
		return (method != null) && ("add".equals(method) || "save".equals(method)
			|| "saveAdd".equals(method));
	}

	protected void addOrEdit()
	{
		// Nothing yet.
	}

	protected void saveOrUpdate()
	{
		if (isNewEntity())
		{
			// TODO: Surround with try/catch;
			//       print "Unexpected error. Failed to save <Entity> '<Name>'.";
			//       return "error" result.
			entityManager.persist(getEntity());
		}
		entityManager.flush();
		ID newId = getEntity().getEntityId();
		setId(newId);
	}

	public ID getId()
	{
		return id;
	}

	public void setId(ID id)
	{
		this.id = id;
	}

	protected abstract E getEntity();
	protected abstract void setEntity(E entity);
}
