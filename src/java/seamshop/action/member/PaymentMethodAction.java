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
import seamshop.dao.PaymentMethodDao;
import seamshop.dto.Currency;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.PaymentMethod;
import seamshop.model.Shop;

import com.opensymphony.xwork2.Preparable;

/**
 * @author Alex Siman 2009-11-09
 */
/*
 * TODO: Refactor: Duplication: Extract abstract superclass?
 *       See [seamshop.action.member.ShippingMethodAction]
 */
@Component("memberPaymentMethodAction")
@Scope(Spring.ACTION_SCOPE)

@Results({
	@Result(
		name = RESULT_REDIRECT_LIST,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "payment-method!list"}
	),
	@Result(
		name = RESULT_REDIRECT_VIEW,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "payment-method!view", RequestParam.ID, "${id}"}
	),
	@Result(
		name = RESULT_REDIRECT_ADD,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "payment-method!add", RequestParam.ID, "${id}"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})

@SuppressWarnings("serial")
public class PaymentMethodAction extends AbstractHasShopMemberAction<PaymentMethod, Long>
	implements Preparable
{
	private PaymentMethod paymentMethod = new PaymentMethod();
	private List<PaymentMethod> paymentMethods = null;

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
			paymentMethod = paymentMethodDao.getMyPaymentMethodById(getId());
			shopId = paymentMethod.getShop().getId();

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

		Long paymentMethodCount = paymentMethodDao.countMyPaymentMethods();
		if (paymentMethodCount > 0)
		{
			paymentMethods = paymentMethodDao.getPageOfMyPaymentMethods();
			getPager().setAllResults(paymentMethodCount.intValue());
			getPager().setResults(paymentMethods.size());
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
		log.debug("paymentMethod.name (before saving): " + paymentMethod.getName());

		saveOrUpdate();

		log.debug("paymentMethod.name (after saving): " + paymentMethod.getName());

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to newly created payment method.
		addActionMessage("Payment method <strong>" + escape(paymentMethod.getName()) + "</strong> saved successfully.");

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
			// Use just created payment method instance as base form new payment method.
			// And remove payment method ID, such as new payment method must has its own new ID.
//			paymentMethod.setId(null);

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

		// TODO: Update paymentMethod fields from inputs or try prepare() (load paymentMethod before to update its values from input by Struts).
		saveOrUpdate();

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to updated paymentMethod.
		addActionMessage("Payment method <strong>" + escape(paymentMethod.getName()) + "</strong> updated successfully.");

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

		String nameOfDeletedPaymentMethod = paymentMethod.getName();

		// TODO: Delete paymentMethod, its products, etc.

		// TODO: i18n: Externalize message.
		addActionMessage("TODO: Payment method <strong>" + escape(nameOfDeletedPaymentMethod) + "</strong> deleted successfully.");

		return RESULT_REDIRECT_LIST;
	}

	@Override
	protected Shop getCurrentShop()
	{
		return paymentMethod.getShop();
	}

	@Override
	protected void setChangedShop(Shop changedShop)
	{
		paymentMethod.setShop(changedShop);
	}

	@Override
	protected void addOrEdit()
	{
		super.addOrEdit();
		if (paymentMethod != null)
		{
			description = paymentMethod.getHtmlDescription();
		}
	}

	@Override
	protected void saveOrUpdate()
	{
		paymentMethod.setDescription(description);
		super.saveOrUpdate();
	}

	public Collection<Currency> getCurrencies()
	{
		return currencyCollection.getCollection();
	}

	public PaymentMethod getPaymentMethod()
	{
		return paymentMethod;
	}

	public List<PaymentMethod> getPaymentMethods()
	{
		return paymentMethods;
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
	protected PaymentMethod getEntity()
	{
		return paymentMethod;
	}

	@Override
	protected void setEntity(PaymentMethod entity)
	{
		paymentMethod = entity;
	}
}
