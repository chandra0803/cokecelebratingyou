package com.biperf.core.dao.client.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.TcccConvertCertDAO;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.TcccConvertCertValueBean;

/**
 * 
 * MedtronicConvertCertDAOImpl.
 * 
 * @author bethke
 * @since Dec 3, 2013
 */
public class TcccConvertCertDAOImpl extends BaseDAO implements TcccConvertCertDAO
{
  
  private DataSource dataSource;


  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
  
  /**
   * 
   * @return costCenter
   */
  public TcccConvertCertValueBean getCostCenterFromQCardBatch(String certNumber)
  {
    AwbqCertCostCtrSP proc = new AwbqCertCostCtrSP( dataSource );
    Map outParams = (Map)proc.executeProcedure(certNumber, UserManager.getUserId());
    
    String billingCode1 = (String)outParams.get( "p_out_billing_code_1" );
    String billingCode2 = (String)outParams.get( "p_out_billing_code_2" );
    
    TcccConvertCertValueBean bean = new TcccConvertCertValueBean();
    bean.setBillingCode1(billingCode1);
    bean.setBillingCode2( billingCode2 );

    return bean;
  }
  
  
  
}