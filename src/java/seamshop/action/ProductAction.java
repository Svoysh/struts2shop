package seamshop.action;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.Product;
import seamshop.model.Shop;

@Component
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.GUEST_STACK)
})
@SuppressWarnings("serial")
public class ProductAction extends AbstractGuestAction
{
	public static final String RESULT_VIEW = "view";

	// TODO: Rename to "id"? (n)
	private Long productId;

	private Product product = null;
	private Shop shop = null;

	@SkipValidation
	@Override
	public String execute()
	{
		// TODO: Impl.
		return SUCCESS;
	}

	/**
	 * View product detail info by product ID.
	 */
	@AllowedMethod
	@SkipValidation
	public String view()
	{
		if (productId == null)
		{
			log.debug("'productId' is null");
			// TODO: i18n: Externalize message.
			addActionError("Such product does not exist.");
			return ERROR;
		}

		product = productDao.getModelById(productId);
		if (product == null)
		{
			log.debug("Product not fount by productId=" + productId);
			// TODO: i18n: Externalize message.
			addActionError("Such product does not exist or was removed.");
			return ERROR;
		}

		shop = shopDao.get(product.getShop().getId());

		return RESULT_VIEW;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Product getProduct()
	{
		return product;
	}

	public Shop getShop()
	{
		return shop;
	}
}
