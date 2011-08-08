package seamshop.util;

import static org.apache.commons.lang.StringUtils.abbreviate;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Helper class to parse to/from JSON objects.
 * Lazy singleton
 *
 * @author Alex Siman 2009-12-04
 */
public class Json
{
	private Json() {}

	private static class SingletonHolder
	{
		private static final Json INSTANCE = new Json();
	}

	private static Json getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private static final Log log = new Log(Json.class);
	private final ObjectMapper mapper = new ObjectMapper();

	public static ObjectMapper getMapper()
	{
		return getInstance().mapper;
	}

	public static String to(Object object)
	{
		try
		{
			return getMapper().writeValueAsString(object);
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			log.error("Unable to serialize to JSON: " + object, e);

			// TODO: Decide: Return "" , "{}" or null?
			return "";
//			return null;
		}
	}

	/**
	 * To prevent untyped generics, use next code for <code>klass</code> argument:
	 * <pre>List&lt;String&gt; result = from(json,
	 *     new TypeReference&lt;List&lt;String&gt;&gt;() {});</pre>
	 * or:
	 * <pre>List&lt;String&gt; result = from(json,
	 *     TypeFactory.collectionType(ArrayList.class, String.class));</pre>
	 * See Jackson's <a href="http://wiki.fasterxml.com/JacksonFAQ?highlight=%28TypeFactory%29">Deserializing Generic types</a>.
	 */
	public static <T> T from(String json, Class<T> klass)
	{
		try
		{
			return getMapper().readValue(json, klass);
		}
		catch (RuntimeException e)
		{
			log.error("Runtime exception during deserializing "
				+ klass.getSimpleName() + " from " + abbreviate(json, 80));
			throw e;
		}
		catch (Exception e)
		{
			log.error("Exception during deserializing " + klass.getSimpleName()
				+ " from " + abbreviate(json, 80));
			return null;
		}
	}
}
