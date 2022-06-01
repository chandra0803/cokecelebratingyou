package com.biperf.core.dao.client;

import com.biperf.core.dao.DAO;
import com.biperf.core.value.client.TcccConvertCertValueBean;

/**
 * 
 * MedtronicConvertCertDAO.
 * 
 * @author bethke
 * @since Dec 3, 2013
 */
public interface TcccConvertCertDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "tcccConvertCertDAO";
 
  /**
   * 
   * @return costCenter
   */
  public TcccConvertCertValueBean getCostCenterFromQCardBatch(String certNumber);
}
