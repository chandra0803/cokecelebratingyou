/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/merchorder/impl/MerchOrderServiceImpl.java,v $
 */

package com.biperf.core.service.merchorder.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.merchandise.MerchOrderBillCodeDAO;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.service.merchorder.MerchOrderBillCodeService;

public class MerchOrderBillCodeServiceImpl implements MerchOrderBillCodeService
{
  private static final Log logger = LogFactory.getLog( MerchOrderServiceImpl.class );

  private MerchOrderBillCodeDAO merchOrderBillCodeDAO;

  public MerchOrderBillCode getMerchOrderBillCodes( Long merchOrderId )
  {
    return merchOrderBillCodeDAO.getMerchOrderBillCodes( merchOrderId );
  }

  public MerchOrderBillCode getMerchOrderBillCodesByGiftCode( String giftCode )
  {
    return merchOrderBillCodeDAO.getMerchOrderBillCodesByGiftCode( giftCode );
  }

  public MerchOrderBillCodeDAO getMerchOrderBillCodeDAO()
  {
    return merchOrderBillCodeDAO;
  }

  public void setMerchOrderBillCodeDAO( MerchOrderBillCodeDAO merchOrderBillCodeDAO )
  {
    this.merchOrderBillCodeDAO = merchOrderBillCodeDAO;
  }

}
