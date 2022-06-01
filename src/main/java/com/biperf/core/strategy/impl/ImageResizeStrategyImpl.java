
package com.biperf.core.strategy.impl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;

public class ImageResizeStrategyImpl extends BaseStrategy implements ImageResizeStrategy
{
  private static final Object HINT = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
  private static final boolean IS_HIGHER_QUALITY = true;

  public BufferedImage process( BufferedImage image, int targetWidth, int targetHeight ) throws ServiceErrorException
  {
    if ( image.getWidth() < targetWidth || image.getHeight() < targetHeight )
    {
      return image;
    }

    return getScaledInstance( image, targetWidth, targetHeight, HINT, IS_HIGHER_QUALITY );
  }

  /**
     * @param img the original image to be scaled
     * @param targetDimension the desired dimension of the scaled image, in pixels
     * @return a scaled version of the original {@code BufferedImage}, which will maintain the original
     * ratio of height to width
     */
  public BufferedImage process( BufferedImage image, int targetDimension ) throws ServiceErrorException
  {
    int targetHeight = targetDimension;
    int targetWidth = targetDimension;
    double originalHeight = image.getHeight();
    double originalWidth = image.getWidth();

    if ( originalHeight >= originalWidth )
    {
      if ( originalHeight > targetDimension )
      {
        double scalePercentage = originalHeight / targetDimension;
        targetWidth = (int) ( originalWidth / scalePercentage );
      }
    }
    else
    {
      if ( originalWidth > targetDimension )
      {
        double scalePercentage = originalWidth / targetDimension;
        targetHeight = (int) ( originalHeight / scalePercentage );
      }
    }

    return process( image, targetWidth, targetHeight );
  }

  /**
   * 
   * AUTHOR : Chris Campbell
   * Chris Campbell is an engineer on the Java 2D Team at Sun Microsystems, 
   * working on OpenGL hardware acceleration and imaging related issues. 
   * 
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
  public static BufferedImage getScaledInstance( BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality )
  {
    int type = img.getTransparency() == Transparency.OPAQUE ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = (BufferedImage)img;
    int w, h;
    if ( higherQuality )
    {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    }
    else
    {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do
    {
      if ( higherQuality && w > targetWidth )
      {
        w /= 2;
        if ( w < targetWidth )
        {
          w = targetWidth;
        }
      }

      if ( higherQuality && h > targetHeight )
      {
        h /= 2;
        if ( h < targetHeight )
        {
          h = targetHeight;
        }
      }

      BufferedImage tmp = new BufferedImage( w, h, type );
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, hint );
      g2.drawImage( ret, 0, 0, w, h, null );
      g2.dispose();

      ret = tmp;
    }
    while ( w != targetWidth || h != targetHeight );

    return ret;
  }

}
