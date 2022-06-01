
package com.biperf.core.domain.activity;

import com.biperf.core.domain.merchandise.MerchOrder;

public class SweepstakesMerchLevelActivity extends SweepstakesActivity implements MerchandiseActivity
{
  private MerchOrder merchOrder;

  public SweepstakesMerchLevelActivity()
  {
    // empty constructor
  }

  public SweepstakesMerchLevelActivity( String guid )
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
