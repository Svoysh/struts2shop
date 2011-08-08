package seamshop.action.member;

import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_ADD;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_LIST;
import static seamshop.action.member.AbstractCrudMemberAction.RESULT_REDIRECT_VIEW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
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
import seamshop.interceptor.method.AllowedMethod;
import seamshop.interceptor.transaction.TransactionType;
import seamshop.interceptor.transaction.Transactional;
import seamshop.model.Category;
import seamshop.model.Image;
import seamshop.model.Product;
import seamshop.model.ProductCategory;
import seamshop.model.ProductImage;
import seamshop.model.ProductVariant;
import seamshop.model.Shop;
import seamshop.util.CollectionUtils;
import seamshop.util.ImageUtils;

import com.opensymphony.xwork2.Preparable;

@Component("memberProductAction")
@Scope(Spring.ACTION_SCOPE)

@Results({
	@Result(
		name = RESULT_REDIRECT_LIST,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "product!list"}
	),
	@Result(
		name = RESULT_REDIRECT_VIEW,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "product!view", RequestParam.ID, "${id}"}
	),
	@Result(
		name = RESULT_REDIRECT_ADD,
		type = ResultType.REDIRECT_ACTION,
		params = {"actionName", "product!add", RequestParam.ID, "${id}"}
	)
})
@InterceptorRefs({
	@InterceptorRef(Interceptor.MEMBER_STACK)
})

@SuppressWarnings("serial")
public class ProductAction extends AbstractHasShopMemberAction<Product, Long>
	implements Preparable
{
	protected static final String FIELD_PRODUCT_NAME = "product.name";
//	protected static final String FIELD_PRODUCT_SHOP_ID = RequestParam.SHOP_ID;
//	protected static final String FIELD_IMAGES = "images";

	private Product product = new Product();
	private List<Product> products = null;

	private List<ProductVariant> variants;
	private List<Category> categories;
	private String description;
	private boolean generateSummary = false;

	/**
	 * Fields to contain file upload info. Must follow such Struts convention rules:
	 * <ul>
	 * <li>[File Name] : File - the actual File</li>
	 * <li>[File Name]ContentType : String - the content type of the file</li>
	 * <li>[File Name]FileName : String - the actual name of the file uploaded (not the HTML name)</li>
	 * </ul>
	 * Where [File Name] is the name of the HTML file input.
	 */
	private File[] images;
	private String[] imagesContentType;
	private String[] imagesFileName;

	private Map<File, Image> imageFilesAndImages = new HashMap<File, Image>();

	@Override
	public void prepare()
	{
		log.debug("---------------- prepare(); id=" + getId());

		/*
		 * TODO: Security: Is it save to preload ANY product by ID? (n)
		 * FIXME: Limit product selection by current user ID? (y)
		 */
		if (id != null)
		{
			// FIXME: productDao.getMyProductById(id);
			product = productDao.get(id);
			shopId = product.getShop().getId();

			// Find variants (IDs) to delete. It's any missed variant.
			variants = product.getProductVariants();

			// Find categories (IDs) to remove from product or delete at all
			// if it is not used anywhere. It's any missed category.
			// TODO: Impl the same as for variants.
//			categories = product.getCategories();

			// TODO: Detach entity, if method is "saveAdd".
		}
	}

	@Override
	public void doBeforeValidation()
	{
		super.doBeforeValidation();

		logIterableEntities();

		log.debug("Cleaning up variants and categories from null elements");
		CollectionUtils.removeNulls(variants);
		CollectionUtils.removeNulls(categories);

		logIterableEntities();
	}

	@Override
	public void validate()
	{
		log.debug("---------------- validate()");

		super.validate();
	}

	// TODO: Low: Delete: To test FormCopier
	private void logIterableEntities()
	{
		log.debug("Begin: Test: JS FormCopier.");

		if (!CollectionUtils.isNullOrEmpty(variants))
		{
			log.debug("Variants: " + variants.size());
			int i = 1;
			for (ProductVariant variant : variants)
			{
				log.debug("" + i + ") " + variant);
				i++;
			}
		}
		else
		{
			log.debug("Variants: 0");
		}

		if (!CollectionUtils.isNullOrEmpty(categories))
		{
			log.debug("Categories: " + categories.size());
			int i = 1;
			for (Category category : categories)
			{
				log.debug("" + i + ") " + category);
				i++;
			}
		}
		else
		{
			log.debug("Categories: 0");
		}
		log.debug("End: Test: JS FormCopier.");
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

		Long productCount = 0L;
		if (shopId != null)
		{
			productCount = productDao.countMyProductsByShopId(shopId);
			if (productCount != 0)
			{
				products = productDao.getPageOfMyProductsByShopId(shopId);
			}
		}
		else
		{
			productCount = productDao.countMyProducts();
			if (productCount != 0)
			{
				products = productDao.getPageOfMyProducts();
			}
		}

		if (productCount > 0)
		{
			getPager().setAllResults(productCount.intValue());
			getPager().setResults(products.size());
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

		//---------------------------------------------------------------------
		// Add categories.
		// FIXME: Add categories names which do not exist in DB created by this user only.
		if (!CollectionUtils.isNullOrEmpty(categories))
		{
			Set<ProductCategory> saveProductCategories = new HashSet<ProductCategory>();
			for (Category category : categories)
			{
				// TODO: Replace by Hibernate Category validator here.
				if (isBlank(category.getName()))
				{
					// Skip all categories without name.
					continue;
				}

				category.setUser(getCurrentUser());
				ProductCategory productCategory = new ProductCategory();
				productCategory.setProduct(product);
				productCategory.setCategory(category);

				saveProductCategories.add(productCategory);
			}

			if (!CollectionUtils.isNullOrEmpty(saveProductCategories))
			{
				product.setProductCategories(saveProductCategories);
			}
		}

		//---------------------------------------------------------------------
		// Add images to product.
		if (!ArrayUtils.isEmpty(images))
		{
			int imageNumber = 0;
			for (int i = 0; i < images.length; i++)
			{
				File imageFile = images[i];
				if (imageFile == null)
				{
					continue;
				}

				Image image = new Image();
				image.setUser(getCurrentUser());
				image.setFileName(imagesFileName[i]);
				image.setFileSize(imageFile.length());
				image.setWidth(ImageUtils.getWidth(imageFile));
				image.setHeight(ImageUtils.getHeight(imageFile));

				ProductImage productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setImage(image);
				productImage.setNumber(imageNumber);
				imageNumber++;

				product.getProductImages().add(productImage);
				imageFilesAndImages.put(imageFile, image);
			}
		}

		if (hasErrors())
		{
			return INPUT;
		}

		saveOrUpdate();

		// TODO: Low: Fix: What if product save/update failed?
		//       Save image files with UUID name to temp dir? (y)
		//       Maybe save uploaded file(s) before validation process?
		saveImageFiles();

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to newly created product.
		addActionMessage("Product <strong>" + escape(product.getName()) +
			"</strong> saved successfully.");

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

		// TODO: Validate categories, variants, etc.
		// TODO: Update variants, categories, images.
		// TODO: Delete variants, categories, images that were not found during update.
		// TODO: Update product fields from inputs or try prepare()
		//       (load product before to update its values from input by Struts).
		saveOrUpdate();

		// TODO: i18n: Externalize message.
		// TODO: Enhance: insert link to updated product.
		addActionMessage("Product <strong>" + escape(product.getName()) +
			"</strong> updated successfully.");

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

		String nameOfDeletedProduct = product.getName();

		// TODO: Delete product, its variants, categories (if orphans).

		// TODO: i18n: Externalize message.
		addActionMessage("TODO: Product <strong>" + escape(nameOfDeletedProduct) +
			"</strong> deleted successfully.");

		return RESULT_REDIRECT_LIST;
	}

	@Override
	protected Shop getCurrentShop()
	{
		return product.getShop();
	}

	@Override
	protected void setChangedShop(Shop changedShop)
	{
		product.setShop(changedShop);
	}

	@Override
	protected void addOrEdit()
	{
		super.addOrEdit();

		// TODO: Low: Duplication on "description": See [seamshop.action.member.ShopAction]
		if (product != null)
		{
			description = product.getHtmlDescription();
		}
	}

	@Override
	protected void saveOrUpdate()
	{
		// TODO: Low: Duplication on "description": See [seamshop.action.member.ShopAction]
		product.setDescription(description);
		product.generateSummaryFromDescriptionIfSummaryIsEmpty(generateSummary);

		//---------------------------------------------------------------------
		// Variants.
		// TODO: High: Delete variants that not found on update.
		List<ProductVariant> saveVariants = new ArrayList<ProductVariant>();
		if (!isEmpty(variants))
		{
			for (ProductVariant variant : variants)
			{
				// TODO: Or empty variant name is error? (y)
				// TODO: Do this check in validator.
				if (isBlank(variant.getName()))
				{
					// Skip all variants without name.
					continue;
				}
				variant.setProduct(product);
				saveVariants.add(variant);
			}
		}
		product.setProductVariants(saveVariants);

		super.saveOrUpdate();
	}

	private void saveImageFiles()
	{
		if (ArrayUtils.isEmpty(images))
		{
			return;
		}
		for (File imageFile : images)
		{
			if (imageFile == null)
			{
				continue;
			}

			Image image = imageFilesAndImages.get(imageFile);
			if (image == null)
			{
				continue;
			}

			try
			{
				File saveFile = imageService.saveUploadedProductImageFile(
					imageFile, image);

				// Creating thumbs of uploaded image.
				ImageUtils.createSmallThumbnail(saveFile);
				ImageUtils.createMediumThumbnail(saveFile);
				ImageUtils.createLargeThumbnail(saveFile);
			}
			catch (Exception ex)
			{
				log.error("Failed to process uploaded image file: " + imageFile, ex);

				// TODO: i18n: Externalize.
				addActionError("Failed to upload product image \"" +
					escape(image.getFileName()) + "\".");
			}
		}
	}

	public Product getProduct()
	{
		return product;
	}

	public List<Product> getProducts()
	{
		return products;
	}

	public List<ProductVariant> getVariants()
	{
		return variants;
	}

	public void setVariants(List<ProductVariant> variants)
	{
		this.variants = variants;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
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

	public void setImages(File[] images)
	{
		this.images = images;
	}

	public void setImagesContentType(String[] imagesContentType)
	{
		this.imagesContentType = imagesContentType;
	}

	public void setImagesFileName(String[] imagesFileName)
	{
		this.imagesFileName = imagesFileName;
	}

	@Override
	protected Product getEntity()
	{
		return product;
	}

	@Override
	protected void setEntity(Product entity)
	{
		product = entity;
	}
}
