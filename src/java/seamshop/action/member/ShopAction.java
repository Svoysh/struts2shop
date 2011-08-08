package seamshop.action.member;

import static seamshop.consts.I18nKey.*;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_ADD;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_LIST;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_VIEW;

import java.io.File;
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
import seamshop.dto.Country;
import seamshop.dto.Currency;
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Address;
import seamshop.model.Image;
import seamshop.model.Shop;
import seamshop.model.validation.UrlNameValidationResult;
import seamshop.util.CollectionUtils;
import seamshop.util.ImageUtils;

import com.opensymphony.xwork2.Preparable;

/*
 * TODO: Include common @SkipValidation methods (ex: list, edit,...) into "excludeMethods":
 * <interceptor-stack name="appDefaultStack">
 * ...
 *      <interceptor-ref name="validation">
 *           <param name="excludeMethods">input,back,cancel,browse,index,show,edit,editNew</param>
 *      </interceptor-ref>
 *      <interceptor-ref name="restWorkflow">
 *           <param name="excludeMethods">input,back,cancel,browse,index,show,edit,editNew</param>
 *      </interceptor-ref>
 * ...
 * </interceptor-stack>
 */

@Component("memberShopAction")
@Scope(Spring.ACTION_SCOPE)

@Results({
	@Result(
		name = RESULT_REDIRECT_LIST,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shop!list"}
	),
	@Result(
		name = RESULT_REDIRECT_VIEW,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shop!view", RequestParam.ID, "${id}"}
	),
	@Result(
		name = RESULT_REDIRECT_ADD,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "shop!add", RequestParam.ID, "${id}"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})

@SuppressWarnings("serial")
public class ShopAction extends AbstractCrudMemberAction<Shop, Long>
	implements Preparable
{
	protected static final String FIELD_SHOP_NAME = "shop.name";
	protected static final String FIELD_SHOP_URL_NAME = "shop.urlName";
	protected static final String FIELD_SHOP_COUNTRY = "shop.countryCode";
	protected static final String FIELD_SHOP_CURRENCY = "shop.currency";

	private Shop shop = new Shop();
	private List<Shop> shops = null;

	private String description;
	private boolean generateSummary = false;

	// TODO: Impl: Add to "save" and "update".
	private List<Address> addresses;

	/**
	 * Fields to contain file upload info. Must follow such Struts convention rules:
	 * <ul>
	 * <li>[File Name] : File - the actual File</li>
	 * <li>[File Name]ContentType : String - the content type of the file</li>
	 * <li>[File Name]FileName : String - the actual name of the file uploaded (not the HTML name)</li>
	 * </ul>
	 * Where [File Name] is the name of the HTML file input.
	 */
	private File logoFile;
	private String logoContentType;
	private String logoFileName;
	private boolean deleteLogo = false;

	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();

		log.debug("Cleaning up addresses from null elements");
		CollectionUtils.removeNulls(addresses);
	}

	@Override
	public void validate()
	{
		log.debug("call validate()");

		super.validate();

		// TODO: High: Unit test.
		if (shop.hasUrlName())
		{
			if (!shop.isValidUrlName())
			{
				UrlNameValidationResult result = shop.getResultOfUrlNameValidation();
				// TODO: Low: Set default msgkey? (y, ex: "URL name is invalid.")
				String validationMessageKey = null;
				if (result == UrlNameValidationResult.PRIMITIVE)
				{
					validationMessageKey = MSGKEY_ERROR_URL_NAME_PRIMITIVE;
				}
				else if (result == UrlNameValidationResult.INVALID_CHARS)
				{
					validationMessageKey = MSGKEY_ERROR_URL_NAME_INVALID_CHARS;
				}
				else if (result == UrlNameValidationResult.RESERVED)
				{
					validationMessageKey = MSGKEY_ERROR_URL_NAME_RESERVED;
				}

				addFieldError(FIELD_SHOP_URL_NAME,
					getTextErrorChooseAnotherValueWithReason(
						validationMessageKey));
			}
			else if (!shopDao.isUniqueByUrlName(shop.getUrlName(), shop.getId()))
			{
				addFieldError(FIELD_SHOP_URL_NAME,
					getTextErrorChooseAnotherValueWithReason(
						MSGKEY_ERROR_URL_NAME_ALREADY_TAKEN));
			}
		}

		if (!shop.isValidCountryCodeIfHas())
		{
			log.error("Country code is invalid: " + shop.getCountryCode());
			addFieldError(FIELD_SHOP_COUNTRY,
				getTextErrorChooseAnotherValueWithReason(
					MSGKEY_ERROR_INVALID_COUNTRY_CODE));
		}

		if (!shop.isValidCurrencyCodeIfHas())
		{
			log.error("Currency code is invalid: " + shop.getCurrencyCode());
			addFieldError(FIELD_SHOP_CURRENCY,
				getTextErrorChooseAnotherValueWithReason(
					MSGKEY_ERROR_INVALID_CURRENCY_CODE));
		}
	}

	public void prepare()
	{
		log.debug("call prepare(); id=" + getId());

		if (getId() != null)
		{
			shop = shopDao.getMyShopById(id);
			if ((shop != null) && isNewEntity())
			{
				// TODO: Detach entity, if method is "saveAdd".
				// TODO: High: Test this block and fix if needed.
				log.debug("Before detaching shop. ID = " + shop.getId());
				shopDao.detach(shop);
				log.debug("After detaching shop. ID = " + shop.getId());
			}
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

		Long shopCount = shopDao.countMyShops();
		if (shopCount > 0)
		{
			shops = shopDao.getPageOfMyShops();
			getPager().setAllResults(shopCount.intValue());
			getPager().setResults(shops.size());
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

		addOrEdit();

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String save()
	{
		log.debug("---------------- save()");

		shop.setUser(getCurrentUser());

		saveOrUpdate();

		// TODO: i18n: Externalize.
		// TODO: Enhance: Print link to newly created shop.
		addActionMessage("Shop <strong>" + escape(shop.getName()) + "</strong> saved successfully.");

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
			// Use just created shop instance as base form new shop.
			// And remove shop ID, such as new shop must has its own new ID.
//			shop.setId(null);

			return RESULT_REDIRECT_ADD;
		}

		return saveResult;
	}

	@AllowedMethod
	@SkipValidation
	public String edit()
	{
		log.debug("---------------- edit()");

		addOrEdit();

		return INPUT;
	}

	@AllowedMethod
	@Transactional(TransactionType.WRITE)
	public String update()
	{
		log.debug("---------------- update()");

		saveOrUpdate();

		// TODO: i18n: Externalize.
		// TODO: Enhance: Print link to updated shop.
		addActionMessage("Shop <strong>" + escape(shop.getName()) + "</strong> updated successfully.");

		return RESULT_REDIRECT_VIEW;
	}

	// TODO: Low: Rename to: "confirmDelete", "requestDelete", "beforeDelete"? (y)
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

		String nameOfDeletedShop = shop.getName();

		// TODO: Delete shop, its products, etc.

		// TODO: i18n: Externalize message.
		addActionMessage("TODO: Shop <strong>" + escape(nameOfDeletedShop) + "</strong> deleted successfully.");

		return RESULT_REDIRECT_LIST;
	}

	@Override
	protected void addOrEdit()
	{
		super.addOrEdit();

		// TODO: Low: Duplication on "description": See [seamshop.action.member.ProductAction]
		if (shop != null)
		{
			description = shop.getHtmlDescription();
		}
	}

	@Override
	protected void saveOrUpdate()
	{
		// TODO: Low: Duplication on "description": See [seamshop.action.member.ProductAction]
		shop.setDescription(description);
		shop.generateSummaryFromDescriptionIfSummaryIsEmpty(generateSummary);

		if (deleteLogo)
		{
			shop.setLogo(null);
		}

		Image oldLogo = shop.getLogo();
		Image newLogo = null;
		if (logoFile != null)
		{
			newLogo = new Image();
			newLogo.setUser(getCurrentUser());
			newLogo.setFileName(logoFileName);
			newLogo.setFileSize(logoFile.length());
			newLogo.setWidth(ImageUtils.getWidth(logoFile));
			newLogo.setHeight(ImageUtils.getHeight(logoFile));
			shop.setLogo(newLogo);
			deleteLogo = true;
		}

		if (deleteLogo && (oldLogo != null))
		{
			// TODO: Fix: Remove old logo's Image entity and its dependent associations.
			entityManager.remove(oldLogo);
		}

		super.saveOrUpdate();

		// TODO: Fix: Delete old logo's File after session flush.

		if (newLogo != null)
		{
			try
			{
				File saveFile = imageService.saveUploadedShopLogoFile(
					logoFile, newLogo);

				// Creating thumbs of uploaded image.
				// TODO: High: Create logos in all sizes: 40, 100, 200, 300 px.
				ImageUtils.createShopLogoS200(saveFile);
			}
			catch (Exception ex)
			{
				log.error("Failed to process uploaded image file: " + logoFile, ex);

				// TODO: i18n: Externalize.
				addActionError("Failed to upload shop logo \"" +
					escape(newLogo.getFileName()) + "\".");
			}
		}
	}

	public Collection<Currency> getCurrencies()
	{
		return currencyCollection.getCollection();
	}

	public Collection<Country> getCountries()
	{
		return countryCollection.getCollection();
	}

	public Shop getShop()
	{
		return shop;
	}

	public List<Shop> getShops()
	{
		return shops;
	}

	public List<Address> getAddresses()
	{
		return addresses;
	}

	public void setAddresses(List<Address> addresses)
	{
		this.addresses = addresses;
	}

	// NOTE: Do NOT rename setter, it follows Struts2 convention for file uploading.
	// See logoFile javadoc.
	public void setLogo(File logoFile)
	{
		this.logoFile = logoFile;
	}

	public void setLogoContentType(String logoContentType)
	{
		this.logoContentType = logoContentType;
	}

	public void setLogoFileName(String logoFileName)
	{
		this.logoFileName = logoFileName;
	}

	public void setDeleteLogo(boolean deleteLogo)
	{
		this.deleteLogo = deleteLogo;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isGenerateSummary()
	{
		return generateSummary;
	}

	public void setGenerateSummary(boolean generateSummary)
	{
		this.generateSummary = generateSummary;
	}

	@Override
	protected Shop getEntity()
	{
		return shop;
	}

	@Override
	protected void setEntity(Shop entity)
	{
		shop = entity;
	}
}
