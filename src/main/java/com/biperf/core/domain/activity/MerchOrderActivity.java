/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/MerchOrderActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.merchandise.MerchOrder;

public class MerchOrderActivity extends RecognitionActivity implements MerchandiseActivity
{
  private MerchOrder merchOrder;

  public MerchOrderActivity()
  {
    // empty constructor
  }

  /**
   * Constructs a <code>MerchOrderActivity</code> object.
   * 
   * @param guid this object's unique business identifier.
   */
  public MerchOrderActivity( String guid )
  {
    super( guid );
  }

  public MerchOrder getMerchOrder()
  {
    return merchOrder;
  }

  public void setMerchOrder( MerchOrder merchOrder )
  {
    this.merchOrder = merchOrder;
  }
}
