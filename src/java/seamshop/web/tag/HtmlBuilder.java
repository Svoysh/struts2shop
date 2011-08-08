package seamshop.web.tag;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import seamshop.util.CollectionUtils;
import seamshop.util.Log;

/**
 * Simple builder of XHTML string. Powered by {@link StringBuilder}.
 *
 * @author Alex Siman 2009-10-20
 */
// TODO: Low: Rename to "XhtmlBuilder"? (mb) (Currently "HtmlBuilder")
public class HtmlBuilder
{
	/**
	 * Whether to escape appending text.
	 */
	public static final boolean DEFAULT_ESCAPE_VALUE = true;

	protected final Log log = new Log(this.getClass());

	private final StringBuilder builder = new StringBuilder();
	private Writer writer;

	public HtmlBuilder()
	{}

	public HtmlBuilder(Writer writer)
	{
		this();
		this.writer = writer;
	}

	public HtmlBuilder startTag(String name, TagAttribute... attributes)
	{
		return startTag(name, Arrays.asList(attributes));
	}

	public HtmlBuilder startTag(String name, Collection<TagAttribute> attributes)
	{
		if (isValidTagName(name))
		{
			builder.append("<").append(name);
			if (!CollectionUtils.isNullOrEmpty(attributes))
			{
				for (TagAttribute attr : attributes)
				{
					if (TagAttribute.isValid(attr))
					{
						builder
							.append(" ")
							.append(attr.getName())
							.append("=\"")
							.append(attr.getValue())
							.append("\"");
					}
				}
			}
			builder.append(">");
		}

		return this;
	}

	public HtmlBuilder endTag(String name)
	{
		if (isValidTagName(name))
		{
			builder
				.append("</")
				.append(name)
				.append(">");
		}

		return this;
	}

	private boolean isValidTagName(String name)
	{
		return !StringUtils.isBlank(name);
	}

	public HtmlBuilder startAndEndTag(String name, TagAttribute... attributes)
	{
		return startAndEndTag(name, Arrays.asList(attributes));
	}

	public HtmlBuilder startAndEndTag(String name, Collection<TagAttribute> attributes)
	{
		startTag(name, attributes);
		endTag(name);

		return this;
	}

	public HtmlBuilder append(String text)
	{
		return append(text, DEFAULT_ESCAPE_VALUE);
	}

	/**
	 * @param text Any text to be appended.
	 * @param escape <code>true</code> to escape <code>text</code>.
	 */
	public HtmlBuilder append(String text, boolean escape)
	{
		builder.append(escape
			? StringEscapeUtils.escapeHtml(text)
			: text);

		return this;
	}

	public HtmlBuilder flush()
	{
		return flush(writer);
	}

	/**
	 * Print all text from buffer to <code>writer</code> and then clear text buffer.
	 */
	public HtmlBuilder flush(Writer writer)
	{
		if (writer == null)
		{
			throw new NullPointerException("'writer' is null.");
		}

		try
		{
			writer.write(builder.toString());
		}
		catch (Exception ex)
		{
			log.error("Failed to print text to writer: ", ex);
		}
		clean();

		return this;
	}

	/**
	 * Clean text buffer.
	 */
	public HtmlBuilder clean()
	{
		builder.setLength(0);
		return this;
	}

	public Writer getWriter()
	{
		return writer;
	}

	public HtmlBuilder setWriter(Writer writer)
	{
		this.writer = writer;
		return this;
	}

	@Override
	public String toString()
	{
		return builder.toString();
	}
}
