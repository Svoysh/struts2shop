package seamshop.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import seamshop.context.RequestContext;
import seamshop.context.SessionContext;
import seamshop.dao.CartDao;
import seamshop.dao.CartItemDao;
import seamshop.model.Cart;
import seamshop.model.User;
import seamshop.util.Log;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAwareSupport;

/**
 * Base class for any service to work with domain model.
 * Some features:
 * <ul>
 * <li>Any service can contain methods that are common for more than one Struts Action class.</li>
 * <li>Any service can add and clear messages and errors of currently executing Struts Action.</li>
 * </ul>
 * <p/>
 * NOTE: Errors and messages related methods were copied from {@link ActionSupport}.
 *
 * @author Alex Siman 2009-07-30
 */
/*
 * TODO: Low: Refactor: Make all *Service as subclass of AbstractCommonAction
 * and move all services to secured "action.common" package.
 */
public abstract class AbstractService
{
	protected final Log log = new Log(this.getClass());

	// Contexts. --------------------------------------------------------------

	@Autowired
	protected SessionContext sessionContext;

	@Autowired
	protected RequestContext requestContext;

	// DAOs. ------------------------------------------------------------------

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	protected CartDao cartDao;

	@Autowired
	protected CartItemDao cartItemDao;

	// Methods ----------------------------------------------------------------

	private ValidationAwareSupport getValidationAware()
	{
		return requestContext.getValidationAware();
	}

	// TODO: Low: Refactor: Move to [UserService.getCurrentUser()]. See AbstractAction.
	public User getCurrentUser()
	{
		return sessionContext.getUser();
	}

	// TODO: Low: Refactor: Move to [CartService.getMyCart()]. See AbstractAction.
	public Cart getCurrentCart()
	{
		return cartDao.getCurrentCart();
	}

	public String escape(String html)
	{
		return StringEscapeUtils.escapeHtml(html);
	}

	// Based on [com.opensymphony.xwork2.ActionSupport] -----------------------
	// TODO: Low: Refactor: Duplication: See [action.AbstractAction] methods related to ValidationAwareSupport.

	public void setActionErrors(Collection<String> errorMessages)
	{
		getValidationAware().setActionErrors(errorMessages);
	}

	public Collection<String> getActionErrors()
	{
		return getValidationAware().getActionErrors();
	}

	public void setActionMessages(Collection<String> messages)
	{
		getValidationAware().setActionMessages(messages);
	}

	public Collection<String> getActionMessages()
	{
		return getValidationAware().getActionMessages();
	}

	public void setFieldErrors(Map<String, List<String>> errorMap)
	{
		getValidationAware().setFieldErrors(errorMap);
	}

	public Map<String, List<String>> getFieldErrors()
	{
		return getValidationAware().getFieldErrors();
	}

	public void addActionError(String anErrorMessage)
	{
		getValidationAware().addActionError(anErrorMessage);
	}

	public void addActionMessage(String aMessage)
	{
		getValidationAware().addActionMessage(aMessage);
	}

	public void addFieldError(String fieldName, String errorMessage)
	{
		getValidationAware().addFieldError(fieldName, errorMessage);
	}

	public boolean hasActionErrors()
	{
		return getValidationAware().hasActionErrors();
	}

	public boolean hasActionMessages()
	{
		return getValidationAware().hasActionMessages();
	}

	public boolean hasErrors()
	{
		return getValidationAware().hasErrors();
	}

	public boolean hasFieldErrors()
	{
		return getValidationAware().hasFieldErrors();
	}

	/**
	 * Clears field errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearFieldErrors()
	{
		getValidationAware().clearFieldErrors();
	}

	/**
	 * Clears action errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearActionErrors()
	{
		getValidationAware().clearActionErrors();
	}

	/**
	 * Clears messages. Useful for Continuations and other situations where you
	 * might want to clear parts of the state on the same action.
	 */
	public void clearMessages()
	{
		getValidationAware().clearMessages();
	}

	/**
	 * Clears all errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearErrors()
	{
		getValidationAware().clearErrors();
	}

	/**
	 * Clears all errors and messages. Useful for Continuations and other
	 * situations where you might want to clear parts of the state on the same
	 * action.
	 */
	public void clearErrorsAndMessages()
	{
		getValidationAware().clearErrorsAndMessages();
	}
}
