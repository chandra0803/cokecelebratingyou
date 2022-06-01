
package com.biperf.core.service.goalquest.impl;

import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.service.BaseAssociationRequest;

public class GoalQuestPaxActivityAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PROMOTION = 2;
  public static final int PARTICIPANT = 3;
  public static final int ACTIVITY = 4;

  public GoalQuestPaxActivityAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    GoalQuestParticipantActivity goalQuestParticipantActivity = (GoalQuestParticipantActivity)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( goalQuestParticipantActivity );
        break;
      case PROMOTION:
        hydratePromotion( goalQuestParticipantActivity );
        break;
      case PARTICIPANT:
        hydrateParticipant( goalQuestParticipantActivity );
        break;
      case ACTIVITY:
        hydrateActivity( goalQuestParticipantActivity );
        break;
      default:
        break;
    }
  }

  /**
   * hydrates the promotion.
   * 
   * @param goalQuestParticipantActivity
   */
  private void hydratePromotion( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    initialize( goalQuestParticipantActivity.getGoalQuestPromotion() );
  }

  /**
   * hydrates the participant.
   * 
   * @param goalQuestParticipantActivity
   */
  private void hydrateParticipant( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    initialize( goalQuestParticipantActivity.getParticipant() );
    if ( goalQuestParticipantActivity.getParticipant() != null )
    {
      initialize( goalQuestParticipantActivity.getParticipant().getUserEmailAddresses() );
      initialize( goalQuestParticipantActivity.getParticipant().getUserAddresses() );
    }
  }

  /**
   * hydrates the activity.
   * 
   * @param goalQuestParticipantActivity
   */
  private void hydrateActivity( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    initialize( goalQuestParticipantActivity.getPayoutActivity() );
  }

  /**
   * hydrates all pieces of the paxGoal
   * 
   * @param goalQuestParticipantActivity
   */
  private void hydrateAll( GoalQuestParticipantActivity goalQuestParticipantActivity )
  {
    hydratePromotion( goalQuestParticipantActivity );
    hydrateParticipant( goalQuestParticipantActivity );
    hydrateActivity( goalQuestParticipantActivity );
  }

}
