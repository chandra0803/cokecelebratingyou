/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/integration/hibernate/MockAccountTransactionDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.integration.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.integration.MockAccountTransactionDAO;
import com.biperf.core.dao.participant.hibernate.UserDAOImpl;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.AccountTransaction;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * MockAccountTransactionDAOImpl <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public class MockAccountTransactionDAOImpl extends BaseDAO implements MockAccountTransactionDAO
{
  private static final Log log = LogFactory.getLog( UserDAOImpl.class );

  /**
   * Get a list of all account transactions.
   * 
   * @param accountNumber
   * @return List
   */
  public List getAccountTransactionsByAccountNumber( String accountNumber )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.participant.AccountTransaction.AllXactions" ).setString( "accountNumber", accountNumber ).list();
  }

  /**
   * Get a list of account transactions within the supplied range.
   * 
   * @param accountNumber
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getAccountTransactionsByAccountNumberWithRange( String accountNumber, Date startDate, Date endDate )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.participant.AccountTransaction.XactionsWithinRange" ).setString( "accountNumber", accountNumber ).setDate( "startDate", startDate )
        .setDate( "endDate", endDate ).list();
  }

  /**
   * Get the current balance of the specified account.
   * 
   * @param accountNumber
   * @return long
   */
  public Long getAccountBalance( String accountNumber )
  {
    // Account balance doesn't really hold the balance. instead add up tx amounts to produce
    // balance.
    // Long balance = (Long)getSession().getNamedQuery(
    // "com.biperf.core.domain.participant.AccountTransaction.MockAccountBalance" )
    // .setString( "accountNumber", accountNumber ).uniqueResult();
    long balance = 0;
    List accountTransactionsByAccountNumber = getAccountTransactionsByAccountNumber( accountNumber );
    for ( Iterator iter = accountTransactionsByAccountNumber.iterator(); iter.hasNext(); )
    {
      AccountTransaction accountTransaction = (AccountTransaction)iter.next();
      if ( accountTransaction.getTransactionType().equals( "deposit" ) || accountTransaction.getTransactionType().equals( "reverse" ) )

      {
        balance += accountTransaction.getTransactionAmount();
      }
      else if ( accountTransaction.getTransactionType().equals( "withdrawal" ) )
      {
        balance -= accountTransaction.getTransactionAmount();
      }
      else
      {
        log.error( "Unknown mock bank transaction type: " + accountTransaction.getTransactionType() );
      }
    }
    return new Long( balance );
    // return balance;
  }

  /**
   * Save the journal entry to the AwardsBanq
   * 
   * @param journal
   * @return Journal
   */
  public Journal saveJournalEntryToAwardsBanq( Journal journal )
  {
    Long oldBalanceLong = getAccountBalance( journal.getAccountNumber() );
    long oldBalance = oldBalanceLong != null ? oldBalanceLong.longValue() : 0;

    long newBalance = oldBalance + journal.getTransactionAmount().longValue();

    AccountTransaction accountTransaction = new AccountTransaction();
    accountTransaction.setAccountNumber( journal.getAccountNumber() );
    accountTransaction.setBalance( newBalance );
    accountTransaction.setTransactionAmount( journal.getTransactionAmount().longValue() );
    accountTransaction.setTransactionDate( journal.getTransactionDate() );
    accountTransaction.setTransactionDescription( journal.getTransactionDescription() );
    accountTransaction.setTransactionType( journal.getTransactionType().getCode() );

    accountTransaction = (AccountTransaction)HibernateUtil.saveOrUpdateOrShallowMerge( accountTransaction );

    return journal;
  }

}
