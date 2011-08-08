package seamshop.action;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.User;

@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class UserAction extends AbstractGuestAction
{
	public static final String RESULT_LIST = "list";
	public static final String RESULT_VIEW = "view";

	private Long id;

	private User user = null;
	private List<User> users;

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");
		log.debug("id: " + id);

		if (id != null)
		{
			return view();
		}
		else
		{
			return list();
		}
	}

	/**
	 * Print list of shops.
	 */
	// TODO: Low: Filter users by country (use billing/shipping address)? (maybe)
	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		// TODO: Init count of shops per user? (y)
		Long userCount = userDao.count();
		if (userCount > 0)
		{
			users = userDao.getPage();
			getPager().setAllResults(userCount.intValue());
			getPager().setResults(users.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	@AllowedMethod
	@SkipValidation
	public String view()
	{
		log.debug("call view()");

		user = userDao.get(id);

		return SUCCESS;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public List<User> getUsers()
	{
		return users;
	}
}
