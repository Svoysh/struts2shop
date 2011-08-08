package seamshop.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Cookie;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.util.StringUtils;

import com.opensymphony.xwork2.Action;

@Component
@Scope(Spring.ACTION_SCOPE)
@Results({
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "home", "namespace", "/"}
	)
})
@SuppressWarnings("serial")
public class LogoutAction extends AbstractGuestAction
{
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		String sessionId = getUserSessionId();

		// TODO: Refactor: Almost duplicated block: See [SilentLoginInterceptor.do...].
		if (!StringUtils.isNullOrEmpty(sessionId))
		{
			log.debug("Delete session from DB and flush changes...");
			// Delete current session from DB.
			sessionDao.delete(sessionId);
			entityManager.flush();

			log.debug("Remove session cookie...");
			removeCookie(Cookie.REMEMBER_ME);
		}

		sessionContext.removeUser();

		return SUCCESS;
	}
}
