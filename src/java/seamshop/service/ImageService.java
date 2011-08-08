package seamshop.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import seamshop.model.Image;
import seamshop.util.FileFinder;

/**
 * @author Alex Siman 2009-09-10
 */
@Component
public class ImageService extends AbstractService
{
	public File saveUploadedProductImageFile(File tempImageFile, Image savedImage)
		throws IOException
	{
		return saveUploadedImageFile(tempImageFile, savedImage, "/products");
	}

	public File saveUploadedShopLogoFile(File tempImageFile, Image savedImage)
		throws IOException
	{
		return saveUploadedImageFile(tempImageFile, savedImage, "/shop-logos");
	}

	/**
	 * @return saved image file.
	 * @throws IOException
	 */
	public File saveUploadedImageFile(File tempImageFile, Image savedImage,
		String imageSubdir) throws IOException
	{
		File saveFile = null;
		if ((tempImageFile!= null) && (savedImage != null))
		{
			Long imageId = savedImage.getId();
			if (imageId == null)
			{
				// If image ID is null then (maybe) image entity was not saved.
				return saveFile;
			}

			// Create directory for uploaded image.
			String imageDirPath = FileFinder.makeWebappSubdir(
				"/uploads/images" + imageSubdir + "/" + imageId).getPath();

			saveFile = new File(imageDirPath + "/" + savedImage.getFileName());
			FileUtils.moveFile(tempImageFile, saveFile);
		}

		return saveFile;
	}
}
