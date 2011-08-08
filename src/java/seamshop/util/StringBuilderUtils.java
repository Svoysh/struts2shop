package seamshop.util;

/**
 * @author Alex Siman 2009-11-16
 */
public class StringBuilderUtils
{
	private StringBuilderUtils()
	{}

	public static void removeCharsFromEnd(StringBuilder builder, int charCountToRemove)
	{
		int endIndex = builder.length();
		int beginIndex = endIndex - charCountToRemove;
		beginIndex = beginIndex < 0 ? 0 : beginIndex;
		builder.delete(beginIndex, endIndex);
	}
}
