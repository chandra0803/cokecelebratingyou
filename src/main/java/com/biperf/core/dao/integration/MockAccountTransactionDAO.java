/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/integration/MockAccountTransactionDAO.java,v $
 *
 */

package com.biperf.core.dao.integration;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.journal.Journal;

/**
 * MockAccountTransactionDAO - Mock out the AwardsBanQ. <p/> <b>Change History:</b><br>
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
public interface MockAccountTransactionDAO extends DAO
{
  /**
   * Get a list of all account transactions.
   * 
   * @param accountNumber
   * @return List
   */
  public List getAccountTransactionsByAccountNumber( String accountNumber );

  /**
   * Get a list of account transactions within the supplied range.
   * 
   * @param accountNumber
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getAccountTransactionsByAccountNumberWithRange( String accountNumber, Date startDate, Date endDate );

  /**
   * Get the current balance of the specified account.
   * 
   * @param accountNumber
   * @return Long
   */
  public Long getAccountBalance( String accountNumber );

  /**
   * Save the journal entry to the AwardsBanq
   * 
   * @param journal
   * @return Journal
   */
  public Journal saveJournalEntryToAwardsBanq( Journal journal );
}
