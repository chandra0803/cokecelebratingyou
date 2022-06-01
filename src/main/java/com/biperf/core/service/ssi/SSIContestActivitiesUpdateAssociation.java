
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * 
 * SSIContestActivitiesUpdateAssociation.
 * 
 * @author chowdhur
 * @since Dec 10, 2014
 */
public class SSIContestActivitiesUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>SSIContestActivitiesUpdateAssociation</code> object.
   * 
   * @param detachedContest a detached {@link SSIContest} object.
   */
  public SSIContestActivitiesUpdateAssociation( SSIContest detachedContest )
  {
    super( detachedContest );
  }

  /**
   * Use the detached {@link SSiContest} object to update the attached {@link SSIContest} object.
   * 
   * @param attachedDomain the attached version of the {@link SSIContest} object.
   */
  public void execute( BaseDomain attachedDomain )
  {
    SSIContest attachedContest = (SSIContest)attachedDomain;
    updateActivities( attachedContest );
  }

  /**
   * Update the contest activities
   * 
   * @param attachedContest
   */
  private void updateActivities( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestActivity attachedContestActivity = null;
    SSIContestActivity detachedContestActivity = null;

    Set<SSIContestActivity> detachedContestActivities = detachedContest.getContestActivities();
    Iterator<SSIContestActivity> detachedContestActivitiesIter = detachedContest.getContestActivities().iterator();

    Set<SSIContestActivity> attachedContestActivities = attachedContest.getContestActivities();
    Iterator<SSIContestActivity> attachedContestActivitiesIter = attachedContestActivities.iterator();

    // If the attached contest contains any activities not in the detached set
    // then it should be removed from the contest
    while ( attachedContestActivitiesIter.hasNext() )
    {
      attachedContestActivity = (SSIContestActivity)attachedContestActivitiesIter.next();
      if ( !detachedContestActivities.contains( attachedContestActivity ) )
      {
        attachedContestActivitiesIter.remove();
      }
    }

    // This will attempt to add all detached contest activities to contest.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestActivitiesIter.hasNext() )
    {
      detachedContestActivity = (SSIContestActivity)detachedContestActivitiesIter.next();
      // Check for any update columns and update the table
      if ( attachedContestActivities != null && attachedContestActivities.contains( detachedContestActivity ) )
      {
        Iterator<SSIContestActivity> attachedContestActivitiesIter1 = attachedContestActivities.iterator();
        while ( attachedContestActivitiesIter1.hasNext() )
        {
          attachedContestActivity = (SSIContestActivity)attachedContestActivitiesIter1.next();
          if ( detachedContestActivity.equals( attachedContestActivity ) )
          {
            attachedContestActivity.setDescription( detachedContestActivity.getDescription() );
            attachedContestActivity.setGoalAmount( detachedContestActivity.getGoalAmount() );
            attachedContestActivity.setIncrementAmount( detachedContestActivity.getIncrementAmount() );
            attachedContestActivity.setMinQualifier( detachedContestActivity.getMinQualifier() );
            attachedContestActivity.setPayoutAmount( detachedContestActivity.getPayoutAmount() );
            attachedContestActivity.setPayoutCapAmount( detachedContestActivity.getPayoutCapAmount() );
            attachedContestActivity.setPayoutDescription( detachedContestActivity.getPayoutDescription() );
          }
        }
      }
      else
      {
        attachedContest.addContestActivity( detachedContestActivity );
      }
    }
  }

}
