
package com.biperf.core.service.awardbanq;

public class GiftcodeStatusResponseValueObject
{
  private String type;
  private int errCode;
  private String errDescription;
  private int balanceAvailable;
  private String giftCode;
  private String status;
  private String issueDate;
  private String referenceNumber;
  private String expireDate;

  public GiftcodeStatusResponseValueObject()
  {

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

  public int getBalanceAvailable()
  {
    return balanceAvailable;
  }

  public void setBalanceAvailable( int balanceAvailable )
  {
    this.balanceAvailable = balanceAvailable;
  }

  public String getGiftCode()
  {
    return giftCode;
  }

  public void setGiftCode( String giftCode )
  {
    this.giftCode = giftCode;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate( String issueDate )
  {
    this.issueDate = issueDate;
  }

  public String getReferenceNumber()
  {
    return referenceNumber;
  }

  public void setReferenceNumber( String referenceNumber )
  {
    this.referenceNumber = referenceNumber;
  }

  public String getExpireDate()
  {
    return expireDate;
  }

  public void setExpireDate( String expireDate )
  {
    this.expireDate = expireDate;
  }

}
