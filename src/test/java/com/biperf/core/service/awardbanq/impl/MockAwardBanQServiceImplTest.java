/*
 File: MockAwardBanQServiceImplTest.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      May 24, 2005   1.0      Created
 
 */

package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.integration.MockAccountTransactionDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.AccountTransaction;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.utils.MockContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * @author crosenquest
 */
public class MockAwardBanQServiceImplTest extends MockObjectTestCase
{
  private Date today = new Date();
  private Date fourDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 4 );
  private Date fiveDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 5 );
  private Date tenDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 10 );
  private Date twentyDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 20 );
  private Date thirtyDaysAgo = new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30 );

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public MockAwardBanQServiceImplTest( String test )
  {
    super( test );
  }

  /** MockAwardBanQServiceImpl */
  private MockAwardBanQServiceImpl awardBanQService = new MockAwardBanQServiceImpl();

  /** mockParticipantDAO */
  private Mock mockParticipantDAO = null;

  /** mock mockAccountTransactionDAO */
  private Mock mockMockAccountTransactionDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockParticipantDAO = new Mock( ParticipantDAO.class );
    mockMockAccountTransactionDAO = new Mock( MockAccountTransactionDAO.class );
    awardBanQService.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );
    awardBanQService.setMockAccountTransactionDAO( (MockAccountTransactionDAO)mockMockAccountTransactionDAO.proxy() );
    // Prepares the pickListFactory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }
  }

  /**
   * Test getting the awardBanQ balance for a participant by id.
   */
  public void testGetAccountBalanceForParticipantId()
  {
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( participant.getId() ) ).will( returnValue( participant ) );

    mockMockAccountTransactionDAO.expects( once() ).method( "getAccountBalance" ).with( same( participant.getAwardBanqNumber() ) ).will( returnValue( new Long( 2000 ) ) );

    // The service does the work of integrating with awardBanQ
    Long awardBanQBalance = awardBanQService.getAccountBalanceForParticipantId( participant.getId() );

    assertNotNull( "Balance is null when it isn't supposed to be.", awardBanQBalance );

    assertEquals( awardBanQBalance, new Long( 2000 ) );

    mockParticipantDAO.verify();

  }

  /**
   * Testing getting the account summary for a participant by id.
   */
  public void testGetAccountSummaryByParticipantIdAndDateRange()
  {
    Participant participant = ParticipantServiceImplTest.buildStaticParticipant();
    List expectedAccountTransactions = new ArrayList();
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 100, "deposit", thirtyDaysAgo, 100 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 20, "withdrawl", thirtyDaysAgo, 80 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 10, "deposit", twentyDaysAgo, 90 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 40, "withdrawl", twentyDaysAgo, 50 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 5, "withdrawl", tenDaysAgo, 45 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 30, "adjustment", tenDaysAgo, 75 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 5, "deposit", fiveDaysAgo, 80 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 11, "withdrawl", fiveDaysAgo, 69 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", -9, "adjustment", fourDaysAgo, 60 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 35, "deposit", fourDaysAgo, 95 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 5, "pending", today, 90 ) );
    expectedAccountTransactions.add( buildAccountTransaction( "test_account", 10, "pending", today, 80 ) );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( participant.getId() ) ).will( returnValue( participant ) );

    mockMockAccountTransactionDAO.expects( once() ).method( "getAccountTransactionsByAccountNumberWithRange" ).with( same( participant.getAwardBanqNumber() ), same( thirtyDaysAgo ), same( today ) )
        .will( returnValue( expectedAccountTransactions ) );

    AccountSummary accountSummary = awardBanQService.getAccountSummaryByParticipantIdAndDateRange( participant.getId(), thirtyDaysAgo, today );

    assertEquals( "Exected beginning balance of 100 when it isn't.", accountSummary.getBeginningBalance(), 100 );
    assertEquals( "Exected earnedThisPeriod of 150 when it isn't.", accountSummary.getEarnedThisPeriod(), 150 );
    assertEquals( "Exected redeemedThisPeriod of 76 when it isn't.", accountSummary.getRedeemedThisPeriod(), 76 );
    assertEquals( "Exected adjustmentsThisPeriod of 21 when it isn't.", accountSummary.getAdjustmentsThisPeriod(), 21 );
    assertEquals( "Exected pendingOrder of 15 when it isn't.", accountSummary.getPendingOrder(), 15 );
    assertEquals( "Exected endingBalance of 80 when it isn't.", accountSummary.getEndingBalance(), 80 );

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
