
package com.biperf.core.dao.goalquest;

import java.util.Map;

import com.biperf.core.dao.DAO;

public interface GoalQuestDataRepositoryDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "goalQuestDataRepositoryDAO";

  public Map goalQuestDataRepositoryExtract( Long promotionId, String locale );

}
