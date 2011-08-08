package seamshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

// TODO: Low: Create "File" entity with name, size, ext, etc.
@Entity
@SuppressWarnings("serial")
public class Image extends AbstractIdBasedEntity
{
	/**
	 * On the most file systems (Windows, Unix, Mac OS) maximum length of file
	 * name is 255 chars.
	 * See [http://en.wikipedia.org/wiki/Filename#Comparison_of_file_name_limitations].
	 * <p/>
	 * File name can be used as value for <code>src</code> attribute of
	 * <code>img</code> HTML tag. NOTE: File name must be case sensitive.
	 * <p/>
	 * Example:
	 * <p/>
	 * <code>&ltimg src="fileName"/&gt</code>
	 */
	// TODO: Low: Rename to "name"? (maybe)
	// TODO: Define appropriate image file name size. (Maybe 32)
	@Column(length = 255, nullable = false)
	private String fileName;

	/**
	 * File size in bytes.
	 */
	private Long fileSize;

	private Integer width;
	private Integer height;

	// TODO: Impl: Content type (MIME) received by Apache Common Upload from browser.
//	private String contentType;

	// TODO: Impl: Extension of image file: "jpg", "png", etc.
//	private String extension;


	/**
	 * Can be used as value for <code>alt</code> attribute of <code>img</code>
	 * HTML tag. Also can be used in combination with {@link Image#title} as
	 * value for <code>title</code> attribute of <code>img</code> HTML tag.
	 * <p/>
	 * Examples:
	 * <p/>
	 * <code>&ltimg alt="description"/&gt</code> <br/>
	 * <code>&ltimg title="description" alt="description"/&gt</code> <br/>
	 * <code>&ltimg title="description" alt=""/&gt</code>
	 */
	@Column(length = 1023, nullable = true)
	private String description;

	// TODO: Rename to "uploader"? (n)
	/** Image owner (uploader). */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	@ForeignKey(name = "fk_image_user_id")
	private User user;

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "{" +
			"id=" + getId() + "; " +
			"fileName=" + fileName + "; " +
			"width=" + width + "; " +
			"height=" + height + "; " +
			"fileSize=" + fileSize +
		"}";
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public Long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}
}
