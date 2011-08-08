package seamshop.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;

import seamshop.consts.Interceptor;

@InterceptorRefs({@InterceptorRef(Interceptor.GUEST_STACK)})
@SuppressWarnings("serial")
public abstract class AbstractGuestAction extends AbstractAction
{}
