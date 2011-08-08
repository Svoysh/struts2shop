package seamshop.interceptor;

import org.springframework.stereotype.Component;

import seamshop.consts.Cookie;
import seamshop.model.Session;
import seamshop.model.User;
import seamshop.util.Command;
import seamshop.util.CookieUtils;

import com.opensymphony.xwork2.ActionInvocation;

/**
 * Tries to auto-login user silently if there was found "Remember me" cookie in
 * current request and its value is session ID of valid and not expired session
 * stored in DB. If session was found but is already expired then it will be
 * deleted immediately.
 *
 * @author Alex Siman 2009-08-26
 */
// TODO: Low: Rename to: "AutoLoginInterceptor"? (mb)
@Component
@SuppressWarnings("serial")
public class SilentLoginInterceptor extends AbstractInterceptor
{
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception
	{
		log.debug("Begin of interception.");

		// TODO: Store in SessionContext only user ID instead of entire user? (think)
		// TODO: Store fresh user entity in RequestContext. (re-think, why it was needed?)

		// TODO: High: Test: Reload user with every request that all changes to
		//       user are Up-to-date.
		if (isUserLoggedIn())
		{
			transactionContext.doInReadTransaction(new Command()
			{
				@Override
				public void execute() throws Exception
				{
					userDao.reloadCurrentUser();
				}
			});
		}
		// If there is no current (logged in) user.
		else
		{
			Object actionObject = actionInvocation.getAction();
			if (actionObject instanceof SilentLoginAware)
			{
				SilentLoginAware action = (SilentLoginAware) actionObject;
				final String sessionId = action.getUserSessionId();
				log.debug("Session ID cookie: " + sessionId);
				if (sessionId != null)
				{
					transactionContext.doInWriteTransaction(new Command()
					{
						@Override
						public void execute() throws Exception
						{
							doSilentLoginOrDeleteExpiredSession(sessionId);
						}
					});
				}
			}
		}

		log.debug("End of interception.");
		return actionInvocation.invoke();
	}

	private void doSilentLoginOrDeleteExpiredSession(final String sessionId)
	{
		boolean sessionIdIsInvalid = false;

		// Find session by session ID cookie form DB.
		Session session = sessionDao.get(sessionId);
		log.debug("Found session: " + session);
		if (session != null)
		{
			// Do silent login by setting current user in session context.
			if (!session.isExpired())
			{
				User user = session.getUser();
				sessionContext.setUser(user);
				log.debug("Session is OK. Current user: " + user);
			}
			// Delete expired session from DB.
			// TODO: Refactor: Almost duplicated block: See [LogoutAction.execute()].
			else
			{
				log.debug("Session is expired and will deleted from DB.");
				sessionDao.delete(sessionId);
				entityManager.flush();

				sessionIdIsInvalid = true;
			}
		}
		else
		{
			sessionIdIsInvalid = true;
		}

		if (sessionIdIsInvalid)
		{
			log.debug("Remove invalid session ID cookie...");
			CookieUtils.removeCookie(Cookie.REMEMBER_ME);
		}
	}
}
