
package com.biperf.core.service.awardbanq.impl;

public class AwardBanqMultiStatementResponseValueObject
{
  private String type;
  private int errCode;
  private String errDescription;
  private AccountStatements accountStatements;

  public AwardBanqMultiStatementResponseValueObject()
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

  public AccountStatements getAccountStatements()
  {
    return accountStatements;
  }

  public void setAccountStatements( AccountStatements accountStatements )
  {
    this.accountStatements = accountStatements;
  }

}
