package seamshop.action.buyer;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.RequestParam;
import seamshop.consts.Spring;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.model.Order;
import seamshop.model.OrderItem;

@Component("buyerOrderAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})
@SuppressWarnings("serial")
// TODO: Almost duplication: Refactor: Extract generic class. See "action.member.OrderAction".
public class OrderAction extends AbstractBuyerAction
{
	public static final String RESULT_LIST = "list";
	public static final String RESULT_ITEMS = "items";

	// TODO: Rename to "orderId"? (maybe)
	private Long id;

	private Order order = null;
	private List<Order> orders;
	private List<OrderItem> orderItems;

	@Override
	public void validate()
	{
		super.validate();
	}

	@SkipValidation
	@Override
	public String execute()
	{
		log.debug("call execute()");

		if (id != null)
		{
			return view();
		}
		else
		{
			return list();
		}
	}

	/**
	 * Lists orders placed by current user (buyer).
	 */
	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		Long orderCount = orderDao.countPlacedByMe();
		if (orderCount > 0)
		{
			orders = orderDao.getPagePlacedByMe();
			getPager().setAllResults(orderCount.intValue());
			getPager().setResults(orders.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	/**
	 * Prints detail info about order and its items.
	 */
	@AllowedMethod
	@SkipValidation
	public String view()
	{
		log.debug("call view()");
		log.debug("id: " + id);

		order = orderDao.getByIdPlacedByMe(id);
		initOrderItems();

		return SUCCESS;
	}

	/**
	 * Lists order items placed by current user (buyer).
	 */
	@AllowedMethod
	@SkipValidation
	public String items()
	{
		log.debug("call items()");

		initOrderItems();

		return RESULT_ITEMS;
	}

	private void initOrderItems()
	{
		log.debug("call initOrderItems()");

		Long orderItemsCount = orderItemDao.countByOrderIdPlacedByMe(id);
		if (orderItemsCount > 0)
		{
			orderItems = orderItemDao.getPageByOrderIdPlacedByMe(id);
			getPager().setAllResults(orderItemsCount.intValue());
			getPager().setResults(orderItems.size());
			getPager().setUrl(getPagerUrl());
			getPager().setParameter(RequestParam.ID, id);
		}
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Order getOrder()
	{
		return order;
	}

	public List<Order> getOrders()
	{
		return orders;
	}

	public List<OrderItem> getOrderItems()
	{
		return orderItems;
	}
}
