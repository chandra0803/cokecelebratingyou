/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;

public interface MerchOrderBillCodeDAO extends DAO
{

  public static final String BEAN_NAME = "merchOrderBillCodeDAO";

  public MerchOrderBillCode getMerchOrderBillCodes( Long merchOrderId );

  public MerchOrderBillCode getMerchOrderBillCodesByGiftCode( String giftCode );

}
