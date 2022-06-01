
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestApproversView.
 * 
 * @author kandhi
 * @since Nov 14, 2014
 * @version 1.0
 */
public class SSIContestApproversView
{
  private List<SSIContestApproverView> contestApprovers;

  public SSIContestApproversView( Map<String, Set<Participant>> allowedContestApprovers, Map<String, Set<Participant>> selectedContestApprovers )
  {
    if ( allowedContestApprovers != null )
    {
      contestApprovers = new ArrayList<SSIContestApproverView>();
      Set<Participant> allowedLevel1Approvers = allowedContestApprovers.get( "contest_approver_level_1" );
      Set<Participant> selectedLevel1Approvers = selectedContestApprovers.get( "selected_contest_approver_level_1" );
      SSIContestApproverView contestLevel1ApproverView = new SSIContestApproverView( 1L,
                                                                                     CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL_ONE" ),
                                                                                     allowedLevel1Approvers,
                                                                                     selectedLevel1Approvers );
      contestApprovers.add( contestLevel1ApproverView );

      Set<Participant> allowedLevel2Approvers = allowedContestApprovers.get( "contest_approver_level_2" );
      if ( allowedLevel2Approvers != null && allowedLevel2Approvers.size() > 0 )
      {
        Set<Participant> selectedLevel2Approvers = selectedContestApprovers.get( "selected_contest_approver_level_2" );
        SSIContestApproverView contestLevel2ApproverView = new SSIContestApproverView( 2L,
                                                                                       CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL_TWO" ),
                                                                                       allowedLevel2Approvers,
                                                                                       selectedLevel2Approvers );
        contestApprovers.add( contestLevel2ApproverView );
      }
    }
  }

  public List<SSIContestApproverView> getContestApprovers()
  {
    return contestApprovers;
  }

  public void setContestApprovers( List<SSIContestApproverView> contestApprovers )
  {
    this.contestApprovers = contestApprovers;
  }

}
