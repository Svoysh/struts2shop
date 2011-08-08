package seamshop.action.member;

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

@Component("sellerOrderAction")
@Scope(Spring.ACTION_SCOPE)
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})
@SuppressWarnings("serial")
// TODO: Duplication: Refactor: Extract generic class. See "action.buyer.OrderAction".
public class OrderAction extends AbstractMemberAction
{
	public static final String RESULT_LIST = "list";
	public static final String RESULT_ITEMS = "items";

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
		log.debug("id: " + id);

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
	 * Lists orders placed for current user (seller).
	 */
	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		Long orderCount = orderDao.countPlacedForMe();
		if (orderCount > 0)
		{
			orders = orderDao.getPagePlacedForMe();
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

		order = orderDao.getByIdPlacedForMe(id);
		initOrderItems();

		return SUCCESS;
	}

	/**
	 * Lists order items placed for current user (seller).
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

		Long orderItemsCount = orderItemDao.countByOrderIdPlacedForMe(id);
		if (orderItemsCount > 0)
		{
			orderItems = orderItemDao.getPageByOrderIdPlacedForMe(id);
			getPager().setAllResults(orderItemsCount.intValue());
			getPager().setResults(orderItems.size());
			getPager().setUrl(getPagerUrl());
			getPager().setParameter(RequestParam.ID, id);
		}
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
