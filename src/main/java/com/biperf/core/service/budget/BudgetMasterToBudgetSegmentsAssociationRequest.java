/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/budget/Attic/BudgetMasterToBudgetSegmentsAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.budget;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * BudgetMasterToBudgetsAssociationRequest.
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
 * <td>May 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterToBudgetSegmentsAssociationRequest extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    BudgetMaster budgetMaster;
    if ( domainObject instanceof RecognitionPromotion )
    {
      budgetMaster = ( (RecognitionPromotion)domainObject ).getBudgetMaster();
    }
    else if ( domainObject instanceof BudgetMaster )
    {
      budgetMaster = (BudgetMaster)domainObject;
    }
    else
    {
      throw new BeaconRuntimeException( "Can't use BudgetMasterToBudgetsAssociationRequest.execute() passing in a class of type: " + domainObject.getClass().getName() );
    }
    initialize( budgetMaster.getBudgetSegments() );
  }
}
