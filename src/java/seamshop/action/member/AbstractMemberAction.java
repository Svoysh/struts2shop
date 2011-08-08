package seamshop.action.member;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;

import seamshop.action.AbstractAction;
import seamshop.consts.Interceptor;
import seamshop.consts.Package;

@ParentPackage(Package.MEMBER_DEFAULT)
@InterceptorRefs({@InterceptorRef(Interceptor.MEMBER_STACK)})
@SuppressWarnings("serial")
public abstract class AbstractMemberAction extends AbstractAction
{}
