/*
 * (c) 2012 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.promotion;

import java.util.List;

import com.biperf.awardbanq.databeans.account.IssuedOMGiftCode;

/**
 * PromoMerchProgramLevelGiftCodes.
 * Transformer object to hold bulk codes by program and level
 */
public class PromoMerchProgramLevelGiftCodes
{
  // load these first
  private String levelName;
  private long noOfGiftCodes;
  private String programId;

  // set these values based on OM call
  private List<IssuedOMGiftCode> giftCodes;
  private long maxValue;

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public long getNoOfGiftCodes()
  {
    return noOfGiftCodes;
  }

  public void setNoOfGiftCodes( long noOfGiftCodes )
  {
    this.noOfGiftCodes = noOfGiftCodes;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public List<IssuedOMGiftCode> getGiftCodes()
  {
    return giftCodes;
  }

  public void setGiftCodes( List<IssuedOMGiftCode> giftCodes )
  {
    this.giftCodes = giftCodes;
  }

  public long getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( long maxValue )
  {
    this.maxValue = maxValue;
  }

}
