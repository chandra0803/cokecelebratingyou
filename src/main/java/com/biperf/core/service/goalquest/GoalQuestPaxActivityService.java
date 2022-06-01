/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/goalquest/GoalQuestPaxActivityService.java,v $
 */

package com.biperf.core.service.goalquest;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * GoalQuestPaxActivityService.
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
 * <td>Todd</td>
 * <td>Jan 1, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface GoalQuestPaxActivityService extends SAO
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "goalQuestPaxActivityService";

  /**
   * Deletes the specified goalquest pax activity.
   *
   * @param goalQuestParticipantActivity  the goalquestPaxActivity to delete.
   */
  public void deleteGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity );

  /**
   * Save the goalQuestParticipantActivity.
   * 
   * @param goalQuestParticipantActivity
   * @return goalQuestParticipantActivity
   */
  public GoalQuestParticipantActivity saveGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity );

  /**
   * Get the goalQuestParticipantActivity entry by id
   * 
   * @param id
   * @return GoalQuestParticipantActivity
   */
  public GoalQuestParticipantActivity getGoalQuestParticipantActivityById( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the goalQuestParticipantActivity entry by id
   * 
   * @param id
   * @return GoalQuestParticipantActivity
   */
  public GoalQuestParticipantActivity getGoalQuestParticipantActivityById( Long id );

  /**
   * Get a list of GoalQuestParticipantActivity by promotion id and user id.
   * 
   * @param promotionId
   * @param userId 
   * @param associationRequestCollection
   * @return list of GoalQuestParticipantActivity objects
   */
  public List<GoalQuestParticipantActivity> getGoalQuestParticipantActivityByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets the latest GoalQuestParticipantActivity object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @return a GoalQuestParticipantActivity object.
   */
  public GoalQuestParticipantActivity getLatestPaxActivityByPromotionIdAndUserId( Long promotionId, Long userId );

  public GoalQuestParticipantActivity getPaxActivityByPromotionIdAndUserIdAndSubDate( Long promotionId, Long userId, Date submissionDate );

  public void updateGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity, Double step );
}
