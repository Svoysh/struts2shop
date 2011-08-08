package seamshop.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * @author Alex Siman 2008-12-24
 */
@Entity
@SuppressWarnings("serial")
public class ProductCategory extends AbstractIdBasedEntity
{
	// TODO: Use productID + categoryID as primary key?

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	@ForeignKey(name = "fk_product_category_product_id")
	private Product product;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "category_id", nullable = false)
	@ForeignKey(name = "fk_product_category_category_id")
	private Category category;

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}
}
