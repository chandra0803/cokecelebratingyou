
package com.biperf.core.service.challengepoint;

import java.util.List;

import com.biperf.core.domain.challengepoint.RptChallengepointProduction;
import com.biperf.core.service.SAO;

public interface RptCpProductionService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "rptCpProductionService";

  /**
   * Selects records from the RPT_CP_PRODUCTION by promotion id.
   *
   *@param id
   *@return List of RptGoalROI objects
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
   * Updates rptCpProduction with return on investment counts.
   */
  public void updateCpProductionCounts();
}
