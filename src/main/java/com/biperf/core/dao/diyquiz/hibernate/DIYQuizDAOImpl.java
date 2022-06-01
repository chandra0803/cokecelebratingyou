
package com.biperf.core.dao.diyquiz.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.diyquiz.DIYQuizDAO;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.utils.DateUtils;

/**
 * 
 * DIYQuizDAOImpl.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizDAOImpl extends BaseDAO implements DIYQuizDAO
{

  /**
   * Get the quiz by Id. Overridden from
   * 
   * @see com.biperf.core.dao.diyquiz.DIYQuizDAO#getQuizById(java.lang.Long)
   * @param quizId
   * @return DIYQuiz
   */
  public DIYQuiz getQuizById( Long quizId )
  {
    return (DIYQuiz)getSession().get( DIYQuiz.class, quizId );
  }

  /**
   * Saves a quiz to the database. Overridden from
   * 
   * @see com.biperf.core.dao.diyquiz.DIYQuizDAO#saveQuiz(com.biperf.core.domain.diyquiz.DIYQuiz)
   * @param diyQuiz
   * @return DIYQuiz
   */
  public DIYQuiz saveQuiz( DIYQuiz diyQuiz )
  {
    getSession().save( diyQuiz );

    return diyQuiz;
  }

  /**
   * Get all the DIY Quizzes for the given promotion_id
   * @param promotionId
   * @return diyQuizList
   */
  @SuppressWarnings( "unchecked" )
  public List<DIYQuiz> getAllQuizzesByPromotionId( Long promotionId )
  {
    Criteria crit = getSession().createCriteria( DIYQuiz.class );
    crit.add( Restrictions.eq( "promotion.id", promotionId ) );
    return crit.list();
  }

  /**
   * Get all the DIY Quizzes for the given promotion_id and badgeRuleId
   * @param promotionId
   * @param promotionId
   * @return diyQuizList
   */
  @SuppressWarnings( "unchecked" )
  public List<DIYQuiz> getAllQuizzesByPromotionIdAndBadgeRuleId( Long promotionId, Long badgeRuleId )
  {
    Criteria crit = getSession().createCriteria( DIYQuiz.class );
    crit.add( Restrictions.eq( "promotion.id", promotionId ) );
    crit.add( Restrictions.eq( "badgeRule.id", badgeRuleId ) );
    return crit.list();
  }

  /**
   * Gets list of LeaderBoard using  Status
   * 
   * @see com.biperf.core.dao.LeaderBoardDAO#getLeaderBoardStatus()
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public List<DIYQuiz> getQuizByPromotionAndStatus( String status, Long promotionId )
  {
    Query query = null;

    // incomplete
    if ( status.equalsIgnoreCase( "incomplete" ) )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getAllIncompleteDIYQuizzes" );
      query.setParameter( "status", ClaimFormStatusType.UNDER_CONSTRUCTION );
      query.setParameter( "promotionId", promotionId );
    }
    // pending
    else if ( status.equalsIgnoreCase( "pending" ) )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getAllPendingDIYQuizzes" );
      query.setParameter( "status", ClaimFormStatusType.COMPLETED );
      query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
      query.setParameter( "promotionId", promotionId );
    }
    // active
    else if ( status.equalsIgnoreCase( "active" ) )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getAllActiveDIYQuizzes" );
      query.setParameter( "status", ClaimFormStatusType.COMPLETED );
      query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
      query.setParameter( "promotionId", promotionId );
    }
    // inactive
    else if ( status.equalsIgnoreCase( "inactive" ) )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getAllExpiredDIYQuizzes" );
      query.setParameter( "status", ClaimFormStatusType.COMPLETED );
      query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
      query.setParameter( "promotionId", promotionId );
    }

    return query == null ? null : query.list();

  }

  @SuppressWarnings( "unchecked" )
  public List<Long> getActiveQuizzesForParticipantByPromotion( Long promotionId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getActiveQuizzesForParticipantByPromotion" );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<DIYQuiz> getActiveQuizListForParticipantByPromotion( Long promotionId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getActiveQuizListForParticipantByPromotion" );
    query.setParameter( "referenceDate", DateUtils.getCurrentDateTrimmed() );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setResultTransformer( Transformers.aliasToBean( DIYQuiz.class ) );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<DIYQuiz> getEligibleQuizzesForParticipantByPromotion( Long promotionId, Long participantId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.diyquiz.getEligibleQuizzesForParticipantByPromotion" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "participantId", participantId );
    query.setResultTransformer( Transformers.aliasToBean( DIYQuiz.class ) );
    return query.list();
  }
}
