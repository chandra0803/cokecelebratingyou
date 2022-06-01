/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/AccountTransaction.java,v $
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;

/**
 * AccountTransaction.
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
public class AccountTransaction extends BaseDomain implements Serializable
{

  private String accountNumber;
  private Date transactionDate;
  private String transactionType;
  private long transactionAmount;
  private String transactionDescription;
  private long balance;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof AccountTransaction ) )
    {
      return false;
    }

    final AccountTransaction accountTransaction = (AccountTransaction)object;

    if ( getTransactionAmount() != accountTransaction.getTransactionAmount() )
    {
      return false;
    }
    if ( !getAccountNumber().equals( accountTransaction.getAccountNumber() ) )
    {
      return false;
    }
    if ( !getTransactionDate().equals( accountTransaction.getTransactionDate() ) )
    {
      return false;
    }
    if ( !getTransactionType().equals( accountTransaction.getTransactionType() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getTransactionDate().hashCode();
    result = 29 * result + (int) ( getTransactionAmount() ^ getTransactionAmount() >>> 32 );
    result = 29 * result + getTransactionType().hashCode();
    result = 29 * result + getAccountNumber().hashCode();
    return result;
  }

  public Date getTransactionDate()
  {
    return transactionDate;
  }

  public void setTransactionDate( Date transactionDate )
  {
    this.transactionDate = transactionDate;
  }

  public String getFormattedTransactionDate()
  {
    return DateUtils.toDisplayString( transactionDate );
  }

  public String getTransactionDescription()
  {
    return transactionDescription;
  }

  public void setTransactionDescription( String transactionDescription )
  {
    this.transactionDescription = transactionDescription;
  }

  public long getTransactionAmount()
  {
    return transactionAmount;
  }

  public String getFormattedTransactionAmount()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( transactionAmount );
  }

  public void setTransactionAmount( long transactionAmount )
  {
    this.transactionAmount = transactionAmount;
  }

  public long getBalance()
  {
    return balance;
  }

  public void setBalance( long balance )
  {
    this.balance = balance;
  }

  public String getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( String transactionType )
  {
    this.transactionType = transactionType;
  }

  public String getAccountNumber()
  {
    return accountNumber;
  }

  public void setAccountNumber( String accountNumber )
  {
    this.accountNumber = accountNumber;
  }

}
