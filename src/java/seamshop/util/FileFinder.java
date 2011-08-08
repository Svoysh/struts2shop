package seamshop.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.util.ClassLoaderUtil;

/**
 * Helper class for finding files by file name through classpath, etc.
 *
 * @author Alex Siman 2009-07-22
 */
// TODO: Rename to: "FileUtils"? (mb)
public abstract class FileFinder
{
	private static final Log LOG = new Log(FileFinder.class);

	/**
	 * Returns absolute path of file with specified file name if it's found in classpath.
	 */
	public static String findPath(String fileName)
	{
		File foundFile = findFile(fileName);
		return foundFile == null ? null : foundFile.getAbsolutePath();
	}

	/**
	 * Returns file with specified file name if it's found in classpath.
	 */
	public static File findFile(String fileName)
	{
		LOG.debug("Trying to find file by its name \"" + fileName + "\"...");
		URL foundUrl = ClassLoaderUtil.getResource(fileName, FileFinder.class);

		File foundFile = null;
		if (foundUrl != null)
		{
			try
			{
				foundFile = new File(foundUrl.toURI());
				LOG.debug("File was found: " + foundFile.getAbsolutePath());
			}
			catch (URISyntaxException ex)
			{
				LOG.error("File was found but its URI is invalid: " + foundUrl);
			}
		}
		else
		{
			LOG.debug("File not found");
		}

		return foundFile;
	}

	private static String WEBAPP_PATH = null;

	/**
	 * Getting the path of current web app. If name of the web app is "Someapp",
	 * then current path may be e.g. "C:/tomcat-6.0/webapps/Someapp".
     */
	public static String getWebappPath()
	{
		if (WEBAPP_PATH == null)
		{
			ServletContext servletContext = getServletContext();
			// TODO: Low: Do need check for null? (n) Such as this is WEB app.
			if (servletContext != null)
			{
				WEBAPP_PATH = servletContext.getRealPath("");
			}
		}

		return WEBAPP_PATH;
	}

	public static File makeWebappSubdir(String dirRelativePath)
	{
		File subdir = null;
		String webappPath = getWebappPath();
		if (StringUtils.isNullOrEmpty(new String[] {webappPath, dirRelativePath}))
		{
			return subdir;
		}

		if (!dirRelativePath.startsWith("/"))
		{
			dirRelativePath = "/" + dirRelativePath;
		}

		subdir = new File(webappPath + dirRelativePath);
		// Create directory if not exists.
		if (!subdir.exists())
		{
			subdir.mkdirs();
		}

		return subdir;
	}

	public static String getMimeType(String filePath)
	{
		return getServletContext().getMimeType(filePath);
	}

	private static ServletContext getServletContext()
	{
		return ServletActionContext.getServletContext();
	}
}
