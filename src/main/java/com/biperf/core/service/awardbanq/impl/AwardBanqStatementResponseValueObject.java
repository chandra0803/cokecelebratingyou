
package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.List;

public class AwardBanqStatementResponseValueObject
{
  private String accountNbr;
  private int beginningBalance;
  private String beginningDate;
  private int currentAccountBalance;
  private int endingBalance;
  private String endingDate;
  private int pendingBalance;
  private int totalAdjusted;
  private int totalEarned;
  private int totalRedeemed;
  private List<StatementTransactions> transactions = new ArrayList<StatementTransactions>();
  private int errCode;
  private String errDescription;

  public String getAccountNbr()
  {
    return accountNbr;
  }

  public void setAccountNbr( String accountNbr )
  {
    this.accountNbr = accountNbr;
  }

  public int getBeginningBalance()
  {
    return beginningBalance;
  }

  public void setBeginningBalance( int beginningBalance )
  {
    this.beginningBalance = beginningBalance;
  }

  public String getBeginningDate()
  {
    return beginningDate;
  }

  public void setBeginningDate( String beginningDate )
  {
    this.beginningDate = beginningDate;
  }

  public int getCurrentAccountBalance()
  {
    return currentAccountBalance;
  }

  public void setCurrentAccountBalance( int currentAccountBalance )
  {
    this.currentAccountBalance = currentAccountBalance;
  }

  public int getEndingBalance()
  {
    return endingBalance;
  }

  public void setEndingBalance( int endingBalance )
  {
    this.endingBalance = endingBalance;
  }

  public String getEndingDate()
  {
    return endingDate;
  }

  public void setEndingDate( String endingDate )
  {
    this.endingDate = endingDate;
  }

  public int getPendingBalance()
  {
    return pendingBalance;
  }

  public void setPendingBalance( int pendingBalance )
  {
    this.pendingBalance = pendingBalance;
  }

  public int getTotalAdjusted()
  {
    return totalAdjusted;
  }

  public void setTotalAdjusted( int totalAdjusted )
  {
    this.totalAdjusted = totalAdjusted;
  }

  public int getTotalEarned()
  {
    return totalEarned;
  }

  public void setTotalEarned( int totalEarned )
  {
    this.totalEarned = totalEarned;
  }

  public int getTotalRedeemed()
  {
    return totalRedeemed;
  }

  public void setTotalRedeemed( int totalRedeemed )
  {
    this.totalRedeemed = totalRedeemed;
  }

  public List<StatementTransactions> getTransactions()
  {
    return transactions;
  }

  public void setTransactions( List<StatementTransactions> transactions )
  {
    this.transactions = transactions;
  }

  public String getErrDescription()
  {
    return errDescription;
  }

  public void setErrDescription( String errDescription )
  {
    this.errDescription = errDescription;
  }

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

}
