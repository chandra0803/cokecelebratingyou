
package com.biperf.core.service.goalquest;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface GoalQuestDataRepositoryService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "goalQuestDataRepositoryService";

  public Map goalQuestDataRepositoryExtract( Long promotionId, String locale );

}
