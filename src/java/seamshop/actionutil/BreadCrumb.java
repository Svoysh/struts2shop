package seamshop.actionutil;

import seamshop.dto.BaseDto;

/**
 * @author Alex Siman 2010-01-04
 */
@SuppressWarnings("serial")
public class BreadCrumb extends BaseDto
{
	private String name;
	private String url;

	public BreadCrumb()
	{}

	public BreadCrumb(String name, String url)
	{
		this();
		this.name = name;
		this.url = url;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
