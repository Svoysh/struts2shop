package seamshop.action.settings;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;

import seamshop.action.AbstractAction;
import seamshop.consts.Interceptor;
import seamshop.consts.Package;

/**
 * @author Alex Siman 2009-07-30
 */
@ParentPackage(Package.MEMBER_DEFAULT)
@InterceptorRefs({@InterceptorRef(Interceptor.MEMBER_STACK)})
@SuppressWarnings("serial")
public abstract class AbstractSettingsAction extends AbstractAction
{
	public static final String RESULT_REDIRECT_INDEX = "redirect-index";
}
