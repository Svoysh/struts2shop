package seamshop.model;

import static org.apache.commons.lang.StringUtils.isBlank;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;

import seamshop.util.HtmlUtils;
import seamshop.util.SafeHtmlAndText;
import seamshop.util.TextUtils;

/**
 * @see Shop
 * @see Product
 * @see PaymentMethod
 * @see ShippingMethod
 *
 * @author Alex Siman 2009-10-09
 * @author Alex Siman 2009-12-30
 */
@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractHtmlDescribedEntity extends AbstractNamedEntity
{
	public static final int SUMMARY_LENGTH = 255;

	public static final String[] INDEXED_FIELDS = new String[] {
		"name", "summary", "textDescription"};

	/**
	 * Short description of entity.
	 */
	@Column(length = SUMMARY_LENGTH)
	@Field(index = Index.TOKENIZED)
	private String summary;

	/**
	 * Full description of entity in HTML format.
	 * <p/>
	 * NOTE: Do not use this field for indexing entity description,
	 * index <code>textDescription</code> field instead.
	 */
	@Lob
	private String htmlDescription;

	/**
	 * Full description of entity in plain text format. Used for search indexing.
	 * Contains data from <code>htmlDescription</code> cleaned from HTML markup.
	 * Can be used to generate <code>summary</code>.
	 */
	@Lob
	@Field(index = Index.TOKENIZED)
	private String textDescription;

	@Override
	public String[] getIndexedFields()
	{
		return INDEXED_FIELDS;
	}

	public void setDescription(String htmlDescription)
	{
		setDescription(htmlDescription, true);
	}

	public void setDescription(String description, boolean containsHtmlMarkup)
	{
		if (containsHtmlMarkup)
		{
			SafeHtmlAndText safeHtmlAndText = HtmlUtils.getSafeHtmlAndText(description);
			htmlDescription = safeHtmlAndText.getHtml();
			textDescription = safeHtmlAndText.getText();
		}
		else
		{
			htmlDescription = description;
			textDescription = description;
		}
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	/**
	 * Summary must be generated after description was set.
	 */
	public void generateSummaryFromDescription()
	{
		summary = TextUtils.makeSummary(textDescription, SUMMARY_LENGTH);
	}

	public void generateSummaryFromDescriptionIfSummaryIsEmpty(boolean generateSummary)
	{
		if (generateSummary || !hasSummary())
		{
			generateSummaryFromDescription();
		}
	}

	public boolean hasSummary()
	{
		return !isBlank(getSummary());
	}

	public String getHtmlDescription()
	{
		return htmlDescription;
	}

	/**
	 * Shortcut for {@ #getHtmlDescription()}
	 */
	public String getHtmlDesc()
	{
		return getHtmlDescription();
	}

	public String getTextDescription()
	{
		return textDescription;
	}

	/**
	 * Shortcut for {@ #getTextDescription()}
	 */
	public String getTextDesc()
	{
		return getTextDescription();
	}
}
