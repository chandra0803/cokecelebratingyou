/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/journal/JournalDAO.java,v $
 */

package com.biperf.core.dao.journal;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.journal.Journal;

/*
 * JournalDAO <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 14, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public interface JournalDAO extends DAO
{
  public static final String BEAN_NAME = "journalDAO";

  /**
   * Deletes the specified journal.
   *
   * @param journal  the journal to delete.
   */
  public void deleteJournal( Journal journal );

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint );

  /**
   * Save the journal entry.
   * 
   * @param journal
   * @return Journal
   */
  public Journal saveJournalEntry( Journal journal );

  /**
   * Get the journal entry by id
   * 
   * @param id
   * @return Journal
   */
  public Journal getJournalById( Long id );

  /**
   * Get the journal entry by claim id
   * 
   * @param claimId
   * @param userId
   * @return List of Journal objects
   */
  public List getJournalsByClaimIdAndUserId( Long claimId, Long userId );

  /**
   * Get the total earning for the specific media type
   * 
   * @param userId
   * @param mediaType
   * @return Long total earnings for that media type.
   */
  public Long getTotalEarningsByMediaTypeAndUserId( Long userId, String mediaType );

  /**
   * Get a list of userIds from Journal where awardType matches value passed and transaction date is
   * within a given range
   * 
   * @param awardType
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByAwardTypeWithinRange( String awardType, String startDate, String endDate );

  /**
   * @param journalId
   * @param promotionId
   * @return count value of int
   */
  public int getReverseJournalsByJournalId( Long journalId, Long promotionId );

  public void excecuteOnReversal( Long journalId );

  /**
   * returns the total earnings for this claim (uses journal entries)
   * 
   * @param claimId
   * @param promotionId
   * @param userId
   * @return Long
   */
  public Long getEarningsByClaimIdAndUserId( Long claimId, Long promotionId, Long userId );

  /**
   *  
   * @see com.biperf.core.dao.journal.JournalDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId );

  /**
   * 
   * @see com.biperf.core.dao.journal.JournalDAO#getJournalsByClaimId(java.lang.Long)
   * @param claimId
   * @param userId
   * @return List
   */
  public List<Journal> getJournalsByClaimId( Long claimId );

  public Long getAwardAmountByClaimIdByUserId( Long claimId, Long userId );

  /**
   * Obtain just the journal ID for a reversed claim. This means the reversal must be posted.
   * @param claimId Claim ID of claim that was reversed
   * @param userId User ID of recipient
   * @return Null if no such journal entry, or the ID of the specified journal entry
   */
  public Long getJournalIdForReversedClaim( Long claimId, Long userId, Long approvalRound );

}
