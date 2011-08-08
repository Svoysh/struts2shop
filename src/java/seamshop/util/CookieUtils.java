package seamshop.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * Cookie helper for Struts 2 actions. Can be used by "Remember me" feature.
 *
 * @author Alex Siman 2009-08-26
 */
public abstract class CookieUtils
{
	private static HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	private static HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	/**
	 * @return map of all the <code>Cookie</code>s included with this request,
	 * or empty map if the request has no cookies.
	 */
	public static Map<String, Cookie> getCookies()
	{
		Map<String, Cookie> cookies = new HashMap<String, Cookie>();
		Cookie[] cookieArray = getRequest().getCookies();
		if (cookieArray != null)
		{
			for (Cookie cookie : cookieArray)
			{
				cookies.put(cookie.getName(), cookie);
			}
		}

		return cookies;
	}

	public static Cookie getCookie(String cookieName)
	{
		Cookie foundCookie = null;
		for (Cookie cookie : getRequest().getCookies())
		{
			if (cookie.getName().equals(cookieName))
	        {
				foundCookie = cookie;
				break;
	        }
		}

		return foundCookie;
	}

	public static String getCookieValue(String cookieName)
	{
		Cookie foundCookie = getCookie(cookieName);
		return foundCookie == null ? null : foundCookie.getValue();
	}

	public static boolean hasCookie(String cookieName)
	{
		return getCookie(cookieName) != null;
	}

	public static void addCookie(Cookie cookie)
	{
		getResponse().addCookie(cookie);
	}

	public static void addCookie(String cookieName, String cookieValue)
	{
		Cookie cookie = new Cookie(cookieName, cookieValue);
		addCookie(cookie);
	}

	public static void removeCookie(String cookieName)
	{
		Cookie cookie = new Cookie(cookieName, "");
		// A zero value causes the cookie to be deleted.
		cookie.setMaxAge(0);
		addCookie(cookie);
	}
}
