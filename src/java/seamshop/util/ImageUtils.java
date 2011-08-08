package seamshop.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import seamshop.exception.ImageProcessingException;

// TODO: Refactor this class.
// TODO: Low: Rename to "ThumbnailMaker"? (y)
// TODO: Fix: Enhance: Shop logos and User avatars must be in png format
//       (or if uploaded file is png) to respect quality of logo.
public abstract class ImageUtils
{
	public static void createShopLogoS200(File inputImageFile)
		throws ImageProcessingException
	{
		int shopLogoSize = 200;
		createThumbnail(inputImageFile, inputImageFile.getParent() + "/s" +
			shopLogoSize, shopLogoSize, shopLogoSize);
	}

	public static void createSmallThumbnail(File inputImageFile)
		throws ImageProcessingException
	{
		createThumbnail(inputImageFile,
			inputImageFile.getParent() + getImageSmallDirectory(),
			getImageSmallWidth(),
			getImageSmallHeight());
	}

	public static void createMediumThumbnail(File inputImageFile)
		throws ImageProcessingException
	{
		createThumbnail(inputImageFile,
			inputImageFile.getParent() + getImageMediumDirectory(),
			getImageMediumWidth(),
			getImageMediumHeight());
	}

	public static void createLargeThumbnail(File inputImageFile)
		throws ImageProcessingException
	{
		createThumbnail(inputImageFile,
			inputImageFile.getParent() + getImageLargeDirectory(),
			getImageLargeWidth(),
			getImageLargeHeight());
	}

	public static void createThumbnail(File inputImageFile, String outputThumbPath,
		int maxThumbWidth, int maxThumbHeight) throws ImageProcessingException
	{
		new File(outputThumbPath).mkdirs();
		String thumbFileName = inputImageFile.getName() + "." +
			getImageFileExtention();

		File outputThumbFile = new File(outputThumbPath + "/" + thumbFileName);

		createThumbnail(inputImageFile, outputThumbFile,
			maxThumbWidth, maxThumbHeight);
	}

	public static void createThumbnail(File inputImageFile, File outputThumbFile,
		int maxThumbWidth, int maxThumbHeight) throws ImageProcessingException
	{
		int imageType = BufferedImage.TYPE_INT_RGB;
		String imageTypeStr = getImageType();
		if (!StringUtils.isNullOrEmpty(imageTypeStr))
		{
			if (imageTypeStr.equalsIgnoreCase("color"))
			{
				imageType = BufferedImage.TYPE_INT_RGB;
			}
			else if (imageTypeStr.equalsIgnoreCase("gray"))
			{
				imageType = BufferedImage.TYPE_BYTE_GRAY;
			}
		}

		int scalingHints = Image.SCALE_SMOOTH;
		String scalingAlgorithm = getImageScalingAlgorithm();
		if (!StringUtils.isNullOrEmpty(scalingAlgorithm))
		{
			if (scalingAlgorithm.equalsIgnoreCase("smooth"))
			{
				scalingHints = Image.SCALE_SMOOTH;
			}
			else if (scalingAlgorithm.equalsIgnoreCase("fast"))
			{
				scalingHints = Image.SCALE_FAST;
			}
		}

		String imageFormat = getImageFormat();

		createThumbnail(
			inputImageFile, outputThumbFile,
			maxThumbWidth, maxThumbHeight,
			imageFormat, imageType, scalingHints
		);
	}

	public static void createThumbnail(File inputImageFile, File outputThumbFile,
		int maxThumbWidth, int maxThumbHeight, String imageFormat, int imageType,
		int scalingHints) throws ImageProcessingException
	{
		try
		{
			BufferedImage bufImage = ImageIO.read(inputImageFile);

			// If image width and height < max thumb sizes -> leave original sizes alone.
			int thumbWidth = bufImage.getWidth();
			int thumbHeight = bufImage.getHeight();
			double ratio = (double) thumbHeight / (double) thumbWidth;

			if (thumbWidth > maxThumbWidth)
			{
				thumbWidth = maxThumbWidth;
				thumbHeight = (int) (thumbWidth * ratio);
			}

			if (thumbHeight > maxThumbHeight)
			{
				thumbHeight = maxThumbHeight;
				thumbWidth = (int) (thumbHeight / ratio);
			}

			// FIXME: "getScaledInstance()" is an evil,
			// http://weblogs.java.net/blog/chet/archive/2007/04/dont_use_getsca.html
			Image scaledImage = bufImage.getScaledInstance(
				thumbWidth, thumbHeight, scalingHints);
			BufferedImage bufThumb = new BufferedImage(
				thumbWidth, thumbHeight, imageType);
			Graphics2D graphics = bufThumb.createGraphics();
			graphics.drawImage(scaledImage, 0, 0, null);
			graphics.dispose();

			ImageIO.write(bufThumb, imageFormat, outputThumbFile);
		}
		catch (Throwable ex)
		{
			throw new ImageProcessingException(
				"Failed to create thumbnail for image \"" +
				inputImageFile.getAbsolutePath() + "\"", ex);
		}
	}

	/**
	 * Returns width of image file. <code>-1</code> if failed to calculate image width.
	 */
	public static int getWidth(File imageFile)
	{
		try
		{
			// TODO: Is it cost to create BufferedImage often? (test)
			BufferedImage bufImage = ImageIO.read(imageFile);
			return bufImage.getWidth();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * Returns height of image file. <code>-1</code> if failed to calculate image height.
	 */
	public static int getHeight(File imageFile)
	{
		try
		{
			// TODO: Is it cost to create BufferedImage often? (test)
			BufferedImage bufImage = ImageIO.read(imageFile);
			return bufImage.getHeight();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}

	// TODO: Refactor next methods. -------------------------------------------
	// TODO: Load from Spring as bean? (y)

	public static String getImageFormat()
	{
		return "jpg";
	}

	public static String getImageType()
	{
		return "color";
	}

	public static String getImageScalingAlgorithm()
	{
		return "smooth";
	}

	public static String getImageFileExtention()
	{
		return "jpg";
	}

	public static int getImageSmallWidth()
	{
		return 40;
	}

	public static int getImageSmallHeight()
	{
		return 40;
	}

	public static String getImageSmallDirectory()
	{
		return "/small";
	}

	public static int getImageMediumWidth()
	{
		return 100;
	}

	public static int getImageMediumHeight()
	{
		return 100;
	}

	public static String getImageMediumDirectory()
	{
		return "/medium";
	}

	public static int getImageLargeWidth()
	{
		return 300;
	}

	public static int getImageLargeHeight()
	{
		return 300;
	}

	public static String getImageLargeDirectory()
	{
		return "/large";
	}
}
