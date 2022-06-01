/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/journal/hibernate/JournalDAOImpl.java,v $
 */

package com.biperf.core.dao.journal.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/*
 * JournalDAOImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul 14, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class JournalDAOImpl extends BaseDAO implements JournalDAO
{
  /**
   * Deletes the specified journal.
   *
   * @param journal  the journal to delete.
   */
  public void deleteJournal( Journal journal )
  {
    getSession().delete( journal );
  }

  /**
   * Returns a list of journals that meet the specified criteria.
   * 
   * @param queryConstraint specifies the criteria that the returned journals meet.
   * @return a list of journals that meet the specified criteria, as a <code>List</code> of
   *         {@link Journal} objects.
   */
  public List<Journal> getJournalList( JournalQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectList( queryConstraint );
  }

  /**
   * Save the journal entry.
   * 
   * @param journal
   * @return Journal
   */
  public Journal saveJournalEntry( Journal journal )
  {
    return (Journal)HibernateUtil.saveOrUpdateOrShallowMerge( journal );
  }

  /**
   * Get the journal entry by id
   * 
   * @param id
   * @return Journal
   */
  public Journal getJournalById( Long id )
  {
    return (Journal)getSession().get( Journal.class, id );
  }

  public void excecuteOnReversal( Long journalId )
  {
    Journal journal = (Journal)getSession().get( Journal.class, journalId );
    journal.setReversal( true );
    saveJournalEntry( journal );
  }

  /**
   * Get the journal entry by id Overridden from
   * 
   * @see com.biperf.core.dao.journal.JournalDAO#getJournalsByClaimIdAndUserId(java.lang.Long,
   *      java.lang.Long)
   * @param claimId
   * @param userId
   * @return List
   */
  public List getJournalsByClaimIdAndUserId( Long claimId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.JournalsByClaimIdAndUserId" ).setLong( "claimId", claimId.longValue() ).setLong( "userId", userId.longValue() );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.journal.JournalDAO#getTotalEarningsByMediaTypeAndUserId(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param mediaType
   * @return Long totalEarnings
   */
  public Long getTotalEarningsByMediaTypeAndUserId( Long userId, String mediaType )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.TotalEarningsByMediaTypeAndUserId" );
    query.setParameter( "mediaType", mediaType );
    query.setParameter( "userId", userId );

    return (Long)query.uniqueResult();
  }

  /**
   * Get a list of userIds from Journal where awardType matches value passed and transaction date is
   * within a given range
   * 
   * @param awardType
   * @param startDate
   * @param endDate
   * @return List
   */
  public List getUserIdsByAwardTypeWithinRange( String awardType, String startDate, String endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.JournalUserIdsByAwardTypeForTimePeriod" );

    query.setParameter( "awardType", awardType );
    query.setParameter( "startDate", startDate );
    query.setParameter( "endDate", endDate );

    return query.list();
  }

  /**
   * Overridden from @see com.biperf.core.dao.journal.JournalDAO#getReverseJournalsByJournalId(java.lang.Long, java.lang.Long)
   * @param journalId
   * @param promotionId
   * @return count value of int
   */
  public int getReverseJournalsByJournalId( Long journalId, Long promotionId )
  {

    StringBuffer sb = null;
    if ( journalId != null )
    {
      sb = new StringBuffer( "" );
      sb.append( "%%" );
      sb.append( journalId.toString() );

    }
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( Journal.class, "journal" );
    criteria.setProjection( Projections.rowCount() );

    if ( promotionId != null )
    {
      criteria.add( Restrictions.eq( "journal.promotion.id", promotionId ) );
    }

    criteria.add( Restrictions.eq( "journal.journalStatusType", JournalStatusType.lookup( JournalStatusType.POST ) ) );
    if ( sb != null )
    {
      criteria.add( Restrictions.like( "journal.transactionDescription", sb.toString() ) );
    }
    List list = criteria.list();

    return ( (Long)list.get( 0 ) ).intValue();

  }

  /**
   * returns the total earnings for this claim (uses journal entries)
   * 
   * @param claimId
   * @param promotionId
   * @param userId
   * @return Long
   */
  public Long getEarningsByClaimIdAndUserId( Long claimId, Long promotionId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.JournalsByPromotionIdClaimIdAndUserId" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );

    return (Long)query.uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.journal.JournalDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.ParticipantPayoutComplete" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "userId", userId );
    return query.list().size() > 0;
  }

  /**
   * Get the journal entry by id Overridden from
   * 
   * @see com.biperf.core.dao.journal.JournalDAO#getJournalsByClaimId(java.lang.Long)
   * @param claimId
   * @param userId
   * @return List
   */
  public List<Journal> getJournalsByClaimId( Long claimId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.JournalsByClaimId" ).setLong( "claimId", claimId.longValue() );

    List<Journal> journals = query.list();
    return journals;
  }

  public Long getAwardAmountByClaimIdByUserId( Long claimId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.AwardAmountByClaimIdByUserId" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );

    return (Long)query.uniqueResult();
  }

  @Override
  public Long getJournalIdForReversedClaim( Long claimId, Long userId, Long approvalRound )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.journal.JournalIDForReversedClaim" );

    query.setParameter( "claimId", claimId );
    query.setParameter( "userId", userId );
    query.setParameter( "approvalRound", approvalRound );

    return (Long)query.uniqueResult();
  }

}
