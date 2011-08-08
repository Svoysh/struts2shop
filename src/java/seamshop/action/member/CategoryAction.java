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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import seamshop.consts.Interceptor;
import seamshop.consts.RequestParam;
import seamshop.consts.ResultType;
import seamshop.consts.Spring;
import seamshop.dto.Currency;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Category;

import com.opensymphony.xwork2.Preparable;

@Component("memberCategoryAction")
@Scope(Spring.ACTION_SCOPE)

@Results({
	@Result(
		name = RESULT_REDIRECT_LIST,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "category!list"}
	),
	@Result(
		name = RESULT_REDIRECT_VIEW,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "category!view", RequestParam.ID, "${id}"}
	),
	@Result(
		name = RESULT_REDIRECT_ADD,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "category!add", RequestParam.ID, "${id}"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})

@SuppressWarnings("serial")
public class CategoryAction extends AbstractCrudMemberAction<Category, Long>
	implements Preparable
{
	private Category category = new Category();
	private List<Category> categories = null;

	@Override
	public void validate()
	{
		log.debug("---------------- validate()");

		super.validate();
	}

	public void prepare()
	{
		log.debug("---------------- prepare(); id=" + getId());

		if (getId() != null)
		{
			category = categoryDao.getMyCategoryById(getId());
			// TODO: Detach entity, if method is "saveAdd".
		}
	}

	@SkipValidation
	@Override
	public String execute() throws Exception
	{
		log.debug("---------------- execute(); id=" + getId());

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
		log.debug("---------------- list()");

		Long categoryCount = categoryDao.countMyCategories();
		if (categoryCount > 0)
		{
			categories = categoryDao.getPageOfMyCategories();
			getPager().setAllResults(categoryCount.intValue());
			getPager().setResults(categories.size());
			getPager().setUrl(getPagerUrl());
		}

		return RESULT_LIST;
	}

	@AllowedMethod
	@SkipValidation
	public String view()
	{
		log.debug("---------------- view()");

		return SUCCESS;
	}

	@AllowedMethod
	@SkipValidation
	public String add()
	{
		log.debug("---------------- add()");

//		category = new Category();

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String save()
	{
		log.debug("---------------- save()");
		log.debug("category.name (before saving): " + category.getName());

		category.setUser(getCurrentUser());

		saveOrUpdate();

		log.debug("category.name (after saving): " + category.getName());

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to newly created category.
		addActionMessage("Category <strong>" + escape(category.getName()) + "</strong> saved successfully.");

		return RESULT_REDIRECT_VIEW;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String saveAdd()
	{
		log.debug("---------------- saveAdd()");

		String saveResult = save();
		if (RESULT_REDIRECT_VIEW.equals(saveResult))
		{
			// Use just created category instance as base form new category.
			// And remove category ID, such as new category must has its own new ID.
//			category.setId(null);

			return RESULT_REDIRECT_ADD;
		}

		return saveResult;
	}

	@AllowedMethod
	@SkipValidation
	public String edit()
	{
		log.debug("---------------- edit()");

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String update()
	{
		log.debug("---------------- update()");

		// TODO: Update category fields from inputs or try prepare() (load category before to update its values from input by Struts).
		saveOrUpdate();

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to updated category.
		addActionMessage("Category <strong>" + escape(category.getName()) + "</strong> updated successfully.");

		return RESULT_REDIRECT_VIEW;
	}

	@AllowedMethod
	@SkipValidation
	public String deleteConfirm()
	{
		log.debug("---------------- deleteConfirm()");
		return RESULT_DELETE;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	@SkipValidation
	public String delete()
	{
		log.debug("---------------- delete()");

		String nameOfDeletedCategory = category.getName();

		// TODO: Delete category, its products, etc.

		// TODO: i18n: Externalize message.
		addActionMessage("TODO: Category <strong>" + escape(nameOfDeletedCategory) + "</strong> deleted successfully.");

		return RESULT_REDIRECT_LIST;
	}

	@Override
	protected void saveOrUpdate()
	{
		super.saveOrUpdate();
	}

	public Collection<Currency> getCurrencies()
	{
		return currencyCollection.getCollection();
	}

	public Category getCategory()
	{
		return category;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	@Override
	protected Category getEntity()
	{
		return category;
	}

	@Override
	protected void setEntity(Category entity)
	{
		category = entity;
	}
}
