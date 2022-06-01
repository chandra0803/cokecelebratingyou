package com.biperf.core.service.client;

import com.biperf.core.service.SAO;
import com.biperf.core.value.client.TcccConvertCertValueBean;

/**
 * 
 * MedtronicConvertCertService.
 * 
 * @author bethke
 * @since Dec 3, 2013
 */
public interface TcccConvertCertService extends SAO
{

  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "tcccConvertCertService";

   /**
    * 
    * @return costCenter
    */
   public TcccConvertCertValueBean getCostCenterFromQCardBatch(String certNumber);
}