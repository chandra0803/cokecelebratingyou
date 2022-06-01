
package com.biperf.core.dao.challengepoint;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.challengepoint.RptChallengepointProduction;

public interface RptCpProductionDAO extends DAO
{
  public static final String BEAN_NAME = "rptCpProductionDAO";

  /**
   * Selects records from the RPT_CP_PRODUCTION by promotion id.
   *
   *@param id
   *@return List of RptChallengepointProduction objects
   */
  public List getRptCpProductionByPromotionId( Long id );

  /**
   * Save the rptChallengepointProduction.
   * 
   * @param rptChallengepointProduction
   * @return rptChallengepointProduction
   */
  public RptChallengepointProduction saveRptCpProduction( RptChallengepointProduction rptChallengepointProduction );

  /**
   * @return Integer number of all active participants (used in primary audience)
   */
  public Integer getNbrOfAllActivePax();

  /**
   * @param promotionId
   * @return Integer number of active participants in secondary audience for the given promotion
   */
  public Integer getNbrOfPaxInSpecifyPromoAudience( Long promotionId );

  /**
   * @param promotionId
   * @return Integer number of active participants in secondary audience for the given promotion
   */
  public Integer getNbrOfPaxInSpecifyPromoAudienceIncludeOwners( Long promotionId );

  /**
   * @param promotionId
   * @return Integer number of cp selected count for the given promotion
   */
  public Integer getNbrOfPaxInSelectPromoAudienceIncludeOwners( Long promotionId );

  /**
   * @param promotionId
   * @return List - get cp achieved counts for a certain promotion.
   */
  public List getAchievedCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get cp not achieved over baseline counts for a certain promotion.
   */
  public List getNotAchievedOverBaselineCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get subtotal sum of achieved counts and
   *                cp not achieved over baseline counts for a certain promotion.
   */
  public List getSubtotalCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get cp not achieved under baseline counts for a certain promotion.
   */
  public List getNotAchievedUnderBaselineCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get total sum of achieved counts and
   *                cp not achieved over baseline counts and
   *                cp not achieved under baseline counts for a certain promotion.
   */
  public List getTotalCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get did not select cp counts for a certain promotion.
   */
  public List getDidNotSelectCPCounts( Long promotionId );

}
