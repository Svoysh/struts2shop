package seamshop.util;

import java.io.Serializable;

import org.apache.commons.logging.LogFactory;

/**
 * This is the tuned implementation of Log through Apache Commons Logging.
 * When logging through this log, there is no need to check, if certain
 * log level is enabled. This log makes it automatically.
 * <p/>
 * It's simply to tune logging by editing either "log4j.xml" or
 * "log4j.properties" file in the class-path directory.
 * <p/>
 * The six logging levels used by Log are (in order):
 * <ul>
 * <li>FATAL (the most serious)</li>
 * <li>ERROR</li>
 * <li>WARN</li>
 * <li>INFO</li>
 * <li>DEBUG</li>
 * <li>TRACE (the least serious)</li>
 * </ul>
 *
 * @author Alex Siman
 * @author Alex Siman 2009-05-14
 */
// TODO: Delete this Log class and use commons Log directly? (y)
// TODO: Use AspectJ to wrap logging methods (like: debug, info, etc.) by if (log.enabled...)
@SuppressWarnings("serial")
public final class Log implements org.apache.commons.logging.Log, Serializable
{
	/**
	 * Can be used in logs as indent before each line of some list.
	 */
	public static final String INDENT = "    ";

	private final org.apache.commons.logging.Log log;

	public Log(Class<?> loggerClass)
	{
		log = LogFactory.getLog(loggerClass);
	}

	public Log(String loggerName)
	{
		log = LogFactory.getLog(loggerName);
	}

	/**
	 * Log a message with debug log level.
	 */
	public void debug(Object message)
	{
		if (log.isDebugEnabled())
		{
			log.debug(message);
		}
	}

	/**
	 * Log an error with debug log level.
	 */
	public void debug(Object message, Throwable ex)
	{
		if (log.isDebugEnabled())
		{
			log.debug(message, ex);
		}
	}

	/**
	 * Log a message with error log level.
	 */
	public void error(Object message)
	{
		if (log.isErrorEnabled())
		{
			log.error(message);
		}
	}

	/**
	 * Log an error with error log level.
	 */
	public void error(Object message, Throwable ex)
	{
		if (log.isErrorEnabled())
		{
			log.error(message, ex);
		}
	}

	/**
	 * Log a message with fatal log level.
	 */
	public void fatal(Object message)
	{
		if (log.isFatalEnabled())
		{
			log.fatal(message);
		}
	}

	/**
	 * Log an error with fatal log level.
	 */
	public void fatal(Object message, Throwable ex)
	{
		if (log.isFatalEnabled())
		{
			log.fatal(message, ex);
		}
	}

	/**
	 * Log a message with info log level.
	 */
	public void info(Object message)
	{
		if (log.isInfoEnabled())
		{
			log.info(message);
		}
	}

	/**
	 * Log an error with info log level.
	 */
	public void info(Object message, Throwable ex)
	{
		if (log.isInfoEnabled())
		{
			log.info(message, ex);
		}
	}

	/**
	 * Log a message with trace log level.
	 */
	public void trace(Object message)
	{
		if (log.isTraceEnabled())
		{
			log.trace(message);
		}
	}

	/**
	 * Log an error with trace log level.
	 */
	public void trace(Object message, Throwable ex)
	{
		if (log.isTraceEnabled())
		{
			log.trace(message, ex);
		}
	}

	/**
	 * Log a message with warn log level.
	 */
	public void warn(Object message)
	{
		if (log.isWarnEnabled())
		{
			log.warn(message);
		}
	}

	/**
	 * Log an error with warn log level.
	 */
	public void warn(Object message, Throwable ex)
	{
		if (log.isWarnEnabled())
		{
			log.warn(message, ex);
		}
	}

	/**
	 * Is debug logging currently enabled?
	 */
	public boolean isDebugEnabled()
	{
		return log.isDebugEnabled();
	}

	/**
	 * Is error logging currently enabled?
	 */
	public boolean isErrorEnabled()
	{
		return log.isErrorEnabled();
	}

	/**
	 * Is fatal logging currently enabled?
	 */
	public boolean isFatalEnabled()
	{
		return log.isFatalEnabled();
	}

	/**
	 * Is info logging currently enabled?
	 */
	public boolean isInfoEnabled()
	{
		return log.isInfoEnabled();
	}

	/**
	 * Is trace logging currently enabled?
	 */
	public boolean isTraceEnabled()
	{
		return log.isTraceEnabled();
	}

	/**
	 * Is warn logging currently enabled?
	 */
	public boolean isWarnEnabled()
	{
		return log.isWarnEnabled();
	}
}
