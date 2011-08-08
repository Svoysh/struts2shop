package seamshop.dto;

/**
 * The examples of named codes:
 *
 * <p/>
 * Countries:
 * <ul>
 * <li>US - United States;</li>
 * <li>RU - Russian Federation;</li>
 * </ul>
 * where "US" and "RU" is codes, and "United States" and "Russian Federation"
 * are names (of codes).
 *
 * <p/>
 * Currencies:
 * <ul>
 * <li>USD - US dollar;</li>
 * <li>RUB - Russian ruble;</li>
 * </ul>
 * where "USD" and "RUB" is codes, and "US dollar" and "Russian ruble"
 * are names (of codes).
 *
 * @see Country
 * @see Currency
 * @see Locale
 *
 * @author Alex Siman 2009-05-27
 * @author Alex Siman 2009-07-06
 * @author Alex Siman 2009-10-15
 */
// TODO: Compare by "code" field? (y) Create getCompareField() and return either
//       "code" or "name" based on constructor boolean field.
@SuppressWarnings("serial")
public abstract class NamedCode extends BaseDto implements Comparable<NamedCode>
{
	public static final String CODE_NAME_SEPARATOR = " - ";

	private String code;
	private String name;

	/**
	 * Zero argument constructor is obligatory. Used to create new instance
	 * through Java Generics.
	 */
	public NamedCode()
	{}

	public NamedCode(String code)
	{
		this.code = code;
	}

	public NamedCode(String code, String name)
	{
		this(code);
		this.name = name;
	}

	@Override
	public int compareTo(NamedCode that)
	{
		if (equals(that))
		{
			return 0;
		}
		else if ((name == null) && (that.name != null))
		{
			return -1;
		}
		else if (name != null)
		{
			return name.compareTo(that.name);
		}

		return -1;
	}

	@Override
	public boolean equals(Object thatObject)
	{
		// When compare to null, always return false.
		if (thatObject == null)
		{
			return false;
		}

		// If object addresses equal, then this is the same object.
		if (this == thatObject)
		{
			return true;
		}

		// If other object is not sub-type of DTO class.
		if (!(thatObject instanceof NamedCode))
		{
			return false;
		}

		NamedCode that = (NamedCode) thatObject;
		if ((name == null) && (that.name == null))
		{
			return true;
		}
		else if (name != null)
		{
			return name.equals(that.name);
		}

		return false;
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(getClass().getSimpleName())
			.append("{")
				.append("code=").append(code).append("; ")
				.append("name=").append(name)
			.append("}")
			.toString();
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getFullName()
	{
		return new StringBuilder()
			.append(code)
			.append(CODE_NAME_SEPARATOR)
			.append(name)
			.toString();
	}
}
