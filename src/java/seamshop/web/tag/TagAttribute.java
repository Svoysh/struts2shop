package seamshop.web.tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Represents XHTML tag attribute w/ name and value.
 *
 * @author Alex Siman 2009-10-20
 */
public class TagAttribute
{
	/**
	 * Whether to escape HTML value of this attribute by default.
	 */
	public static final boolean DEFAULT_ESCAPE_VALUE = true;

	private String name;
	private String value;

	public TagAttribute()
	{
		super();
	}

	/**
	 * Will escape <code>value</code>.
	 */
	public TagAttribute(String name, String value)
	{
		this(name, value, DEFAULT_ESCAPE_VALUE);
	}

	/**
	 * @param name Name of the attribute.
	 * @param value Value of the tag attribute.
	 * @param escape <code>true</code> to escape <code>value</code>.
	 */
	public TagAttribute(String name, String value, boolean escape)
	{
		this();
		this.name = name;
		setValue(value, escape);
	}

	public static boolean isValid(TagAttribute attribute)
	{
		return (attribute != null) && attribute.isValid();
	}

	public boolean isValid()
	{
		return !StringUtils.isBlank(getName());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value != null
			? value
			: "";
	}

	public void setValue(String value)
	{
		setValue(value, DEFAULT_ESCAPE_VALUE);
	}

	public void setValue(String value, boolean escape)
	{
		this.value = escape
			? StringEscapeUtils.escapeHtml(value)
			: value;
	}
}
