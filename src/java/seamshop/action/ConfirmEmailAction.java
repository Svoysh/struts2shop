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

import com.opensymphony.xwork2.Action;

@Component
@Scope(Spring.ACTION_SCOPE)
@Results({
	@Result(
		name = Action.INPUT,
		location = "confirm-email.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "home", "namespace", "/member"}
	)
})
@SuppressWarnings("serial")
// TODO: Move action to "settings" or "account"? (mb)
public class ConfirmEmailAction extends AbstractGuestAction
{
	protected static final String FIELD_CODE = "code";

	private String code;

	@SkipValidation
	@Override
	public String execute()
	{
		// Nothing.
		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String submit()
	{
		log.debug("code: " + code);

		ConfirmEmail confirmEmail = confirmEmailDao.get(code);
		if (confirmEmail != null)
		{
			entityManager.remove(confirmEmail);
			User user = confirmEmail.getUser();
			if (confirmEmail.getEmail().equals(user.getEmail())
				&& !confirmEmail.isExpired() && !user.isEnabled())
			{
				user.setEnabled(true);
				// TODO: i18n: Externalize.
				addActionMessage("Email <strong>" + escape(user.getEmail()) +
					"</strong> has been confirmed.");

				return SUCCESS;
			}
			else
			{
				// TODO: i18n: Externalize.
				addFieldError(FIELD_CODE, "Confirmation code has been expired.");

				// TODO: Low: Create new confirmation? (n)

				/*
				 * TODO: Redirect to page "Send new confirmation email".
				 * Or send it automatically and notify about it in action message.
				 */
			}
		}
		else
		{
			// TODO: i18n: Externalize.
			addFieldError(FIELD_CODE, "Invalid confirmation code.");
		}

		return INPUT;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
}
