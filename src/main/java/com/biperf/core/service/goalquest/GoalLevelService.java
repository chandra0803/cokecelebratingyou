
package com.biperf.core.service.goalquest;

import java.util.List;

import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.service.SAO;

public interface GoalLevelService extends SAO
{
  public static final String BEAN_NAME = "goalLevelService";

  /**
   * Get the GoalLevel from the database by the id.
   * 
   * @param id
   * @return abstractGoalLevel
   */
  public AbstractGoalLevel getGoalLevelById( Long id );

  /**
   * Get the max GoalLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence();

  /**
   * Get the max GoalLevel sequence for all active promotions where goal selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereGoalSelectionStarted();

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun();

  /**
   * Get the GoalLevels from the database by the id.
   * 
   * @param id
   * @return list
   */
  public List getGoalLevelsByPromotionId( Long promotionId );

}
