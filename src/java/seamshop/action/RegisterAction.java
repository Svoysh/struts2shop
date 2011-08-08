package seamshop.action;

import static seamshop.model.enums.UserRole.MEMBER;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.ActionResult;
import seamshop.consts.Interceptor;
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
		location = "register.jsp"
	),
	@Result(
		name = ActionResult.REDIRECT_SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "register!success"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class RegisterAction extends LoginAction
{
	private String firstName;
	private String lastName;
	private String passwordControl;

	@Override
	public void validate()
	{
		super.validate();

		// TODO: Require either "firstName" or "lastName".
		//       If both are entered - it's OK too.
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@Override
	public String submit()
	{
		// TODO: If loggedIn -> redirect to "alreadyRegistered" page w/o validation.

		boolean foundUser = userDao.isPresentByEmail(email);

		// Input error, if user with the same email already exists.
		if (foundUser)
		{
			log.info("User with such email = '" + email + "' already registered.");
			// TODO: i18n: Externalize.
			addFieldError(FIELD_EMAIL, "User with such email already registered. " +
					"Please, choose another email.");

			return INPUT;
		}
		// else add new user and auto-login it.
		else
		{
			// Create new user and save it.
			User user = new User(email, password);
			user.setRole(MEMBER);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			entityManager.persist(user);
			entityManager.flush();

			// TODO: i18n: Externalize.
			addActionMessage("Your account has been successfully created." +
				" To finish registration process, you need to confirm your email address.");

			// Auto-login new user w/o remembering.
			doLogin(user, false);

			// Create confirmation for email of new user.
			ConfirmEmail confirmEmail = new ConfirmEmail();
			confirmEmail.setUser(user);
			entityManager.persist(confirmEmail);
			entityManager.flush();

			// Send confirmation email.
			sendConfirmationEmail(confirmEmail);

			return ActionResult.REDIRECT_SUCCESS;
		}
	}

	@AllowedMethod
//	@Transactional(TransactionType.NONE)
	@SkipValidation
	public String success()
	{
		// Nothing.
		return SUCCESS;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getPasswordControl()
	{
		return passwordControl;
	}

	public void setPasswordControl(String passwordControl)
	{
		this.passwordControl = passwordControl;
	}
}
