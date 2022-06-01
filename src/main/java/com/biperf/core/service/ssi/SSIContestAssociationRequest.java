
package com.biperf.core.service.ssi;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * Class to load SSIContest Associations
 * SSIContestAssociationRequest.
 * 
 * @author kandhi
 * @since Nov 6, 2014
 * @version 1.0
 */
public class SSIContestAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;
  public static final int ALL = 1;
  public static final int SSI_CONTEST_ALL_APPROVERS = 2;
  public static final int SSI_CONTEST_LEVEL1_APPROVERS = 3;
  public static final int SSI_CONTEST_LEVEL2_APPROVERS = 4;
  public static final int SSI_CONTEST_CLAIM_APPROVERS = 5;
  public static final int SSI_CONTEST_MANAGERS = 6;
  public static final int SSI_CONTEST_PARTICIPANTS = 7;
  public static final int SSI_CONTEST_ACTIVITIES = 8;
  public static final int SSI_CONTEST_LEVELS = 9;
  public static final int SSI_CONTEST_STACK_RANK_PAYOUTS = 10;
  public static final int SSI_CONTEST_CLAIM_FIELDS = 11;

  /**
   * Constructor with hydrateLevel as argument
   *
   * @param hydrateLevel
   */
  public SSIContestAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    SSIContest contest = (SSIContest)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateSSIContestLevel1Approvers( contest );
        hydrateSSIContestLevel2Approvers( contest );
        hydrateSSIContestClaimApprovers( contest );
        hydrateSSIContestManagers( contest );
        hydrateSSIContestParticipants( contest );
        hydrateSSIContestActivities( contest );
        hydrateSSIContestLevels( contest );
        hydrateSSIContestStackRankPayouts( contest );
        hydrateSSIContestClaimFields( contest );
        break;
      case SSI_CONTEST_ALL_APPROVERS:
        hydrateSSIContestLevel1Approvers( contest );
        hydrateSSIContestLevel2Approvers( contest );
        hydrateSSIContestClaimApprovers( contest );
        break;
      case SSI_CONTEST_LEVEL1_APPROVERS:
        hydrateSSIContestLevel1Approvers( contest );
        break;
      case SSI_CONTEST_LEVEL2_APPROVERS:
        hydrateSSIContestLevel2Approvers( contest );
        break;
      case SSI_CONTEST_CLAIM_APPROVERS:
        hydrateSSIContestClaimApprovers( contest );
        break;
      case SSI_CONTEST_MANAGERS:
        hydrateSSIContestManagers( contest );
        break;
      case SSI_CONTEST_PARTICIPANTS:
        hydrateSSIContestParticipants( contest );
        break;
      case SSI_CONTEST_ACTIVITIES:
        hydrateSSIContestActivities( contest );
        break;
      case SSI_CONTEST_LEVELS:
        hydrateSSIContestLevels( contest );
        break;
      case SSI_CONTEST_STACK_RANK_PAYOUTS:
        hydrateSSIContestStackRankPayouts( contest );
        break;
      case SSI_CONTEST_CLAIM_FIELDS:
        hydrateSSIContestClaimFields( contest );
        break;
      default:
        break;
    }
  }

  /**
   * Hydrate the SSI Contest Level 1 Approvers.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestLevel1Approvers( SSIContest contest )
  {
    initialize( contest.getContestLevel1Approvers() );
  }

  /**
   * Hydrate the SSI Contest Level 2 Approvers.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestLevel2Approvers( SSIContest contest )
  {
    initialize( contest.getContestLevel2Approvers() );
  }

  /**
   * Hydrate the SSI Contest Claim Approvers.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestClaimApprovers( SSIContest contest )
  {
    initialize( contest.getClaimApprovers() );
  }

  /**
   * Hydrate the SSI Contest Managers.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestManagers( SSIContest contest )
  {
    initialize( contest.getContestManagers() );
  }

  /**
   * Hydrate the SSI Contest Participants.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestParticipants( SSIContest contest )
  {
    initialize( contest.getContestParticipants() );
  }

  /**
   * Hydrate the SSI Contest Activities.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestActivities( SSIContest contest )
  {
    initialize( contest.getContestActivities() );
  }

  /**
   * Hydrate the SSI Contest Levels.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestLevels( SSIContest contest )
  {
    initialize( contest.getContestLevels() );
  }

  /**
   * Hydrate the SSI Contest Stack Rank Payouts.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestStackRankPayouts( SSIContest contest )
  {
    initialize( contest.getStackRankPayouts() );
  }

  /**
   * Hydrate the SSI Contest Stack Rank Payouts.
   *
   * @param ssiContest
   */
  private void hydrateSSIContestClaimFields( SSIContest contest )
  {
    initialize( contest.getClaimFields() );
  }

}
