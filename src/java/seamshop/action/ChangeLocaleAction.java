package seamshop.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;

import seamshop.consts.ActionResult;
import seamshop.consts.Interceptor;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;

/**
 * @author Alex Siman 2009-12-07
 */
@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@Results({
	@Result(
		name = Action.SUCCESS,
		type = ResultType.REDIRECT_ACTION,
		params = {"namespace", "/", "actionName", ActionResult.RESULT_HOME}
	)
})
@SuppressWarnings("serial")
public class ChangeLocaleAction extends AbstractGuestAction
{
	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("call execute()");

		// TODO: Low: Maybe redirect to prev page.

		return SUCCESS;
	}
}
