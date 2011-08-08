package seamshop.exception;

// TODO: Maybe extends RuntimeException? (y)
@SuppressWarnings("serial")
public class ImageProcessingException extends Exception
{
	public ImageProcessingException()
	{}

	public ImageProcessingException(String msg)
	{
		super(msg);
	}

	public ImageProcessingException(Throwable msg)
	{
		super(msg);
	}

	public ImageProcessingException(String msg, Throwable ex)
	{
		super(msg, ex);
	}
}
