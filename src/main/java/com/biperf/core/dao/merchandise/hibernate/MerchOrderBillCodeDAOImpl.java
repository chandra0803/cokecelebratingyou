/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.merchandise.MerchOrderBillCodeDAO;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;

public class MerchOrderBillCodeDAOImpl extends BaseDAO implements MerchOrderBillCodeDAO
{
  private static final Log log = LogFactory.getLog( MerchOrderDAOImpl.class );

  public MerchOrderBillCode getMerchOrderBillCodes( Long merchOrderId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getMerchOrderBillCodes" );

    query.setParameter( "merchOrderId", merchOrderId );

    return (MerchOrderBillCode)query.uniqueResult();
  }

  public MerchOrderBillCode getMerchOrderBillCodesByGiftCode( String giftCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.merchandise.getMerchOrderBillCodesByGiftCode" );

    query.setParameter( "giftCode", giftCode );

    return (MerchOrderBillCode)query.uniqueResult();
  }
}
