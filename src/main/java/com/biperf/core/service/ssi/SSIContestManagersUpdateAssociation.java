
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * 
 * @author dudam
 * @since Nov 6, 2014
 * @version 1.0
 */
public class SSIContestManagersUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>SSIContestManagersUpdateAssociation</code> object.
   * 
   * @param detachedContest a detached {@link SSIContest} object.
   */
  public SSIContestManagersUpdateAssociation( SSIContest detachedContest )
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
    updateManagers( attachedContest );
  }

  /**
   * Update the contest managers
   * 
   * @param attachedContest
   */
  private void updateManagers( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestManager ssiContestManager;

    Set<SSIContestManager> detachedContestManagers = detachedContest.getContestManagers();
    Iterator<SSIContestManager> detachedContestManagersIter = detachedContest.getContestManagers().iterator();

    Set<SSIContestManager> attachedContestManagers = attachedContest.getContestManagers();
    Iterator<SSIContestManager> attachedContestManagersIter = attachedContestManagers.iterator();

    // If the attached contest contains any managers not in the detached set
    // then it should be removed from the contest
    while ( attachedContestManagersIter.hasNext() )
    {
      ssiContestManager = (SSIContestManager)attachedContestManagersIter.next();
      if ( !detachedContestManagers.contains( ssiContestManager ) )
      {
        attachedContestManagersIter.remove();
      }
    }

    // This will attempt to add all detached contest managers to contest.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestManagersIter.hasNext() )
    {
      ssiContestManager = (SSIContestManager)detachedContestManagersIter.next();
      attachedContest.addContestManager( ssiContestManager );
    }
  }

}
