
package com.biperf.core.service.awardbanq.impl;

import java.io.Serializable;

public class AwardbanqGiftCodeRequest implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String programNumber;
  private int noOfGiftCodes;
  private long valueOfGiftCodes;
  private long batchId;

  public AwardbanqGiftCodeRequest()
  {

  }

  public AwardbanqGiftCodeRequest( String programNumber, int noOfGiftCodes, long valueOfGiftCodes, long batchId )
  {
    this.programNumber = programNumber;
    this.noOfGiftCodes = noOfGiftCodes;
    this.valueOfGiftCodes = valueOfGiftCodes;
    this.batchId = batchId;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public int getNoOfGiftCodes()
  {
    return noOfGiftCodes;
  }

  public void setNoOfGiftCodes( int noOfGiftCodes )
  {
    this.noOfGiftCodes = noOfGiftCodes;
  }

  public long getValueOfGiftCodes()
  {
    return valueOfGiftCodes;
  }

  public void setValueOfGiftCodes( long valueOfGiftCodes )
  {
    this.valueOfGiftCodes = valueOfGiftCodes;
  }

  public long getBatchId()
  {
    return batchId;
  }

  public void setBatchId( long batchId )
  {
    this.batchId = batchId;
  }

}
