package com.biperf.core.service.client.impl;


import com.biperf.core.dao.client.TcccConvertCertDAO;
import com.biperf.core.service.client.TcccConvertCertService;
import com.biperf.core.value.client.TcccConvertCertValueBean;

/**
 * 
 * MedtronicConvertCertServiceImpl.
 * 
 * @author bethke
 * @since Dec 3, 2013
 */
public class TcccConvertCertServiceImpl implements TcccConvertCertService
{

  /** MedtronicConvertCertDAO */
  private TcccConvertCertDAO tcccConvertCertDAO;
 
  /**
   * 
   * @return costCenter
   */
  public TcccConvertCertValueBean getCostCenterFromQCardBatch(String certNumber)
  {
    return tcccConvertCertDAO.getCostCenterFromQCardBatch(certNumber);
  }
  
  public void setTcccConvertCertDAO( TcccConvertCertDAO tcccConvertCertDAO )
  {
    this.tcccConvertCertDAO = tcccConvertCertDAO;
  }

}
