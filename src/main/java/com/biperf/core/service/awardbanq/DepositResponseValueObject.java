
package com.biperf.core.service.awardbanq;

public class DepositResponseValueObject
{
  private int amount;
  private int errCode;
  private String errDescription;

  public DepositResponseValueObject()
  {

  }

  public int getAmount()
  {
    return amount;
  }

  public void setAmount( int amount )
  {
    this.amount = amount;
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
