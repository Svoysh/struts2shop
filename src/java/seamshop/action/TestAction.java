package seamshop.action;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Spring;

/**
 * @author Alex Siman 2009-07-30
 */
// TODO: DEVELOPMENT: Delete this action in production.
@Component
@Scope(Spring.ACTION_SCOPE)
@SuppressWarnings("serial")
public class TestAction extends AbstractGuestAction
{
	// For mail test.
	private String to = "as@localhost";
	private String subject = "Test email (Spring Mail + FreeMarker)";
	private String name = "John Smith";

	// For dynamic method invocation test.
	private String validCurrentPassword = "admin";
	private String currentPassword = null; // Must be equal to value of validCurrentPassword.

	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("Changing admin password...");
		if (validCurrentPassword.equals(currentPassword))
		{
			String feedback = changeAdminPassword();
			addActionMessage(feedback);
			return SUCCESS;
		}
		else
		{
			log.error("Current password is invalid.");
			addFieldError("currentPassword", "Invalid password.");
			return INPUT;
		}
	}

	public String publicChangeAdminPassword()
	{
		log.debug("Called public changeAdminPassword.");
		return changeAdminPassword();
	}

	protected String protectedChangeAdminPassword()
	{
		log.debug("Called protected changeAdminPassword.");
		return changeAdminPassword();
	}

	private String privateChangeAdminPassword()
	{
		log.debug("Called private changeAdminPassword.");
		return changeAdminPassword();
	}

	private String changeAdminPassword()
	{
		String newPassword = "new-admin";
		log.debug("Admin password has been changed from '" + currentPassword +
			"' to '" + newPassword + "'.");
		return "Admin password has been changed to '" + newPassword + "'.";
	}

	public void privateMethodCaller()
	{
		privateChangeAdminPassword();
	}

	/*@Transactional(TransactionType.NONE)
	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		// Nothing.
		return SUCCESS;
	}

	@AllowedMethod
	@Transactional(TransactionType.NONE)
	public String sendEmail()
	{
		log.debug("call sendEmail()");
		log.debug("Sending test email from localhost...");

		Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
		freemarkerVariables.put("name", name);
		freemarkerVariables.put("action", this);
//		freemarkerVariables.put("", this); // Does NOT work.

		mailService.sendEmail(to, subject, TemplateName.TEST, freemarkerVariables);
		addActionMessage("Test email has been successfully sent on " + new Date());

		log.debug("Test email has been sent.");
		return SUCCESS;
	}*/

	public String getTo()
	{
		return to;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCurrentPassword()
	{
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword)
	{
		this.currentPassword = currentPassword;
	}
}
