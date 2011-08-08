package seamshop.model.validation;

import static seamshop.model.validation.UrlNameValidationResult.INVALID_CHARS;
import static seamshop.model.validation.UrlNameValidationResult.OK;
import static seamshop.model.validation.UrlNameValidationResult.PRIMITIVE;
import static seamshop.model.validation.UrlNameValidationResult.RESERVED;
import static seamshop.model.validation.UrlNameValidationResult.SHORT;

import java.util.regex.Pattern;

/**
 * Lazy singleton.
 *
 * @author Alex Siman 2009-12-11
 */
public class UrlNameValidator
{
	private UrlNameValidator() {}

	private static class SingletonHolder
	{
		private static final UrlNameValidator INSTANCE = new UrlNameValidator();
	}

	public static UrlNameValidator getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	/** The min inclusive length of URL name. Ex "Red" is min URL name. */
	public static final int MIN_LENGTH = 3;

	/**
	 * Matched values are:
	 * <code>"__", "---", "_-_", "000", "123"</code>.
	 */
	private Pattern primitivePattern = Pattern.compile(
		"^[0-9]+|[-_]+$");

	/**
	 * Matched values are:
	 * <code>"a-b", "a_b", "A_B_C", "007--", "abc__",
	 * "__abc", "_a-d_"</code>.
	 */
	private Pattern validCharsPattern = Pattern.compile(
		"^[a-zA-Z0-9-_]+$");

	/**
	 * Matched values are:
	 * <code>"id", "ID", "Id", "iD", "ids",
	 * "Shop", "shop", "SHOP", "ShOp", "Shops", "shops",
	 * "Store", "store", "STORE", "StOrE", "Store", "Stores",
	 * "Product", "product", "products",
	 * "Item", "item", "items",
	 * "The-Shop", "a_store", "an-ID",
	 * "id123", "an-ID123", "Shops666", "THE-STORE777"</code>.
	 */
	private Pattern reservedWordsPattern = Pattern.compile(
		"^((a|an|the).?)?(id|shop|store|product|item)s?[0-9]*$",
		Pattern.CASE_INSENSITIVE);

	public static boolean hasValidLength(String urlName)
	{
		return (null != urlName) && (urlName.length() >= MIN_LENGTH);
	}

	public static boolean isPrimitive(String urlName)
	{
		return getInstance().primitivePattern.matcher(urlName).matches();
	}

	public static boolean hasValidChars(String urlName)
	{
		return getInstance().validCharsPattern.matcher(urlName).matches();
	}

	public static boolean isReserved(String urlName)
	{
		return getInstance().reservedWordsPattern.matcher(urlName).matches();
	}

	public static boolean isValid(String urlName)
	{
		return OK == isValidExt(urlName);
	}

	public static UrlNameValidationResult isValidExt(String urlName)
	{
		if (!hasValidLength(urlName))
		{
			return SHORT;
		}
		else if (isPrimitive(urlName))
		{
			return PRIMITIVE;
		}
		else if (!hasValidChars(urlName))
		{
			return INVALID_CHARS;
		}
		else if (isReserved(urlName))
		{
			return RESERVED;
		}

		return OK;
	}
}
