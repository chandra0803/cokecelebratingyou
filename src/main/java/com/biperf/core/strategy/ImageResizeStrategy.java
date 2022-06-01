
package com.biperf.core.strategy;

import java.awt.image.BufferedImage;

import com.biperf.core.exception.ServiceErrorException;

public interface ImageResizeStrategy extends Strategy
{
  public BufferedImage process( BufferedImage image, int targetWidth, int targetHeight ) throws ServiceErrorException;

  public BufferedImage process( BufferedImage image, int targetDimension ) throws ServiceErrorException;
}
