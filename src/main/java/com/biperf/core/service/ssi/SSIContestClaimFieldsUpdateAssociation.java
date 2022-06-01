
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * 
 * @author dudam
 * @since May 13, 2015
 * @version 1.0
 */
public class SSIContestClaimFieldsUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>SSIContestClaimFieldsUpdateAssociation</code> object.
   * 
   * @param detachedContest a detached {@link SSIContest} object.
   */
  public SSIContestClaimFieldsUpdateAssociation( SSIContest detachedContest )
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
    updateClaimFields( attachedContest );
  }

  /**
   * Update the claim fields
   * 
   * @param attachedContest
   */
  private void updateClaimFields( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    SSIContestClaimField claimField;

    Set<SSIContestClaimField> detachedContestClaimFields = detachedContest.getClaimFields();
    Iterator<SSIContestClaimField> detachedContestClaimFieldsIter = detachedContestClaimFields.iterator();

    Set<SSIContestClaimField> attachedContestClaimFields = attachedContest.getClaimFields();
    Iterator<SSIContestClaimField> attachedContestClaimFieldsIter = attachedContestClaimFields.iterator();

    // If the attached contest contains any claim fields not in the detached set
    // then it should be removed from the contest
    while ( attachedContestClaimFieldsIter.hasNext() )
    {
      claimField = (SSIContestClaimField)attachedContestClaimFieldsIter.next();
      if ( ( !Objects.isNull( claimField.getPaxClaimFields() ) && claimField.getPaxClaimFields().isEmpty() ) && !detachedContestClaimFields.contains( claimField ) )
      {
        attachedContestClaimFieldsIter.remove();
      }
    }

    // This will attempt to add all detached contest claim fields to contest.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestClaimFieldsIter.hasNext() )
    {
      claimField = (SSIContestClaimField)detachedContestClaimFieldsIter.next();
      attachedContest.addClaimField( claimField );
    }
  }

}
