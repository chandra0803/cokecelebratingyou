
package com.biperf.core.service.merchorder;

import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.service.SAO;

public interface MerchOrderBillCodeService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "merchOrderBillCodeService";

  /**
   * returns MerchOrderBillCode objects 
   * 
   * @param giftCode
   * @return MerchOrderBillCode
   */
  public MerchOrderBillCode getMerchOrderBillCodes( Long merchOrderId );

  public MerchOrderBillCode getMerchOrderBillCodesByGiftCode( String giftCode );

}
