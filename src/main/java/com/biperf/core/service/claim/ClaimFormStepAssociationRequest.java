/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/ClaimFormStepAssociationRequest.java,v $
 */

package com.biperf.core.service.claim;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ClaimFormStepAssociationRequest.
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
 * <td>zahler</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepAssociationRequest extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    ClaimFormStep claimFormStep = (ClaimFormStep)domainObject;

    initialize( claimFormStep.getClaimForm() );
    initialize( claimFormStep.getClaimFormStepElements() );
  }

}
