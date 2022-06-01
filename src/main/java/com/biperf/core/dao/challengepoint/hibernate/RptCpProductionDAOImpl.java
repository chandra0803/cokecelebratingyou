
package com.biperf.core.dao.challengepoint.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.challengepoint.RptCpProductionDAO;
import com.biperf.core.domain.challengepoint.RptChallengepointProduction;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class RptCpProductionDAOImpl extends BaseDAO implements RptCpProductionDAO
{

  /**
   * Selects records from the RPT_CP_PRODUCTION by promotion id.
   * 
   * @param promotionId
   * @return list of RptChallengepointProduction objects
   */
  public List getRptCpProductionByPromotionId( Long promotionId )
  {

    List rptGoalROIList = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.RptCPProductionByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    return rptGoalROIList;
  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.RptGoalROIDAO#getNbrOfAllActivePax()
   * @return Integer number of all active participants (used in primary audience)
   */
  public Integer getNbrOfAllActivePax()
  {
    Integer count = (Integer)getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.AllActivePaxCount" ).uniqueResult();
    return count;
  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.RptGoalROIDAO#getNbrOfPaxInPromoAudience(java.lang.Long)
   * @param promotionId
   * @return Integer the number of active participants in the audience for a given promotion
   */
  public Integer getNbrOfPaxInSpecifyPromoAudience( Long promotionId )
  {
    Integer count = (Integer)getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxInSpecifyAudienceByPromotionCount" ).setLong( "promotionId", promotionId.longValue() )
        .uniqueResult();

    return count;
  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.RptCpProductionDAO#getNbrOfPaxInPromoAudience(java.lang.Long)
   * @param promotionId
   * @return Integer the number of active participants in the audience for a given promotion
   */
  public Integer getNbrOfPaxInSpecifyPromoAudienceIncludeOwners( Long promotionId )
  {
    Integer count = (Integer)getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxInSpecifyAudienceByPromotionCountIncludeOwners" ).setLong( "promotionId", promotionId.longValue() )
        .uniqueResult();

    return count;
  }

  /**
   * Overridden from @see com.biperf.core.dao.challengepoint.RptCpProductionDAO#getTotNbrOfPaxInPromoAudience(java.lang.Long)
   * @param promotionId
   * @return Integer the number of cp slected count for a given promotion
   */
  public Integer getNbrOfPaxInSelectPromoAudienceIncludeOwners( Long promotionId )
  {
    Integer count = (Integer)getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxInSelectAudienceByPromotionCountIncludeOwners" ).setLong( "promotionId", promotionId.longValue() )
        .uniqueResult();

    return count;
  }

  /**
   * @param promotionId
   * @return List - get goal achieved counts for a certain promotion.
   */
  public List getAchievedCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxsByCPAchieved" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get goal not achieved over baseline counts for a certain promotion.
   */
  public List getNotAchievedOverBaselineCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxsByCPNotAchievedOverBaseline" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get subtotal sum of achieved counts and
   *                goal not achieved over baseline counts for a certain promotion.
   */
  public List getSubtotalCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.Subtotal" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get goal not achieved under baseline counts for a certain promotion.
   */
  public List getNotAchievedUnderBaselineCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxsByCPNotAchievedUnderBaseline" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get total sum of achieved counts and
   *                goal not achieved over baseline counts and
   *                goal not achieved under baseline counts for a certain promotion.
   */
  public List getTotalCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.Total" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * @param promotionId
   * @return List - get did not select goal counts for a certain promotion.
   */
  public List getDidNotSelectCPCounts( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.challengepoint.PaxsDidNotSelectCP" );
    query.setLong( "promotionId", promotionId.longValue() );
    return query.list();
  }

  /**
   * Saves the CpProduction to the database.
   * 
   * @param CpProduction
   * @return CpProduction
   */
  public RptChallengepointProduction saveRptCpProduction( RptChallengepointProduction rptCpProduction )
  {
    return (RptChallengepointProduction)HibernateUtil.saveOrUpdateOrDeepMerge( rptCpProduction );
  }

}
