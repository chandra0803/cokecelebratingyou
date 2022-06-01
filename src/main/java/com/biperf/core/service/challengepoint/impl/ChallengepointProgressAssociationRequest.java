
package com.biperf.core.service.challengepoint.impl;

import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ChallengepointProgressAssociationRequest.
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
 * <td>reddy</td>
 * <td>Jul 17, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengepointProgressAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PROMOTION = 2;
  public static final int PARTICIPANT = 3;
  public static final int ACTIVITY = 4;

  public ChallengepointProgressAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  public void execute( Object domainObject )
  {
    ChallengepointProgress challengepointProgress = (ChallengepointProgress)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( challengepointProgress );
        break;
      case PROMOTION:
        hydratePromotion( challengepointProgress );
        break;
      case PARTICIPANT:
        hydrateParticipant( challengepointProgress );
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
  private void hydratePromotion( ChallengepointProgress challengepointProgress )
  {
    initialize( challengepointProgress.getChallengePointPromotion() );
  }

  /**
   * hydrates the participant.
   * 
   * @param challengepointProgress
   */
  private void hydrateParticipant( ChallengepointProgress challengepointProgress )
  {
    initialize( challengepointProgress.getParticipant() );
    if ( challengepointProgress.getParticipant() != null )
    {
      initialize( challengepointProgress.getParticipant().getUserEmailAddresses() );
      initialize( challengepointProgress.getParticipant().getUserAddresses() );
    }
  }
  /**
   * hydrates the activity.
   * 
   * @param goalQuestParticipantActivity
   */
  /*
   * private void hydrateActivity( GoalQuestParticipantActivity goalQuestParticipantActivity ) {
   * initialize( goalQuestParticipantActivity.getPayoutActivity() ); }
   */

  /**
   * hydrates all pieces of the challengepointProgress
   * 
   * @param challengepointProgress
   */
  private void hydrateAll( ChallengepointProgress challengepointProgress )
  {
    hydratePromotion( challengepointProgress );
    hydrateParticipant( challengepointProgress );
    // hydrateActivity(goalQuestParticipantActivity);
  }

}
