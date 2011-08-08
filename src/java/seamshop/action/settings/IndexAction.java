package seamshop.action.settings;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;

/**
 * @author Alex Siman 2009-07-31
 */
@Component("settingsIndexAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})
@SuppressWarnings("serial")
public class IndexAction extends AbstractSettingsAction
{}
