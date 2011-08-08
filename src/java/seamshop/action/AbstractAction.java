package seamshop.action;

import static seamshop.consts.I18nKey.MSGKEY_EMAIL_CONFIRM_EMAIL_SUBJECT;
import static seamshop.consts.I18nKey.MSGKEY_ERROR_CHOOSE_ANOTHER_VALUE;
import static seamshop.util.Log.INDENT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import seamshop.actionutil.BreadCrumb;
import seamshop.actionutil.Pager;
import seamshop.actionutil.UrlBuilder;
import seamshop.consts.Text;
import seamshop.consts.freemarker.TemplateName;
import seamshop.context.RequestContext;
import seamshop.context.SessionContext;
import seamshop.dao.AddressDao;
import seamshop.dao.CartDao;
import seamshop.dao.CartItemDao;
import seamshop.dao.CategoryDao;
import seamshop.dao.ConfirmEmailDao;
import seamshop.dao.DaoUtils;
import seamshop.dao.ImageDao;
import seamshop.dao.OrderDao;
import seamshop.dao.OrderItemDao;
import seamshop.dao.PaymentMethodDao;
import seamshop.dao.ProductDao;
import seamshop.dao.ProductVariantDao;
import seamshop.dao.SessionDao;
import seamshop.dao.ShippingMethodDao;
import seamshop.dao.ShopDao;
import seamshop.dao.UserDao;
import seamshop.dao.UserShoppingCountryDao;
import seamshop.exception.NotImplementedException;
import seamshop.interceptor.BeforeValidationAware;
import seamshop.interceptor.RequestContextAware;
import seamshop.interceptor.SilentLoginAware;
import seamshop.interceptor.security.UserRoleAware;
import seamshop.messages.CountryCollection;
import seamshop.messages.CurrencyCollection;
import seamshop.messages.LocaleCollection;
import seamshop.model.Cart;
import seamshop.model.ConfirmEmail;
import seamshop.model.User;
import seamshop.model.enums.UserRole;
import seamshop.service.CartService;
import seamshop.service.ImageService;
import seamshop.service.MailService;
import seamshop.service.search.CategorySearchService;
import seamshop.service.search.ProductSearchService;
import seamshop.service.search.ShopSearchService;
import seamshop.util.CollectionUtils;
import seamshop.util.CookieUtils;
import seamshop.util.Log;
import seamshop.util.UrlUtils;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.TextProviderFactory;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.ValidationAwareSupport;
import com.opensymphony.xwork2.util.ValueStack;

/*
 * TODO: Save selected locale to User entity.
 *
 * TODO: Low: Duplication: Are Spring annotations inherited from superclass?
 *       If yes, then add next code to this class:
 *
 * TODO: Note: Just do not pass the whole action to custom FreeMarker templates.
 *       Pass only needed variables (models) such as: "user", "shop", "products",
 *       etc.
 *
 * TODO: Are Spring annos inherited from superclasses?
 * @Component
 * @Scope(Spring.ACTION_SCOPE)
 */
@SuppressWarnings("serial")
public abstract class AbstractAction implements Action, Validateable,
	ValidationAware, TextProvider, LocaleProvider, BeforeValidationAware,
	RequestContextAware, SilentLoginAware, UserRoleAware, Serializable
{
	protected final Log log = new Log(this.getClass());

	private final transient TextProvider textProvider = new TextProviderFactory().createInstance(getClass(), this);
	private final ValidationAwareSupport validationAware = new ValidationAwareSupport();

	// Contexts. --------------------------------------------------------------

	@Autowired protected SessionContext sessionContext;
	@Autowired protected RequestContext requestContext;

	// DAOs. ------------------------------------------------------------------

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired protected DaoUtils daoUtils;
	@Autowired protected AddressDao addressDao;
	@Autowired protected CartDao cartDao;
	@Autowired protected CartItemDao cartItemDao;
	@Autowired protected CategoryDao categoryDao;
	@Autowired protected ConfirmEmailDao confirmEmailDao;
	@Autowired protected ImageDao imageDao;
	@Autowired protected OrderDao orderDao;
	@Autowired protected OrderItemDao orderItemDao;
	@Autowired protected PaymentMethodDao paymentMethodDao;
	@Autowired protected ProductDao productDao;
	@Autowired protected ProductVariantDao productVariantDao;
	@Autowired protected SessionDao sessionDao;
	@Autowired protected ShippingMethodDao shippingMethodDao;
	@Autowired protected ShopDao shopDao;
	@Autowired protected UserDao userDao;
	@Autowired protected UserShoppingCountryDao userShoppingCountryDao;

	// Services. --------------------------------------------------------------

	@Autowired protected CartService cartService;
	@Autowired protected ImageService imageService;
	@Autowired protected MailService mailService;

	@Autowired protected CategorySearchService categorySearchService;
	@Autowired protected ProductSearchService productSearchService;
	@Autowired protected ShopSearchService shopSearchService;

	// Messages. --------------------------------------------------------------

	@Autowired protected CountryCollection countryCollection;
	@Autowired protected CurrencyCollection currencyCollection;
	@Autowired protected LocaleCollection localeCollection;

	// Fields. ----------------------------------------------------------------

	private UrlBuilder urlBuilder = new UrlBuilder(getRequest());
	private Map<String, Cookie> cookies = null;
	private List<BreadCrumb> breadCrumbs = new ArrayList<BreadCrumb>();

	// Request params. --------------------------------------------------------

	/**
	 * Number of requested page. Starts from 1.
	 */
	private Integer page;

	/**
	 * Max number of returned results per page.
	 */
	private Integer results;

	// Interceptor callbacks --------------------------------------------------

	@Override
	public String getUserSessionId()
	{
		return getCookieValue(seamshop.consts.Cookie.REMEMBER_ME);
	}

	// TODO: Do we need this method? (n) See PrepareInterceptor.
	@Override
	public void initRequestContext()
	{
		requestContext.setValidationAware(validationAware);
	}

	/**
	 * Initialization method for every action.
	 */
	// TODO: Replace this method and its BeforeValidationInterceptor by
	//       PrepareInterceptor? (y) Or create InitInterceptor
	@Override
	public void doBeforeValidation()
	{
		log.debug("page: " + page);
		getPager().setPage(page);
		getPager().setMaxResults(results);
	}

	// Servlet related --------------------------------------------------------

	protected HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	protected HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}

	public UrlBuilder getUrlBuilder()
	{
		return urlBuilder;
	}

	public String getBaseUrl()
	{
		return urlBuilder.getFullBase();
	}

	// TODO: Low: Refactor: Move to: UrlBuilder.build(...)? (mb)
	public String buildUrl(String relativeUrl, Map<String, Object> params)
	{
		String baseUrl = getBaseUrl();
		StringBuilder url = new StringBuilder(baseUrl);

		if (!StringUtils.isBlank(relativeUrl))
		{
			String slash = "/";
			if (!baseUrl.endsWith(slash) && !relativeUrl.startsWith(slash))
			{
				url.append(slash);
			}
			url.append(relativeUrl);
		}

		if (!CollectionUtils.isNullOrEmpty(params))
		{
			url.append("?");
			for (Entry<String, Object> param : params.entrySet())
			{
				url.append(param.getKey());
				url.append("=");
				// Param value must be encoded.
				url.append(UrlUtils.encodeUrl(param.getValue().toString()));
				url.append("&");
			}
			// Delete last '&'.
			url.deleteCharAt(url.length() - 1);
		}

		return url.toString();
	}

	// Cookie related ---------------------------------------------------------

	protected Map<String, Cookie> getCookies()
	{
		if (cookies == null)
		{
			// Init cookies from request.
			cookies = CookieUtils.getCookies();
		}

		return cookies;
	}

	protected Cookie getCookie(String cookieName)
	{
		return getCookies().get(cookieName);
	}

	protected String getCookieValue(String cookieName)
	{
		Cookie foundCookie = getCookie(cookieName);
		return foundCookie == null ? null : foundCookie.getValue();
	}

	protected boolean hasCookie(String cookieName)
	{
		return getCookie(cookieName) != null;
	}

	protected void addCookie(Cookie cookie)
	{
		CookieUtils.addCookie(cookie);
	}

	protected void removeCookie(String cookieName)
	{
		CookieUtils.removeCookie(cookieName);
	}

	// Current action related -------------------------------------------------

	protected ActionContext getActionContext()
	{
		return ActionContext.getContext();
	}

	protected ActionProxy getActionProxy()
	{
		return getActionContext().getActionInvocation().getProxy();
	}

	/**
	 * Returns action's namespace.
	 * If URL="http://site.com/seller/product!list",
	 * then namespace="seller".
	 */
	public String getNamespace()
	{
		String namespace = getActionProxy().getNamespace();
//		log.debug("getNamespace(): " + namespace);
		return namespace;
	}

	/**
	 * Returns action name.
	 * If URL="http://site.com/seller/product!list",
	 * then action="product".
	 */
	public String getAction()
	{
		String action = getActionProxy().getActionName();
//		log.debug("getAction(): " + action);
		return action;
	}

	/**
	 * Returns name of action method.
	 * If URL="http://site.com/seller/product!list",
	 * then method="list".
	 */
	public String getMethod()
	{
		String method = getActionProxy().getMethod();
//		log.debug("getMethod(): " + method);
		return method;
	}

	protected String getAllowedSessionDomain()
	{
		// TODO: Impl: Return site domain allowed for "Remember me".
		throw new NotImplementedException();
	}

	// Email ------------------------------------------------------------------
	// TODO: Low: Refactor: AbstractAction is getting realy fat, like God object.

	protected Map<String, Object> buildEmailTemplateModel()
	{
		Map<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put("action", this);

		return templateModel;
	}

	protected void sendEmail(String to, String subject, String templateName)
	{
		mailService.sendEmail(to, subject, templateName, buildEmailTemplateModel());
	}

	protected void sendConfirmationEmail(ConfirmEmail confirmEmail)
	{
		String to = confirmEmail.getEmail();
		String subject = getText(MSGKEY_EMAIL_CONFIRM_EMAIL_SUBJECT);
		Map<String, Object> templateModel = buildEmailTemplateModel();
		templateModel.put("code", confirmEmail.getId());

		mailService.sendEmail(to, subject,
			TemplateName.CONFIRM_EMAIL, templateModel);

		// TODO: i18n: Externalize.
		addActionMessage("Confirmation email has been sent to <strong>" +
			escape(to) + "</strong>.");
	}

	// Common methods ---------------------------------------------------------

	/**
	 * @return name of this web application.
	 */
	public String getWebappName()
	{
		return Text.WEBAPP_NAME;
	}

	/**
	 * Checks if a String is empty ("") or null.
	 */
	public boolean isEmpty(String str)
	{
		return StringUtils.isEmpty(str);
	}

	/**
	 * Checks if a Collection is null or empty.
	 */
	public boolean isEmpty(Collection<?> coll)
	{
		return CollectionUtils.isNullOrEmpty(coll);
	}

	/**
	 * Checks if a Map is null or empty.
	 */
	public boolean isEmpty(Map<?, ?> map)
	{
		return CollectionUtils.isNullOrEmpty(map);
	}

	/**
	 * Checks if a String is whitespace, empty ("") or null.
	 */
	public boolean isBlank(String str)
	{
		return StringUtils.isBlank(str);
	}

	/**
	 * Escape HTML to prevent XSS attacks.
	 */
	// TODO: Rename to "escapeHtml()"? (mb) Or add alias func.
	public String escape(String html)
	{
		// TODO: Note that StringEscapeUtils.escapeHtml() escape non latin chars
		//       to its HTML entities. Maybe fix it.
		return StringEscapeUtils.escapeHtml(html);
	}

	public Object escape(Object anyValue)
	{
		if (anyValue instanceof String)
		{
			return escape((String) anyValue);
		}
		else
		{
			return anyValue;
		}
	}

	/**
	 * Returns the main pager to iterate shops, products, etc.
	 */
	public Pager getPager()
	{
		return requestContext.getPager();
	}

	/**
	 * @return Something like:
	 * <ul>
	 * <li>[<code>/namespace/action!method</code>]</li>
	 * <li>[<code>/namespace/action</code>] if method is "execute"</li>
	 * <li>[<code>/action</code>] if namespace is "/" and method is "execute"</li>
	 * <ul>
	 */
	// TODO: Low: Rename to "getActionUrl".
	public String getPagerUrl()
	{
		return getPagerUrl(null);
	}

	/**
	 * @return Something like:
	 * <ul>
	 * <li>[<code>/namespace/action!method</code>]</li>
	 * <li>[<code>/namespace/action</code>] if method is "execute"</li>
	 * <li>[<code>/action</code>] if namespace is "/" and method is "execute"</li>
	 * <ul>
	 */
	// TODO: Low: Rename to "getActionUrl".
	public String getPagerUrl(String method)
	{
		String namespace = getNamespace();
		if (!"/".equals(namespace))
		{
			namespace += "/";
		}

		if (method == null)
		{
			method = getMethod();
		}

		String methodSuffix = "";
		if (!"execute".equals(method))
		{
			methodSuffix = "!" + method;
		}

		String pageUrl = namespace + getAction() + methodSuffix;

		return pageUrl;
	}

	// TODO: Deprecated? Delete? (y)
	public Map<String, String> getUrlParameterMap(String url)
	{
		return UrlUtils.getUrlParameterMap(url);
	}

	public List<BreadCrumb> getBreadCrumbs()
	{
		return breadCrumbs;
	}

	public boolean isLoggedIn()
	{
		return sessionContext.hasUser();
	}

	// TODO: Low: Refactor: Move to [UserService.getCurrentUser()].
	public User getCurrentUser()
	{
		return sessionContext.getUser();
	}

	@Override
	public UserRole getUserRole()
	{
		return sessionContext.getUserRole();
	}

	// TODO: Low: Refactor: Move to [CartService.getMyCart()].
	public Cart getCurrentCart()
	{
		return cartDao.getCurrentCart();
	}

	// TODO: Deprecated? (y)
	public String addParamToCurrentUrl(String param, String value)
	{
		throw new NotImplementedException();
	}

	// Getters/Setters. -------------------------------------------------------

	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	public Integer getResults()
	{
		return results;
	}

	public void setResults(Integer results)
	{
		this.results = results;
	}

	// Copied from [com.opensymphony.xwork2.ActionSupport] --------------------
	// TODO: Low: Refactor: Duplication: See [service.AbstractService] methods related to ValidationAwareSupport.

	public void setActionErrors(Collection<String> errorMessages)
	{
		validationAware.setActionErrors(errorMessages);
	}

	public Collection<String> getActionErrors()
	{
		return validationAware.getActionErrors();
	}

	public void setActionMessages(Collection<String> messages)
	{
		validationAware.setActionMessages(messages);
	}

	public Collection<String> getActionMessages()
	{
		return validationAware.getActionMessages();
	}

	public void setFieldErrors(Map<String, List<String>> errorMap)
	{
		validationAware.setFieldErrors(errorMap);
	}

	public Map<String, List<String>> getFieldErrors()
	{
		return validationAware.getFieldErrors();
	}

	public Locale getLocale()
	{
		ActionContext ctx = ActionContext.getContext();
		if (ctx != null)
		{
			// TODO: Low: Performance: Cache locale in filed.
			Locale locale = ctx.getLocale();
//			log.debug("getLocale(): " + locale);
			return locale;
		}
		else
		{
			log.debug("getLocale(): Action context not initialized");
			return null;
		}
	}

	/**
	 * Tries to find locale that this webapp supports.
	 */
	public String getLocaleCode()
	{
		String code = "";
		Locale locale = getLocale();
		if (null != locale)
		{
			code = locale.toString();
			// Try to find locale by code: language + country + variant.
			if (!hasLocaleByCode(code))
			{
				String language = locale.getLanguage();
				String country = locale.getCountry();
				code = new StringBuilder()
					.append(language).append("_").append(country)
					.toString();
				// Try to find locale by code: language + country.
				if (!hasLocaleByCode(code))
				{
					code = language;
					if (!hasLocaleByCode(code))
					{
						code = "";
					}
				}
			}
		}
		log.debug("getLocaleCode(): " + code);
		return code;
	}

	private boolean hasLocaleByCode(String code)
	{
		return localeCollection.isValidCode(code);
	}

	public Collection<seamshop.dto.Locale> getLocales()
	{
		return localeCollection.getCollection();
	}

	public String getTextErrorChooseAnotherValueWithReason(String reasonKey)
	{
		StringBuilder resultText = new StringBuilder();
		if (!isEmpty(reasonKey))
		{
			resultText.append(getText(reasonKey));
			if (!isEmpty(resultText.toString()))
			{
				resultText.append(" ");
			}
		}
		return resultText.append(getText(MSGKEY_ERROR_CHOOSE_ANOTHER_VALUE)).toString();
	}

	public boolean hasKey(String key)
	{
		return textProvider.hasKey(key);
	}

	public String getText(String aTextName)
	{
		return textProvider.getText(aTextName);
	}

	public String getText(String aTextName, String defaultValue)
	{
		return textProvider.getText(aTextName, defaultValue);
	}

	public String getText(String aTextName, String defaultValue, String obj)
	{
		return textProvider.getText(aTextName, defaultValue, obj);
	}

	public String getText(String aTextName, List<Object> args)
	{
		return textProvider.getText(aTextName, args);
	}

	public String getText(String key, String[] args)
	{
		return textProvider.getText(key, args);
	}

	public String getText(String aTextName, String defaultValue,
		List<Object> args)
	{
		return textProvider.getText(aTextName, defaultValue, args);
	}

	public String getText(String key, String defaultValue, String[] args)
	{
		return textProvider.getText(key, defaultValue, args);
	}

	public String getText(String key, String defaultValue, List<Object> args,
		ValueStack stack)
	{
		return textProvider.getText(key, defaultValue, args, stack);
	}

	public String getText(String key, String defaultValue, String[] args,
		ValueStack stack)
	{
		return textProvider.getText(key, defaultValue, args, stack);
	}

	public ResourceBundle getTexts()
	{
		return textProvider.getTexts();
	}

	public ResourceBundle getTexts(String aBundleName)
	{
		return textProvider.getTexts(aBundleName);
	}

	public void addActionError(String anErrorMessage)
	{
		validationAware.addActionError(anErrorMessage);
	}

	public void addActionMessage(String aMessage)
	{
		validationAware.addActionMessage(aMessage);
	}

	public void addFieldError(String fieldName, String errorMessage)
	{
		validationAware.addFieldError(fieldName, errorMessage);
	}

	/*@AllowedMethod
	public String input() throws Exception
	{
		return INPUT;
	}

	@AllowedMethod
	public String doDefault() throws Exception
	{
		return SUCCESS;
	}*/

	/**
	 * A default implementation that does nothing an returns "success".
	 * <p/>
	 * Subclasses should override this method to provide their business logic.
	 * <p/>
	 * See also {@link com.opensymphony.xwork2.Action#execute()}.
	 *
	 * @return returns {@link #SUCCESS}
	 * @throws Exception
	 *             can be thrown by subclasses.
	 */
	public String execute() throws Exception
	{
		log.debug("call execute()");

		// Nothing.

		return SUCCESS;
	}

	public boolean hasActionErrors()
	{
		return validationAware.hasActionErrors();
	}

	public boolean hasActionMessages()
	{
		return validationAware.hasActionMessages();
	}

	public boolean hasErrors()
	{
		return validationAware.hasErrors();
	}

	public boolean hasFieldErrors()
	{
		return validationAware.hasFieldErrors();
	}

	/**
	 * Clears field errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearFieldErrors()
	{
		validationAware.clearFieldErrors();
	}

	/**
	 * Clears action errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearActionErrors()
	{
		validationAware.clearActionErrors();
	}

	/**
	 * Clears messages. Useful for Continuations and other situations where you
	 * might want to clear parts of the state on the same action.
	 */
	public void clearMessages()
	{
		validationAware.clearMessages();
	}

	/**
	 * Clears all errors. Useful for Continuations and other situations where
	 * you might want to clear parts of the state on the same action.
	 */
	public void clearErrors()
	{
		validationAware.clearErrors();
	}

	/**
	 * Clears all errors and messages. Useful for Continuations and other
	 * situations where you might want to clear parts of the state on the same
	 * action.
	 */
	public void clearErrorsAndMessages()
	{
		validationAware.clearErrorsAndMessages();
	}

	/**
	 * A default implementation that validates nothing. Subclasses should
	 * override this method to provide validations.
	 */
	public void validate()
	{}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	/**
	 * <!-- START SNIPPET: pause-method --> Stops the action invocation
	 * immediately (by throwing a PauseException) and causes the action
	 * invocation to return the specified result, such as {@link #SUCCESS},
	 * {@link #INPUT}, etc.
	 * <p/>
	 * <p/>
	 * The next time this action is invoked (and using the same continuation
	 * ID), the method will resume immediately after where this method was
	 * called, with the entire call stack in the execute method restored.
	 * <p/>
	 * <p/>
	 * Note: this method can <b>only</b> be called within the {@link #execute()}
	 * method. <!-- END SNIPPET: pause-method -->
	 *
	 * @param result
	 *            the result to return - the same type of return value in the
	 *            {@link #execute()} method.
	 */
	public void pause(String result)
	{}

	// Common log messages ----------------------------------------------------

	protected void logActionName()
	{
		log.debug("Action: " + getPagerUrl());
	}

	@SuppressWarnings("unchecked")
	protected void logRequestPrameters()
	{
		log.debug("Printing HTTP request parameters...");
		Map<String, String[]> paramMap = getRequest().getParameterMap();
		if (!CollectionUtils.isNullOrEmpty(paramMap))
		{
			int i = 1;
			for (Entry<String, String[]> paramEntry : paramMap.entrySet())
			{
				log.debug(INDENT + i + ") " + paramEntry.getKey() + ": " +
					Arrays.toString(paramEntry.getValue()) + ";");

				i++;
			}
		}
		else
		{
			log.debug(INDENT + "None.");
		}
	}
}
