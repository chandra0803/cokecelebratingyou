/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/integration/hibernate/MockAccountTransactionDAOImplTest.java,v $
 *
 */

package com.biperf.core.dao.integration.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.integration.MockAccountTransactionDAO;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.AccountTransaction;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * MockAccountTransactionDAOImplTest <p/> <b>Change History:</b><br>
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
 *
 */
public class MockAccountTransactionDAOImplTest extends BaseDAOTest
{
  private Date today = new Date();
  private Date fourDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 4 );
  private Date fiveDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5 );
  private Date tenDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 10 );
  private Date twentyDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 20 );
  private Date thirtyDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30 );
  private Date thirtyOneDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 31 );
  private Date oneHundredDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 100 );

  /**
   * Helper method to get the NodeDAO from the beanFactory.
   * 
   * @return NodeDAO
   */
  private MockAccountTransactionDAO getMockAccountTransactionDAO()
  {
    return (MockAccountTransactionDAO)ApplicationContextFactory.getApplicationContext().getBean( "mockAccountTransactionDAO" );
  }

  /**
   * Test dao method getAccountTransactionsByAccountNumber
   */
  public void testGetAccountTransactionsByAccountNumber()
  {
    List xactions = new ArrayList();
    xactions.add( buildAccountTransaction( "test_account", 100, "deposit", oneHundredDaysAgo, 100 ) );
    xactions.add( buildAccountTransaction( "test_account", 110, "deposit", thirtyDaysAgo, 110 ) );
    xactions.add( buildAccountTransaction( "test_account", 25, "withdrawl", twentyDaysAgo, 85 ) );
    xactions.add( buildAccountTransaction( "test_account", 25, "withdrawl", tenDaysAgo, 60 ) );
    xactions.add( buildAccountTransaction( "test_account", 10, "deposit", fiveDaysAgo, 70 ) );
    xactions.add( buildAccountTransaction( "test_account", 20, "withdrawl", today, 50 ) );

    for ( int i = 0; i < xactions.size(); i++ )
    {
      AccountTransaction accountTransaction = (AccountTransaction)xactions.get( i );
      HibernateSessionManager.getSession().saveOrUpdate( accountTransaction );
    }

    // Flush the session to save the transactions.
    HibernateSessionManager.getSession().flush();

    List accountList = getMockAccountTransactionDAO().getAccountTransactionsByAccountNumber( "test_account" );
    assertNotNull( accountList );
    assertTrue( accountList.containsAll( xactions ) );
  }

  /**
   * Test dao method getAccountTransactionsByAccountNumberWithRange
   */
  public void testGetAccountTransactionsByAccountNumberWithRange()
  {
    List xactions = new ArrayList();
    xactions.add( buildAccountTransaction( "test_account", 100, "deposit", oneHundredDaysAgo, 100 ) );
    xactions.add( buildAccountTransaction( "test_account", 110, "deposit", thirtyDaysAgo, 110 ) );
    xactions.add( buildAccountTransaction( "test_account", 25, "withdrawl", twentyDaysAgo, 85 ) );
    xactions.add( buildAccountTransaction( "test_account", 25, "withdrawl", tenDaysAgo, 60 ) );
    xactions.add( buildAccountTransaction( "test_account", 10, "deposit", fiveDaysAgo, 70 ) );
    xactions.add( buildAccountTransaction( "test_account", 20, "withdrawl", today, 50 ) );

    for ( int i = 0; i < xactions.size(); i++ )
    {
      AccountTransaction accountTransaction = (AccountTransaction)xactions.get( i );
      HibernateSessionManager.getSession().saveOrUpdate( accountTransaction );
    }

    // Flush the session to save the transactions.
    HibernateSessionManager.getSession().flush();
    List accountList = getMockAccountTransactionDAO().getAccountTransactionsByAccountNumberWithRange( "test_account", thirtyOneDaysAgo, fourDaysAgo );
    assertNotNull( accountList );
    assertFalse( accountList.containsAll( xactions ) );
    assertEquals( accountList.size(), 4 );
  }

  /**
   * test dao method getAccountBalance
   */
  public void testGetCurrentBalance()
  {
    HibernateSessionManager.getSession().saveOrUpdate( buildAccountTransaction( "test_account", 100, "deposit", thirtyDaysAgo, 100 ) );
    HibernateSessionManager.getSession().saveOrUpdate( buildAccountTransaction( "test_account", 25, "withdrawal", today, 50 ) );
    HibernateSessionManager.getSession().saveOrUpdate( buildAccountTransaction( "test_account", 25, "withdrawal", tenDaysAgo, 75 ) );

    // Flush the session to save the transactions.
    HibernateSessionManager.getSession().flush();

    Long balance = getMockAccountTransactionDAO().getAccountBalance( "test_account" );
    assertEquals( balance.longValue(), 50 );

  }

  public void testSaveJournalEntryToAwardsBanq()
  {
    // Create the journal entry.
    Journal expectedJournal = JournalDAOImplTest.buildAndSaveJournal( String.valueOf( System.currentTimeMillis() % 5503032 ) );

    getMockAccountTransactionDAO().saveJournalEntryToAwardsBanq( expectedJournal );
    flushAndClearSession();

    List accountList = getMockAccountTransactionDAO().getAccountTransactionsByAccountNumber( expectedJournal.getAccountNumber() );
    assertNotNull( accountList );
    assertEquals( accountList.size(), 1 );
  }

  private AccountTransaction buildAccountTransaction( String accountNumber, long amount, String type, Date date, long balance )
  {
    AccountTransaction xaction = new AccountTransaction();
    xaction.setAccountNumber( accountNumber );
    xaction.setTransactionAmount( amount );
    xaction.setTransactionType( type );
    xaction.setTransactionDescription( type );
    xaction.setTransactionDate( date );
    xaction.setBalance( balance );
    return xaction;
  }
}