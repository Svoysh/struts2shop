package seamshop.service.search;

/**
 * @author Alex Siman 2009-12-31
 */
public class SearchResult<R>
{
	private R result;

	/** Size of all results. */
	private int resultSize = 0;

	public R getResult()
	{
		return result;
	}

	public void setResult(R result)
	{
		this.result = result;
	}

	public int getResultSize()
	{
		return resultSize;
	}

	public void setResultSize(int resultSize)
	{
		this.resultSize = resultSize;
	}
}
