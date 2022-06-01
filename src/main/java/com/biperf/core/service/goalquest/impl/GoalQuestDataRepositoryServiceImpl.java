
package com.biperf.core.service.goalquest.impl;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.dao.goalquest.GoalQuestDataRepositoryDAO;
import com.biperf.core.service.goalquest.GoalQuestDataRepositoryService;

public class GoalQuestDataRepositoryServiceImpl implements GoalQuestDataRepositoryService
{

  private GoalQuestDataRepositoryDAO goalQuestDataRepositoryDAO;

  public Map goalQuestDataRepositoryExtract( Long promotionId, String locale )
  {
    Map results = new HashMap();
    results = goalQuestDataRepositoryDAO.goalQuestDataRepositoryExtract( promotionId, locale );
    return results;
  }

  public GoalQuestDataRepositoryDAO getGoalQuestDataRepositoryDAO()
  {
    return goalQuestDataRepositoryDAO;
  }

  public void setGoalQuestDataRepositoryDAO( GoalQuestDataRepositoryDAO goalQuestDataRepositoryDAO )
  {
    this.goalQuestDataRepositoryDAO = goalQuestDataRepositoryDAO;
  }

}
