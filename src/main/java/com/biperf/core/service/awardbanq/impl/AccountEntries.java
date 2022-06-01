
package com.biperf.core.service.awardbanq.impl;

public class AccountEntries
{
  private String key;
  private AwardBanqStatementResponseValueObject value;

  public AccountEntries()
  {

  }

  public AwardBanqStatementResponseValueObject getValue()
  {
    return value;
  }

  public void setValue( AwardBanqStatementResponseValueObject value )
  {
    this.value = value;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }
}
