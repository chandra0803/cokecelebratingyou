
package com.biperf.core.service.ssi;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ssi.SSIContestFileUploadValue;

/**
 * 
 * SSIContestFileUploadService.
 * 
 * @author chowdhur
 * @since Dec 16, 2014
 */
public interface SSIContestFileUploadService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiContestFileUploadService";

  public SSIContestFileUploadValue uploadContestDocument( SSIContestFileUploadValue data ) throws ServiceErrorException;

  public boolean deleteMediaFromWebdav( String filePath );

  public void moveFileToWevdavADC( SSIContestFileUploadValue data ) throws ServiceErrorException;

}
