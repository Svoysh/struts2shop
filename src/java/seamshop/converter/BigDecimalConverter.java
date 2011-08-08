package seamshop.converter;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Alex Siman 2009-05-30
 */
public class BigDecimalConverter extends AbstractTypeConverter
{
	@SuppressWarnings("unchecked")
	@Override
	public Object convertFromString(Map actionContext, String[] valuesToConvert,
		Class toClass)
	{
		BigDecimal converted = null;
		String value = valuesToConvert[0];

		log.debug("Converting from String to BigDecimal: [" + value.toString() + "]");

		if (!StringUtils.isBlank(value))
		{
			try
			{
				converted = new BigDecimal(value);
			}
			catch (Throwable ex)
			{
				// TODO: Low: Allow Exception to be thrown? (n)
				log.error("Failed to convert String to BigDecimal", ex);
			}
		}

		return converted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertToString(Map actionContext, Object objectToConvert)
	{
		log.debug("Converting from BigDecimal to String: [" + objectToConvert.toString() + "]");

		// Using default Struts type converter (locale sensible).
		return (String) performFallbackConversion(actionContext, objectToConvert, String.class);
	}
}
