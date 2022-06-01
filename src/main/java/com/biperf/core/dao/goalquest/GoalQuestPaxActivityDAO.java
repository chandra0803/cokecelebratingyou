/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/goalquest/GoalQuestPaxActivityDAO.java,v $
 */

package com.biperf.core.dao.goalquest;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.service.AssociationRequestCollection;

/*
 * JournalDAO <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan 01,2007</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public interface GoalQuestPaxActivityDAO extends DAO
{
  public static final String BEAN_NAME = "goalQuestPaxActivityDAO";

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
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId );

  public GoalQuestParticipantActivity getPaxActivityByPromotionIdAndUserIdAndSubDate( Long promotionId, Long userId, Date submissionDate );
}
