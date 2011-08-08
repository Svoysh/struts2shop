package seamshop.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public abstract class UrlUtils
{
	// TODO: Move to constant configuration?
	/** Default URL parameter encoding. */
	public static final String URL_PARAMETER_ENCODING = "ISO-8859-1";

	// TODO: Move to configuration?
	/** Encoding of this web application. */
	public static final String APPLICATION_ENCODING = "UTF-8";

	/**
	 * Purify URL of some "bad" chars before encoding this URL.
	 */
	public static String prepareUrlForEncoding(String url)
	{
		return url

			// Replace all non Unicode characters except for the alphanumeric
			// characters "a" through "z", "A" through "Z" and "0" through "9",
			// and the special characters "-" and "_".
			// NOTE: do not allow "." character in URL because it may cause
			// conflict when org.apache.catalina.servlets.DefaultServlet
			// will try to load some file, when URL (with file extension at
			// the end) requested.
			// E.g. URL="http://site.com/theme.css" then DefaultServlet will try
			// to load CSS file "theme.css".
			.replaceAll("[\\p{Punct}&&[^\\w\\-\\_]]", " ")

			// Remove whitespace characters at begin of a string.
			.replaceAll("^[\\s]+", "")

			// Remove whitespace characters at end of a string.
			.replaceAll("[\\s]+$", "")

			// Replace all multi-whitespace characters [ \t\n\x0B\f\r].
			.replaceAll("\\s+", "_");
	}

	public static String encodeUrl(String url)
	{
		return encodeUrl(url, false);
	}

	/**
	 * If <code>prepare==true</code> then prepare URL string by
	 * {@link #prepareUrlForEncoding(String)} before encode it.
	 */
	public static String encodeUrl(String url, boolean prepare)
	{
		if (prepare)
		{
			url = prepareUrlForEncoding(url);
		}

		try
		{
			return URLEncoder.encode(url, APPLICATION_ENCODING);
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public static String decodeUrl(String url)
	{
		try
		{
			return URLDecoder.decode(url, APPLICATION_ENCODING);
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Return decoded value of parameter with name <code>paramName</code> using
	 * {@link UrlUtils#APPLICATION_ENCODING} encoding.
	 *
	 * @param request servlet request
	 * @param paramName name of parameter in request
	 * @return decoded parameter value
	 * @see #decodeUrlParameter(String)
	 */
	public static String getDecodedUrlParameter(HttpServletRequest request,
		String paramName)
	{
		String paramValue = request.getParameter(paramName);
		return decodeUrlParameter(paramValue);
	}

	/**
	 * Return decoded parameter value <code>paramValue</code> using
	 * {@link UrlUtils#APPLICATION_ENCODING} encoding.
	 * <p/>
	 * If a character encoding is not specified, the Servlet specification
	 * requires that an encoding of ISO 8859-1 is used.
	 * <p/>
	 * POST requests should specify the encoding of the parameters and values
	 * they send. Since many clients fail to set an explicit encoding,
	 * the default is used (ISO 8859-1). In many cases this is not the preferred
	 * interpretation so one can employ a javax.servlet.Filter to set request
	 * encodings.
	 * <p/>
	 * If you set the URIEncoding="UTF-8" on the Connector in servlet.xml,
	 * it will also work with method="GET".
	 *
	 * @param paramValue parameter value to decode
	 * @return decoded parameter value
	 * @see org.springframework.web.filter.CharacterEncodingFilter
	 * @see <a href="http://wiki.apache.org/tomcat/FAQ/CharacterEncoding">Tomcat Character Encoding Issues</a>
	 */
	public static String decodeUrlParameter(String paramValue)
	{
		if (paramValue == null)
		{
			return null;
		}

		try
		{
			return new String(paramValue.getBytes(URL_PARAMETER_ENCODING),
				APPLICATION_ENCODING);
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * TODO: Rename to "getLastSubDomain"?
	 * Examples:
	 * URL = "http://en.site.com/"		  -> subdomain = "en";
	 * URL = "http://london.uk.site.com"	-> subdomain = "london";
	 */
	public static String getSubDomain(HttpServletRequest request)
	{
		String url = request.getRequestURL().toString();
		return seamshop.util.StringUtils.getFirstSubString(url, "://", ".");
	}

	/**
	 * If input URL is: "http://site.com/base/path?one=1&two&",
	 * then output param map must be: {"one"="1", "two"=""}.
	 */
	public static Map<String, String> getUrlParameterMap(String url)
	{
		// We need LinkedHashMap to remain order of parameters in the URL.
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
		if (!StringUtils.isBlank(url))
		{
			int questIndex = url.indexOf("?");
			if (questIndex > 0)
			{
				String paramsPartOfUrl = url.substring(questIndex + 1, url.length());
				if (!StringUtils.isBlank(paramsPartOfUrl))
				{
					String[] paramNamesAndValues = paramsPartOfUrl.split("(&amp;|&)");
					if (!ArrayUtils.isEmpty(paramNamesAndValues))
					{
						for (String paramNameAndValue : paramNamesAndValues)
						{
							String[] nameAndValue = paramNameAndValue.split("=");
							if (nameAndValue != null )
							{
								if (nameAndValue.length >= 2)
								{
									urlParams.put(nameAndValue[0], nameAndValue[1]);
								}
								else if (nameAndValue.length == 1)
								{
									urlParams.put(nameAndValue[0], "");
								}
							}
						}
					}
				}
			}
		}

		return urlParams;
	}
}
