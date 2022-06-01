
package com.biperf.core.service.awardbanq;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;

public class AwardBanqMerchResponseValueObject
{
  private List<MerchLevelValueObject> merchLevel = new ArrayList<MerchLevelValueObject>();
  private boolean merchlinqLevelReturned;
  private String type;
  private int errCode;
  private String errDescription;

  public List<MerchLevelValueObject> getMerchLevel()
  {
    return merchLevel;
  }

  public void setMerchLevel( List<MerchLevelValueObject> merchLevel )
  {
    this.merchLevel = merchLevel;
  }

  public boolean isMerchlinqLevelReturned()
  {
    return merchlinqLevelReturned;
  }

  public void setMerchlinqLevelReturned( boolean merchlinqLevelReturned )
  {
    this.merchlinqLevelReturned = merchlinqLevelReturned;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

  public String getErrDescription()
  {
    return errDescription;
  }

  public void setErrDescription( String errDescription )
  {
    this.errDescription = errDescription;
  }

}
