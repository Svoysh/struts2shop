package seamshop.exception;

/*
 * TODO: Inherit all exception classes of this app from ane root exception,
 * say AppRuntimeException and AppException.
 */
@SuppressWarnings("serial")
public class HtmlScannerException extends RuntimeException
{
	public HtmlScannerException()
	{
		super();
	}

	public HtmlScannerException(String message)
	{
		super(message);
	}

	public HtmlScannerException(Throwable cause)
	{
		super(cause);
	}

	public HtmlScannerException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
