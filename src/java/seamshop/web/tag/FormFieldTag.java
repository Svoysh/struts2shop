package seamshop.web.tag;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import seamshop.action.AbstractAction;
import seamshop.util.CollectionUtils;

/**
 * Field decorator for form inputs.
 *
 * @author Alex Siman 2009-10-20
 */
public class FormFieldTag extends AbstractSimpleTag
{
	// Tag Attributes
	private String id;
	private String fieldName;
	private String inputId;
	private String label;
	private String labelKey;
	private boolean required;

	@Override
	public void print() throws Exception
	{
		AbstractAction action = getAction();
		if (action == null)
		{
			throw new NullPointerException(
				"There is no currently executing action.");
		}

		List<String> fieldErrors = Collections.emptyList();
		if (!isBlank(fieldName))
		{
			Map<String, List<String>> allFieldErrors = action.getFieldErrors();
			if (!CollectionUtils.isNullOrEmpty(allFieldErrors))
			{
				fieldErrors = allFieldErrors.get(fieldName);
			}
		}
		else
		{
			fieldName = "";
		}

		// If 'inputId' has no value then set 'inputId' to value of 'fieldName'.
		if (isBlank(inputId))
		{
			inputId = fieldNameToInputId();
		}

		// If 'labelKey' was specified then override 'label' by value by 'labelKey'.
		if (!isBlank(labelKey))
		{
			label = action.getText(labelKey);
		}
		boolean hasLabel = !isBlank(label);

		boolean hasFieldErrors = false;
		String fieldCssClass = "wwgrp";
		if (!CollectionUtils.isNullOrEmpty(fieldErrors))
		{
			hasFieldErrors = true;
			fieldCssClass += " HasFieldErrors";
		}

		Collection<TagAttribute> mainDivAttrs = new ArrayList<TagAttribute>();
		mainDivAttrs.add(classAttribute(fieldCssClass));
		if (!isBlank(id))
		{
			mainDivAttrs.add(idAttribute(id));
		}

		HtmlBuilder htmlBuilder = new HtmlBuilder(getJspWriter())
		.startTag("div", mainDivAttrs)
			.startTag("table", classAttribute("HiddenTable"))
				.startTag("tr")
					.startTag("td", classAttribute("wwlbl"));
					if (hasLabel)
					{
						htmlBuilder
						.startTag("label", classAttribute("label"),
							attribute("for", inputId));
						if (required)
						{
							htmlBuilder
							.startTag("span", classAttribute("required"))
							.append("* ")
							.endTag("span");
						}
						htmlBuilder
						.append(label, false)
						.append(":", false)
						.endTag("label");
					}
					htmlBuilder
					.endTag("td")
					.startTag("td", classAttribute("wwctrl"))
					.flush();

						printJspBodyIfExists();

					if (hasFieldErrors)
					{
						htmlBuilder
						.startTag("ul", classAttribute("wwerr"));
						for (String error : fieldErrors)
						{
							htmlBuilder
							.startTag("li", classAttribute("errorMessage"))
							.append(error, false)
							.endTag("li");
						}
						htmlBuilder
						.endTag("ul");
					}
					htmlBuilder
					.endTag("td")
				.endTag("tr")
			.endTag("table")
		.endTag("div")
		.flush();
	}

	public String fieldNameToInputId()
	{
		String result = new String(fieldName);
		result = result.replace("[", "_");
		result = result.replace("].", "_");
		result = result.replace("]", "_");
		return result;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getInputId()
	{
		return inputId;
	}

	public void setInputId(String inputId)
	{
		this.inputId = inputId;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public void setLabelKey(String labelKey)
	{
		this.labelKey = labelKey;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}
}
