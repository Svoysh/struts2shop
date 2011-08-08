package seamshop.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import seamshop.exception.HtmlScannerException;

/**
 * Helper class to work with HTML.
 *
 * @author Alex Siman 2009-07-21
 */
/*
 * TODO: Normal: Enhance: Refactor to singleton (utilizing inner class of
 * implementation) to initialize AntiSamy only once.
 */
public class HtmlUtils
{
	// TODO: Low: Refactor: Make it more beautiful.
	// TODO: i18n: Externalize file names.
	private static final String ANTISAMY_BASE_DIR = "antisamy/";

	public static final String ANTISAMY_POLICY_FILE = ANTISAMY_BASE_DIR + "antisamy.xml";
	public static final String ANTISAMY_POLICY_FILE_ANYTHING_GOES = ANTISAMY_BASE_DIR + "antisamy-anything-goes.xml";
	public static final String ANTISAMY_POLICY_FILE_EBAY = ANTISAMY_BASE_DIR + "antisamy-ebay.xml";
	public static final String ANTISAMY_POLICY_FILE_MY_SPACE = ANTISAMY_BASE_DIR + "antisamy-myspace.xml";
	public static final String ANTISAMY_POLICY_FILE_SLASHDOT = ANTISAMY_BASE_DIR + "antisamy-slashdot.xml";
	public static final String ANTISAMY_POLICY_FILE_PLAIN_TEXT = ANTISAMY_BASE_DIR + "antisamy-plain-text.xml";

	public static final String DEFAULT_ANTISAMY_POLICY_FILE = ANTISAMY_POLICY_FILE_EBAY;

	private static final Log LOG = new Log(HtmlUtils.class);

	private static HtmlUtils INSTANCE = null;

	// If this method is not synchronized, multiple instances can be created.
	// See [http://yohanliyanage.blogspot.com/2009/09/breaking-singleton.html].
	private static synchronized HtmlUtils getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new HtmlUtils();
		}

		return INSTANCE;
	}

	/**
	 * Returns filtered HTML from XSS hacks. Filtering means that HTML
	 * text will be rebuild, allowing only white list tags to stay.
	 */
	public static String filterFromXss(String inputHtml)
	{
		LOG.debug("Filtering input HTML from XSS hacks...");
		return getInstance().filter(inputHtml, DEFAULT_ANTISAMY_POLICY_FILE);
	}

	/**
	 * Returns text cleaned from HTML markup.
	 * <p>
	 * All HTML entity escapes will be unescaped to the actual Unicode
	 * characters corresponding to the escapes. Supports HTML 4.0 entities.
	 * For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
	 * will become "&lt;Fran&ccedil;ais&gt;".
	 * <p>
	 * All multi-whitespace characters [ \t\n\x0B\f\r] and any kind of Unicode
	 * whitespace or invisible separator will be replaced by one space [ ].
	 * For example, the string "fix &nbsp; &nbsp; multi-spaces"
	 * will become "fix multi-spaces".
	 */
	public static String getPlainText(String inputHtml)
	{
		LOG.debug("Gettig plain text from input HTML...");

		String plainText = getInstance().filter(inputHtml, ANTISAMY_POLICY_FILE_PLAIN_TEXT);
		if (!StringUtils.isBlank(plainText))
		{
			// Unescape HTML entities to Unicode equivalents.
			plainText = StringEscapeUtils.unescapeHtml(plainText);

			// Replace all multi-whitespace characters by one space.
			plainText = plainText.replaceAll("[\\s\\p{Z}]+", " ");
		}

		return plainText;
	}

	/**
	 * @see #filterFromXss(String)
	 * @see #getPlainText(String)
	 */
	public static SafeHtmlAndText getSafeHtmlAndText(String html)
	{
		String text = html;
		if (!StringUtils.isBlank(html))
		{
			html = HtmlUtils.filterFromXss(html);
			text = HtmlUtils.getPlainText(html);
		}

		return new SafeHtmlAndText(html, text);
	}

	/**
	 * Holds AntiSamy HTML scanners lazy loaded by policy file names.
	 */
	private ConcurrentMap<String, AntiSamy> policyFileNamesAndSacnners =
		new ConcurrentHashMap<String, AntiSamy>();

	private HtmlUtils()
	{}

	/**
	 * Filters input HTML using specified policy as white list of allowed tags.
	 */
	@SuppressWarnings("unchecked")
	private String filter(String inputHtml, String policyFileName)
	{
		String filteredHtml = "";
		if (!StringUtils.isBlank(inputHtml))
		{
			if (policyFileName == null)
			{
				LOG.warn("Provided policy file name is null.");
				policyFileName = DEFAULT_ANTISAMY_POLICY_FILE;
			}

			AntiSamy htmlScanner = getHtmlScannerByPolicyFileName(policyFileName);
			if (htmlScanner != null)
			{
				CleanResults scanResults;
				try
				{
					scanResults = htmlScanner.scan(inputHtml);
					filteredHtml = scanResults.getCleanHTML();
					ArrayList<String> scannerErrors = scanResults.getErrorMessages();
					if (!CollectionUtils.isNullOrEmpty(scannerErrors))
					{
						LOG.trace("HTML input contains erorrs (" + scannerErrors.size() + "):");
						int i = 1;
						for (String error : scannerErrors)
						{
							LOG.trace("    " + i + ") " + error);
							i++;
						}
					}
				}
				catch (ScanException ex)
				{
					throw new HtmlScannerException(ex);
				}
				catch (PolicyException ex)
				{
					throw new HtmlScannerException(ex);
				}
			}
		}

		return filteredHtml;
	}

	/**
	 * @param policyFileName The path to the XML policy file.
	 */
	private AntiSamy getHtmlScannerByPolicyFileName(String policyFileName)
	{
		AntiSamy scanner = policyFileNamesAndSacnners.get(policyFileName);
		if (scanner == null)
		{
			scanner = initHtmlScannerByPolicyFileName(policyFileName);
			// Error, if scanner was not initialized for some reasons.
			if (scanner == null)
			{
				throw new HtmlScannerException(
					"Unable to load HTML scanner by policy file name \"" +
					policyFileName + "\".");
			}
		}

		return scanner;
	}

	private AntiSamy initHtmlScannerByPolicyFileName(String policyFileName)
	{
		AntiSamy scanner = null;
		String policyFilePath = FileFinder.findPath(policyFileName);
		if (StringUtils.isBlank(policyFilePath))
		{
			throw new HtmlScannerException(
				"Policy file not found by file name [" + policyFileName + "].");
		}
		else
		{
			Policy policy = null;
			try
			{
				policy = Policy.getInstance(policyFilePath);
			}
			catch (PolicyException ex)
			{
				throw new HtmlScannerException(ex);
			}

			if (policy != null)
			{
				scanner = new AntiSamy(policy);
				AntiSamy actualScanner = policyFileNamesAndSacnners
					.putIfAbsent(policyFileName, scanner);
				boolean wasScannerInitedByAnotherThread = actualScanner != null;
				if (wasScannerInitedByAnotherThread)
				{
					scanner = actualScanner;
				}
			}
		}

		return scanner;
	}
}
