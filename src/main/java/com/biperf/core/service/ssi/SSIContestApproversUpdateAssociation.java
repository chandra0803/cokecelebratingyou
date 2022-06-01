
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * 
 * @author dudam
 * @since Nov 6, 2014
 * @version 1.0
 */
public class SSIContestApproversUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>SSIContestApproversUpdateAssociation</code> object.
   * 
   * @param detachedContest a detached {@link SSIContest} object.
   */
  public SSIContestApproversUpdateAssociation( SSIContest detachedContest )
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
    updateLevel1Approvers( attachedContest );
    updateLevel2Approvers( attachedContest );
  }

  /**
   * Update the contest level1 approvers
   * 
   * @param attachedContest
   */
  private void updateLevel1Approvers( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestApprover ssiContestLevel1Approver;

    Set<SSIContestApprover> detachedContestLevel1Approvers = detachedContest.getContestLevel1Approvers();
    Iterator<SSIContestApprover> detachedContestLevel1ApproversIter = detachedContestLevel1Approvers.iterator();

    Set<SSIContestApprover> attachedContestLevel1Approvers = attachedContest.getContestLevel1Approvers();
    Iterator<SSIContestApprover> attachedContestLevel1ApproversIter = attachedContestLevel1Approvers.iterator();

    // If the attached contest contains any level 1 approvers not in the detached set
    // then it should be removed from the contest
    while ( attachedContestLevel1ApproversIter.hasNext() )
    {
      ssiContestLevel1Approver = (SSIContestApprover)attachedContestLevel1ApproversIter.next();
      if ( !detachedContestLevel1Approvers.contains( ssiContestLevel1Approver ) )
      {
        attachedContestLevel1ApproversIter.remove();
      }
    }

    // This will attempt to add all detached contest level 1 approvers to contest.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestLevel1ApproversIter.hasNext() )
    {
      ssiContestLevel1Approver = (SSIContestApprover)detachedContestLevel1ApproversIter.next();
      attachedContest.addContestLevel1Approver( ssiContestLevel1Approver );
    }
  }

  /**
   * Update the contest level2 approvers
   * 
   * @param attachedContest
   */
  private void updateLevel2Approvers( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestApprover ssiContestLevel2Approver;

    Set<SSIContestApprover> detachedContestLevel2Approvers = detachedContest.getContestLevel2Approvers();
    Iterator<SSIContestApprover> detachedContestLevel2ApproversIter = detachedContestLevel2Approvers.iterator();

    Set<SSIContestApprover> attachedContestLevel2Approvers = attachedContest.getContestLevel2Approvers();
    Iterator<SSIContestApprover> attachedContestLevel2ApproversIter = attachedContestLevel2Approvers.iterator();

    // If the attached contest contains any level 2 approvers not in the detached set
    // then it should be removed from the contest
    while ( attachedContestLevel2ApproversIter.hasNext() )
    {
      ssiContestLevel2Approver = (SSIContestApprover)attachedContestLevel2ApproversIter.next();
      if ( !detachedContestLevel2Approvers.contains( ssiContestLevel2Approver ) )
      {
        attachedContestLevel2ApproversIter.remove();
      }
    }

    // This will attempt to add all detached contest level 2 approvers to contest.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestLevel2ApproversIter.hasNext() )
    {
      ssiContestLevel2Approver = (SSIContestApprover)detachedContestLevel2ApproversIter.next();
      attachedContest.addContestLevel2Approver( ssiContestLevel2Approver );
    }
  }

}
