
package com.biperf.core.service.publicrecognitionwall;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.publicrecognitionwall.dto.PublicRecognitionWall;

public interface PublicRecognitionWallService extends SAO
{
  public static final String BEAN_NAME = "publicRecognitionWallService";

  /**
   * Return a list of public recognitions for a client, and data from Ehcache.
   *
   * 
   * @return PublicRecognitionWall
   */
  public PublicRecognitionWall getPublicRecognitionWall() throws ServiceErrorException;

  /**
   * Return a list of public recognitions through Quartz Process.
   *
   * 
   * @return PublicRecognitionWall
   */
  public PublicRecognitionWall refresh() throws ServiceErrorException;

  public boolean isPublicRecognitionWallFeedEnabled();

}
