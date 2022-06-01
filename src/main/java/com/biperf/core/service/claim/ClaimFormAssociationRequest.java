/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/ClaimFormAssociationRequest.java,v $
 */

package com.biperf.core.service.claim;

import java.util.Iterator;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ClaimFormAssociationRequest.
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
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int STEPS = 2;

  public ClaimFormAssociationRequest( int hydrateLevel )
  {
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
    ClaimForm claimForm = (ClaimForm)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
      {
        hydrateStepsAndElements( claimForm );
        break;
      }
      case STEPS:
      {
        hydrateSteps( claimForm );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * @param claimForm
   */
  private void hydrateSteps( ClaimForm claimForm )
  {
    initialize( claimForm.getClaimFormSteps() );
  }

  /**
   * @param claimForm
   */
  private void hydrateStepsAndElements( ClaimForm claimForm )
  {
    hydrateSteps( claimForm );

    Iterator iter = claimForm.getClaimFormSteps().iterator();
    while ( iter.hasNext() )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)iter.next();

      if ( claimFormStep == null )
      {
        continue;
      }

      initialize( claimFormStep.getClaimFormStepElements() );
    }
  }
}
