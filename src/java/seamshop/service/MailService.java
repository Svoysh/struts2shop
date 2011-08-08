package seamshop.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import seamshop.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * @author Alex Siman 2009-08-18
 */
@Component
public class MailService extends AbstractService
{
	private static final String FREEMARKER_EXTENSION = ".ftl";

	@Autowired
	private MailSender mailSender;

	@Autowired
	private SimpleMailMessage templateMessage;

	@Autowired
	private Configuration freemarkerConfiguration;

	// TODO: Use some DTO like: MailInfo.
	public void sendEmail(String toEmail, String subject,
		String freemarkerTemplateName, Map<String, Object> freemarkerVariables)
	{
		if (!freemarkerTemplateName.endsWith(FREEMARKER_EXTENSION))
		{
			freemarkerTemplateName += FREEMARKER_EXTENSION;
		}

		String freeMarkerString = getFreeMarkerString(
			freemarkerTemplateName, freemarkerVariables);

		if (!StringUtils.isNullOrEmpty(freeMarkerString))
		{
			SimpleMailMessage message = new SimpleMailMessage(templateMessage);
			message.setTo(toEmail);
			message.setSubject(subject);
			message.setText(freeMarkerString);
			try
			{
				mailSender.send(message);
			}
			catch(MailException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

	private String getFreeMarkerString(String teplateFilename,
		Map<String, Object> variables)
	{
		String result = "";
		try
		{
			result = FreeMarkerTemplateUtils.processTemplateIntoString(
				freemarkerConfiguration.getTemplate(teplateFilename), variables);
		}
		catch (IOException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		catch (TemplateException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		return result;
	}
}
