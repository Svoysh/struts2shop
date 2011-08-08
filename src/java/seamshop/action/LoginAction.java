package seamshop.action;

import javax.servlet.http.Cookie;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.consts.Time;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Session;
import seamshop.model.User;

import com.opensymphony.xwork2.Action;

// TODO: Low: Impl "Return to prev page". Maybe w/ URL param "from" or "returnto".
@Component
@Scope(Spring.ACTION_SCOPE)
@Results({
	@Result(
		// TODO: This result must be in AbstractAction class - generic for all Actions.
		name = Action.ERROR,
		location = "error.jsp"
	),
	@Result(
		name = Action.INPUT,
		location = "login.jsp"
	),
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "home", "namespace", "/member"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class LoginAction extends AbstractGuestAction
{
	protected static final String FIELD_EMAIL = "email";
	protected static final String FIELD_PASSWORD = "password";
	protected static final String FIELD_REMEMBER_ME = "rememberMe";

	protected String email;
	protected String password;
	protected boolean rememberMe = false;

	// TODO: Fix: Accounting: If (loggedIn and rememberMe == false) then redirect
	//       to "alreadyLoggedIn" (etc.) page w/o validation.

//	@Transactional(TransactionType.NONE)
	@SkipValidation
	@Override
	public String execute()
	{
		logForTest();

		// Nothing.
		return INPUT;
	}

	// TODO: Test: Delete.
	private void logForTest()
	{
		logActionName();
		logRequestPrameters();
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String submit()
	{
		logForTest();

		log.debug("rememberMe: " + rememberMe);

		User user = userDao.getUserByEmail(email);
		if (user != null)
		{
			boolean validPassword = user.isValidPassword(password);
			if (validPassword)
			{
				doLogin(user, rememberMe);
				return SUCCESS;
			}
			// Password is invalid...
			else
			{
				log.error("Invalid password='" + password + "' for user w/ email='" + email + "'.");
				// TODO: i18n: Externalize.
				addFieldError(FIELD_PASSWORD, "Invalid password.");

				// TODO: Set number of attempts to login with invalid password.
				// TODO: After success login reset attempts.
			}
		}
		// There is no user w/ such email.
		else
		{
			log.error("User with such email='" + email + "' does not exist.");
			// TODO: i18n: Externalize message.
			addFieldError(FIELD_EMAIL, "User with such email does not exist.");

			// TODO: If user count == 0 then check if there is preset default user (admin).
			// TODO: After default user created store flag into session context.
			// TODO: Need? (maybe not, redundant)
//			checkForDefaultUserPreset();
		}

		return INPUT;
	}

	protected void doLogin(User user, boolean rememberLogin)
	{
		log.debug("call doLogin()");
		if (rememberLogin)
		{
			// Create and store new user's session.
			log.debug("Create and store new user's session...");
			Session newSession = new Session();
			newSession.setUser(user);
			entityManager.persist(newSession);
			entityManager.flush();

			// Create session ID cookie.
			log.debug("Create and add session cookie...");
			/*
			 * TODO: Refactor: Create either AbstractAction.createSessionCookie()
			 * or Session.createCookie().
			 */
			Cookie rememberLoginCookie = new Cookie(
				seamshop.consts.Cookie.REMEMBER_ME, newSession.getId());
			rememberLoginCookie.setMaxAge(Time.REMEMBER_ME_MAX_COOKIE_AGE_IN_SECS);

			// Add session ID cookie to response.
			addCookie(rememberLoginCookie);
		}

		log.debug("Set current user...");
		sessionContext.setUser(user);
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isRememberMe()
	{
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe)
	{
		this.rememberMe = rememberMe;
	}
}
