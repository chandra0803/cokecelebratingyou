
package com.biperf.core.service.awardbanq.impl;

import java.util.List;

public class AccountStatements
{
  private List<AccountEntries> entry;

  public AccountStatements()
  {

  }

  public List<AccountEntries> getEntry()
  {
    return entry;
  }

  public void setEntry( List<AccountEntries> entry )
  {
    this.entry = entry;
  }
}
