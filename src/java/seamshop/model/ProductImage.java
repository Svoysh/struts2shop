package seamshop.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * @author Alex Siman 2009-01-13
 * @see Product
 * @see Image
 */
@Entity
@SuppressWarnings("serial")
public class ProductImage extends AbstractIdBasedEntity
{
	/*
	 * TODO: Refactor: Enhance: Remove "number" field and implement ProductImage as linked list
	 * (with prev and next ProductImage).
	 */
	/**
	 * Serial number of image in product image list. Starts from 0.
	 */
	private Integer number;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	@ForeignKey(name = "fk_product_image_product_id")
	private Product product;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "image_id", nullable = false)
	@ForeignKey(name = "fk_product_image_image_id")
	private Image image;

	public ProductImage()
	{
		number = 0;
	}

	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}
}
