/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/goalquest/impl/GoalQuestPaxActivityServiceImpl.java,v $
 */

package com.biperf.core.service.goalquest.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.PaxGoalService;

/**
 * GoalQuestPaxActivityServiceImpl.
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
public class GoalQuestPaxActivityServiceImpl implements GoalQuestPaxActivityService
{

  /** GoalQuestPaxActivityDAO * */
  private GoalQuestPaxActivityDAO goalQuestPaxActivityDAO;
  /** PaxGoalService **/
  private PaxGoalService paxGoalService;

  /**
   * Overridden from @see com.biperf.core.service.goalquest.GoalQuestPaxActivityService#deleteGoalQuestPaxActivity(com.biperf.core.domain.goalquest.GoalQuestParticipantActivity)
   * @param goalQuestParticipantActivity
   */
  public void deleteGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    getGoalQuestPaxActivityDAO().deleteGoalQuestPaxActivity( goalQuestParticipantActivity );
  }

  /**
   * Overridden from @see com.biperf.core.service.goalquest.GoalQuestPaxActivityService#getGoalQuestParticipantActivityById(java.lang.Long)
   * @param id
   * @return
   */
  public GoalQuestParticipantActivity getGoalQuestParticipantActivityById( Long id )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = this.goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById( id );
    return goalQuestParticipantActivity;
  }

  /**
   * Overridden from @see com.biperf.core.service.goalquest.GoalQuestPaxActivityService#getGoalQuestParticipantActivityById(java.lang.Long)
   * @param id
   * @return
   */
  public GoalQuestParticipantActivity getGoalQuestParticipantActivityById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = this.goalQuestPaxActivityDAO.getGoalQuestParticipantActivityById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( goalQuestParticipantActivity );
    }
    return goalQuestParticipantActivity;
  }

  /**
   * Overridden from @see com.biperf.core.service.goalquest.GoalQuestPaxActivityService#saveGoalQuestPaxActivity(com.biperf.core.domain.goalquest.GoalQuestParticipantActivity)
   * @param goalQuestParticipantActivity
   * @return
   */
  public GoalQuestParticipantActivity saveGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    GoalQuestParticipantActivity participantActivity = getGoalQuestPaxActivityDAO().saveGoalQuestPaxActivity( goalQuestParticipantActivity );
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( goalQuestParticipantActivity.getGoalQuestPromotion().getId(), participantActivity.getParticipant().getId() );
    // This shouldn't happen but they may not have a goal selected when progress is loaded
    if ( paxGoal == null )
    {
      paxGoal = new PaxGoal();
      paxGoal.setGoalQuestPromotion( goalQuestParticipantActivity.getGoalQuestPromotion() );
      paxGoal.setParticipant( participantActivity.getParticipant() );
    }
    // We are assuming this is last load and no loads with greater date have already happened
    if ( participantActivity.getType().equals( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) ) )
    {
      if ( paxGoal.getCurrentValue() == null )
      {
        paxGoal.setCurrentValue( participantActivity.getQuantity() );
      }
      else
      {

        if ( goalQuestParticipantActivity.getId() != null )
        {
          paxGoal.setCurrentValue( paxGoal.getCurrentValue().add( goalQuestParticipantActivity.getQuantity() ) );
        }
        else
        {
          paxGoal.setCurrentValue( paxGoal.getCurrentValue().add( participantActivity.getQuantity() ) );
        }
      }
    }
    else
    {
      paxGoal.setCurrentValue( participantActivity.getQuantity() );
    }
    paxGoalService.savePaxGoal( paxGoal );
    return participantActivity;

  }

  /**
   * Get a list of GoalQuestParticipantActivity by promotion id and user id.
   * 
   * @param promotionId
   * @param userId 
   * @param associationRequestCollection
   * @return list of GoalQuestParticipantActivity objects
   */
  public List<GoalQuestParticipantActivity> getGoalQuestParticipantActivityByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection )
  {
    return this.goalQuestPaxActivityDAO.getGoalQuestParticipantActivityByPromotionIdAndUserId( promotionId, userId, associationRequestCollection );
  }

  /**
   * Gets the latest GoalQuestParticipantActivity object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @return a GoalQuestParticipantActivity object.
   */
  public GoalQuestParticipantActivity getLatestPaxActivityByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    List goalQuestPaxActivityList = getGoalQuestParticipantActivityByPromotionIdAndUserId( promotionId, userId, new AssociationRequestCollection() );
    GoalQuestParticipantActivity goalQuestParticipantActivity = null;
    for ( Iterator paxActivityIter = goalQuestPaxActivityList.iterator(); paxActivityIter.hasNext(); )
    {
      GoalQuestParticipantActivity currentPaxActivity = (GoalQuestParticipantActivity)paxActivityIter.next();
      if ( goalQuestParticipantActivity == null )
      {
        goalQuestParticipantActivity = currentPaxActivity;
      }
      else
      {
        if ( currentPaxActivity.getSubmissionDate() != null && goalQuestParticipantActivity.getSubmissionDate() != null
            && currentPaxActivity.getSubmissionDate().after( goalQuestParticipantActivity.getSubmissionDate() ) )
        {
          goalQuestParticipantActivity = currentPaxActivity;
        }
      }
    }
    return goalQuestParticipantActivity;
  }

  public GoalQuestParticipantActivity getPaxActivityByPromotionIdAndUserIdAndSubDate( Long promotionId, Long userId, Date submissionDate )
  {
    return this.goalQuestPaxActivityDAO.getPaxActivityByPromotionIdAndUserIdAndSubDate( promotionId, userId, submissionDate );
  }

  public GoalQuestPaxActivityDAO getGoalQuestPaxActivityDAO()
  {
    return goalQuestPaxActivityDAO;
  }

  public void setGoalQuestPaxActivityDAO( GoalQuestPaxActivityDAO goalQuestPaxActivityDAO )
  {
    this.goalQuestPaxActivityDAO = goalQuestPaxActivityDAO;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  @Override
  public void updateGoalQuestPaxActivity( GoalQuestParticipantActivity goalQuestParticipantActivity, Double stepUpdated )
  {
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( goalQuestParticipantActivity.getGoalQuestPromotion().getId(), goalQuestParticipantActivity.getParticipant().getId() );
    if ( goalQuestParticipantActivity.getId() != null && paxGoal != null )
    {
      paxGoal.setCurrentValue( paxGoal.getCurrentValue().add( new BigDecimal( stepUpdated ).subtract( goalQuestParticipantActivity.getQuantity() ) ) );
      paxGoalService.savePaxGoal( paxGoal );
    }
    goalQuestParticipantActivity.setQuantity( new BigDecimal( stepUpdated ) );
    goalQuestPaxActivityDAO.saveGoalQuestPaxActivity( goalQuestParticipantActivity );
  }
}
