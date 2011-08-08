package seamshop.util;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Date;
import java.util.UUID;

/**
 * Universally unique identifier generation utils.
 *
 * @author Alex Siman 2008-10-25
 */
public abstract class UuidGenerator
{
	public static final int UUID_LENGTH = 32;

	/**
	 * Generate universally unique identifier using {@link UUID} realization.
	 */
	public static String generateRandomUuid()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * Generates universally unique identifier as a combination of current date
	 * time in millis and of {@link UUID} generated random UUID.
	 */
	public static String generateTimeBasedUuid()
	{
		return "" + new Date().getTime() + "-" + generateRandomUuid();
	}

	/**
	 * Retrieve time in millis from <code>timeBasedUuid</code> and convert its
	 * time to <code>Date</code> format
	 */
	public static Date getCreationDate(String timeBasedUuid)
	{
		Date created = null;
		if (isBlank(timeBasedUuid))
		{
			return created;
		}

		int endIndex = timeBasedUuid.indexOf("-");
		if (endIndex > 0)
		{
			try
			{
				String timeStr = timeBasedUuid.substring(0, endIndex);
				long timeInMillis = new Long(timeStr).longValue();
				created = new Date(timeInMillis);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return created;
	}
}
