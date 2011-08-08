package seamshop.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException
{
	public BusinessException()
	{
		super();
	}

	public BusinessException(String message)
	{
		super(message);
	}

	public BusinessException(Throwable cause)
	{
		super(cause);
	}

	public BusinessException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
