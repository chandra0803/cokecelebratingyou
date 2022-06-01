
package com.biperf.core.strategy;

import java.awt.image.BufferedImage;

import com.biperf.core.exception.ServiceErrorException;

public interface ImageCropStrategy extends Strategy
{
  public BufferedImage process( BufferedImage image, int targetWidth, int targetHeight ) throws ServiceErrorException;

  public byte[] process( byte[] image, int targetWidth, int targetHeight ) throws ServiceErrorException;
}
