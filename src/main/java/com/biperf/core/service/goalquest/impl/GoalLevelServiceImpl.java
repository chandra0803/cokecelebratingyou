
package com.biperf.core.service.goalquest.impl;

import java.util.List;

import com.biperf.core.dao.goalquest.GoalLevelDAO;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.service.goalquest.GoalLevelService;

public class GoalLevelServiceImpl implements GoalLevelService
{
  private GoalLevelDAO goalLevelDAO;

  /**
   * Set the goalLevelDAO through IoC.
   * 
   * @param goalLevelDAO
   */
  public void setGoalLevelDAO( GoalLevelDAO goalLevelDAO )
  {
    this.goalLevelDAO = goalLevelDAO;
  }

  /**
   * Get the GoalLevel from the database by the id.
   * 
   * @param id
   * @return abstractGoalLevel
   */
  public AbstractGoalLevel getGoalLevelById( Long id )
  {
    return this.goalLevelDAO.getGoalLevelById( id );
  }

  /**
   * Get the max GoalLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence()
  {
    return this.goalLevelDAO.getMaxSequence();
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where goal selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereGoalSelectionStarted()
  {
    return this.goalLevelDAO.getMaxSequenceWhereGoalSelectionStarted();
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun()
  {
    return this.goalLevelDAO.getMaxSequenceWhereIssueAwardsRun();
  }

  /**
   * Get the GoalLevels from the database by the id.
   * 
   * @param id
   * @return list
   */
  public List getGoalLevelsByPromotionId( Long promotionId )
  {
    return this.goalLevelDAO.getGoalLevelsByPromotionId( promotionId );
  }
}
