
package com.biperf.core.service.budget;

import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.BaseAssociationRequest;

public class BudgetSegmentToBudgetsAssociationRequest extends BaseAssociationRequest
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    BudgetSegment budgetSegment;
    if ( domainObject instanceof BudgetSegment )
    {
      budgetSegment = (BudgetSegment)domainObject;
    }
    else
    {
      throw new BeaconRuntimeException( "Can't use BudgetSegmentToBudgetsAssociationRequest.execute() passing in a class of type: " + domainObject.getClass().getName() );
    }
    initialize( budgetSegment.getBudgets() );
  }
}
