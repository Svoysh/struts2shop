package seamshop.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.ConfirmEmail;
import seamshop.model.User;
import seamshop.util.StringUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Alex Siman 2009-09-03
 */
@Component
@Scope(Spring.ACTION_SCOPE)
@Results({
	@Result(
		name = Action.INPUT,
		location = "send-confirmation-email.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "home", "namespace", "/member"}
	)
})
@SuppressWarnings("serial")
// TODO: Move action to "settings" or "account"? (mb)
public class SendConfirmationEmailAction extends AbstractGuestAction
{
	protected static final String FIELD_EMAIL = "email";

	private String email;

	@SkipValidation
	@Override
	public String execute()
	{
		// Try to preset email as email of current user.
		if (StringUtils.isNullOrEmpty(email) && isLoggedIn())
		{
			email = getCurrentUser().getEmail();
		}

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String submit()
	{
		log.debug("email: " + email);

		User userByEmail = null;
		// If this is email of current user.
		if (email.equals(getCurrentUser().getEmail()))
		{
			userByEmail = getCurrentUser();
		}
		// Trying to find user by entered email.
		else
		{
			userByEmail = userDao.getUserByEmail(email);
		}

		if (userByEmail != null)
		{
			ConfirmEmail confirmEmail = confirmEmailDao
				.getByEmailAndUserId(email, userByEmail.getId());

			if (confirmEmail != null)
			{
				if (userByEmail.isEnabled())
				{
					// TODO: i18n: Externalize.
					addActionMessage("Email <strong>" + escape(email) +
						"</strong> is already confirmed.");
					return SUCCESS;
				}
				else if (confirmEmail.isExpired())
				{
					// Delete expired confirmation...
					entityManager.remove(confirmEmail);
					entityManager.flush();
				}
				else
				{
					// TODO: Need smth here? (n)
				}
			}

			// Create new confirmation, if it was not found or has expired and deleted.
			if (confirmEmail == null)
			{
				// TODO: Low: Refactor nex block? (n) See RegisterAction.
				confirmEmail = new ConfirmEmail();
				confirmEmail.setUser(userByEmail);
				entityManager.persist(confirmEmail);
				entityManager.flush();
			}

			sendConfirmationEmail(confirmEmail);
			return SUCCESS;
		}
		// Error: confirmation email cannot be sent, if user with such email was not found.
		else
		{
			// TODO: i18n: Externalize.
			// TODO: Low: Suppose to register account with this email? (y)
			addActionError("Confirmation email cannot be sent.");
			addFieldError(FIELD_EMAIL, "There is no account with such email." +
				" Check that you have entered your email address correctly.");
		}

		return INPUT;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
}
