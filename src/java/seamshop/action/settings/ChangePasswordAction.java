package seamshop.action.settings;

import static seamshop.action.settings.AbstractSettingsAction.RESULT_REDIRECT_INDEX;

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
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.User;
import seamshop.util.StringUtils;

import com.opensymphony.xwork2.Action;

/**
 * @author Alex Siman 2009-07-30
 */
@Component("settingsChangePasswordAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})
@Results({
	@Result(
		name = Action.INPUT,
		location = "change-password.jsp"
	),
	@Result(
		name = RESULT_REDIRECT_INDEX,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "index"}
	)
})
@SuppressWarnings("serial")
public class ChangePasswordAction extends AbstractSettingsAction
{
	private static final String FIELD_CURRENT_PASSWORD = "currentPassword";
//	private static final String FIELD_NEW_PASSWORD = "newPassword";
//	private static final String FIELD_NEW_PASSWORD_CONTROL = "newPasswordControl";

	private String currentPassword;
	private String newPassword;
	private String newPasswordControl;

	private User currentUser = null;

	@Override
	public void validate()
	{
		log.debug("call validate()");

		super.validate();

		if (!StringUtils.isNullOrEmpty(currentPassword))
		{
			currentUser = userDao.reloadCurrentUser();
			if (!currentUser.isValidPassword(currentPassword))
			{
				addFieldError(FIELD_CURRENT_PASSWORD,
					"Does not match to your current password.");
			}
		}
	}

	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("call execute()");

		// Nothing.

		return SUCCESS;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String submit()
	{
		log.debug("call submit()");

		currentUser.setPassword(newPassword);
		entityManager.flush();
		addActionMessage("Password changed successfully.");
		// TODO: Notify by email.

		return RESULT_REDIRECT_INDEX;
	}

	public String getCurrentPassword()
	{
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword)
	{
		this.currentPassword = currentPassword;
	}

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	public String getNewPasswordControl()
	{
		return newPasswordControl;
	}

	public void setNewPasswordControl(String newPasswordControl)
	{
		this.newPasswordControl = newPasswordControl;
	}
}
