
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * 
 * SSIContestStackRankPayoutsUpdateAssociation.
 * 
 * @author simhadri
 * @since Jan 22, 2015
 *
 */
public class SSIContestStackRankPayoutsUpdateAssociation extends UpdateAssociationRequest
{

  public SSIContestStackRankPayoutsUpdateAssociation( SSIContest detachedContest )
  {
    super( detachedContest );
  }

  public void execute( BaseDomain attachedDomain )
  {
    SSIContest attachedContest = (SSIContest)attachedDomain;
    updateStackRankPayouts( attachedContest );
  }

  /**
   * Update the StackRank Payouts
   * 
   * @param attachedContest
   */
  private void updateStackRankPayouts( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestStackRankPayout detachedStackRankPayout;
    SSIContestStackRankPayout attachedStackRankPayout;

    Set<SSIContestStackRankPayout> detachedStackRankPayouts = detachedContest.getStackRankPayouts();
    Iterator<SSIContestStackRankPayout> detachedStackRankPayoutsIter = detachedStackRankPayouts.iterator();

    Set<SSIContestStackRankPayout> attachedStackRankPayouts = attachedContest.getStackRankPayouts();
    Iterator<SSIContestStackRankPayout> attachedStackRankPayoutsIter = attachedStackRankPayouts.iterator();

    // If the attached contest contains any stack rank payouts not in the detached set
    // then it should be removed from the contest
    while ( attachedStackRankPayoutsIter.hasNext() )
    {
      attachedStackRankPayout = (SSIContestStackRankPayout)attachedStackRankPayoutsIter.next();
      if ( !detachedStackRankPayouts.contains( attachedStackRankPayout ) )
      {
        attachedStackRankPayoutsIter.remove();
      }
    }

    // This will attempt to add/update all detached contest activities to contest.
    while ( detachedStackRankPayoutsIter.hasNext() )
    {
      detachedStackRankPayout = (SSIContestStackRankPayout)detachedStackRankPayoutsIter.next();
      if ( detachedStackRankPayout != null && attachedStackRankPayouts.contains( detachedStackRankPayout ) )
      {
        // reinitialize the iterator
        attachedStackRankPayoutsIter = attachedStackRankPayouts.iterator();
        while ( attachedStackRankPayoutsIter.hasNext() )
        {
          attachedStackRankPayout = (SSIContestStackRankPayout)attachedStackRankPayoutsIter.next();
          if ( attachedStackRankPayout.getId().equals( detachedStackRankPayout.getId() ) )
          {
            attachedStackRankPayout.setPayoutAmount( detachedStackRankPayout.getPayoutAmount() );
            attachedStackRankPayout.setPayoutDescription( detachedStackRankPayout.getPayoutDescription() );
            attachedStackRankPayout.setRankPosition( detachedStackRankPayout.getRankPosition() );
            attachedStackRankPayout.setBadgeRule( detachedStackRankPayout.getBadgeRule() );
            break;
          }
        }
      }
      else
      {
        attachedContest.addStackRankPayout( detachedStackRankPayout );
      }
    }
  }

}
