
package com.biperf.core.service.goalquest.impl;

import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.service.BaseAssociationRequest;

public class PaxGoalAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PROMOTION = 2;
  public static final int PARTICIPANT = 3;
  public static final int GOAL_LEVEL = 4;

  public PaxGoalAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    PaxGoal paxGoal = (PaxGoal)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( paxGoal );
        break;
      case PROMOTION:
        hydratePromotion( paxGoal );
        break;
      case PARTICIPANT:
        hydrateParticipant( paxGoal );
        break;
      case GOAL_LEVEL:
        hydrateGoalLevel( paxGoal );
        break;
      default:
        break;
    }
  }

  /**
   * hydrates the promotion.
   * 
   * @param paxGoal
   */
  private void hydratePromotion( PaxGoal paxGoal )
  {
    initialize( paxGoal.getGoalQuestPromotion() );
  }

  /**
   * hydrates the participant.
   * 
   * @param paxGoal
   */
  private void hydrateParticipant( PaxGoal paxGoal )
  {
    initialize( paxGoal.getParticipant() );
    if ( paxGoal.getParticipant() != null )
    {
      initialize( paxGoal.getParticipant().getUserEmailAddresses() );
      initialize( paxGoal.getParticipant().getUserAddresses() );
      initialize( paxGoal.getParticipant().getUserPhones() );
    }

  }

  /**
   * hydrates the goal level.
   * 
   * @param paxGoal
   */
  private void hydrateGoalLevel( PaxGoal paxGoal )
  {
    initialize( paxGoal.getGoalLevel() );
  }

  /**
   * hydrates all pieces of the paxGoal
   * 
   * @param paxGoal
   */
  private void hydrateAll( PaxGoal paxGoal )
  {
    hydratePromotion( paxGoal );
    hydrateParticipant( paxGoal );
    hydrateGoalLevel( paxGoal );
  }

}
