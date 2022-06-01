
package com.biperf.core.dao.challengepoint.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ChallengepointAwardDAOImpl.
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
 * <td>Prabu Babu</td>
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointAwardDAOImpl extends BaseDAO implements ChallengepointAwardDAO
{

  /**
   * Get the challengepointAward of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return challengepointAward
   */
  public ChallengepointAward getChallengepointAwardByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    ChallengepointAward challengepointAward = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.ChallengepointAwardByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    List challengepointAwardList = query.list();

    if ( challengepointAwardList != null && challengepointAwardList.size() > 0 )
    {
      challengepointAward = (ChallengepointAward)challengepointAwardList.get( 0 );
    }

    return challengepointAward;
  }

  /**
   * Get the  Basic Award of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return Basic Award
   */
  public ChallengepointAward getBasicAwardByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    ChallengepointAward challengepointAward = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.BasicAwardByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    List challengepointAwardList = query.list();

    if ( challengepointAwardList != null && challengepointAwardList.size() > 0 )
    {
      challengepointAward = (ChallengepointAward)challengepointAwardList.get( 0 );
    }

    return challengepointAward;
  }

  /**
   * Get the  Sum of Basic Award of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return Basic Award
   */
  public Long getSumBasicAwardByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    ChallengepointAward challengepointAward = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.SumBasicAwardByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    Long sumBasicAward = (Long)query.uniqueResult();
    if ( sumBasicAward != null )
    {
      return sumBasicAward;
    }
    return 0L;
  }

  /**
   * Get the challengepointAward of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return challengepointAward
   */
  public ChallengepointAward getAwardByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    ChallengepointAward challengepointAward = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.AllAwardByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    List challengepointAwardList = query.list();

    if ( challengepointAwardList != null && challengepointAwardList.size() > 0 )
    {
      challengepointAward = (ChallengepointAward)challengepointAwardList.get( 0 );
    }

    return challengepointAward;
  }

  public List<ChallengepointAward> getAllChallengepointAwardsByPromotionIdAndUserId( Long promotionId, Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.AllAwardByUserIdAndPromotionId" );
    query.setLong( "userId", userId.longValue() );
    query.setLong( "promotionId", promotionId.longValue() );
    List<ChallengepointAward> challengepointAwardList = query.list();
    return challengepointAwardList;
  }

  /**
   * Get a list of user ids that have selected a challengepoint in a
   * promotion.
   * 
   * @param userId
   * @return challengepointAward
   */
  public List getUserIdsWithChallengepointAwardByPromotionId( Long promotionId )
  {

    List userIdList = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.UserIdsWithChallengepointAwardByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    return userIdList;
  }

  /**
   * Saves the challengepointAward to the database.
   * 
   * @param challengepointAward
   * @return ChallengepointAward
   */
  public ChallengepointAward saveChallengepointAward( ChallengepointAward challengepointAward )
  {
    return (ChallengepointAward)HibernateUtil.saveOrUpdateOrShallowMerge( challengepointAward );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.cp.MaxSequence" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where goal selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereCpSelectionStarted()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.cp.MaxSequenceCpSelectionStarted" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.cp.MaxSequenceIssueAwardsRun" );
    return runMaxSequenceQuery( query );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  private int runMaxSequenceQuery( Query query )
  {
    Integer max = (Integer)query.uniqueResult();
    if ( max != null )
    {
      return max.intValue();
    }
    return 0;

  }

  /**
   * Check for payout complete for a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return boolean - true if payout complete , false if payout not complete
   * date : May 23 2011
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.CPActivityByPromotionAndUserIdWithPayout" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "awardType", "challengepoint" );
    return query.list().size() > 0;
  }

}
