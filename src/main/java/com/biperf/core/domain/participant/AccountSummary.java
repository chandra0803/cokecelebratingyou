/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/AccountSummary.java,v $
 */

package com.biperf.core.domain.participant;

import java.util.List;

import com.biperf.core.utils.NumberFormatUtil;

/**
 * AccountSummary.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AccountSummary
{
  private String accountNumber;
  private long beginningBalance;
  private long earnedThisPeriod;
  private long redeemedThisPeriod;
  private long adjustmentsThisPeriod;
  private long pendingOrder;
  private long endingBalance;

  private int errCode;

  private List accountTransactions;

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public void setAccountNumber( String accountNumber )
  {
    this.accountNumber = accountNumber;
  }

  public long getAdjustmentsThisPeriod()
  {
    return adjustmentsThisPeriod;
  }

  public void setAdjustmentsThisPeriod( long adjustmentsThisPeriod )
  {
    this.adjustmentsThisPeriod = adjustmentsThisPeriod;
  }

  public long getBeginningBalance()
  {
    return beginningBalance;
  }

  public void setBeginningBalance( long beginningBalance )
  {
    this.beginningBalance = beginningBalance;
  }

  public long getEarnedThisPeriod()
  {
    return earnedThisPeriod;
  }

  public void setEarnedThisPeriod( long earnedThisPeriod )
  {
    this.earnedThisPeriod = earnedThisPeriod;
  }

  public long getEndingBalance()
  {
    return endingBalance;
  }

  public void setEndingBalance( long endingBalance )
  {
    this.endingBalance = endingBalance;
  }

  public long getPendingOrder()
  {
    return pendingOrder;
  }

  public void setPendingOrder( long pendingOrder )
  {
    this.pendingOrder = pendingOrder;
  }

  public long getRedeemedThisPeriod()
  {
    return redeemedThisPeriod;
  }

  public void setRedeemedThisPeriod( long redeemedThisPeriod )
  {
    this.redeemedThisPeriod = redeemedThisPeriod;
  }

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "AccountSummary" );
    buf.append( "{accountNumber=" ).append( accountNumber );
    buf.append( ",beginningBalance=" ).append( beginningBalance );
    buf.append( ",earnedThisPeriod=" ).append( earnedThisPeriod );
    buf.append( ",redeemedThisPeriod=" ).append( redeemedThisPeriod );
    buf.append( ",adjustmentsThisPeriod=" ).append( adjustmentsThisPeriod );
    buf.append( ",pendingOrder=" ).append( pendingOrder );
    buf.append( ",endingBalance=" ).append( endingBalance );
    buf.append( '}' );
    return buf.toString();
  }

  public List getAccountTransactions()
  {
    return accountTransactions;
  }

  public void setAccountTransactions( List accountTransactions )
  {
    this.accountTransactions = accountTransactions;
  }

  public String getDisplayBeginningBalance()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( beginningBalance );
  }

  public String getDisplayEarnedThisPeriod()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( earnedThisPeriod );
  }

  public String getDisplayRedeemedThisPeriod()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( redeemedThisPeriod );
  }

  public String getDisplayAdjustmentsThisPeriod()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( adjustmentsThisPeriod );
  }

  public String getDisplayPendingOrder()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( pendingOrder );
  }

  public String getDisplayEndingBalance()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( endingBalance );
  }

}
