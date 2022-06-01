
package com.biperf.core.service.journal;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * JournalService.
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
 * <td>jenniget</td>
 * <td>Sep 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface JournalService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "journalService";

  /**
   * Persists a Journal to the database via the JournalDAO
   * 
   * @param journalEntry the Journal to be persisted
   * @return the persisted {@link com.biperf.core.domain.journal.Journal} object.
   */
  public Journal saveJournalEntry( Journal journalEntry );

  /**
   * Persists a Journal to the database via the JournalDAO and also calls AwardBarqService to do a
   * deposit
   * 
   * @param journalEntry the Journal to be persisted and deposited
   * @return the persisted {@link com.biperf.core.domain.journal.Journal} object.
   */
  public Journal saveAndDepositJournalEntry( Journal journalEntry ) throws ServiceErrorException;

  /**
   * Calls AwardBarqService to do a deposit
   * 
   * @param journalEntry the Journal to be persisted and deposited
   * @param isRetriable true if failed attempts should be tried again
   * @return the persisted {@link com.biperf.core.domain.journal.Journal} object.
   */
  public Journal saveAndDepositJournalEntry( Journal journalEntry, boolean isRetriable ) throws ServiceErrorException;

  /**
   * Persists a Journal to the database via the JournalDAO and also calls AwardBarqService to do a
   * deposit
   * 
   * @param journalEntry the Journal to be persisted and deposited
   * @param isRetriable
   * @return the persisted {@link com.biperf.core.domain.journal.Journal} object.
   */
  public boolean depositJournalEntry( Journal journalEntry, boolean isRetriable ) throws ServiceErrorException;

  /**
   * Reverse the Journal
   * 
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal reverseJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException;

  /**
   * Approve the Journal
   * 
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal approveJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException;

  /**
   * Deny the Journal
   * 
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal denyJournal( Long journalId, String comments, String reasonCode );

  /**
   * Void the Journal
   * 
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal voidJournal( Long journalId, String comments, String reasonCode );

  /**
   * Post the Journal
   * 
   * @param journalId
   * @param comments
   * @param reasonCode
   * @return Journal
   */
  public Journal postJournal( Long journalId, String comments, String reasonCode ) throws ServiceErrorException;

  /**
   * Post the Journal
   * 
   * @param journal
   * @return Journal
   */
  public Journal postJournal( Journal journal ) throws ServiceErrorException;

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint );

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @param journalAssociationRequestCollection
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint, AssociationRequestCollection journalAssociationRequestCollection );

  /**
   * Gets the journal by ID
   * 
   * @param id
   * @return Journal
   */
  public Journal getJournalById( Long id );

  /**
   * Gets the Journal by Id with associations
   * 
   * @param id
   * @param associationRequestCollection
   * @return Journal
   */
  public Journal getJournalById( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets the journal by ID
   * 
   * @param claimId
   * @param userId
   * @param journalAssociationRequest
   * @return Journal
   */
  public List getJournalsByClaimIdAndUserId( Long claimId, Long userId, AssociationRequestCollection journalAssociationRequest );

  /**
   * @param journal
   * @param ratioMap
   * @return BigDecimal calculated awardQuantity
   */
  public BigDecimal getAwardQuantityEquivalent( Journal journal );

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
   * Deletes the specified journal.
   *
   * @param journal  the journal to delete.
   */
  public void deleteJournal( Journal journal );

  /**
   * @param journalId
   * @param promotionId
   * @return count value of int
   * 
   */
  public int getReverseJournalsByJournalId( Long journalId, Long promotionId );

  /**
   * @param journalId
   *  
   * @return void
   * 
   */
  public void excecuteOnReversal( Long journalId );

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
   * @param journalEntry
   */
  public void claimJournalPostProcess( Journal journalEntry ) throws ServiceErrorException;

  public Long getAwardAmountByClaimIdByUserId( Long claimId, Long userId );

  public Mailing buildSweepstakeMailing( Journal journal );

  public boolean isJournalFromSweepstakes( Journal journal );

  /**
   * Obtain just the journal ID for a reversed claim. This means the reversal must be posted.
   * @param claimId Claim ID of claim that was reversed
   * @param userId User ID of the recipient
   * @return Null if no such journal entry, or the ID of the specified journal entry
   */
  public Long getJournalIdForReversedClaim( Long claimId, Long userId, Long approvalRound );

  public void saveAndLaunchPointsDepositProcess( Journal journal, boolean retryEnabled ) throws ServiceErrorException; // bug 73458
  
  public void postDepositStep( Journal journalEntry ) throws ServiceErrorException; // bug 73458

  public boolean processPointsDepositJournal( Long journalId, boolean retriable ) throws ServiceErrorException; // bug 73458
  
  public boolean processDepositRetryJournal( Long journalId, boolean retriable ) throws ServiceErrorException; // bug 73458
  
  public void sendCustomRecognizedReceivedEmail( Claim claim );
}
