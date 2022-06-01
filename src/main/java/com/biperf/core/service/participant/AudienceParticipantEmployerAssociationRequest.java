/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/participant/AudienceParticipantEmployerAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.participant;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * AudienceToParticipantAssociationRequest.
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
 * <td>sharma</td>
 * <td>Jun 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 *          repko Exp $
 */
public class AudienceParticipantEmployerAssociationRequest extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    PaxAudience paxAudience = (PaxAudience)domainObject;
    initialize( paxAudience.getAudienceParticipants() );
    List paxList = paxAudience.getAudienceParticipants();
    for ( Iterator iter = paxList.iterator(); iter.hasNext(); )
    {
      AudienceParticipant audiencePax = (AudienceParticipant)iter.next();
      initialize( audiencePax.getParticipant() );
    }
  }

}
