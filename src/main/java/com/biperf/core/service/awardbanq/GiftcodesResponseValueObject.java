
package com.biperf.core.service.awardbanq;

import java.util.ArrayList;
import java.util.List;

public class GiftcodesResponseValueObject
{
  private String type;
  private int errCode;
  private String errDescription;
  private int noOfGiftCodes;
  private String programNumber;
  private int valueOfGiftCodes;
  private List<GiftCodes> giftCodes = new ArrayList<GiftCodes>();

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

  public int getNoOfGiftCodes()
  {
    return noOfGiftCodes;
  }

  public void setNoOfGiftCodes( int noOfGiftCodes )
  {
    this.noOfGiftCodes = noOfGiftCodes;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public int getValueOfGiftCodes()
  {
    return valueOfGiftCodes;
  }

  public void setValueOfGiftCodes( int valueOfGiftCodes )
  {
    this.valueOfGiftCodes = valueOfGiftCodes;
  }

  public List<GiftCodes> getGiftCodes()
  {
    return giftCodes;
  }

  public void setGiftCodes( List<GiftCodes> giftCodes )
  {
    this.giftCodes = giftCodes;
  }

}
