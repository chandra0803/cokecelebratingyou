/**
 * 
 */

package com.biperf.core.strategy;

import java.awt.image.BufferedImage;

import com.biperf.core.exception.ServiceErrorException;

/**
 * @author poddutur
 *
 */
public interface QuizImageResizeStrategy extends Strategy
{
  public BufferedImage process( BufferedImage image ) throws ServiceErrorException;
}
