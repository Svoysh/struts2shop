package seamshop.util;

import org.apache.commons.lang.StringUtils;

/**
 * Helper class to process texts.
 *
 * @author Alex Siman 2009-11-07
 */
public class TextUtils
{
	private TextUtils()
	{}

	/**
	 * @param text
	 * @param maxLength
	 * @return
	 */
	public static String makeSummary(String text, int maxLength)
	{
		String summary = null;
		if ((maxLength <= 0) || StringUtils.isBlank(text))
		{
			// TODO: Maybe return "" instead of null? (y)
			return summary;
		}

		text = text.trim();
		if (text.length() <= maxLength)
		{
			summary = text;
			return summary;
		}

		// TODO: Enhance: UI: If text has been cropped on word, then remove this word.
		int endIndex = maxLength;
		summary = text.substring(0, endIndex).trim();

		/*// Find last punctuation or whitespace character.
		// If ends with space character then remove it.
		// If ends with punctuation character then leave it.
		for (int i = endIndex - 1; i >= 0; i--)
		{
			String lastChar = String.valueOf(summary.charAt(i));

			// If last character matches to one of whitespace characters then remove it.
			// Regex help:
			// "\p{Z}" or "\p{Separator}" - Any kind of whitespace or invisible separator.
			// "\s" - A whitespace character: [ \t\n\x0B\f\r].
			Pattern spacePattern = Pattern.compile("^[\\p{Z}\\s]$");
			if (spacePattern.matcher(lastChar).matches())
			{
				summary = summary.substring(0, i);
				return summary;
			}

			// If last character matches to one of the Unicode punctuation characters
			// then leave it.
			// Regex help:
			// "\p{P}" or "\p{Punctuation}" - Any kind of punctuation character.
			Pattern punctPattern = Pattern.compile("^[\\p{P}]$");
			if (punctPattern.matcher(lastChar).matches())
			{
				summary = summary.substring(0, i + 1);
				return summary;
			}
		}*/

		return summary;
	}
}
