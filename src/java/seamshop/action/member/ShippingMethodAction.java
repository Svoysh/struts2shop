package seamshop.action.member;

import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_ADD;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_LIST;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_VIEW;

import java.util.Collection;
import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.RequestParam;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.dao.ShippingMethodDao;
import seamshop.dto.Currency;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.ShippingMethod;
import seamshop.model.Shop;

import com.opensymphony.xwork2.Preparable;

/**
 * @author Alex Siman 2009-11-05
 */
/*
 * TODO: Refactor: Duplication: Extract abstract superclass?
 *       See [seamshop.action.member.PaymentMethodAction]
 */
@Component("memberShippingMethodAction")
@Scope(Spring.ACTION_SCOPE)

@Results({
	@Result(
		name = RESULT_REDIRECT_LIST,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shipping-method!list"}
	),
	@Result(
		name = RESULT_REDIRECT_VIEW,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shipping-method!view", RequestParam.ID, "${id}"}
	),
	@Result(
		name = RESULT_REDIRECT_ADD,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shipping-method!add", RequestParam.ID, "${id}"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})

@SuppressWarnings("serial")
public class ShippingMethodAction extends AbstractHasShopMemberAction<ShippingMethod, Long>
	implements Preparable
{
	private ShippingMethod shippingMethod = new ShippingMethod();
	private List<ShippingMethod> shippingMethods = null;

	private String description;

	@Override
	public void validate()
	{
		log.debug("call validate()");

		super.validate();
	}

	public void prepare()
	{
		log.debug("call prepare(); id=" + getId());

		if (getId() != null)
		{
			shippingMethod = shippingMethodDao.getMyShippingMethodById(getId());
			shopId = shippingMethod.getShop().getId();

			// TODO: Detach entity, if method is "saveAdd".
		}
	}

	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("call execute(); id=" + getId());

		if (getId() != null)
		{
			return view();
		}
		else
		{
			return list();
		}
	}

	@AllowedMethod
	@SkipValidation
	public String list()
	{
		log.debug("call list()");

		Long shippingMethodCount = shippingMethodDao.countMyShippingMethods();
		if (shippingMethodCount > 0)
		{
			shippingMethods = shippingMethodDao.getPageOfMyShippingMethods();
			getPager().setAllResults(shippingMethodCount.intValue());
			getPager().setResults(shippingMethods.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	@AllowedMethod
	@SkipValidation
	public String view()
	{
		log.debug("call view()");

		return SUCCESS;
	}

	@AllowedMethod
	@SkipValidation
	public String add()
	{
		log.debug("call add()");

		addOrEdit();

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String save()
	{
		log.debug("call save()");
		log.debug("shippingMethod.name (before saving): " + shippingMethod.getName());

		saveOrUpdate();

		log.debug("shippingMethod.name (after saving): " + shippingMethod.getName());

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to newly created shipping method.
		addActionMessage("Shipping method <strong>" + escape(shippingMethod.getName()) + "</strong> saved successfully.");

		return RESULT_REDIRECT_VIEW;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String saveAdd()
	{
		log.debug("call saveAdd()");

		String saveResult = save();
		if (RESULT_REDIRECT_VIEW.equals(saveResult))
		{
			// Use just created shipping method instance as base form new shipping method.
			// And remove shipping method ID, such as new shipping method must has its own new ID.
//			shippingMethod.setId(null);

			return RESULT_REDIRECT_ADD;
		}

		return saveResult;
	}

	@AllowedMethod
	@SkipValidation
	public String edit()
	{
		log.debug("call edit()");

		addOrEdit();

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String update()
	{
		log.debug("call update()");

		// TODO: Update shippingMethod fields from inputs or try prepare() (load shippingMethod before to update its values from input by Struts).
		saveOrUpdate();

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to updated shippingMethod.
		addActionMessage("Shipping method <strong>" + escape(shippingMethod.getName()) + "</strong> updated successfully.");

		return RESULT_REDIRECT_VIEW;
	}

	@AllowedMethod
	@SkipValidation
	public String deleteConfirm()
	{
		log.debug("call deleteConfirm()");
		return RESULT_DELETE;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	public String delete()
	{
		log.debug("call delete()");

		String nameOfDeletedShippingMethod = shippingMethod.getName();

		// TODO: Delete shippingMethod, its products, etc.

		// TODO: i18n: Externalize message.
		addActionMessage("TODO: Shipping method <strong>" + escape(nameOfDeletedShippingMethod) + "</strong> deleted successfully.");

		return RESULT_REDIRECT_LIST;
	}

	@Override
	protected Shop getCurrentShop()
	{
		return shippingMethod.getShop();
	}

	@Override
	protected void setChangedShop(Shop changedShop)
	{
		shippingMethod.setShop(changedShop);
	}

	@Override
	protected void addOrEdit()
	{
		super.addOrEdit();
		if (shippingMethod != null)
		{
			description = shippingMethod.getHtmlDescription();
		}
	}

	@Override
	protected void saveOrUpdate()
	{
		shippingMethod.setDescription(description);
		super.saveOrUpdate();
	}

	public Collection<Currency> getCurrencies()
	{
		return currencyCollection.getCollection();
	}

	public ShippingMethod getShippingMethod()
	{
		return shippingMethod;
	}

	public List<ShippingMethod> getShippingMethods()
	{
		return shippingMethods;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Override
	protected ShippingMethod getEntity()
	{
		return shippingMethod;
	}

	@Override
	protected void setEntity(ShippingMethod entity)
	{
		shippingMethod = entity;
	}
}
